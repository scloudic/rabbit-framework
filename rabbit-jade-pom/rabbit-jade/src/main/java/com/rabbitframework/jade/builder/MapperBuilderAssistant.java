package com.rabbitframework.jade.builder;

import java.util.List;

import com.rabbitframework.jade.dataaccess.KeyGenerator;
import com.rabbitframework.jade.mapping.MappedStatement;
import com.rabbitframework.jade.mapping.SqlCommendType;
import com.rabbitframework.jade.scripting.LanguageDriver;
import com.rabbitframework.jade.scripting.SqlSource;
import org.springframework.jdbc.core.RowMapper;

import com.rabbitframework.jade.cache.Cache;

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
