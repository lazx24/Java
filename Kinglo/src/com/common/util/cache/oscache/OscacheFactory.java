package com.common.util.cache.oscache;


import com.common.log.ISystemLogger;
import com.common.log.LoggerFactory;

public class OscacheFactory {

	@SuppressWarnings("unused")
	private static final ISystemLogger log = LoggerFactory.getSystemLogger(OscacheFactory.class);
	
	private OscacheExtends oscacheExtends;
	private static final OscacheFactory oscacheFactory = new OscacheFactory(); 
	
	private OscacheFactory() {
		oscacheExtends = OscacheExtends.getInstance();
	}
	
	public static OscacheFactory getInstance() {
        return oscacheFactory;
    }
	
	//缓存一个对象
	public void putObject(String key, Object value) {
		oscacheExtends.put(key, value);
	}
	
	//获取一个对象
	public Object getObject(String key) {
		try {
			return oscacheExtends.get(key);
		} catch (Exception e) {
			System.out.println("getObject("+key+"):"+e.getMessage());
			return null;
		}
	} 
	
	//删除一个对象
	public void removeObject(String key) {
		oscacheExtends.remove(key);
	} 
	
	//删除所有对象
	public void removeAllNews() {
		oscacheExtends.removeAll();
	} 
}
