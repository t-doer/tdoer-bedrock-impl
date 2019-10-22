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

import com.tdoer.bedrock.impl.definition.tenant.TenantClientDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantProductDefinition;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface TenantProvider {

    /**
     * Get tenant definition of specific tenant Id
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @return Tenant definition or <code>null</code>
     */
    TenantDefinition getTenantDefinitionById(Long tenantId);

    /**
     * Get tenant definition of specific tenant code
     * @param tenantCode Tenant Id, cannot be blank
     * @return Tenant definition or <code>null</code>
     */
    TenantDefinition getTenantDefinitionByCode(String tenantCode);

    /**
     * Get tenant definition of specific tenant code
     * @param guid Tenant guid, cannot be blank
     * @return Tenant definition or <code>null</code>
     */
    TenantDefinition getTenantDefinitionByGuid(String guid);

    /**
     * Get tenant product definitions of tenant Id
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @return Tenant product definition or <code>null</code>
     */
    List<TenantProductDefinition> getTenantProductDefinitions(Long tenantId);

    /**
     * Get tenant client definitions of tenant Id
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @return Tenant client definition or <code>null</code>
     */
    List<TenantClientDefinition> getTenantClientDefinitions(Long tenantId);

    /**
     * Get tenant client definitions of access domain
     * @param host Access domain or host, cannot be <code>null</code>
     * @return Tenant client definition or <code>null</code>
     */
    TenantClientDefinition getTenantClientDefinition(String host);

}
