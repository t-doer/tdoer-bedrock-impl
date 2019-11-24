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
package com.tdoer.bedrock.impl.context;

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.context.ContextType;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.definition.context.*;
import com.tdoer.bedrock.impl.product.DefaultClientResource;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import com.tdoer.bedrock.resource.ResourceType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextBuilder {

    private DefaultServiceRepository serviceRepository;

    private DefaultApplicationRepository applicationRepository;

    private ContextPathParser contextPathParser;

    private DefaultContextCenter contextCenter;

    public ContextBuilder(ContextPathParser contextPathParser, DefaultApplicationRepository applicationRepository,
                          DefaultServiceRepository serviceRepository) {
        Assert.notNull(contextPathParser, "Context path parser cannot be null");
        Assert.notNull(applicationRepository, "Application repository cannot be null");
        Assert.notNull(serviceRepository, "Service repository cannot be null");

        this.contextPathParser = contextPathParser;
        this.applicationRepository = applicationRepository;
        this.serviceRepository = serviceRepository;
    }

    void setContextCenter(DefaultContextCenter contextCenter){
        Assert.notNull(contextCenter, "Context center cannot be null");

        this.contextCenter = contextCenter;
    }

    public ContextType buildContextType(ContextType parent, ContextTypeDefinition definition){
        // todo check definition
        if(ContextType.USER.getType().equals(definition.getId())){
            return ContextType.USER;
        }
        return new ContextType(definition.getId(), definition.getCode(), definition.getCategory(), parent);
    }


    public DefaultContextRole buildContextRole(ContextRoleDefinition definition){
        // todo check definition

        ContextPath contextPath = contextPathParser.parse(definition.getContextPath());

        return new DefaultContextRole(definition, contextPath, contextCenter);
    }

    public DefaultContextApplicationInstallation buildContextApplicationInstallation(ContextApplicationDefinition definition){
        //todo check definition

        ContextPath contextPath = null;
        if(StringUtils.hasText(definition.getContextPath())){
            contextPath = contextPathParser.parse(definition.getContextPath());
        }

        return new DefaultContextApplicationInstallation(definition, contextPath, applicationRepository);
    }

    public DefaultClientResource buildPublicClientResource(ContextPublicResourceDefinition definition){
        // todo check definition

        ResourceType type = ResourceType.resolve(definition.getResourceType());
        return new DefaultClientResource(definition.getClientId(),
                definition.getResourceId(), type);
    }

    public DefaultClientResource buildRoleClientResource(ContextRoleResourceDefinition definition){
        // todo check definition

        ResourceType type = ResourceType.resolve(definition.getResourceType());
        return new DefaultClientResource(definition.getClientId(),
                definition.getResourceId(), type);
    }

    public DefaultServiceMethod buildRoleServiceMethods(ContextRoleMethodDefinition definition){
        // todo check definition

        return serviceRepository.getServiceMethod(definition.getServiceId(), definition.getMethodId());
    }

    public DefaultServiceMethod buildPublicServiceMethods(ContextPublicMethodDefinition definition){
        // todo check definition

        return serviceRepository.getServiceMethod(definition.getServiceId(), definition.getMethodId());
    }
}
