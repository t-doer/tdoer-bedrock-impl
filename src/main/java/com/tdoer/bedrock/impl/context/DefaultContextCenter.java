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
import com.tdoer.bedrock.impl.context.cache.*;
import com.tdoer.bedrock.impl.product.DefaultClientResource;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.bedrock.product.ClientResource;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextCenter implements ContextCenter {

    private ContextTypesCacheManager contextTypesCacheManager;

    private ContextInstanceByIdCacheManager contextInstanceByIdCacheManager;

    private ContextInstanceByGuidCacheManager contextInstanceByGuidCacheManager;

    private ContextRolesCacheManager contextRolesCacheManager;

    private RoleIdsCacheManager roleIdsCacheManager;

    private ContextApplicationsInstallationCacheManager contextApplicationsInstallationCacheManager;

    private PublicClientResourcesCacheManager publicClientResourceCacheManager;

    private PublicServiceMethodsCacheManager publicServiceMethodsCacheManager;

    private RoleClientResourcesCacheManager roleClientResourcesCacheManager;

    private RoleServiceMethodsCacheManager roleServiceMethodsCacheManager;

    public DefaultContextCenter(ContextLoader contextLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        Assert.notNull(contextLoader, "ContextLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");

        contextLoader.setContextCenter(this);

        this.contextTypesCacheManager = new ContextTypesCacheManager(cachePolicy, cleaner, contextLoader);
        this.contextInstanceByGuidCacheManager = new ContextInstanceByGuidCacheManager(cachePolicy, cleaner, contextLoader);
        this.contextInstanceByIdCacheManager = new ContextInstanceByIdCacheManager(cachePolicy, cleaner, contextLoader);
        this.contextRolesCacheManager = new ContextRolesCacheManager(cachePolicy, cleaner, contextLoader);
        this.roleIdsCacheManager = new RoleIdsCacheManager(cachePolicy, cleaner, contextLoader);
        this.contextApplicationsInstallationCacheManager =
                new ContextApplicationsInstallationCacheManager(cachePolicy, cleaner, contextLoader);
        this.publicServiceMethodsCacheManager = new PublicServiceMethodsCacheManager(cachePolicy, cleaner,
                contextLoader);
        this.roleServiceMethodsCacheManager = new RoleServiceMethodsCacheManager(cachePolicy, cleaner, contextLoader);
        this.publicClientResourceCacheManager = new PublicClientResourcesCacheManager(cachePolicy, cleaner, contextLoader);
        this.roleClientResourcesCacheManager = new RoleClientResourcesCacheManager(cachePolicy, cleaner, contextLoader);
        // Initialize cache managers
        this.contextTypesCacheManager.initialize();
        this.contextInstanceByGuidCacheManager.initialize();
        this.contextInstanceByIdCacheManager.initialize();
        this.contextRolesCacheManager.initialize();
        this.roleIdsCacheManager.initialize();
        this.contextApplicationsInstallationCacheManager.initialize();
        this.publicClientResourceCacheManager.initialize();
        this.publicServiceMethodsCacheManager.initialize();
        this.roleServiceMethodsCacheManager.initialize();
        this.roleClientResourcesCacheManager.initialize();
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
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextType, "Context type cannot be null");

        ContextType[] types = contextTypesCacheManager.getSource(tenantId);
        for(ContextType type : types){
            if(type.getType().equals(contextType)){
                return type;
            }
        }
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
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextCode, "Context code cannot be blank");

        ContextType[] types = contextTypesCacheManager.getSource(tenantId);
        for(ContextType type : types){
            if(type.getCode().equals(contextCode)){
                return type;
            }
        }
        return null;
    }

    /**
     * List all content types available for the tenant
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @param list     List to holds context types, cannot be <code>null</code>
     */
    @Override
    public void listContextTypes(Long tenantId, List<ContextType> list) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ContextType[] types = contextTypesCacheManager.getSource(tenantId);
        for(ContextType type : types){
            list.add(type);
        }
    }

    /**
     * List context types of specific category available for the tenant
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @param category Context category, cannot be blank
     * @param list     List to holds context types, cannot be <code>null</code>
     */
    @Override
    public void listContextTypes(Long tenantId, String category, List<ContextType> list) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.hasText(category, "Context category cannot be blank");
        Assert.notNull(list, "List cannot be null");

        ContextType[] types = contextTypesCacheManager.getSource(tenantId);
        for(ContextType type : types){
            if(type.getCategory().equals(category)){
                list.add(type);
            }
        }
    }

    /**
     * The configuration of specific context instance.
     *
     * @param contextInstance Context instance, cannot be <code>null</code>
     * @return Context configuration, must not be <code>null</code>
     */
    @Override
    public ContextConfig getContextConfig(ContextInstance contextInstance) {
        Assert.notNull(contextInstance, "Context instance cannot be null");

        return new DefaultContextConfig(contextInstance, this);
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
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");

        return getContextInstance(tenantId, contextPath.getType(), contextPath.getInstanceId());
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
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(guid, "Context instance's guid cannot be blank");

        ContextInstanceGuidCacheKey key = ContextInstanceGuidCacheKey.getKey(tenantId, guid);
        ContextInstance ret = contextInstanceByGuidCacheManager.getSource(key);
        if(ret != null){
            return ret;
        }else{
            throw new ContextInstanceNotFoundException(key);
        }
    }

    /**
     * Get context instance of specific context path in specific tenant
     *
     * @param tenantId   Tenant Id, cannot be <code>null</code>
     * @param contextType Context type, cannot be <code>null</code>
     * @param instanceId Context instance Id, cannot be <code>null</code>
     * @return Context instance if it exists and is enabled
     * @throws ContextInstanceNotFoundException if it is not found
     */
    @Override
    public ContextInstance getContextInstance(Long tenantId, Long contextType, Long instanceId) throws ContextInstanceNotFoundException {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextType, "Context type cannot be null");
        Assert.notNull(instanceId, "Context instance's Id cannot be null");

        ContextInstanceIdCacheKey key = ContextInstanceIdCacheKey.getKey(tenantId, contextType, instanceId);
        ContextInstance ret = contextInstanceByIdCacheManager.getSource(key);
        if(ret != null){
            return ret;
        }else{
            throw new ContextInstanceNotFoundException(key);
        }
    }

    /**
     * Get specific role defined in a tenant' context
     *
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code>
     * @param roleId      Role Id, cannot be <code>null</code>
     * @return Context role or <code>null</code>
     */
    @Override
    public ContextRole getContextRole(Long tenantId, ContextPath contextPath, Long roleId) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(roleId, "Role Id cannot be null");

        ArrayList<ContextRole> list = new ArrayList<>();
        listContextRoles(tenantId, contextPath, list);
        return searchContextRole(list, roleId);
    }

    /**
     * Get specific role defined in a tenant' context
     *
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code>
     * @param roleCode    Role code, cannot be blank
     * @return Context role or <code>null</code>
     */
    @Override
    public ContextRole getContextRole(Long tenantId, ContextPath contextPath, String roleCode) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(roleCode, "Role Id cannot be blank");

        ArrayList<ContextRole> list = new ArrayList<>();
        listContextRoles(tenantId, contextPath, list);
        for(ContextRole role : list){
            if(role.getCode().equals(roleCode)){
                return role;
            }
        }
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
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(userId, "User Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        UserRolesCacheKey key = UserRolesCacheKey.getKey(tenantId, contextPath, userId);
        Long[] roleIds = roleIdsCacheManager.getSource(key);

        if(roleIds != null){
            ContextRole role = null;
            ArrayList<ContextRole> ret = new ArrayList<>(roleIds.length);
            ArrayList<ContextRole> all = new ArrayList<>();
            listContextRoles(tenantId, contextPath, all);
            for(Long roleId : roleIds){
                role = searchContextRole(all, roleId);
                if(role != null){
                    ret.add(role);
                }else{
                    // TODO
                }
            }
        }
    }

    private ContextRole searchContextRole(List<ContextRole> list, Long roleId){
        for(ContextRole role : list){
            if(role.getId().equals(roleId)){
                return role;
            }
        }
        return null;
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
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(list, "List cannot be null");

        ContextDomainEnumerator enumerator = new ContextDomainEnumerator(tenantId, contextPath);
        while(enumerator.hasMoreElements()){
            DefaultContextRole[] roles = contextRolesCacheManager.getSource(enumerator.nextElement());
            if(roles != null){
                for(DefaultContextRole role : roles){
                    list.add(role);
                }
            }
        }
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
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(list, "List cannot be null");

        ClientContextDomainEnumerator enumerator = new ClientContextDomainEnumerator(clientId, tenantId, contextPath);
        while(enumerator.hasMoreElements()){
            ContextApplicationInstallation[] arr =
                    contextApplicationsInstallationCacheManager.getSource(enumerator.nextElement());
            if(arr != null){
                for(ContextApplicationInstallation ins : arr){
                    list.add(ins);
                }
            }
        }
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
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(list, "List cannot be null");

        ClientContextDomainEnumerator enumerator = new ClientContextDomainEnumerator(clientId, tenantId, contextPath);
        while(enumerator.hasMoreElements()){
            DefaultClientResource[] arr =
                    publicClientResourceCacheManager.getSource(enumerator.nextElement());
            if(arr != null){
                for(DefaultClientResource ins : arr){
                    list.add(ins);
                }
            }
        }
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
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(list, "List cannot be null");

        ClientContextDomainEnumerator enumerator = new ClientContextDomainEnumerator(clientId, tenantId, contextPath);
        while(enumerator.hasMoreElements()){
            DefaultServiceMethod[] arr =
                    publicServiceMethodsCacheManager.getSource(enumerator.nextElement());
            if(arr != null){
                for(DefaultServiceMethod ins : arr){
                    list.add(ins);
                }
            }
        }
    }


    /**
     * List resource authorities of specific role in specific tenant's specific context instance
     *
     * @param clientId    Client Id, cannot be <code>null</code>
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code>
     * @param roleId      Role Id, cannot be <code>null</code>
     * @param list        List to hold public client resources, cannot be <code>null</code>
     */
    @Override
    public void listRoleResources(Long clientId, Long tenantId, ContextPath contextPath, Long roleId, List<ClientResource> list) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(roleId, "Role Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ContextRoleCacheKey key = ContextRoleCacheKey.getKey(clientId, tenantId, contextPath, roleId);
        DefaultClientResource[] arr =
                roleClientResourcesCacheManager.getSource(key);
        if(arr != null){
            for(DefaultClientResource ins : arr){
                list.add(ins);
            }
        }
    }

    /**
     * List service method authorities of specific role in specific tenant's specific context instance
     *
     * @param clientId    Client Id, cannot be <code>null</code>
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path of context instance, cannot be <code>null</code
     * @param roleId      Role Id, cannot be <code>null</code>
     * @param list        List to hold public service methods, cannot be <code>null</code>
     */
    @Override
    public void listRoleMethods(Long clientId, Long tenantId, ContextPath contextPath, Long roleId, List<ServiceMethod> list) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(roleId, "Role Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ContextRoleCacheKey key = ContextRoleCacheKey.getKey(clientId, tenantId, contextPath, roleId);
        DefaultServiceMethod[] arr =
                roleServiceMethodsCacheManager.getSource(key);
        if(arr != null){
            for(DefaultServiceMethod ins : arr){
                list.add(ins);
            }
        }
    }
}
