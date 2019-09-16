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

import com.tdoer.bedrock.application.Action;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.definition.application.ActionDefinition;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.util.Assert;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultAction implements Action {

    private ActionDefinition actionDefinition;

    private final DefaultServiceMethod[] serviceMethods;

    private ContextPath contextPath;

    public DefaultAction(ActionDefinition actionDefinition, ContextPath contextPath, DefaultServiceMethod[] serviceMethods){
        Assert.notNull(actionDefinition, "ActionDefinition cannot be null");
        Assert.notNull(actionDefinition.getId(), "Resource Id cannot be null");

        this.actionDefinition = actionDefinition;
        this.serviceMethods = serviceMethods;
        this.contextPath = contextPath; // can be null
    }

    /**
     * Resource Id
     *
     * @return Resource Id
     */
    @Override
    public Long getId() {
        return actionDefinition.getId();
    }

    /**
     * Action Id, that's, the resource Id
     *
     * @return
     */
    @Override
    public Long getActionId() {
        return actionDefinition.getId();
    }

    /**
     * Get the name, or a brief description, of the resource
     *
     * @return
     */
    @Override
    public String getName() {
        return actionDefinition.getName();
    }

    /**
     * Resource code
     *
     * @return
     */
    @Override
    public String getCode() {
        return actionDefinition.getCode();
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
     * Get the page's Id to which the action belongs
     *
     * @return
     */
    @Override
    public Long getPageId() {
        return actionDefinition.getPageId();
    }

    /**
     * The application's Id to which the resource belongs
     *
     * @return
     */
    @Override
    public String getApplicationId() {
        return actionDefinition.getApplicationId();
    }

    /**
     * Product Id, to which the resource belongs
     *
     * @return Product Id, it may be {@code Null}
     */
    @Override
    public String getProductId() {
        return actionDefinition.getProductId();
    }

    /**
     * Client Id, to which the resource belongs to
     *
     * @return Client Id, it may be {@code Null}
     */
    @Override
    public String getClientId() {
        return actionDefinition.getClientId();
    }

    /**
     * Tenant Id, to which the resource belongs to
     *
     * @return Tenant Id, it may be {@code Null}
     */
    @Override
    public Long getTenantId() {
        return actionDefinition.getTenantId();
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
