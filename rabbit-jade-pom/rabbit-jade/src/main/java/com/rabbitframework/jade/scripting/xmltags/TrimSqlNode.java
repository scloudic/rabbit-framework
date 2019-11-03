package com.rabbitframework.jade.scripting.xmltags;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.scripting.DynamicContext;

import java.util.*;

/**
 * trim节点
 * <p/>
 * 例：
 * &lt;trim prefix=&quot(&quot suffix=&quot)&quot prefixOverrides=&quotand | or&quot suffixOverrides=&quotand | or&quot&gt;
 * &lt;/trim&gt;
 * <p/>
 * prefix:在sql语句添加&quot;(&quot;
 * prefixOverrides:须与prefix属性配合使用，如果sql语句以and或or打头，用prefix属性覆盖
 * suffix:在sql语句尾添加&quot;)&quot;
 * suffixOverrides: 须与suffix属性配合使用，如果sql语句以and或or结尾，用suffix属性覆盖
 */
public class TrimSqlNode implements SqlNode {
    private Configuration configuration;
    private SqlNode contentSqlNode;
    private String prefix;
    private List<String> prefixesToOverride;
    private String suffix;
    private List<String> suffixesToOverride;

    public TrimSqlNode(Configuration configuration, SqlNode contentSqlNode,
                       String prefix, String prefixesToOverride,
                       String suffix, String suffixesToOverride) {
        this(configuration, contentSqlNode,
                prefix, parseOverrides(prefixesToOverride),
                suffix, parseOverrides(suffixesToOverride));
    }

    protected TrimSqlNode(Configuration configuration, SqlNode contentSqlNode,
                          String prefix, List<String> prefixesToOverride,
                          String suffix, List<String> suffixesToOverride) {
        this.configuration = configuration;
        this.contentSqlNode = contentSqlNode;
        this.prefix = prefix;
        this.prefixesToOverride = prefixesToOverride;
        this.suffix = suffix;
        this.suffixesToOverride = suffixesToOverride;
    }

    private static List<String> parseOverrides(String overrides) {
        if (overrides != null) {
            final StringTokenizer parser = new StringTokenizer(overrides, "|", false);
            return new ArrayList<String>() {
                {
                    while (parser.hasMoreTokens()) {
                        add(parser.nextToken().toUpperCase(Locale.ENGLISH));
                    }
                }
            };
        }
        return Collections.emptyList();
    }

    @Override
    public boolean apply(DynamicContext context) {
        FilteredDynamicContext filteredDynamicContext = new FilteredDynamicContext(context);
        boolean result = contentSqlNode.apply(filteredDynamicContext);
        filteredDynamicContext.applyAll();
        return result;
    }

    private class FilteredDynamicContext extends DynamicContext {
        private DynamicContext delegate;
        private boolean prefixApplied;
        private boolean suffixApplied;
        private StringBuilder sqlBuilder;

        public FilteredDynamicContext(DynamicContext dynamicContext) {
            super(configuration, null);
            this.delegate = dynamicContext;
            this.prefixApplied = false;
            this.suffixApplied = false;
            this.sqlBuilder = new StringBuilder();
        }

        public void applyAll() {
            sqlBuilder = new StringBuilder(sqlBuilder.toString().trim());
            String trimmedUpperCaseSql = sqlBuilder.toString().toUpperCase(Locale.ENGLISH);
            if (trimmedUpperCaseSql.length() > 0) {
                applyPrefix(sqlBuilder, trimmedUpperCaseSql);
                applySuffix(sqlBuilder, trimmedUpperCaseSql);
            }
            delegate.appendSql(sqlBuilder.toString());
        }

        @Override
        public Map<String, Object> getBindings() {
            return delegate.getBindings();
        }

        @Override
        public void bind(String name, Object value) {
            delegate.bind(name, value);
        }

        @Override
        public int getUniqueNumber() {
            return delegate.getUniqueNumber();
        }

        @Override
        public void appendSql(String sql) {
            sqlBuilder.append(sql);
        }

        @Override
        public String getSql() {
            return delegate.getSql();
        }

        private void applyPrefix(StringBuilder sql, String trimmedUpperCaseSql) {
            if (!prefixApplied) {
                prefixApplied = true;
                if (prefixesToOverride != null) {
                    for (String toRemove : prefixesToOverride) {
                        if (trimmedUpperCaseSql.startsWith(toRemove)) {
                            sql.delete(0, toRemove.trim().length());
                            break;
                        }
                    }
                }
                if (prefix != null) {
                    sql.insert(0, " ");
                    sql.insert(0, prefix);
                }
            }
        }

        private void applySuffix(StringBuilder sql, String trimmedUpperCaseSql) {
            if (!suffixApplied) {
                suffixApplied = true;
                if (suffixesToOverride != null) {
                    for (String toRemove : suffixesToOverride) {
                        if (trimmedUpperCaseSql.endsWith(toRemove)
                                || trimmedUpperCaseSql.endsWith(toRemove.trim())) {
                            int start = sql.length() - toRemove.trim().length();
                            int end = sql.length();
                            sql.delete(start, end);
                            break;
                        }
                    }
                }
                if (suffix != null) {
                    sql.append(" ");
                    sql.append(suffix);
                }
            }
        }
    }
}
