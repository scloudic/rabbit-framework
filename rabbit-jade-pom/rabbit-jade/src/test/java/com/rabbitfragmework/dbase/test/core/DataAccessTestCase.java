package com.rabbitfragmework.dbase.test.core;

import java.io.IOException;
import java.io.Reader;

import com.rabbitframework.jade.RabbitJadeFactory;
import com.rabbitframework.jade.RabbitJadeFactoryBuilder;
import com.rabbitframework.jade.dataaccess.SqlDataAccess;
import com.tjzq.commons.utils.ResourceUtils;

public class DataAccessTestCase extends AbstractDbaseTestCase {
	private SqlDataAccess sqlDataAccess;
	
	@Override
	protected void initDbase() throws IOException {
		RabbitJadeFactoryBuilder builder = new RabbitJadeFactoryBuilder();
		Reader reader;
		reader = ResourceUtils.getResourceAsReader("dbaseConfig.xml");
		RabbitJadeFactory dbaseFactory = builder.build(reader);
		sqlDataAccess = dbaseFactory.openSqlDataAccess();
	}

	public <T> T getMapper(Class<T> clazz) {
		return sqlDataAccess.getMapper(clazz);
	}
}
