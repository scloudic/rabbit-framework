package com.rabbitframework.jbatis.builder;

import java.util.List;

import com.rabbitframework.jbatis.dataaccess.KeyGenerator;
import com.rabbitframework.jbatis.mapping.MappedStatement;
import com.rabbitframework.jbatis.mapping.SqlCommendType;
import com.rabbitframework.jbatis.scripting.LanguageDriver;
import com.rabbitframework.jbatis.scripting.SqlSource;
import org.springframework.jdbc.core.RowMapper;

import com.rabbitframework.jbatis.cache.Cache;

public class MapperBuilderAssistant extends BaseBuilder {
    private String catalog;

    public MapperBuilderAssistant(Configuration configuration) {
        super(configuration);
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalog() {
        return catalog;
    }

    public void addMappedStatement(String mappedStatementId, SqlCommendType sqlCommendType, Cache cache,
                                   String[] cacheKey, SqlSource sqlSource, LanguageDriver languageDriver, List<KeyGenerator> keyGenerators,
                                   RowMapper rowMapper, boolean batchUpdate) {
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, mappedStatementId,
                sqlCommendType, catalog);
        statementBuilder.cacheKey(cacheKey);
        statementBuilder.cache(cache);
        statementBuilder.sqlSource(sqlSource);
        statementBuilder.languageDriver(languageDriver);
        statementBuilder.keyGenerators(keyGenerators);
        statementBuilder.batchUpdate(batchUpdate);
        statementBuilder.rowMapper(rowMapper);
        configuration.addMappedStatement(statementBuilder.build());
    }

}
