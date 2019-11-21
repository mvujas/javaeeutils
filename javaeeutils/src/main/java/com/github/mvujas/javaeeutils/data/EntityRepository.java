package com.github.mvujas.javaeeutils.data;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.github.mvujas.javaeeutils.datasources.GlobalPersistentDataSource;
import com.github.mvujas.javaeeutils.datasources.PersistentDataSource;

public abstract class EntityRepository<T, K> {
	
	private PersistentDataSource dataSource;
	private Class<T> entityClass;
	private String className;
	
	public EntityRepository(PersistentDataSource dataSource) {
		initializeRepository(dataSource);
	}
	
	public EntityRepository() {
		this(null);
	}
	
	public final void initializeRepository(PersistentDataSource dataSource) {
		this.dataSource = dataSource;
		if(this.dataSource == null) {
			this.dataSource = GlobalPersistentDataSource.getInstance();
		}
		
		initializeGenericClass();
		System.out.println(entityClass.toString());
	}
	
	@SuppressWarnings("unchecked")
	private void initializeGenericClass() {
		if(entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass()
	                .getGenericSuperclass()).getActualTypeArguments()[0];
			className = entityClass.getName();
		}
	}
	  
	protected final <R> R safeTransactionBoilerplate(Function<EntityManager, R> transactionalFunction) throws Exception {
		EntityManager entityManager = dataSource.getEnityManager();
		EntityTransaction transaction = null;
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			
			R result = transactionalFunction.apply(entityManager);
			
			transaction.commit();
			return result;
		}
		catch(Exception e) {
			System.out.print("Transaction block excpetion: ");
			e.printStackTrace();
			if(transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
		finally {
			entityManager.close();
		}
	}
	
	public T save(final T entity) {
		try {
			// this somehow works as a way to save as well as to update the entity
			return safeTransactionBoilerplate(em -> em.merge(entity));
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		try {
			return safeTransactionBoilerplate(em -> {
				return em.createQuery(String.format("select e from %s e", className)).getResultList();
			});
		} catch(Exception e) {
			return null;
		}
	}
	
	public T getById(K key) {
		EntityManager entityManager = dataSource.getEnityManager();
		
		T result = entityManager.find(entityClass, key);
		
		entityManager.close();
		return result;
	}
	
	@Deprecated
	public boolean delete(final T entity) {
		try {
			safeTransactionBoilerplate(em -> {
				T newEntity = em.merge(entity);
				em.remove(newEntity);
				return null;
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	
}
