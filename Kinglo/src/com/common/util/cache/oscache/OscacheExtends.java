package com.common.util.cache.oscache;

import java.util.Date;

import com.common.log.ISystemLogger;
import com.common.log.LoggerFactory;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

@SuppressWarnings("serial")
public class OscacheExtends extends GeneralCacheAdministrator {

	@SuppressWarnings("unused")
	private static final ISystemLogger log = LoggerFactory.getSystemLogger(OscacheExtends.class);
	
	private static final OscacheExtends oscacheExtends = new OscacheExtends("kingloOScache", 10*365*24*60*60);//10年
	
	@SuppressWarnings("unused")
	private OscacheExtends() {
		
	}
	
	public static OscacheExtends getInstance() {
        return oscacheExtends;
    }
	
	private int refreshPeriod;//过期时间(单位为秒)
	
	private String keyPrefix = "kingloOScache";//关键字前缀字符

	public OscacheExtends(String keyPrefix, int refreshPeriod) {
		super();
		this.refreshPeriod = refreshPeriod;
		this.keyPrefix = keyPrefix;
	}
	
	//添加被缓存的对象
	public void put(String key, Object value){
		this.putInCache(this.keyPrefix+"_"+key, value);
	}
	
	//删除被缓存的对象; 　　
	public void remove(String key){
		this.flushEntry(this.keyPrefix+"_"+key);
	}
	
	//删除所有被缓存的对象
	public void removeAll(Date date){ 
		this.flushAll(date);
	}
	
	//删除所有被缓存的对象
	public void removeAll(){ 
		this.flushAll();
	} 
	
	//获取被缓存的对象; 　　
	public Object get(String key) throws Exception{
		try {
			return this.getFromCache(this.keyPrefix+"_"+key,this.refreshPeriod); 
		} catch (NeedsRefreshException e) {
			this.cancelUpdate(this.keyPrefix+"_"+key);
			throw e;
		}
	} 
}
