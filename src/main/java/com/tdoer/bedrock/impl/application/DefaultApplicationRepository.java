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

import com.tdoer.bedrock.application.*;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.application.cache.*;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.bedrock.service.ServiceMethod;
import org.springframework.util.Assert;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultApplicationRepository implements ApplicationRepository {

    protected ApplicationByCodeCacheManager applicationByCodeCacheManager;

    protected ApplicationByIdCacheManager applicationByIdCacheManager;

    protected PagesCacheManager pagesCacheManager;

    protected PageIdsCacheManager pageIdsCacheManager;

    protected ActionsCacheManager actionsCacheManager;

    protected ActionIdsCacheManager actionIdsCacheManager;

    protected ServiceIdsByDomainCacheManager serviceIdsByDomainCacheManager;

    protected ServiceIdsByIdCacheManager serviceIdsByIdCacheManager;

    protected MethodsByPageIdCacheManager methodsByPageIdCacheManager;

    protected MethodsByActionIdCacheManager methodsByActionIdCacheManager;

    public DefaultApplicationRepository(ApplicationLoader applicationLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        Assert.notNull(applicationLoader, "ApplicationLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");

        applicationLoader.setApplicationRepository(this);
        applicationByCodeCacheManager = new ApplicationByCodeCacheManager(cachePolicy, cleaner, applicationLoader);
        applicationByIdCacheManager = new ApplicationByIdCacheManager(cachePolicy, cleaner, applicationLoader);
        pagesCacheManager = new PagesCacheManager(cachePolicy, cleaner, applicationLoader);
        pageIdsCacheManager = new PageIdsCacheManager(cachePolicy, cleaner, applicationLoader);
        actionsCacheManager = new ActionsCacheManager(cachePolicy, cleaner, applicationLoader);
        actionIdsCacheManager = new ActionIdsCacheManager(cachePolicy, cleaner, applicationLoader);
        serviceIdsByDomainCacheManager = new ServiceIdsByDomainCacheManager(cachePolicy, cleaner, applicationLoader);
        serviceIdsByIdCacheManager = new ServiceIdsByIdCacheManager(cachePolicy, cleaner, applicationLoader);
        methodsByPageIdCacheManager = new MethodsByPageIdCacheManager(cachePolicy, cleaner, applicationLoader);
        methodsByActionIdCacheManager = new MethodsByActionIdCacheManager(cachePolicy, cleaner, applicationLoader);

        // Initialize cache managers
        applicationByCodeCacheManager.initialize();
        applicationByIdCacheManager.initialize();
        pagesCacheManager.initialize();
        pageIdsCacheManager.initialize();
        actionsCacheManager.initialize();
        actionIdsCacheManager.initialize();
        serviceIdsByDomainCacheManager.initialize();
        serviceIdsByIdCacheManager.initialize();
        methodsByPageIdCacheManager.initialize();
        methodsByActionIdCacheManager.initialize();
    }

    /**
     * Get application of specific Id
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return Application if found
     * @throws ApplicationNotFoundException if application does not exist or is disabled
     */
    @Override
    public DefaultApplication getApplication(Long applicationId) throws ApplicationNotFoundException {
        Assert.notNull(applicationId, "Application Id cannot be null");

        DefaultApplication ret = applicationByIdCacheManager.getSource(applicationId);
        if(ret != null){
            return ret;
        }else{
            throw new ApplicationNotFoundException(applicationId);
        }
    }

    /**
     * Get application of specific code
     *
     * @param applicationCode Application code, cannot be <code>null</code>
     * @return Application if found
     * @throws ApplicationNotFoundException if application does not exist or is disabled
     */
    @Override
    public DefaultApplication getApplication(String applicationCode) throws ApplicationNotFoundException {
        Assert.hasText(applicationCode, "Application code cannot be blank");

        DefaultApplication ret = applicationByCodeCacheManager.getSource(applicationCode);
        if(ret != null){
            return ret;
        }else{
            throw new ApplicationNotFoundException(applicationCode);
        }
    }

    /**
     * Get page of specific Id
     *
     * @param applicationId Application Id, cannot be null
     * @param pageId        Page Id, cannot be null
     * @return Page if found
     * @throws PageNotFoundException if the page dose not exist or is disabled
     */
    @Override
    public DefaultPage getPage(Long applicationId, Long pageId) throws PageNotFoundException {
        Assert.notNull(applicationId, "Application Id cannot be null");
        Assert.notNull(pageId, "Page Id cannot be null");

        DefaultPage[] pages = pagesCacheManager.getSource(applicationId);
        if(pages != null){
            for(DefaultPage pg : pages){
                if(pg.getId().equals(pageId)){
                    return pg;
                }
            }
        }

        throw new PageNotFoundException(pageId);
    }

    /**
     * Get page of specific page code in the application
     *
     * @param applicationId application Id, cannot be <code>null</code>
     * @param pageCode      page code, cannot be <code>null</code>
     * @return Page if found
     * @throws PageNotFoundException if the page dose not exist or is disabled
     */
    @Override
    public DefaultPage getPage(Long applicationId, String pageCode) throws PageNotFoundException {
        Assert.notNull(applicationId, "Application Id cannot be null");
        Assert.hasText(pageCode, "Page Id cannot be blank");

        DefaultPage[] pages = pagesCacheManager.getSource(applicationId);
        if(pages != null){
            for(DefaultPage pg : pages){
                if(pg.getCode().equals(pageCode)){
                    return pg;
                }
            }
        }

        throw new PageNotFoundException(applicationId, pageCode);
    }

    /**
     * List pages of specific application which are available for current product, client, tenant, and context instance.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param productId     Product Id, cannot be <code>null</code>
     * @param clientId      Client Id, cannot be <code>null</code>
     * @param tenantId      Tenant Id, cannot be <code>null</code>
     * @param contextPath   Context path, cannot be <code>null</code>
     * @param list          List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void  listCurrentPages(Long applicationId, Long productId, Long clientId, Long tenantId,
                                  ContextPath contextPath, List<Page> list) {
        Assert.notNull(applicationId, "Application Id cannot be null");
        Assert.notNull(productId, "Product Id cannot be null");
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(list, "List cannot be null");

        ApplicationDomainEnumerator enumerator = new ApplicationDomainEnumerator(applicationId, productId, clientId
                , tenantId, contextPath);
        while(enumerator.hasMoreElements()){
            Long[] pageIds = pageIdsCacheManager.getSource(enumerator.nextElement());
            if(pageIds != null){
                for(Long pageId : pageIds){
                    list.add(getPage(applicationId, pageId));
                }
            }
        }
    }

    /**
     * List an application's common pages only, excluding customized ones.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param list          List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listCommonPages(Long applicationId, List<Page> list) {
        Assert.notNull(applicationId, "Application Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ApplicationDomain domain = new ApplicationDomain(applicationId, 0L, 0L, 0L, null);
        Long[] pageIds = pageIdsCacheManager.getSource(domain);
        if(pageIds != null){
            for(Long pageId : pageIds){
                list.add(getPage(applicationId, pageId));
            }
        }
    }

    /**
     * List an application's all available pages, including common and customized ones.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param list          List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listAllPages(Long applicationId, List<Page> list) {
        Assert.notNull(list, "List cannot be null");

        DefaultPage[] pages = pagesCacheManager.getSource(applicationId);
        if(pages != null){
            for(DefaultPage pg : pages){
                list.add(pg);
            }
        }
    }

    /**
     * Get action of specific Id in a specific application's page
     *
     * @param pageId
     * @param actionId Action Id, cannot be <code>null</code>
     * @return Action if found
     * @throws ActionNotFoundException if the action dose not exist or is disabled
     * @
     */
    @Override
    public DefaultAction getAction(Long pageId, Long actionId) throws ActionNotFoundException {
        Assert.notNull(pageId, "Page Id cannot be null");
        Assert.notNull(actionId, "Action Id cannot be null");

        DefaultAction[] actions = actionsCacheManager.getSource(pageId);
        if(actions != null){
            for(DefaultAction act : actions){
                if(act.getId().equals(actionId)){
                    return act;
                }
            }
        }

        throw new ActionNotFoundException(actionId);
    }

    /**
     * Get action of specific action code in the page
     *
     * @param pageId     page Id, cannot be <code>null</code>
     * @param actionCode action code, cannot be <code>null</code>
     * @return Action if found
     * @throws ActionNotFoundException if the action dose not exist or is disabled
     */
    @Override
    public DefaultAction getAction(Long pageId, String actionCode) throws ActionNotFoundException {
        Assert.notNull(pageId, "Page Id cannot be null");
        Assert.hasText(actionCode, "Action Id cannot be blank");

        DefaultAction[] actions = actionsCacheManager.getSource(pageId);
        if(actions != null){
            for(DefaultAction act : actions){
                if(act.getCode().equals(actionCode)){
                    return act;
                }
            }
        }

        throw new ActionNotFoundException(pageId, actionCode);
    }

    /**
     * List actions of specific page of the application which are available for
     * current product, client, tenant, and context instance.
     *
     * @param pageId        Page Id, cannot be <code>null</code>
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param productId     Product Id, cannot be <code>null</code>
     * @param clientId      Client Id, cannot be <code>null</code>
     * @param tenantId      Tenant Id, cannot be <code>null</code>
     * @param contextPath   Context path, cannot be <code>null</code>
     * @param list          List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listCurrentActions(Long pageId, Long applicationId, Long productId, Long clientId, Long tenantId, ContextPath contextPath, List<Action> list) {
        Assert.notNull(pageId, "Page Id cannot be null");
        Assert.notNull(applicationId, "Application Id cannot be null");
        Assert.notNull(productId, "Product Id cannot be null");
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(list, "List cannot be null");

        PageDomainEnumerator enumerator = new PageDomainEnumerator(pageId, productId, clientId
                , tenantId, contextPath);
        while(enumerator.hasMoreElements()){
            Long[] actionIds = actionIdsCacheManager.getSource(enumerator.nextElement());
            if(actionIds != null){
                for(Long actionId : actionIds){
                    list.add(getAction(pageId, actionId));
                }
            }
        }
    }

    /**
     * List common actions of a page only, excluding customized ones
     *
     * @param pageId Page Id, cannot be <code>null</code>
     * @param list   List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listCommonActions(Long pageId, List<Action> list) {
        Assert.notNull(pageId, "Page Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        PageDomain domain = new PageDomain(pageId, 0L, 0L, 0L,null);
        Long[] actionIds = actionIdsCacheManager.getSource(domain);
        if(actionIds != null){
            for(Long actionId : actionIds){
                list.add(getAction(pageId, actionId));
            }
        }
    }

    /**
     * List all available actions of a page, including common and customized ones
     *
     * @param pageId Page Id, cannot be <code>null</code>
     * @param list   List to hold pages, cannot be <code>null</code>
     */
    @Override
    public void listAllActions(Long pageId, List<Action> list) {
        Assert.notNull(list, "List cannot be null");

        DefaultAction[] actions = actionsCacheManager.getSource(pageId);
        if(actions != null){
            for(DefaultAction act : actions){
                list.add(act);
            }
        }
    }

    /**
     * List the Ids of referee services which are referred to or called by specific application,
     * and are available for current product, client, tenant, and context instance.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param productId     Product Id, cannot be <code>null</code>
     * @param clientId      Client Id, cannot be <code>null</code>
     * @param tenantId      Tenant Id, cannot be <code>null</code>
     * @param contextPath   Context path, cannot be <code>null</code>
     * @param list          List to referee services, cannot be <code>null</code>
     */
    @Override
    public void listCurrentRefereeServiceIds(Long applicationId, Long productId, Long clientId, Long tenantId, ContextPath contextPath, List<Long> list) {
        Assert.notNull(applicationId, "Application Id cannot be null");
        Assert.notNull(productId, "Product Id cannot be null");
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(list, "List cannot be null");

        ApplicationDomainEnumerator enumerator = new ApplicationDomainEnumerator(applicationId, productId, clientId
                , tenantId, contextPath);
        while(enumerator.hasMoreElements()){
            Long[] serviceIds = serviceIdsByDomainCacheManager.getSource(enumerator.nextElement());
            if(serviceIds != null){
                for(Long serviceId : serviceIds){
                    list.add(serviceId);
                }
            }
        }
    }

    /**
     * List all common referee services of the application only, excluding customized ones.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param list          List to referee services, cannot be <code>null</code>
     */
    @Override
    public void listCommonRefereeServiceIds(Long applicationId, List<Long> list) {
        Assert.notNull(applicationId, "Application Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ApplicationDomain domain = new ApplicationDomain(applicationId, 0L, 0L
                , 0L, null);
        Long[] serviceIds = serviceIdsByDomainCacheManager.getSource(domain);
        if(serviceIds != null){
            for(Long serviceId : serviceIds){
                list.add(serviceId);
            }
        }
    }

    /**
     * List all referee services of the application only, including common ones and  customized ones.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param list          List to referee services, cannot be <code>null</code>
     */
    @Override
    public void listAllRefereeServiceIds(Long applicationId, List<Long> list) {
        Assert.notNull(applicationId, "Application Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ApplicationDomain domain = new ApplicationDomain(applicationId, 0L, 0L
                , 0L, null);
        Long[] serviceIds = serviceIdsByIdCacheManager.getSource(applicationId);
        if(serviceIds != null){
            for(Long serviceId : serviceIds){
                list.add(serviceId);
            }
        }
    }

    /**
     * List the service methods which the action needs to call
     *
     * @param actionId Action Id, cannot be <code>null</code>
     * @param list     List to hold service methods, cannot be <code>null</code>
     */
    @Override
    public void listServiceMethodsOfAction(Long actionId, List<ServiceMethod> list) {
        Assert.notNull(actionId, "Action Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        DefaultServiceMethod[] methods = methodsByActionIdCacheManager.getSource(actionId);
        if(methods != null){
            for(DefaultServiceMethod method : methods){
                list.add(method);
            }
        }
    }

    /**
     * List the service methods which will be called when the page is being loaded
     *
     * @param pageId Page Id, cannot be <code>null</code>
     * @param list   List to hold service methods, cannot be <code>null</code>
     */
    @Override
    public void listServiceMethodsOfPage(Long pageId, List<ServiceMethod> list) {
        Assert.notNull(pageId, "Page Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        DefaultServiceMethod[] methods = methodsByPageIdCacheManager.getSource(pageId);
        if(methods != null){
            for(DefaultServiceMethod method : methods){
                list.add(method);
            }
        }
    }
}
