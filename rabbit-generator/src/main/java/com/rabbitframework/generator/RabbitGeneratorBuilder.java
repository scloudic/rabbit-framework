package com.rabbitframework.generator;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import com.rabbitframework.generator.builder.Configuration;
import com.rabbitframework.generator.builder.XMLConfigBuilder;
import com.rabbitframework.generator.exceptions.GeneratorException;

public class RabbitGeneratorBuilder {
	public RabbitGenerator build(Reader reader) {
		return build(reader, null);
	}

	public RabbitGenerator build(Reader reader, Properties properties) {
		try {
			XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader,
					properties);
			return build(xmlConfigBuilder.parse());
		} catch (Exception e) {
			throw new GeneratorException("Error building RabbitGenerator.", e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}

	public RabbitGenerator build(InputStream inputStream) {
		return build(inputStream, null);
	}

	public RabbitGenerator build(InputStream inputStream,
			Properties properties) {
		try {
			XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(
					inputStream, properties);
			return build(xmlConfigBuilder.parse());
		} catch (Exception e) {
			throw new GeneratorException("Error building RabbitGenerator.", e);
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}
	}

	public RabbitGenerator build(Configuration configuration) {
		return new RabbitGenerator(configuration);
	}
}
