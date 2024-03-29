package com.scloudic.rabbitframework.jbatis.spring;

import static org.springframework.util.Assert.notNull;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.dataaccess.SqlDataAccess;
import com.scloudic.rabbitframework.jbatis.RabbitJbatisFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * BeanFactory that enables injection of MyBatis mapper interfaces. It can be
 * set up with a SqlSessionFactory or a pre-configured SqlSessionTemplate.
 * <p>
 * Sample configuration:
 *
 * <pre class="code">
 * {@code
 *   <bean id="baseMapper" class="com.scloudic.rabbitframework.jbatis.spring.MapperFactoryBean" abstract="true" lazy-init="true">
 *     <property name="rabbitJbatisFactory" ref="rabbitJbatisFactory" />
 *   </bean>
 *
 *   <bean id="oneMapper" parent="baseMapper">
 *     <property name="mapperInterface" value="my.package.MyMapperInterface" />
 *   </bean>
 *
 *   <bean id="anotherMapper" parent="baseMapper">
 *     <property name="mapperInterface" value="my.package.MyAnotherMapperInterface" />
 *   </bean>
 * }
 * </pre>
 * <p>
 * Note that this factory can only inject <em>interfaces</em>, not concrete
 * classes.
 */
public class MapperFactoryBean<T> implements FactoryBean<T>, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MapperFactoryBean.class);
    private Class<T> mapperInterface;
    private SqlDataAccess sqlDataAccess;

    public void setRabbitJbatisFactory(RabbitJbatisFactory rabbitJbatisFactory) {
        this.sqlDataAccess = rabbitJbatisFactory.openSqlDataAccess();
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    protected void checkMapperConfig() {

        notNull(this.mapperInterface, "Property 'mapperInterface' is required");

        Configuration configuration = sqlDataAccess.getConfiguration();
        if (!configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Throwable t) {
                logger.error("Error while adding the mapper '"
                        + this.mapperInterface + "' to configuration.", t);
                throw new IllegalArgumentException(t);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public T getObject() throws Exception {
        return sqlDataAccess.getMapper(this.mapperInterface);
    }

    /**
     * {@inheritDoc}
     */
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        checkMapperConfig();
    }

}
