package com.common.util.common;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 
 * 类的描述:Hibernate辅助类
 * 创建人:邹建华
 * 创建时间:2015-4-12
 */
public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {
	
	private Class<T> clazz;

	//作用：得到T在实际的XXXDaoImpl.java类中是什么具体的entity
	public BaseDaoImpl() {
		//ParameterizedType 表示参数化类型
		ParameterizedType pt = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		/*getActualTypeArguments[0]的作用就是得到泛型参数的类型的第一个参数
		 *比如：Test<T>,得到T的类型
		*/
		clazz = (Class<T>) pt.getActualTypeArguments()[0];
	}

	
	public boolean save(T obj) {
		boolean b = true;
		try {
			getHibernateTemplate().save(obj);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}


	public boolean update(T obj) {
		boolean b = true;
		try {
			getHibernateTemplate().saveOrUpdate(obj);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	
	public boolean delete(Integer id) {
		boolean b = true;
		try {
			T t = (T)this.getHibernateTemplate().get(clazz, id);
			getHibernateTemplate().delete(t);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}
	
	public boolean delete(String id) {
		boolean b = true;
		try {
			T t = (T)this.getHibernateTemplate().get(clazz, id);
			getHibernateTemplate().delete(t);
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}
	
	public List<T> findAll() {
		List list = getHibernateTemplate().find(
				"from " + clazz.getName());
		return list;
	}

	public List<T> findAll(String hql,Object... value){
		List list = getHibernateTemplate().find(hql,value);
		return list;
	}
	
	
	public T findById(Integer id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	public T findById(String id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}
	
	/*
	 *  注意：使用doInHibernate中传递的参数前面一般要加上final关键字 
	 *  输出语句：delete from t_user where id in (? , ? , ? , ? , ?)
	 */
	public boolean deleteByIdList(final Integer[] idList) {
		boolean b = true;
		try {
			final String hql = "delete from " + clazz.getName() + " where id in (:idList)";
			//调用HibernateCallback的目的,在里面可以获取session对象,从而实现很多实现不了的功能
			getHibernateTemplate().execute(new HibernateCallback() {
				
				public Object doInHibernate(Session session) throws HibernateException,
						SQLException {
					Query query = session.createQuery(hql)
									.setParameterList("idList", idList);
					query.executeUpdate();
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			b =false;
		}
		return b;
	}
	
	/*
	 * 分页
	 * String hql ：要执行的hql
	 * int beginIndex: 从第几条开始
	 * int pageSize: 每页显示多少条
	 */
	public List<T> findByPage(final String hql,final int beginIndex,final int pageSize){
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				//此方法查询后返回list结果
				List result =session.createQuery(hql).setFirstResult((beginIndex-1)*pageSize)
										.setMaxResults(pageSize).list();
				return result;
			}
		});
		return list;
	}
	
	/*
	 * hql  
	 * paras 参数
	 * beginIndex 开始页
	 * pageSize  页大小
	 */
	public List findByPage(final String hql,final Object[] paras,final int beginIndex,final int pageSize){
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
				SQLException {
				//此方法查询后返回list结果
				Query query = session.createQuery(hql);
				if(paras!=null){
					for(int i =0;i<paras.length;i++){
						query.setParameter(i, paras[i]);
					}
				}
				List result = query.setFirstResult(beginIndex).setMaxResults(pageSize).list();
				return result;
			}
		});
		return list;
	}
	
	//获取总记录九
	public Integer findByPage(final String hql,final Object[] paras){
		Object obj =getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createQuery(hql);
				if(paras!=null){
					for(int i =0;i<paras.length;i++){
						query.setParameter(i, paras[i]);
					}
				}
				return query.uniqueResult();
			}
		});
		return (Integer)obj;
	}
}
