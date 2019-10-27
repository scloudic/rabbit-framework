package com.rabbitfragmework.dbase.test.core;

import java.io.IOException;
import java.io.Reader;

import com.rabbitframework.dbase.RabbitDbaseFactory;
import com.rabbitframework.dbase.RabbitDbaseFactoryBuilder;
import com.rabbitframework.dbase.dataaccess.SqlDataAccess;
import com.tjzq.commons.utils.ResourceUtils;

public class DataAccessTestCase extends AbstractDbaseTestCase {
	private SqlDataAccess sqlDataAccess;
	
	@Override
	protected void initDbase() throws IOException {
		RabbitDbaseFactoryBuilder builder = new RabbitDbaseFactoryBuilder();
		Reader reader;
		reader = ResourceUtils.getResourceAsReader("dbaseConfig.xml");
		RabbitDbaseFactory dbaseFactory = builder.build(reader);
		sqlDataAccess = dbaseFactory.openSqlDataAccess();
	}

	public <T> T getMapper(Class<T> clazz) {
		return sqlDataAccess.getMapper(clazz);
	}
}
