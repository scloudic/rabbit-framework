/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scloudic.rabbitframework.web.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.web.filter.sensitive.WordFilter;
import com.scloudic.rabbitframework.core.utils.StringUtils;

public class XSSFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(XSSFilter.class);

    /**
     * @see ContainerRequestFilter#filter(ContainerRequestContext)
     */
    @Override
    public void filter(ContainerRequestContext request) {
        cleanQueryParams(request);
        // cleanHeaders(request.getHeaders());
    }

    private void cleanQueryParams(ContainerRequestContext request) {
        UriBuilder builder = request.getUriInfo().getRequestUriBuilder();
        MultivaluedMap<String, String> queries = request.getUriInfo().getQueryParameters();

        for (Map.Entry<String, List<String>> query : queries.entrySet()) {
            String key = query.getKey();
            List<String> values = query.getValue();

            List<String> xssValues = new ArrayList<String>();
            for (String value : values) {
                xssValues.add(stripXSS(value));
            }

            int size = CollectionUtils.size(xssValues);
            builder.replaceQueryParam(key);

            if (size == 1) {
                String value = xssValues.get(0);
                value = value == null ? "" : value;
                builder.replaceQueryParam(key, value);
            } else if (size > 1) {
                builder.replaceQueryParam(key, xssValues.toArray());
            }
        }

        request.setRequestUri(builder.build());
    }


    private void cleanHeaders(MultivaluedMap<String, String> headers) {
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            String key = header.getKey();
            List<String> values = header.getValue();

            List<String> cleanValues = new ArrayList<String>();
            for (String value : values) {
                cleanValues.add(stripXSS(value));
            }

            headers.put(key, cleanValues);
        }
    }

    public String stripXSS(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        // try {
        // value = ESAPI.encoder().encodeForHTML(value);
        // } catch (Exception e) {
        // logger.warn(e.getMessage(),e); //
        // }

        // Use the ESAPI library to avoid encoded attacks.
        value = ESAPI.encoder().canonicalize(value);
        //
        // // Avoid null characters
        value = value.replaceAll("\0", "");
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        //
        // // Clean out HTML
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(EscapeMode.xhtml);
        outputSettings.prettyPrint(false);
        value = Jsoup.clean(value, "", Whitelist.none(), outputSettings);
        try {
            value = WordFilter.doFilter(value); // 增加敏感词
        } catch (Exception e) {
            logger.error(e.getMessage(), e); // 做日志记录
        }
        return value;
    }
}