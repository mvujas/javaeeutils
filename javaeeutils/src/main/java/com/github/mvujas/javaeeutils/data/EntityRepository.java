package com.github.mvujas.javaeeutils.data;

import com.github.mvujas.javaeeutils.datasources.GlobalPersistentDataSource;
import com.github.mvujas.javaeeutils.datasources.PersistentDataSource;

public abstract class EntityRepository<T, K> {
	
	private PersistentDataSource dataSource;

	public EntityRepository(PersistentDataSource dataSource) {
		super();
		this.dataSource = dataSource;
		if(this.dataSource == null) {
			this.dataSource = GlobalPersistentDataSource.getInstance();
		}
	}
	
}
