package com.example.fulltext;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.common.exception.ShipSuiteRuntimeException;
import com.common.util.validator.ValidUtil;

public class ExtendMaxWordAnalyzer extends Analyzer {
    private static final String numberScopePatternString = "^\\d+\\.\\d+";
    private static Analyzer ikAnalyzer = new IKAnalyzer(false);
    private static Analyzer ikAnalyzerSmart = new IKAnalyzer(true);

    public TokenStream tokenStream(String arg0, Reader reader) {
	StringBuffer sb = new StringBuffer(2048);
	char[] buffer = new char[1024];
	StringReader newReader = null;
	try {
	    while (reader.read(buffer) > 0) {
		sb.append(buffer);
	    }
	    String orginalString = sb.toString();
	    Set<String> newWordSet = new LinkedHashSet<String>(
		    FulltextUtil.getTermList(orginalString, ikAnalyzer));

	    List<String> simpleWordList = FulltextUtil.getTermList(
		    orginalString, ikAnalyzerSmart);
	    for (String w : simpleWordList) {
		if (!newWordSet.contains(w)) {
		    newWordSet.add(w);
		}
	    }
	    for (String w : newWordSet) {
		if (ValidUtil.matches(w, "^\\d+\\.\\d+")) {
		    sb.append(" ").append(
			    Math.floor(Double.valueOf(w).doubleValue()));
		} else {
		    sb.append(" ").append(w);
		}
	    }
	    newReader = new StringReader(sb.toString());
	    buffer = (char[]) null;
	    sb = null;

	    return ikAnalyzer.tokenStream(null, newReader);
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
}
