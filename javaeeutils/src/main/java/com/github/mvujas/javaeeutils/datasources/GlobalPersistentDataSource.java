package com.github.mvujas.javaeeutils.datasources;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class GlobalPersistentDataSource implements PersistentDataSource {

	private static final GlobalPersistentDataSource instance = 
			new GlobalPersistentDataSource();
	
	public static GlobalPersistentDataSource getInstance() {
		return instance;
	}
	
	private EntityManagerFactory entityManagerFactory;
	
	private GlobalPersistentDataSource() {}
	
	
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public EntityManager getEnityManager() {
		if(entityManagerFactory == null) {
			throw new NullPointerException("Entity manager factory is not initialized!");
		}
		return entityManagerFactory.createEntityManager();
	}

}
