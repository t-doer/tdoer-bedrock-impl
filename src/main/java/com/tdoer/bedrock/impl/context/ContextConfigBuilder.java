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
import com.tdoer.bedrock.impl.application.DefaultApplication;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.definition.context.ContextApplicationDefinition;
import com.tdoer.bedrock.impl.definition.context.ContextRoleResourceDefinition;
import com.tdoer.bedrock.impl.definition.context.ContextRoleDefinition;
import com.tdoer.bedrock.impl.definition.context.PublicResourceDefinition;
import com.tdoer.bedrock.resource.Resource;
import com.tdoer.bedrock.resource.ResourceType;
import org.springframework.util.StringUtils;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextConfigBuilder {

    private DefaultApplicationRepository applicationRepository;

    private ContextPathParser contextPathParser;

    public ContextConfigBuilder(ContextPathParser contextPathParser) {
        this.contextPathParser = contextPathParser;
    }

    public void setApplicationRepository(DefaultApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public DefaultContextRole buildContextRole(ContextRoleDefinition definition, DefaultRoleAuthority[] authorities){
        // todo check definition

        ContextPath contextPath = null;
        if(StringUtils.hasText(definition.getContextPath())){
            contextPath = contextPathParser.parse(definition.getContextPath());
        }

        return new DefaultContextRole(definition, contextPath, authorities);
    }

    public DefaultPublicAuthority buildPublicAuthority(PublicResourceDefinition definition){
        // todo check definition

        ResourceType type = ResourceType.resolve(definition.getResourceType());
        Resource resource = findResource(definition.getApplicationId(), type, definition.getResourceId());
        if(resource == null){
            // todo
        }

        return new DefaultPublicAuthority(resource);
    }

    public DefaultRoleAuthority buildRoleAuthority(ContextRoleResourceDefinition definition){
        // todo check definition

        ResourceType type = ResourceType.resolve(definition.getResourceType());
        Resource resource = findResource(definition.getApplicationId(), type, definition.getResourceId());
        if(resource == null){
            // todo
        }

        return new DefaultRoleAuthority(definition.getRoleId(), resource);
    }

    protected Resource findResource(String applicationId, ResourceType type, Long resourceId){
        DefaultApplication application = null;
        switch(type){
            case PAGE:
                application = applicationRepository.getApplication(applicationId);
                return application.getPage(resourceId);
            case ACTION:
                application = applicationRepository.getApplication(applicationId);
                return application.getAction(resourceId);
            case NAVIGATION:
                // todo
                return null;
            default:
                // null
        }
        return null;
    }

    public DefaultContextApplicationInstallation buildContextApplicationInstallation(ContextApplicationDefinition definition){
        //todo check definition

        ContextPath contextPath = null;
        if(StringUtils.hasText(definition.getContextPath())){
            contextPath = contextPathParser.parse(definition.getContextPath());
        }

        return new DefaultContextApplicationInstallation(definition, contextPath, applicationRepository);
    }
}
