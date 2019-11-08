package com.rabbitfragmework.jbatis.test.core;

import java.io.IOException;
import java.io.Reader;

import com.rabbitframework.jbatis.RabbitJbatisFactory;
import com.rabbitframework.jbatis.RabbitJbatisFactoryBuilder;
import com.rabbitframework.jbatis.dataaccess.SqlDataAccess;
import com.tjzq.commons.utils.ResourceUtils;

public class DataAccessTestCase extends AbstractDbaseTestCase {
	private SqlDataAccess sqlDataAccess;
	
	@Override
	protected void initDbase() throws IOException {
		RabbitJbatisFactoryBuilder builder = new RabbitJbatisFactoryBuilder();
		Reader reader;
		reader = ResourceUtils.getResourceAsReader("dbaseConfig.xml");
		RabbitJbatisFactory dbaseFactory = builder.build(reader);
		sqlDataAccess = dbaseFactory.openSqlDataAccess();
	}

	public <T> T getMapper(Class<T> clazz) {
		return sqlDataAccess.getMapper(clazz);
	}
}
