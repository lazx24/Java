package com.common.util.compare;

import java.util.Comparator;

/**
 * 关键字排序比较器
 * @author zou
 * 2015-1-27
 */
public class PairKeywordComparator implements Comparator<PairKeyword> {

    @Override
    public int compare(PairKeyword keyword0, PairKeyword keyword1) {
        return keyword0.getIndex() > keyword1.getIndex() ? 1 : -1;
    }

}
