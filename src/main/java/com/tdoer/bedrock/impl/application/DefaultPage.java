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

    /**
     * Resource Id, that's, Page Id
     *
     * @return Resource Id
     */
    @Override
    public Long getId() {
        return pageDefinition.getId();
    }

    /**
     * Page Id, that's, the resource Id
     *
     * @return age Id
     */
    @Override
    public Long getPageId() {
        return getId();
    }

    /**
     * Page name
     *
     * @return Page name
     */
    @Override
    public String getName() {
        return pageDefinition.getName();
    }

    /**
     * Page code
     *
     * @return Page code
     */
    @Override
    public String getCode() {
        return pageDefinition.getCode();
    }

    @Override
    public void listServiceMethods(List<ServiceMethod> list) {
        if(serviceMethods != null){
            for(DefaultServiceMethod method : serviceMethods){
                list.add(method);
            }
        }
    }

    /**
     * List actions of the page available in current environment {@link CloudEnvironment},
     * that's, list available actions of the page available to current client, product, tenant and context instance.
     * <br>
     *
     * @param list
     */
    @Override
    public void listCurrentActions(List<Action> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        applicationRepository.listActions(getPageId(), getApplicationId(), env.getProductId(), env.getClientId(), env.getTenantId(), env.getContextPath(),list);
    }

    @Override
    public String getApplicationId() {
        return pageDefinition.getApplicationId();
    }

    @Override
    public DefaultAction getAction(Long actionId) {
        ArrayList<Action> list = new ArrayList<>();
        listCurrentActions(list);
        for(Action act : list){
            if(act.getActionId().equals(actionId)){
                return (DefaultAction)act;
            }
        }

        return null;
    }

    /**
     * Product Id, to which the resource belongs
     *
     * @return Product Id, it may be {@code Null}
     */
    @Override
    public String getProductId() {
        return pageDefinition.getProductId();
    }

    /**
     * Tenant Id, to which the resource belongs to
     *
     * @return Tenant Id, it may be {@code Null}
     */
    @Override
    public Long getTenantId() {
        return pageDefinition.getTenantId();
    }

    /**
     * Client Id, to which the resource belongs to
     *
     * @return Client Id, it may be {@code Null}
     */
    @Override
    public String getClientId() {
        return pageDefinition.getClientId();
    }

    /**
     * Context path, to which the resource belongs to
     *
     * @return Context path, it may be {@code Null}
     */
    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }
}
