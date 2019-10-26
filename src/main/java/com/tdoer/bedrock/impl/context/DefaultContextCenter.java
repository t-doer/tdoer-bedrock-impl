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

import com.tdoer.bedrock.context.*;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
//import com.tdoer.bedrock.impl.context.cache.ContextApplicationsInstallationCacheManager;
//import com.tdoer.bedrock.impl.context.cache.ContextRolesCacheManager;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.util.Assert;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextCenter implements ContextCenter {

//    private ContextRolesCacheManager rolesCacheManager;
//
//    private ContextApplicationsInstallationCacheManager applicationsInstallationCacheManager;

//    private PublicAuthoritiesCacheManager publicAuthoritiesCacheManager;

    public DefaultContextCenter(ContextLoader contextConfigLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        Assert.notNull(contextConfigLoader, "ContextConfigLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");

        contextConfigLoader.setContextCenter(this);

//        this.rolesCacheManager = new ContextRolesCacheManager(cachePolicy, cleaner, contextConfigLoader);
//        this.applicationsInstallationCacheManager = new ContextApplicationsInstallationCacheManager(cachePolicy, cleaner, contextConfigLoader);
//        this.publicAuthoritiesCacheManager = new PublicAuthoritiesCacheManager(cachePolicy, cleaner, contextConfigLoader);

        // Initialize cache managers
//        rolesCacheManager.initialize();
//        applicationsInstallationCacheManager.initialize();
//        publicAuthoritiesCacheManager.initialize();
    }

    /**
     * Get the root context type for specific tenant
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @return Context type, must not be <code>null</code>
     */
    @Override
    public DefaultContextType getRootContextType(Long tenantId) {
        return null;
    }

    /**
     * List all context types defined by the tenant.
     * <p>
     * Note the list should not include "TENANT" context type which is
     * the root context type for all.
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @param list     List to hold a tenant's context types.
     */
    @Override
    public void listContextTypes(Long tenantId, List<ContextType> list) {

    }

    /**
     * Get specific context type available for specific tenant
     *
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextType Context type, cannot be <code>null</code>
     * @return Context type or <code>null</code>
     */
    @Override
    public ContextType getContextType(Long tenantId, Long contextType) {
        return null;
    }

    /**
     * Get specific context type available for specific tenant
     *
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextCode Context code, cannot be blank
     * @return Context type or <code>null</code>
     */
    @Override
    public ContextType getContextType(Long tenantId, String contextCode) {
        return null;
    }

    /**
     * Get context instance of specific context path in specific tenant
     *
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path, cannot be <code>null</code>
     * @return Context instance if it exists and is enabled
     * @throws ContextInstanceNotFoundException if it is not found
     */
    @Override
    public ContextInstance getContextInstance(Long tenantId, ContextPath contextPath) throws ContextInstanceNotFoundException {
        return null;
    }

    /**
     * Get context instance of specific context path in specific tenant
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @param guid     Context instance GUID, cannot be blank
     * @return Context instance if it exists and is enabled
     * @throws ContextInstanceNotFoundException if it is not found
     */
    @Override
    public ContextInstance getContextInstance(Long tenantId, String guid) throws ContextInstanceNotFoundException {
        return null;
    }

    /**
     * Get context instance of specific context path in specific tenant
     *
     * @param tenantId   Tenant Id, cannot be <code>null</code>
     * @param instanceId Context instance Id, cannot be <code>null</code>
     * @return Context instance if it exists and is enabled
     * @throws ContextInstanceNotFoundException if it is not found
     */
    @Override
    public ContextInstance getContextInstance(Long tenantId, Long instanceId) throws ContextInstanceNotFoundException {
        return null;
    }

    /**
     * List user roles of specific user Id in specific tenant's specific context instance
     *
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code>
     * @param userId      User Id, cannot be <code>null</code>
     * @param list        List to hold context roles, cannot be <code>null</code>
     */
    @Override
    public void listUserRoles(Long tenantId, ContextPath contextPath, Long userId, List<ContextRole> list) {

    }

    /**
     * List context roles in specific tenant's specific context instance
     *
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code>
     * @param list        List to hold context roles, cannot be <code>null</code>
     */
    @Override
    public void listContextRoles(Long tenantId, ContextPath contextPath, List<ContextRole> list) {

    }

    /**
     * List application installations which are in specific client and specific tenant's
     * specific context instance
     *
     * @param clientId    Client Id, cannot be <code>null</code>
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code>
     * @param list        List to hold {@link ContextApplicationInstallation}, cannot be <code>null</code>
     */
    @Override
    public void listApplicationInstallation(Long clientId, Long tenantId, ContextPath contextPath, List<ContextApplicationInstallation> list) {

    }

    /**
     * List public authorities in specific tenant's specific context instance
     *
     * @param clientId    Client Id, cannot be <code>null</code>
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code>
     * @param list        List to hold public client resources, cannot be <code>null</code>
     */
    @Override
    public void listPublicResources(Long clientId, Long tenantId, ContextPath contextPath, List<ClientResource> list) {

    }

    /**
     * List public service methods in specific tenant's specific context instance
     *
     * @param clientId    Client Id, cannot be <code>null</code>
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code
     * @param list        List to hold public service methods, cannot be <code>null</code>
     */
    @Override
    public void listPublicMethods(Long clientId, Long tenantId, ContextPath contextPath, List<ServiceMethod> list) {

    }

    /**
     * List resource authorities of specific role in specific tenant's specific context instance
     *
     * @param roleId      Role Id, cannot be <code>null</code>
     * @param clientId    Client Id, cannot be <code>null</code>
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code>
     * @param list        List to hold public client resources, cannot be <code>null</code>
     */
    @Override
    public void listRoleResources(Long roleId, Long clientId, Long tenantId, ContextPath contextPath, List<ClientResource> list) {

    }

    /**
     * List service method authorities of specific role in specific tenant's specific context instance
     *
     * @param roleId      Role Id, cannot be <code>null</code>
     * @param clientId    Client Id, cannot be <code>null</code>
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code
     * @param list        List to hold public service methods, cannot be <code>null</code>
     */
    @Override
    public void listRoleMethods(Long roleId, Long clientId, Long tenantId, ContextPath contextPath, List<ServiceMethod> list) {

    }
}
