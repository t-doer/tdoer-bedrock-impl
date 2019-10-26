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

import com.tdoer.bedrock.CloudEnvironment;
import com.tdoer.bedrock.Platform;
import com.tdoer.bedrock.context.ClientResource;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.definition.context.ContextRoleDefinition;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextRole implements com.tdoer.bedrock.context.ContextRole {

    private ContextRoleDefinition definition;

    private ContextPath contextPath;

    private DefaultContextCenter configCenter;

    public DefaultContextRole(ContextRoleDefinition definition, ContextPath contextPath, DefaultContextCenter configCenter) {
        Assert.notNull(definition, "Context role definition cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(configCenter, "Config center cannot be null");
        this.definition = definition;
        this.contextPath = contextPath;
        this.configCenter = configCenter;
    }

    /**
     * Role Id
     *
     * @return Role Id, must not be <code>null</code>
     */
    @Override
    public Long getId() {
        return definition.getId();
    }

    /**
     * Role name, should be unique in a context type or instance
     *
     * @return Role name, must not be blank
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * Role code, should be unique in a context type or instance.
     * Role code is mainly used in program for access checking
     *
     * @return Role code, must not be blank
     */
    @Override
    public String getCode() {
        return definition.getCode();
    }

    /**
     * The path of context (type or instance) in which the role is defined
     *
     * @return Context path, must not be <code>null</code>
     */
    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }

    /**
     * The Id of tenant in which the role is defined. If the role is a system
     * role, tenant Id will be zero.
     *
     * @return Tenant Id, must not be <code>null</code>
     */
    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    /**
     * List front-end resources which are authorized to the context role,
     * such like page, action and navigation etc.
     *
     * @param list List to hold client resource, cannot be <code>null</code>
     */
    @Override
    public void listClientResources(List<ClientResource> list) {
        Assert.notNull(list, "List cannot be null");
        CloudEnvironment env = Platform.getCurrentEnvironment();
        configCenter.listRoleResources(getId(), env.getClientId(), getTenantId(), getContextPath(), list);
    }

    /**
     * List back-end service methods which are authorized to the context role.
     *
     * @param list List to hold service methods, cannot be <code>null</code>
     */
    @Override
    public void listServiceMethods(List<ServiceMethod> list) {
        Assert.notNull(list, "List cannot be null");
        CloudEnvironment env = Platform.getCurrentEnvironment();
        configCenter.listRoleMethods(getId(), env.getClientId(), getTenantId(), getContextPath(), list);
    }

    /**
     * Check if the role permits the request
     *
     * @param httpMethod HTTP method, must not be blank
     * @param URI        Request URI, must not be blank
     * @return true if the request is permitted
     */
    @Override
    public boolean permitServiceMethodAccess(String httpMethod, String URI) {
        Assert.notNull(HttpMethod.resolve(httpMethod), "Unknown HTTP method: " + httpMethod);
        Assert.hasText(URI, "Request URI cannot be blank");

        // go through user roles
        ArrayList<ServiceMethod> lst = new ArrayList<>();
        listServiceMethods(lst);
        for(ServiceMethod method : lst){
            if(method.match(httpMethod, URI)){
                return true;
            }
        }

        return false;
    }

    /**
     * Same with {@link #getCode()}
     * @return Role code
     */
    @Override
    public String getAuthority() {
        return getCode();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContextRole[");
        sb.append(getId()).append(", ");
        sb.append(getCode()).append(", ");
        sb.append(getName()).append(", ");
        sb.append(getTenantId()).append(", ");
        sb.append(getContextPath());
        sb.append("]");
        return sb.toString();
    }
}
