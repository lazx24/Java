package com.test.collection;

import org.junit.Test;

import com.coscon.shipsuite.common.util.generic.Queue;

public class QueueTest {

	@Test
	public void testAdd() {
		Queue queue=new Queue();
		System.out.println("队列中是否没有元素:"+queue.isEmpty());
		String str="abc";
		queue.add(str);
		queue.add(str);
		System.out.println("获取队首元素:"+queue.peek());
//		System.out.println(queue.pop());//获取队首数据并删除
//		System.out.println(queue.remove());
		System.out.println(queue.indexOf(str));
		System.out.println(queue.lastIndexOf(str));
		System.out.println("清空队列中的所有信息");
		queue.clear();
	}
}
