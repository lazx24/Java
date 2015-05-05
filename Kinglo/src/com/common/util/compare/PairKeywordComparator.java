package com.common.util.compare;

import java.util.Comparator;

/**
 * 
 * 类的描述:关键字排序比较器
 * 创建时间:2015-5-5
 * 创建人:邹建华
 */
public class PairKeywordComparator implements Comparator<PairKeyword> {

    @Override
    public int compare(PairKeyword keyword0, PairKeyword keyword1) {
        return keyword0.getIndex() > keyword1.getIndex() ? 1 : -1;
    }
}
