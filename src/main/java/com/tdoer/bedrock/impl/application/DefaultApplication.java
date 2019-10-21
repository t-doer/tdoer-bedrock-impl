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

    private DefaultApplicationRepository repository;

    public DefaultApplication(ApplicationDefinition applicationDefinition, DefaultApplicationRepository repository) {
        Assert.notNull(applicationDefinition, "ApplicationDefinition cannot be null");
        Assert.notNull(repository, "ApplicationRepository cannot be null");
        this.applicationDefinition = applicationDefinition;
        this.repository = repository;
    }

    /**
     * Application Id
     *
     * @return Application Id
     */
    @Override
    public Long getId() {
        return null;
    }

    /**
     * Application code
     *
     * @return Application code, must not be blank
     */
    @Override
    public String getCode() {
        return null;
    }

    /**
     * Application name
     *
     * @return Application name, must not be blank
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Application description
     *
     * @return Application description, maybe be {@code Null}
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * Application provider's name, it can be an organization or a person
     *
     * @return Provide's name, it must not be blank
     */
    @Override
    public String getProvider() {
        return null;
    }

    /**
     * Application version
     *
     * @return Application version, must not be blank
     */
    @Override
    public String getVersion() {
        return null;
    }

    /**
     * Get page of specific Id available in the application
     *
     * @param pageId Page Id, it cannot be <code>null</code>
     * @return Page if found, otherwise {@code null}
     */
    @Override
    public Page getPage(Long pageId) {
        return null;
    }

    /**
     * Get page of specific code available in the application
     *
     * @param pageCode
     * @return Page if found, otherwise {@code null}
     */
    @Override
    public Page getPage(String pageCode) {
        return null;
    }

    /**
     * List available pages of the application in current cloud environment
     * {@link CloudEnvironment}.
     *
     * @param list List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listCurrentPages(List<Page> list) {

    }

    /**
     * List the application's all enabled pages, including common and customized ones.
     *
     * @param list List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listAllPages(List<Page> list) {

    }

    /**
     * List services the application needs to call in current cloud environment
     * {@link CloudEnvironment}.
     *
     * @param list List to hold services.
     */
    @Override
    public void listCurrentRefereeServices(List<Service> list) {

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
        return false;
    }

    /**
     * Get action of specific Id available in the application
     *
     * @param actionId Action Id, cannot be <code>null</code>
     * @return {@link Action} if it exists in the application, otherwise return <code>null</code>
     */
    @Override
    public Action getAction(Long actionId) {
        return null;
    }

    /**
     * Get action of specific code available in the application
     *
     * @param actionCode Action code, cannot be <code>null</code>
     * @return {@link Action} if it exists in the application, otherwise return <code>null</code>
     */
    @Override
    public Action getAction(String actionCode) {
        return null;
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
