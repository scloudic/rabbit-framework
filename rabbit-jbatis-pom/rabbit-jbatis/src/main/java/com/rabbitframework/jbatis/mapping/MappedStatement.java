package com.rabbitframework.jbatis.mapping;

import java.util.ArrayList;
import java.util.List;

import com.rabbitframework.jbatis.dataaccess.KeyGenerator;
import com.rabbitframework.jbatis.scripting.LanguageDriver;
import com.rabbitframework.jbatis.scripting.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.rabbitframework.jbatis.builder.Configuration;
import com.rabbitframework.jbatis.cache.Cache;

/**
 * mapper声明类
 */
public class MappedStatement {
    private String id;
    private Configuration configuration;
    private SqlSource sqlSource;
    private LanguageDriver languageDriver;
    private SqlCommendType sqlCommendType;
    private Logger statementLogger;
    private String catalog;
    private List<KeyGenerator> keyGenerators;
    private RowMapper rowMapper;
    private String[] cacheKey;
    private Cache cache;
    private boolean batchUpdate;

    private MappedStatement() {

    }

    public List<KeyGenerator> getKeyGenerators() {
        return keyGenerators;
    }

    public String getId() {
        return id;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public LanguageDriver getLanguageDriver() {
        return languageDriver;
    }

    public SqlCommendType getSqlCommendType() {
        return sqlCommendType;
    }

    public Logger getStatementLogger() {
        return statementLogger;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getCatalog() {
        return catalog;
    }

    public BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject, rowBounds);
        return boundSql;
    }

    public RowMapper getRowMapper() {
        return rowMapper;
    }

    public String[] getCacheKey() {
        return cacheKey;
    }

    public Cache getCache() {
        return cache;
    }

    public boolean isBatchUpdate() {
        return batchUpdate;
    }

    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id,
                       SqlCommendType sqlCommendType, String catalog) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommendType = sqlCommendType;
            mappedStatement.catalog = catalog;
            mappedStatement.statementLogger = LoggerFactory.getLogger(id);
            mappedStatement.keyGenerators = new ArrayList<KeyGenerator>();
        }

        public Builder keyGenerators(List<KeyGenerator> keyGenerators) {
            if (keyGenerators != null && keyGenerators.size() > 0) {
                mappedStatement.keyGenerators.addAll(keyGenerators);
            }
            return this;
        }

        public Builder languageDriver(LanguageDriver languageDriver) {
            mappedStatement.languageDriver = languageDriver;
            return this;
        }

        public Builder rowMapper(RowMapper rowMapper) {
            mappedStatement.rowMapper = rowMapper;
            return this;
        }

        public Builder batchUpdate(boolean batchUpdate) {
            mappedStatement.batchUpdate = batchUpdate;
            return this;
        }

        public Builder sqlSource(SqlSource sqlSource) {
            mappedStatement.sqlSource = sqlSource;
            return this;
        }

        public Builder cacheKey(String[] cacheKey) {
            mappedStatement.cacheKey = cacheKey;
            return this;
        }

        public Builder cache(Cache cache) {
            mappedStatement.cache = cache;
            return this;
        }

        public MappedStatement build() {
            return mappedStatement;
        }

    }

}
