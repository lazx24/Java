package com.common.util.generic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.common.util.string.StringUtil;
import com.test.Student;

public class CollectionUtilTest {
    LinkedHashMap<String, Object> map = new LinkedHashMap<String,Object>();

    @Before
    public void setUp() throws Exception {
	for (Integer i = Integer.valueOf(0); i.intValue() < 20; i = Integer
		.valueOf(i.intValue() + 1)) {
	    this.map.put(i.toString(), i);
	}
    }

    @Test
    public void headMap() {
	int index = 10;
	LinkedHashMap<String, Object> headMap = CollectionUtil.headMap(
		this.map, index);

	Assert.assertEquals(index, headMap.size());
	for (String s : headMap.keySet()) {
	    System.out.println(s);
	}
	System.out.println("------------");
	headMap = CollectionUtil.headMap(this.map, index, true);

	Assert.assertEquals(index + 1, headMap.size());
	for (String s : headMap.keySet()) {
	    System.out.println(s);
	}
	System.out.println("------------");
    }

    @Test
    public void tailMap() {
	int index = 10;
	LinkedHashMap<String, Object> tailMap = CollectionUtil.tailMap(
		this.map, index);

	Assert.assertEquals(this.map.size() - index, tailMap.size());
	for (String s : tailMap.keySet()) {
	    System.out.println(s);
	}
	System.out.println("------------");
	tailMap = CollectionUtil.tailMap(this.map, index, false);

	Assert.assertEquals(this.map.size() - index - 1, tailMap.size());
	for (String s : tailMap.keySet()) {
	    System.out.println(s);
	}
	System.out.println("------------");
    }

    @Test
    public void sort() {
	RootObject r1 = new RootObject();
	RootObject r2 = new RootObject();
	RootObject r3 = new RootObject();
	RootObject r4 = new RootObject();

	r1.setId("1");
	r1.setName("上海万升国际代理有限公司");
	r1.setNick("nick5");

	r2.setId("3");
	r2.setName("上海万硕国际代理有限公司");
	r2.setNick("nick3");

	r3.setId("2");
	r3.setName("上海东菱国际货物运输代理有限公司");
	r3.setNick("nick2");

	r4.setId("4");
	r4.setName("上海互洋国际货代理有限公司");
	r4.setNick("nick1");

	List<RootObject> rootList = new ArrayList<RootObject>();
	rootList.add(r1);
	rootList.add(r2);
	rootList.add(r3);
	rootList.add(r4);

	CollectionUtil.sort(rootList, new String[] { "name", "nick" });
	for (RootObject root : rootList) {
	    System.out.println(root.getId() + "-" + root.getName() + "-"
		    + root.getNick());
	}
	
	
	Map<String,Object> sortMap=new HashMap<String,Object>();
	sortMap.put("1", r1);
	sortMap.put("10", r2);
	sortMap.put("5", r3);
	sortMap.put("7", r4);
	
	Map rtMap=CollectionUtil.sort(sortMap, new String[]{"nick"});
	System.out.println(rtMap);
    }

    @Test
    public void maxMin() {
	RootObject r1 = new RootObject();
	RootObject r2 = new RootObject();
	RootObject r3 = new RootObject();
	RootObject r4 = new RootObject();

	r1.setId("1");
	r1.setName("上海万升国际代理有限公司");
	r1.setNick(null);
	r1.setDatetime(new Date());

	r2.setId("2");
	r2.setName("上海万硕国际代理有限公司");
	r2.setNick("nick3");
	r2.setDatetime(DateUtil.addDay(new Date(), -1));

	r3.setId("9");
	r3.setName("上海东菱国际货物运输代理有限公司");
	r3.setNick("nick2");
	r3.setDatetime(DateUtil.addDay(new Date(), 1));

	r4.setId("3");
	r4.setName("上海互洋国际货代理有限公司");
	r4.setNick("nick2");
	r4.setDatetime(new Date());

	List<RootObject> rootList = new ArrayList();
	rootList.add(r1);
	rootList.add(r2);
	rootList.add(r3);
	rootList.add(r4);

	RootObject rootMax = (RootObject) CollectionUtil.max(rootList,
		"datetime");

	System.out.println(rootMax.getId() + "-" + rootMax.getName() + "-"
		+ rootMax.getDatetime());

	RootObject rootMin = (RootObject) CollectionUtil.min(rootList,
		"datetime");

	System.out.println(rootMin.getId() + "-" + rootMin.getName() + "-"
		+ rootMin.getDatetime());
    }
    
    @Test
    public void compareContents(){
	Student s=new Student();
	Student s2=new Student();
	Map map1=new HashMap();
	map1.put("a", s);
	Map map2=new HashMap();
	map2.put("a", s2);
	
	boolean result=CollectionUtil.compareContents(map1, map2);
	System.out.println(result);
    }
    
    @Test
    public void convertListToMap(){
	List<Student> list=new ArrayList<Student>();
	Student s=new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("zoujianhua");
	list.add(s);
	Map map=CollectionUtil.convertListToMap(list, "name");
	System.out.println(map);
    }
    
    @Test
    public void getIndexListByKeys(){
	Map map=new HashMap();
	map.put("1", "1");
	map.put("2", "2");
	map.put("2", "4");
	
	List<Integer> list=CollectionUtil.getIndexListByKeys(map, new Object[]{"2"});
	System.out.println(StringUtil.toString(list));
    }
    
    @Test
    public void replaceSubList(){
	List list=new ArrayList();
	list.add("1");
	list.add("2");
	list.add("3");
	list.add("4");
	list.add("5");
	
	List subList=new ArrayList();
	subList.add("6");
	subList.add("7");
	subList.add("8");
	
	
	
	CollectionUtil.replaceSubList(list, subList, 0, 1);
	System.out.println(StringUtil.toString(list));
    }
    
    @Test
    public void reverseKeyValue(){
	Map sortMap=new HashMap();
	sortMap.put("1", "1");
	sortMap.put("2", "2");
	sortMap.put("6", "4");
	
	System.out.println(CollectionUtil.reverseKeyValue(sortMap));
    }
    
}
