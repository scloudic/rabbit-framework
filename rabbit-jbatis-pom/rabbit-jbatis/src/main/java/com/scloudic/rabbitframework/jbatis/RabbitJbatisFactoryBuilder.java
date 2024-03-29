/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.scloudic.rabbitframework.jbatis;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.builder.XMLConfigBuilder;
import com.scloudic.rabbitframework.jbatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link RabbitJbatisFactory}
 */
public class RabbitJbatisFactoryBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RabbitJbatisFactoryBuilder.class);

    public RabbitJbatisFactory build(Reader reader) {
        return build(reader, null);
    }

    public RabbitJbatisFactory build(Reader reader, Properties properties) {
        try {
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader,
                    properties);
            return build(xmlConfigBuilder.parse());
        } catch (Exception e) {
            String msg = "Error building SqlSession.";
            logger.error(msg, e);
            throw new PersistenceException(msg, e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public RabbitJbatisFactory build(InputStream inputStream) {
        return build(inputStream, null);
    }

    public RabbitJbatisFactory build(InputStream inputStream,
                                     Properties properties) {
        try {
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(
                    inputStream, properties);
            return build(xmlConfigBuilder.parse());
        } catch (Exception e) {
            String msg = "Error building SqlSession.";
            logger.error(msg, e);
            throw new PersistenceException(msg, e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    public RabbitJbatisFactory build(Configuration configuration) {
        return new DefaultRabbitJbatisFactory(configuration);
    }
}
