package com.common.util.compare;

/**
 * 关键字对，用于列表排序
 * @author zou
 * 2015-1-27
 */
public class PairKeyword {

    private final String name;
    private final int index;

    public PairKeyword(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
