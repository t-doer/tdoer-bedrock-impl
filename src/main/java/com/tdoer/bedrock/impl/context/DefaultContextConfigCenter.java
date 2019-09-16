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

import com.tdoer.bedrock.application.ApplicationInstallation;
import com.tdoer.bedrock.context.ContextConfigCenter;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextRole;
import com.tdoer.bedrock.context.PublicAuthority;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ContextDomain;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextConfigCenter implements ContextConfigCenter {

    private ContextConfigLoader contextConfigLoader;

    private ContextRolesCacheManager rolesCacheManager;

    private ContextApplicationsInstallationCacheManager applicationsInstallationCacheManager;

    private PublicAuthoritiesCacheManager publicAuthoritiesCacheManager;

    public DefaultContextConfigCenter(ContextConfigLoader contextConfigLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        Assert.notNull(contextConfigLoader, "ContextConfigLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");

        this.contextConfigLoader = contextConfigLoader;
        this.rolesCacheManager = new ContextRolesCacheManager(cachePolicy, cleaner, contextConfigLoader);
        this.applicationsInstallationCacheManager = new ContextApplicationsInstallationCacheManager(cachePolicy, cleaner, contextConfigLoader);
        this.publicAuthoritiesCacheManager = new PublicAuthoritiesCacheManager(cachePolicy, cleaner, contextConfigLoader);

        // Initialize cache managers
        rolesCacheManager.initialize();
        applicationsInstallationCacheManager.initialize();
        publicAuthoritiesCacheManager.initialize();
    }

    @Override
    public void listUserRoles(ContextPath contextPath, Long userId, List<ContextRole> list){
        // TODO no cache for now
        DefaultContextRole[] contextRoles = contextConfigLoader.loadUserRoles(contextPath, userId);
        for(DefaultContextRole role : contextRoles){
            list.add(role);
        }
    }

    @Override
    public void listPublicAuthorities(ContextPath contextPath, String productId, String clientId, Long tenantId, List<PublicAuthority> list) {
        ContextDomain domain = new ContextDomain(contextPath, productId, clientId, tenantId);
        PublicAuthority[] candidates = null;
        do{
            candidates = publicAuthoritiesCacheManager.getSource(domain);
            if(candidates != null && candidates.length>0){
                for(PublicAuthority it : candidates){
                    list.add(it);
                }
            }

            domain = domain.nextLookup();

        } while(domain != null);
    }

    @Override
    public void listContextRoles(ContextPath contextPath, String productId, String clientId, Long tenantId, List<ContextRole> list) {
        ContextDomain domain = new ContextDomain(contextPath, productId, clientId, tenantId);
        DefaultContextRole[] candidates = null;
        do{
            candidates = rolesCacheManager.getSource(domain);
            if(candidates != null && candidates.length>0){
                for(DefaultContextRole it : candidates){
                    list.add(it);
                }
            }

            domain = domain.nextLookup();

        } while(domain != null);
    }

    @Override
    public ContextRole getContextRole(Long roleId, ContextPath contextPath, String productId, String clientId, Long tenantId) {
        ArrayList<ContextRole> list = new ArrayList<>();
        listContextRoles(contextPath, productId, clientId, tenantId, list);
        for(ContextRole role : list){
            if(role.getId().equals(roleId)){
                return role;
            }
        }
        return null;
    }

    @Override
    public void listApplicationInstallation(ContextPath contextPath, String productId, String clientId, Long tenantId, List<ApplicationInstallation> list) {
        ContextDomain domain = new ContextDomain(contextPath, productId, clientId, tenantId);
        DefaultContextApplicationInstallation[] candidates = null;
        do{
            candidates = applicationsInstallationCacheManager.getSource(domain);
            if(candidates != null && candidates.length>0){
                for(DefaultContextApplicationInstallation it : candidates){
                    list.add(it);
                }
            }

            domain = domain.nextLookup();

        } while(domain != null);
    }

    @Override
    public DefaultContextApplicationInstallation getApplicationInstallation(String applicationId, ContextPath contextPath, String productId, String clientId, Long tenantId) {
        ArrayList<ApplicationInstallation> list =  new ArrayList<>();
        listApplicationInstallation(contextPath, productId, clientId, tenantId, list);
        for(ApplicationInstallation ins : list){
            if(ins.getApplication().getId().equals(applicationId)){
                return (DefaultContextApplicationInstallation) ins;
            }
        }
        return null;
    }
}
