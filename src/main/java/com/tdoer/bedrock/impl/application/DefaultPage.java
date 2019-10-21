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
package com.tdoer.bedrock.impl.application;

import com.tdoer.bedrock.CloudEnvironment;
import com.tdoer.bedrock.CloudEnvironmentHolder;
import com.tdoer.bedrock.application.Action;
import com.tdoer.bedrock.application.ApplicationRepository;
import com.tdoer.bedrock.application.Page;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.definition.application.PageDefinition;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.bedrock.resource.ResourceCategory;
import com.tdoer.bedrock.resource.ResourceType;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultPage implements Page {
    private PageDefinition pageDefinition;

    private DefaultServiceMethod[] serviceMethods;

    private ApplicationRepository applicationRepository;

    private ContextPath contextPath;

    public DefaultPage(PageDefinition pageDefinition, ContextPath contextPath, DefaultServiceMethod[] serviceMethods, DefaultApplicationRepository applicationRepository) {
        Assert.notNull(pageDefinition, "PageDefinition cannot be null");
        Assert.notNull(applicationRepository, "ApplicationRepository Id cannot be null");

        this.pageDefinition = pageDefinition;
        this.contextPath = contextPath;
        this.serviceMethods = serviceMethods;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Page name
     *
     * @return Page name, it must not be blank
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Page code
     *
     * @return Page code, it must not be blank
     */
    @Override
    public String getCode() {
        return null;
    }

    /**
     * Page's URI
     *
     * @return Page's URI, it must not be blank
     */
    @Override
    public String getURI() {
        return null;
    }

    /**
     * Get action of specific Id available in the application. If the action
     * exists, but it dose not below to the application, it will return <code>null</code>
     *
     * @param actionId Action Id, cannot be <code>null</code>
     * @return Action if the action exists and is enabled and below to the application,
     * otherwise return <code>null</code>
     */
    @Override
    public Action getAction(Long actionId) {
        return null;
    }

    /**
     * Get action of specific code available in the application. If the action
     * exists, but it dose not below to the application, it will return <code>null</code>
     *
     * @param actionCode Action code, cannot be <code>null</code>
     * @return Action if the action exists and is enabled and below to the application,
     * otherwise return <code>null</code>
     */
    @Override
    public Action getAction(String actionCode) {
        return null;
    }

    /**
     * List actions of the page available in current environment {@link CloudEnvironment},
     *
     * @param list List to hold actions, cannot be <code>null</code>
     */
    @Override
    public void listCurrentActions(List<Action> list) {

    }

    /**
     * List the page's all enabled actions, including common and customized ones.
     *
     * @param list List to hold actions, cannot be <code>null</code>
     */
    @Override
    public void listAllActions(List<Action> list) {

    }

    /**
     * The Id of the Application to which the resource belongs
     *
     * @return Application Id, never be null
     */
    @Override
    public Long getApplicationId() {
        return null;
    }

    /**
     * List the service methods which the application resource associates with
     *
     * @param list List to hold service methods, cannot be <code>null</code>
     */
    @Override
    public void listServiceMethods(List<ServiceMethod> list) {

    }

    /**
     * Resource Id
     *
     * @return Resource Id
     */
    @Override
    public Long getId() {
        return null;
    }
}
