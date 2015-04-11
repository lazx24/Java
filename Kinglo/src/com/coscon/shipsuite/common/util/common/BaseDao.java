package com.coscon.shipsuite.common.util.common;

import java.util.List;
public interface BaseDao<T>{
	
public boolean save(T obj);
	
	public boolean update(T obj);
	
	public boolean delete(Integer id);
	
	public boolean delete(String id);
	
	public T findById(Integer id);
	
	public T findById(String id);
	
	public boolean deleteByIdList(Integer[] idList);
	
	public List<T> findAll();
	
	public List<T> findAll(String hql,Object[] value);
	
	public List<T> findByPage(final String hql,final int beginIndex,final int pageSize);
	
	public List<T> findByPage(final String hql,Object[] paras,final int beginIndex,final int pageSize);
	
	public Integer findByPage(String hql,Object[] paras);
}
