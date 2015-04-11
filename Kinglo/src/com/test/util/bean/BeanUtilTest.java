package com.test.util.bean;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import com.coscon.shipsuite.common.permissions.User;
import com.coscon.shipsuite.common.util.bean.BeanUtil;
import com.coscon.shipsuite.common.util.string.StringUtil;
import com.test.Student;
import com.test.Teacher;

public class BeanUtilTest
{
  
  @Test
  public void clone2(){
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      System.out.println(BeanUtil.clone(s));
  }
  
  @Test
  public void copyBeans(){
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      
      Student s2=new Student();
      System.out.println(BeanUtil.copyBeans(s, s2, new String[]{"list"}));
  }
  
  @Test
  public void copyObject(){
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      
      Student s2=new Student();
      BeanUtil.copyObject(s2,s);
      System.out.println(s2);
  }
  
  @Test
  public void deepClone(){
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      
      System.out.println(BeanUtil.deepClone(s));
  }
  
  @Test
  public void getProperties(){
      Student s = new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("zoujianhua");
      
      List<Object> list =new ArrayList<Object>();
      Teacher t =new Teacher();
      t.setId(2);
      t.setAge(25);
      t.setName("zou");
      
      list.add(t);
      
      s.setList(list);
      Object[] object=BeanUtil.getProperties(s);
      
      for (int i = 0; i < object.length; i++) {
	  System.out.println(object[i].toString());
      }
      
//      System.out.println(StringUtil.toString(BeanUtil.getProperties(BeanUtil.class)));
  }
  
  @Test
  public void getProperty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      
      System.out.println(BeanUtil.getProperty(s, "age"));
    
  }
  
  @Test
  public void getPropertyType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException{
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      
      System.out.println(BeanUtil.getPropertyType(s, "age"));
  }
  
  @Test
  public void getActualPropertyType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException{
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      
      System.out.println(BeanUtil.getActualPropertyType(s, "age"));
  }
  
  @Test
  public void setProperty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException{
      Student s=new Student();
      
      System.out.println(BeanUtil.setProperty(s, "age", 26));
      System.out.println(s.getAge());
  }
  
  @Test
  public void bean2xml(){
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      
      System.out.println(BeanUtil.bean2xml(s));
  }
  
  @Test
  public void emptyProxy(){
      Teacher s=new Teacher();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      System.out.println(BeanUtil.emptyProxy(s));
  }
  
  @Test
  public void newHashCglibEntiy(){
      System.out.println(BeanUtil.newHashCglibEntiy(Student.class));
  }
  
  @Test
  public void getPropertyByMethod(){
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      List<Object> list=new ArrayList<Object>();
      Teacher t=new Teacher();
      t.setId(2);
      t.setAge(24);
      t.setName("rr");
      list.add(t);
      s.setList(list);
      
      System.out.println(BeanUtil.getPropertyByMethod(s, "setAge"));
  }
  
  @Test
  public void getPropertyGetMethodName(){
      System.out.println(StringUtil.toString(BeanUtil.getPropertyGetMethodName("age")));
  }
  
  @Test
  public void hashCodeByProperties(){
      
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      System.out.println(BeanUtil.hashCodeByProperties(s, true));
  }
  
  @Test
  public void hashCodeByPropertyValues(){
      Map<String,Object> propertyValueMap=new HashMap<String,Object>();
      
      System.out.println(BeanUtil.hashCodeByPropertyValues(Student.class, propertyValueMap));
  }
  
  @Test
  public void newHashCglibBean(){
      System.out.println(BeanUtil.newHashCglibBean(Student.class));
  }
  
  @Test
  public void setPropertyByMethod() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException{
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      System.out.println(BeanUtil.setPropertyByMethod(s, "age", 26));
      
      System.out.println(s.getAge());
  }
  
  @Test
  public void convertBean() throws IntrospectionException, IllegalAccessException, InvocationTargetException{
      Student s=new Student();
      s.setId(1);
      s.setAge(23);
      s.setName("ff");
      
      System.out.println(BeanUtil.convertBean(s));
  }
  
  @Test
  public void getAccessUnitUuid(){
      User u =new User();
      u.setId(12);
      System.out.println(BeanUtil.getAccessUnitUuid(u,"id"));
  }
  
}
