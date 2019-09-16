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

import com.tdoer.bedrock.application.ApplicationResource;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.RoleAuthority;
import com.tdoer.bedrock.impl.definition.context.ContextRoleDefinition;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.security.access.AccessDecisionManager;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextRole implements com.tdoer.bedrock.context.ContextRole {

    private ContextRoleDefinition definition;

    private ContextPath contextPath;

    private DefaultRoleAuthority[] authorities;

    public DefaultContextRole(ContextRoleDefinition definition, ContextPath contextPath, DefaultRoleAuthority[] authorities) {
        this.definition = definition;
        this.contextPath = contextPath;
        this.authorities = authorities;
    }

    @Override
    public Long getId() {
        return definition.getId();
    }

    @Override
    public String getCode() {
        return definition.getCode();
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public String getProductId() {
        return definition.getProductId();
    }

    @Override
    public String getClientId() {
        return definition.getClientId();
    }

    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }

    @Override
    public boolean permitServiceMethod(String httpMethod, String path) {
        ApplicationResource resource = null;
        for(DefaultRoleAuthority authority : authorities){
            if(authority.getResource() instanceof ApplicationResource){
                resource = (ApplicationResource) authority.getResource();
                ArrayList<ServiceMethod> list = new ArrayList<>();
                resource.listServiceMethods(list);
                for(ServiceMethod method : list){
                    if(method.match(httpMethod, path)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void listAuthorities(List<RoleAuthority> list) {
        if(authorities != null){
            for(DefaultRoleAuthority authority : authorities){
                list.add(authority);
            }
        }
    }

    /**
     * A global unique authority string which will be used by {@link AccessDecisionManager},
     * the format will be "role://(context path)/(role code)", take app "o2o-engineer-app" and role "admin" for example,
     * the authority string will be "role://1.1-2.1/admin".
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Override
    public String getAuthority() {
        return new StringBuilder("role://").append(contextPath).append("/").append(getCode()).toString();
    }
}
