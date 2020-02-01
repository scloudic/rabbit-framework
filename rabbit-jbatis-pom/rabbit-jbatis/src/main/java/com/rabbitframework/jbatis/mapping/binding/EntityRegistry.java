package com.rabbitframework.jbatis.mapping.binding;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rabbitframework.jbatis.annontations.Table;
import com.rabbitframework.jbatis.builder.Configuration;
import com.rabbitframework.jbatis.builder.EntityBuilder;
import com.rabbitframework.jbatis.mapping.EntityMap;

/**
 * 注册实体类
 *
 * @author Justin Liang
 *
 *
 */
public class EntityRegistry {
	private final Map<String, EntityMap> entityMaps = new HashMap<String, EntityMap>();
	private final EntityBuilder parseBuilder;

	public EntityRegistry(Configuration configuration) {
		parseBuilder = new EntityBuilder(configuration);
	}

	public void addEntity(Class<?> entity) {
		Table entityAnnotation = entity.getAnnotation(Table.class);
		if (entityAnnotation != null) {
			addEntityMap(parseBuilder.parseEntity(entity));
		}

	}

	public void addEntityMap(EntityMap entityMap) {
		if (entityMap != null) {
			entityMaps.put(entityMap.getId(), entityMap);
		}
	}

	public Collection<String> getEntityMapNames() {
		return Collections.unmodifiableCollection(entityMaps.keySet());
	}

	public Collection<EntityMap> getEntityMaps() {
		return Collections.unmodifiableCollection(entityMaps.values());
	}

	public EntityMap getEntityMap(String id) {
		return entityMaps.get(id);
	}

	public boolean hasEntityMap(String id) {
		return entityMaps.containsKey(id);
	}

}
