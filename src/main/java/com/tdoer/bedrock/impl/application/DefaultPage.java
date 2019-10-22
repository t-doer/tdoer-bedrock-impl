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
import com.tdoer.bedrock.Platform;
import com.tdoer.bedrock.application.Action;
import com.tdoer.bedrock.application.ApplicationRepository;
import com.tdoer.bedrock.application.Page;
import com.tdoer.bedrock.impl.definition.application.PageDefinition;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.util.Assert;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultPage implements Page {
    private PageDefinition pageDefinition;

    private ApplicationRepository applicationRepository;

    public DefaultPage(PageDefinition pageDefinition, DefaultApplicationRepository applicationRepository) {
        Assert.notNull(pageDefinition, "PageDefinition cannot be null");
        Assert.notNull(applicationRepository, "ApplicationRepository Id cannot be null");

        this.pageDefinition = pageDefinition;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Page name
     *
     * @return Page name, it must not be blank
     */
    @Override
    public String getName() {
        return pageDefinition.getName();
    }

    /**
     * Page code
     *
     * @return Page code, it must not be blank
     */
    @Override
    public String getCode() {
        return pageDefinition.getCode();
    }

    /**
     * Page's URI
     *
     * @return Page's URI, it must not be blank
     */
    @Override
    public String getURI() {
        return pageDefinition.getUri();
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
        Assert.notNull(actionId, "Action Id cannot be null");

        return applicationRepository.getAction(getId(), actionId);
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
        Assert.hasText(actionCode, "Action code cannot be blank");

        return applicationRepository.getAction(getId(), actionCode);
    }

    /**
     * List actions of the page available in current environment {@link CloudEnvironment},
     *
     * @param list List to hold actions, cannot be <code>null</code>
     */
    @Override
    public void listCurrentActions(List<Action> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        applicationRepository.listCurrentActions(getId(), env.getApplicationId(), env.getProductId(), env.getClientId(),
                env.getTenantId(), env.getContextPath(), list);
    }

    /**
     * List the page's common actions, excluding customized ones.
     *
     * @param list List to hold actions, cannot be <code>null</code>
     */
    @Override
    public void listCommonActions(List<Action> list) {
        Assert.notNull(list, "List cannot be null");

        applicationRepository.listCommonActions(getId(), list);
    }

    /**
     * List the page's all enabled actions, including common and customized ones.
     *
     * @param list List to hold actions, cannot be <code>null</code>
     */
    @Override
    public void listAllActions(List<Action> list) {
        Assert.notNull(list, "List cannot be null");

        applicationRepository.listAllActions(getId(), list);
    }

    /**
     * The Id of the Application to which the resource belongs
     *
     * @return Application Id, never be null
     */
    @Override
    public Long getApplicationId() {
        return pageDefinition.getApplicationId();
    }

    /**
     * List the service methods which the application resource associates with
     *
     * @param list List to hold service methods, cannot be <code>null</code>
     */
    @Override
    public void listServiceMethods(List<ServiceMethod> list) {
        Assert.notNull(list, "List cannot be null");

        applicationRepository.listServiceMethodsOfPage(getId(), list);
    }

    /**
     * Resource Id
     *
     * @return Resource Id
     */
    @Override
    public Long getId() {
        return pageDefinition.getId();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Page[");
        sb.append(getId()).append(", ");
        sb.append(getApplicationId()).append(", ");
        sb.append(getCode()).append(", ");
        sb.append(getName()).append(", ");
        sb.append(getURI());
        sb.append("]");
        return sb.toString();
    }

}
