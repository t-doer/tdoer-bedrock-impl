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

    @Override
    public String getId() {
        return applicationDefinition.getId();
    }

    @Override
    public String getName() {
        return applicationDefinition.getName();
    }

    @Override
    public String getAuthor() {
        return applicationDefinition.getAuthor();
    }

    @Override
    public String getMaintainers() {
        return applicationDefinition.getMaintainers();
    }

    @Override
    public String getVersion() {
        return applicationDefinition.getVersion();
    }

    @Override
    public String getDescription() {
        return applicationDefinition.getDescription();
    }

    /**
     * List available pages in current environment {@link CloudEnvironment},
     * that's, list available page according to current client, tenant and context instance.
     * <br>
     * Pages will be appended to the given list.
     *
     * @param list List to hold pages.
     */
    @Override
    public void listCurrentPages(List<Page> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        // Note, use this application's serviceId, instead of env's
        repository.listPages(getId(), env.getProductId(), env.getClientId(), env.getTenantId(), env.getContextPath(), list);
    }

    /**
     * Get page of specific page Id
     *
     * @param pageId
     * @return Page if found, otherwise {@code null}
     */
    @Override
    public DefaultPage getPage(Long pageId) {
        ArrayList<Page> list = new ArrayList<>();
        listCurrentPages(list);
        for(Page pg : list){
            if(pg.getPageId().equals(pageId)){
                return (DefaultPage)pg;
            }
        }
        return null;
    }

    /**
     * List available services in current environment {@link CloudEnvironment},
     * that's, list available services according to current client, tenant and context instance.
     * <br>
     * Services will be appended to the given list.
     *
     * @param list List to hold services.
     */
    @Override
    public void listCurrentServices(List<Service> list) {

    }

    /**
     * Get action of specific Id available in the application
     *
     * @param actionId
     * @return
     */
    @Override
    public DefaultAction getAction(Long actionId) {
        ArrayList<Page> list = new ArrayList<>();
        listCurrentPages(list);
        for(Page pg : list){
            ArrayList<Action> actions = new ArrayList<>();
            pg.listCurrentActions(actions);
            for(Action action : actions){
                if(action.getActionId().equals(actionId)){
                    return (DefaultAction) action;
                }
            }
        }
        return null;
    }
}
