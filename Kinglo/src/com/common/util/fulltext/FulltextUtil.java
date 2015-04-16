package com.common.util.fulltext;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.common.exception.ShipSuiteRuntimeException;
import com.common.util.generic.CollectionUtil;
import com.common.util.string.StringUtil;
import com.common.util.validator.ValidUtil;

public class FulltextUtil {
    private static final String[] IGNORE_WORDS = { "(", ")", "（", "）", "[",
	    "]", "<", ">", "{", "}", "《", "》", "【", "】", "“", "”", "'", "\"",
	    "，", ",", "。", "." };

    public static List<WordSegment> getPairSymbolTerm(String words,
	    String startSymbol, String endSymbol) {
	List<WordSegment> list = new ArrayList<WordSegment>();
	int startIndex = words.indexOf(startSymbol);
	int lastIndex = words.lastIndexOf(endSymbol);
	int index = startIndex;
	String newString = words;
	if ((startIndex >= 0) && (startIndex < lastIndex)) {
	    while ((!newString.isEmpty()) && (startIndex >= 0)) {
		newString = newString.substring(startIndex + 1);
		int endIndex = newString.indexOf(endSymbol);
		if (endIndex < 0) {
		    break;
		}
		String subString = newString.substring(0, endIndex);
		WordSegment ws = new WordSegment(subString, index);
		list.add(ws);

		newString = newString.substring(endIndex + 1);
		index = index + subString.length() + 1;
		startIndex = newString.indexOf(startSymbol);
		index = index + startIndex + 1;
	    }
	}
	return list;
    }

    public static List<String> getTermOnlyQuotes(String words) {
	List<WordSegment> list = getPairSymbolTerm(words, "\"", "\"");
	list.addAll(getPairSymbolTerm(words, "'", "'"));
	list.addAll(getPairSymbolTerm(words, "“", "”"));
	list.addAll(getPairSymbolTerm(words, "‘", "’"));
	list.addAll(getPairSymbolTerm(words, "《", "》"));
	list.addAll(getPairSymbolTerm(words, "<", ">"));
	list.addAll(getPairSymbolTerm(words, "【", "】"));
	list.addAll(getPairSymbolTerm(words, "[", "]"));

	CollectionUtil.sort(list, new String[] { "index" });
	List<String> wordsList = new ArrayList<String>();
	for (WordSegment ws : list) {
	    String word = ws.getWord();
	    if (!wordsList.contains(word)) {
		wordsList.add(word);
	    }
	}
	return wordsList;
    }

    public static List<String> getTermListByQuote(String words) {
	List<WordSegment> list = getPairSymbolTerm(words, "\"", "\"");
	list.addAll(getPairSymbolTerm(words, "'", "'"));
	list.addAll(getPairSymbolTerm(words, "“", "”"));
	list.addAll(getPairSymbolTerm(words, "‘", "’"));
	list.addAll(getPairSymbolTerm(words, "《", "》"));
	list.addAll(getPairSymbolTerm(words, "<", ">"));
	list.addAll(getPairSymbolTerm(words, "【", "】"));
	list.addAll(getPairSymbolTerm(words, "[", "]"));

	CollectionUtil.sort(list, new String[] { "index" });
	List<String> wordsList = new ArrayList<String>();
	int preIndex = 0;
	for (WordSegment ws : list) {
	    if (ws.getIndex() > preIndex) {
		wordsList.add(words.substring(preIndex, ws.getIndex()));
	    }
	    String word = ws.getWord();
	    if (!wordsList.contains(word)) {
		wordsList.add(word);
	    }
	    preIndex = ws.getIndex() + ws.getWord().length() + 2;
	}
	wordsList.add(words.substring(preIndex));
	return wordsList;
    }

    public static List<String> getIKAnalyzerTermList(String keywords) {
	return getTermList(keywords, new IKAnalyzer(true));
    }

    public static List<String> getIKAnalyzerTermList(String keywords,
	    boolean useSmart) {
	return getTermList(keywords, new IKAnalyzer(useSmart));
    }

    public static List<String> getMaxWordTermList(String keywords) {
	return getTermList(keywords, new ExtendMaxWordAnalyzer());
    }

    public static List<String> getTermList(String keywords, Analyzer analyzer) {
	List<String> termList = new ArrayList<String>();
	StringReader reader = new StringReader(keywords);
	TokenStream tokenStream = analyzer.tokenStream(null, reader);
	try {
	    CharTermAttribute charTermAttribute = (CharTermAttribute) tokenStream
		    .addAttribute(CharTermAttribute.class);
	    while (tokenStream.incrementToken()) {
		String term = charTermAttribute.toString();
		if (!termList.contains(term)) {
		    termList.add(term);
		}
	    }
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    try {
		tokenStream.end();
		tokenStream.close();
	    } catch (Exception localException) {
	    }
	}
	return termList;
    }

    public static List<String> getTermList(String words) {
	List<String> wordsList = getTermListByQuote(words);
	Set<String> quotesSet = new HashSet<String>(getTermOnlyQuotes(words));
	Set<String> termSet = new LinkedHashSet<String>();
	String ikWord;
	List<String> ikWordList = new ArrayList<String>();
	for (String word : wordsList) {
	    if ((quotesSet.contains(word)) && (!termSet.contains(word))) {
		termSet.add(word);
	    } else {
		ikWordList = getIKAnalyzerTermList(word);
		for (Iterator<?> localIterator2 = ikWordList.iterator(); localIterator2
			.hasNext();) {
		    ikWord = (String) localIterator2.next();
		    ikWord = ikWord.replaceAll("-", " ").replaceAll("\\+", " ")
			    .trim();
		    if (!StringUtil.isNullOrEmpty(ikWord)) {
			if (ValidUtil.matches(ikWord, "\\D+\\d+")) {
			    Pattern pattern = Pattern.compile("\\D+");
			    Matcher matcher = pattern.matcher(ikWord);
			    if (matcher.find()) {
				String enWord = matcher.group(0);
				String numWord = ikWord.substring(enWord
					.length());
				if (!termSet.contains(enWord.trim())) {
				    termSet.add(enWord.trim());
				}
				if (!termSet.contains(numWord)) {
				    termSet.add(numWord);
				}
			    } else if (!termSet.contains(ikWord)) {
				termSet.add(ikWord);
			    }
			} else if (ValidUtil.matches(ikWord, "\\d+\\D+")) {
			    Pattern pattern = Pattern.compile("\\d+");
			    Matcher matcher = pattern.matcher(ikWord);
			    if (matcher.find()) {
				String numWord = matcher.group(0);
				String textWord = ikWord.substring(numWord
					.length());
				if (!termSet.contains(numWord.trim())) {
				    termSet.add(numWord.trim());
				}
				if (!termSet.contains(textWord)) {
				    termSet.add(textWord);
				}
			    } else if (!termSet.contains(ikWord)) {
				termSet.add(ikWord);
			    }
			} else if (!termSet.contains(ikWord)) {
			    termSet.add(ikWord);
			}
		    }
		}
	    }
	}
	for (int localList1 = 0; localList1 < ikWordList.size(); localList1++) {
	    String ingoreString = IGNORE_WORDS[localList1];
	    if (termSet.contains(ingoreString)) {
		termSet.remove(ingoreString);
	    }
	}
	List<String> termList = new ArrayList<String>(termSet);
	return termList;
    }

    public static boolean isIngore(String word) {
	for (String ingoreString : IGNORE_WORDS) {
	    if (ingoreString.equals(word)) {
		return true;
	    }
	}
	return false;
    }

    public static String getTermString(String keywords) {
	if (StringUtil.isNullOrEmptyWithTrim(keywords)) {
	    return "";
	}
	List<String> termList = getTermList(keywords);
	return StringUtil.toString(termList, " ").trim();
    }
}
