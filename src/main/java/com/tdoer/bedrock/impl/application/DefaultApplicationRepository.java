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
import com.tdoer.bedrock.application.ApplicationRepository;
import com.tdoer.bedrock.application.Page;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ApplicationDomain;
import com.tdoer.bedrock.impl.service.DefaultService;
import com.tdoer.bedrock.service.Service;
import org.springframework.util.Assert;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultApplicationRepository implements ApplicationRepository {

    protected ApplicationCacheManager applicationCacheManager;

    protected PagesCacheManager pagesCacheManager;

    protected ActionsCacheManager actionsCacheManager;

    protected ServicesCacheManager servicesCacheManager;

    public DefaultApplicationRepository(ApplicationLoader applicationLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        Assert.notNull(applicationLoader, "ApplicationLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");

        applicationLoader.setApplicationRepository(this);
        applicationCacheManager = new ApplicationCacheManager(cachePolicy, cleaner, applicationLoader);
        pagesCacheManager = new PagesCacheManager(cachePolicy, cleaner, applicationLoader);
        actionsCacheManager = new ActionsCacheManager(cachePolicy, cleaner, applicationLoader);
        servicesCacheManager = new ServicesCacheManager(cachePolicy, cleaner, applicationLoader);

        // Initialize cache managers
        applicationCacheManager.initialize();
        pagesCacheManager.initialize();
        actionsCacheManager.initialize();
        servicesCacheManager.initialize();
    }

    @Override
    public DefaultApplication getApplication(String applicationId){
        return applicationCacheManager.getSource(applicationId);
    }

    /**
     * List pages of specific application which for the product, client, tenant, and context instance.
     *
     * @param applicationId
     * @param productId
     * @param clientId
     * @param tenantId
     * @param contextPath
     * @param list
     */
    @Override
    public void listPages(String applicationId, String productId, String clientId, Long tenantId, ContextPath contextPath, List<Page> list) {
        ApplicationDomain domain = new ApplicationDomain(applicationId, productId, clientId, tenantId, contextPath);
        do{
            DefaultPage[] pages = pagesCacheManager.getSource(domain);
            if(pages != null){
                for(DefaultPage pg : pages){
                    list.add(pg);
                }
            }
            domain = domain.nextLookup();
        }while(domain != null);

    }

    /**
     * List actions of specific page which for the product, client, tenant, and context instance.
     *
     * @param pageId
     * @param applicationId
     * @param productId
     * @param clientId
     * @param tenantId
     * @param contextPath
     * @param list
     */
    @Override
    public void listActions(Long pageId, String applicationId, String productId, String clientId, Long tenantId, ContextPath contextPath, List<Action> list) {
        ApplicationDomain domain = new ApplicationDomain(applicationId, productId, clientId, tenantId, contextPath);
        do{
            DefaultAction[] actions = actionsCacheManager.getSource(domain);
            if(actions != null){
                for(DefaultAction action : actions){
                    if(action.getPageId().equals(pageId)){
                        list.add(action);
                    }
                }
            }
            domain = domain.nextLookup();
        }while(domain != null);
    }

    /**
     * List services of specific service Id which for the product, client, tenant, and context instance.
     *
     * @param applicationId
     * @param productId
     * @param clientId
     * @param tenantId
     * @param contextPath
     * @param list
     */
    @Override
    public void listServices(String applicationId, String productId, String clientId, Long tenantId, ContextPath contextPath, List<Service> list) {
        ApplicationDomain domain = new ApplicationDomain(applicationId, productId, clientId, tenantId, contextPath);
        do{
            DefaultService[] services = servicesCacheManager.getSource(domain);
            if(services != null){
                for(DefaultService service : services){
                    list.add(service);
                }
            }
            domain = domain.nextLookup();
        }while(domain != null);
    }
}
