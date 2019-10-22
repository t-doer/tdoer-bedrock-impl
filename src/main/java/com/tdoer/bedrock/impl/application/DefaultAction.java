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
import com.tdoer.bedrock.application.Application;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.definition.application.ActionDefinition;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.bedrock.resource.ResourceCategory;
import com.tdoer.bedrock.resource.ResourceType;
import com.tdoer.bedrock.service.Service;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.util.Assert;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultAction implements Action {

    private ActionDefinition actionDefinition;

    private DefaultApplicationRepository applicationRepository;

    public DefaultAction(ActionDefinition actionDefinition, DefaultApplicationRepository applicationRepository){
        Assert.notNull(actionDefinition, "ActionDefinition cannot be null");
        Assert.notNull(applicationRepository, "Application repository cannot be null");

        this.actionDefinition = actionDefinition;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Action name
     *
     * @return Acton name,it must not be blank
     */
    @Override
    public String getName() {
        return actionDefinition.getName();
    }

    /**
     * Action code
     *
     * @return Action code, it must not be blank
     */
    @Override
    public String getCode() {
        return actionDefinition.getCode();
    }

    /**
     * Get the Id of the page to which the action belongs
     *
     * @return Page Id, it must not be <code>null</code>
     */
    @Override
    public Long getPageId() {
        return actionDefinition.getPageId();
    }

    /**
     * The Id of the Application to which the resource belongs
     *
     * @return Application Id, never be null
     */
    @Override
    public Long getApplicationId() {
        return actionDefinition.getApplicationId();
    }

    /**
     * List the service methods which the application resource associates with
     *
     * @param list List to hold service methods, cannot be <code>null</code>
     */
    @Override
    public void listServiceMethods(List<ServiceMethod> list) {
        Assert.notNull(list, "List cannot be null");

        applicationRepository.listServiceMethodsOfAction(getId(), list);
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof Service){
            return this.getId().equals(((Action) obj).getId());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Action[");
        sb.append(getId()).append(", ");
        sb.append(getPageId()).append(", ");
        sb.append(getApplicationId()).append(", ");
        sb.append(getCode()).append(", ");
        sb.append(getName());
        sb.append("]");
        return sb.toString();
    }
}
