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
import com.tdoer.bedrock.application.Application;
import com.tdoer.bedrock.application.Page;
import com.tdoer.bedrock.impl.definition.application.ApplicationDefinition;
import com.tdoer.bedrock.service.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultApplication implements Application {

    private ApplicationDefinition applicationDefinition;

    private DefaultApplicationRepository applicationRepository;

    public DefaultApplication(ApplicationDefinition applicationDefinition, DefaultApplicationRepository applicationRepository) {
        Assert.notNull(applicationDefinition, "ApplicationDefinition cannot be null");
        Assert.notNull(applicationRepository, "ApplicationRepository cannot be null");
        this.applicationDefinition = applicationDefinition;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Application Id
     *
     * @return Application Id
     */
    @Override
    public Long getId() {
        return applicationDefinition.getId();
    }

    /**
     * Application code
     *
     * @return Application code, must not be blank
     */
    @Override
    public String getCode() {
        return applicationDefinition.getCode();
    }

    /**
     * Application name
     *
     * @return Application name, must not be blank
     */
    @Override
    public String getName() {
        return applicationDefinition.getName();
    }

    /**
     * Application description
     *
     * @return Application description, maybe be {@code Null}
     */
    @Override
    public String getDescription() {
        return applicationDefinition.getDescription();
    }

    /**
     * Application provider's name, it can be an organization or a person
     *
     * @return Provide's name, it must not be blank
     */
    @Override
    public String getProvider() {
        return applicationDefinition.getProvider();
    }

    /**
     * Application version
     *
     * @return Application version, must not be blank
     */
    @Override
    public String getVersion() {
        return applicationDefinition.getVersion();
    }

    /**
     * Get page of specific Id available in the application
     *
     * @param pageId Page Id, it cannot be <code>null</code>
     * @return Page if found, otherwise {@code null}
     */
    @Override
    public Page getPage(Long pageId) {
        Assert.notNull(pageId, "Page Id cannot be null");

        return applicationRepository.getPage(getId(), pageId);
    }

    /**
     * Get page of specific code available in the application
     *
     * @param pageCode
     * @return Page if found, otherwise {@code null}
     */
    @Override
    public Page getPage(String pageCode) {
        Assert.hasText(pageCode, "Page code cannot be blank");

        return applicationRepository.getPage(getId(), pageCode);
    }

    /**
     * List available pages of the application in current cloud environment
     * {@link CloudEnvironment}.
     *
     * @param list List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listCurrentPages(List<Page> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        applicationRepository.listCurrentPages(env.getApplicationId(), env.getProductId(), env.getClientId(),
                env.getTenantId(), env.getContextPath(), list);
    }

    /**
     * List the application's common pages, excluding customized ones.
     *
     * @param list List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listCommonPages(List<Page> list) {
        Assert.notNull(list, "List cannot be null");

        applicationRepository.listCommonPages(getId(), list);
    }

    /**
     * List the application's all enabled pages, including common and customized ones.
     *
     * @param list List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listAllPages(List<Page> list) {
        Assert.notNull(list, "List cannot be null");

        applicationRepository.listAllPages(getId(), list);
    }

    /**
     * List the Ids of services the application needs to call in current cloud environment
     * {@link CloudEnvironment}.
     *
     * @param list List to hold service Ids.
     */
    @Override
    public void listCurrentRefereeServiceIds(List<Long> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        applicationRepository.listCurrentRefereeServiceIds(env.getApplicationId(), env.getProductId(),
                env.getClientId(), env.getTenantId(), env.getContextPath(), list);
    }

    /**
     * List the Ids of services the application commonly needs to call.
     *
     * @param list List to hold service Ids, cannot be <code>null</code>.
     */
    @Override
    public void listCommonRefereeServiceIds(List<Long> list) {
        Assert.notNull(list, "List cannot be null");

        applicationRepository.listCommonRefereeServiceIds(getId(), list);
    }

    /**
     * List the Ids of all services the application totally needs to call,
     * including common ones and cutomized ones.
     *
     * @param list List to hold service Ids, cannot be <code>null</code>.
     */
    @Override
    public void listAllRefereeServiceIds(List<Long> list) {
        Assert.notNull(list, "List cannot be null");

        applicationRepository.listAllRefereeServiceIds(getId(), list);
    }

    /**
     * Check whether the application can access the service or not. If the service
     * is installed for the tenant's client, it's accessible.
     *
     * @param service The service to check
     * @return true if the service is accessible, otherwise false
     */
    @Override
    public boolean isServiceAccessible(Service service) {
        Assert.notNull(service, "Service cannot be null");

        ArrayList<Long> list = new ArrayList<>();
        listCurrentRefereeServiceIds(list);

        return list.contains(service.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof Service){
            return this.getId().equals(((Application) obj).getId());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Application[");
        sb.append(getId()).append(", ");
        sb.append(getCode()).append(", ");
        sb.append(getName()).append(", ");
        sb.append(getVersion());
        sb.append("]");
        return sb.toString();
    }
}
