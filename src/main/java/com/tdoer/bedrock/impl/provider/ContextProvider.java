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
package com.tdoer.bedrock.impl.provider;

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.definition.context.*;

import java.util.List;

/**
 * Context provider provides all enabled or active context type, context instance
 * context role, public resource and their relationship definitions, including with
 * applications, that's, client context application installation.
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface ContextProvider {

    /**
     * Get all context type definitions of a tenant or for a tenant.
     * @param tenantId Tenant Id, cannot be <code>null</code>, cannot be zero
     * @return List of context type defintions, must not be empty
     */
    List<ContextTypeDefinition> getContextTypes(Long tenantId);

    /**
     * Get context instance of specific context path
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @return Context instance or <code>null</code>
     */
    ContextInstanceDefinition getContextInstance(Long tenantId, ContextPath contextPath);

    /**
     * Get context instance by GUID
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param guid Context instance's GUID, cannot be blank
     * @return Context instance or <code>null</code>
     */
    ContextInstanceDefinition getContextInstance(Long tenantId, String guid);

    /**
     * Get context application definitions of specific context in specific tenant client
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @return List of context application definitions, must not be empty
     */
    List<ContextApplicationDefinition> getContextApplications(Long clientId, Long tenantId, ContextPath contextPath);

    /**
     * Get context role definitions of specific tenant's context
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @return List of context role definitions, may be empty
     */
    List<ContextRoleDefinition> getContextRoles(Long tenantId, ContextPath contextPath);

    /**
     * Get public resources of specific context in specific tenant's client
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @return List of public resource definitions, may be empty
     */
    List<ContextPublicResourceDefinition> getContextPublicResources(Long clientId, Long tenantId, ContextPath contextPath);

    /**
     * Get authorized resources to a context role.
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @param roleId Role Id, cannot be <code>null</code>
     * @return List of context role resource definitions or <code>null</code>
     */
    List<ContextRoleResourceDefinition> getContextRoleResources(Long tenantId, ContextPath contextPath, Long roleId);

    /**
     * Get context public methods which associated with public resources, for example, public page.
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @return List of context public method definitions or <code>null</code>
     */
    List<ContextPublicMethodDefinition> getContextPublicMethods(Long tenantId, ContextPath contextPath);

    /**
     * Get context role method definitions of specific role in a tenant's context.
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @param roleId Role Id, cannot be <code>null</code>
     * @return List of context role method definitions or <code>null</code>
     */
    List<ContextRoleMethodDefinition> getContextRoleMethods(Long tenantId, ContextPath contextPath, Long roleId);

    /**
     * Get role Ids authorized to a user in a context
     * @param tenantId Tenant Id, cannot be <code>null</code>, but cannot be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @param userId User Id, cannot be <code>null</code>
     * @return List of role Ids or <code>null</code>
     */
    List<Long> getRoleIdsOfUserInContext(Long tenantId, ContextPath contextPath, Long userId);

}
