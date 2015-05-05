package com.common.util.compare;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 类的描述:用来解决字段的组合排列问题的比较器
 * 创建时间:2015-5-5
 * 创建人:邹建华	
 * @param <T>
 */
public class CompositeComparator<T> implements Comparator<T> {

    /**
     * 比较器列表, 越排在列表前面的比较器优先级越高
     */
    private final List<Comparator<T>> comparatorList = new LinkedList<Comparator<T>>();

    
    /**
     * 获取比较器列表
     * @return
     */
    public List<Comparator<T>> getComparatorList() {
        return comparatorList;
    }

    /**
     * 添加一个比较器到比较器列表中
     * @param comparator
     */
    public void addComparator(Comparator<T> comparator) {
        if (comparator == null) {
            return;
        }

        comparatorList.add(comparator);
    }

    /**
     * 添加多个比较器到比较器列表中
     * @param comparators
     */
    public void addComparators(Comparator<T>[] comparators) {
        if (comparators == null) {
            return;
        }

        for (int i = 0; i < comparators.length; i++) {
            comparatorList.add(comparators[i]);
        }
    }

    /**
     * 如果两对象比较结果相等, 则会继续使用其次优先级的比较器进行比较
     */
    @Override
    public int compare(T o1, T o2) {
        for (Comparator<T> comparator : comparatorList) {
            int result = comparator.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
