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

import com.tdoer.bedrock.impl.definition.service.ServiceMethodDefinition;
import com.tdoer.bedrock.resource.ResourceCategory;
import com.tdoer.bedrock.resource.ResourceType;
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

    public DefaultServiceMethod(ServiceMethodDefinition definition){
        Assert.notNull(definition, "ServiceMethodDefinition cannot be null");

        this.definition = definition;
    }

    /**
     * Get the service methond's Id
     *
     * @return Service method's Id, must not be <code>null</code>
     */
    @Override
    public Long getId() {
        return definition.getId();
    }

    /**
     * Get the Id of the service which provides the service method
     *
     * @return Service Id, must not be <code>null</code>
     */
    @Override
    public Long getServiceId() {
        return definition.getServiceId();
    }

    /**
     * Get service method's name
     *
     * @return Service method's name, must not be blank
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * Get the method's HTTP method, for example, POST, GET, DELETE, UPDATE etc..
     *
     * @return HTTP method, must not be <code>null</code>
     */
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.resolve(definition.getMethod());
    }

    /**
     * Get the methods'a request URI, which following ant path pattern, for example,
     * "/user/*", "/user/**" etc.
     *
     * @return Method URI, must not be blank
     */
    @Override
    public String getURI() {
        return definition.getUri();
    }

    /**
     * Whether the service method is a customized one or not. A customized service
     * method can only be available for some cloud environment's elements.
     *
     * @return true if the method is a customized one
     */
    @Override
    public boolean isCustomized() {
        return "Y".equalsIgnoreCase(definition.getCustomized());
    }

    /**
     * Check if the service method matches given http method and uri
     *
     * @param httpMethod HTTP method, for example, POST, GET etc.
     * @param requestURI Request URI
     * @return true if matches
     */
    @Override
    public boolean match(String httpMethod, String requestURI) {
        if(!antPathMatcher.match(definition.getUri(), requestURI)){
            return false;
        }

        if(getHttpMethod() == null || getHttpMethod().matches(httpMethod)){
            return true;
        }

        return false;
    }
}
