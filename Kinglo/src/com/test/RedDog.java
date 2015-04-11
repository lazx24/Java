package com.test;

import java.util.ArrayList;
import java.util.List;

public class RedDog extends Dog<Student> implements having<Teacher>{
    
    public static List<String> array;
    
    public static List<String> getObject(){
	return new ArrayList<String>();
    }
    
    public static List<String> getParam(List<Student> list){
	return null;
    }
}
