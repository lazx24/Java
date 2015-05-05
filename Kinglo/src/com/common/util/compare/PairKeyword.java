package com.common.util.compare;

/**
 * 
 * 类的描述:关键字对   用于列表排序
 * 创建时间:2015-5-5
 * 创建人:邹建华
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
