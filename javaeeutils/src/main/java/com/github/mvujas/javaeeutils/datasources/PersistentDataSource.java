package com.github.mvujas.javaeeutils.datasources;

import javax.persistence.EntityManager;

public interface PersistentDataSource {
	EntityManager getEnityManager();
}
