package com.test.util.html;

import static org.junit.Assert.*;

import org.junit.Test;

import com.common.util.html.JsonUtil;
import com.test.Student;

public class JsonUtilTest {

    @Test
    public void testBean2JsonObject() {
	Student s=new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("zoujianhua");
	System.out.println(JsonUtil.bean2JsonObject(s));
    }

    @Test
    public void testBean2JsonObjectString() {
	Student s=new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("zoujianhua");
	System.out.println(JsonUtil.bean2JsonObjectString(s));
    }

    @Test
    public void testBean2JsonArray() {
	Student s=new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("zoujianhua");
	System.out.println(JsonUtil.bean2JsonArray(s));
    }

    @Test
    public void testBean2JsonArrayString() {
	Student s=new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("zoujianhua");
	System.out.println(JsonUtil.bean2JsonArrayString(s));
    }

    @Test
    public void testJson2Bean() {
	Student s=new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("zoujianhua");
	String json=JsonUtil.bean2JsonObjectString(s);
	System.out.println(JsonUtil.json2Bean(json, Student.class));
    }

    @Test
    public void testJson2BeanArray() {
	fail("Not yet implemented");
    }

    @Test
    public void testJson2BeanList() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetExjsFormReponseJsonExtjsFormResponse() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetExjsFormReponseJsonBoolean() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetExjsFormReponseJsonBooleanString() {
	fail("Not yet implemented");
    }

}
