package com.test.util.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

import com.common.util.compare.PairKeyword;
import com.common.util.compare.PairKeywordComparator;

public class PairKeywordComparatorTest {

	@Test
	public void testCompare() {
		System.out.println("对关键字进行排序");
		PairKeyword keyWord1=new PairKeyword("色情",1);
		PairKeyword keyWord2=new PairKeyword("反共",2);
		PairKeyword keyWord3=new PairKeyword("胸器",3);
		
		List<PairKeyword> list=new ArrayList<PairKeyword>();
		list.add(keyWord1);
		list.add(keyWord3);
		list.add(keyWord2);
		
		Collections.sort(list, new PairKeywordComparator());
		
		for (int i = 0; i < list.size(); i++) {
			PairKeyword keyword=list.get(i);
			System.out.println(keyword.getIndex()+"-"+keyword.getName());
		}
	}

}
