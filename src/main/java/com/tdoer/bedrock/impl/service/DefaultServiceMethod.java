/*
 * Copyright 2017-2019 T-Doer (tdoer.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tdoer.bedrock.impl.service;

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.definition.service.ServiceMethodDefinition;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultServiceMethod implements ServiceMethod {

    private ServiceMethodDefinition definition;

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private ContextPath contextPath;

    public DefaultServiceMethod(ServiceMethodDefinition definition, ContextPath contextPath){
        Assert.notNull(definition, "ServiceMethodDefinition cannot be null");

        this.definition = definition;
        this.contextPath = contextPath;
    }

    /**
     * The Id of the service who provides the service method
     *
     * @return
     */
    @Override
    public String getServiceId() {
        return definition.getServiceId();
    }

    /**
     * Get provider method's name
     *
     * @return
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * Get provider method's HTTP method, for example, POST, GET, DELETE, UPDATE etc..
     *
     * @return
     */
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.resolve(definition.getMethod());
    }

    /**
     * Get provider methods'a request URI
     *
     * @return
     */
    @Override
    public String getURI() {
        return definition.getUri();
    }

    /**
     * Get the service methond's Id
     *
     * @return
     */
    @Override
    public Long getId() {
        return definition.getId();
    }

    /**
     * Product Id, to which the service method belongs
     *
     * @return Product Id, it may be {@code Null}
     */
    @Override
    public String getProductId() {
        return definition.getProductId();
    }

    /**
     * Tenant Id, to which the service method belongs to
     *
     * @return Tenant Id, it may be {@code Null}
     */
    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    /**
     * Client Id, to which the service method belongs to
     *
     * @return Client Id, it may be {@code Null}
     */
    @Override
    public String getClientId() {
        return definition.getClientId();
    }

    /**
     * Context path, to which the service method belongs to
     *
     * @return Context path, it may be {@code Null}
     */
    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }

    @Override
    public boolean match(String httpMethod, String path) {
        if(!antPathMatcher.match(definition.getUri(), path)){
            return false;
        }

        if(getHttpMethod() == null || getHttpMethod().matches(httpMethod)){
            return true;
        }

        return false;
    }
}
