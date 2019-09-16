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
package com.tdoer.bedrock.impl.product;

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ClientDomain;
import com.tdoer.bedrock.impl.domain.ProductDomain;
import com.tdoer.bedrock.product.ClientApplicationInstallation;
import com.tdoer.bedrock.product.ClientConfigCenter;
import com.tdoer.bedrock.product.ClientService;
import com.tdoer.bedrock.product.ContextInstallation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientConfigCenter implements ClientConfigCenter {

    private ProductLoader productLoader;

    private ClientApplicationInstallationsCacheManager applicationInstallationsCache;

    private ClientServicesCacheManager servicesCacheManager;

    private ContextInstallationsCacheManager contextInstallationCache;

    private TokenConfigCacheManager tokenConfigCacheManager;

    public DefaultClientConfigCenter(ProductLoader productLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        this.productLoader = productLoader;

        applicationInstallationsCache = new ClientApplicationInstallationsCacheManager(cachePolicy, cleaner, productLoader);
        servicesCacheManager = new ClientServicesCacheManager(cachePolicy, cleaner, productLoader);
        contextInstallationCache = new ContextInstallationsCacheManager(cachePolicy, cleaner, productLoader);
        tokenConfigCacheManager = new TokenConfigCacheManager(cachePolicy, cleaner,productLoader);

        // Initialize cache managers
        applicationInstallationsCache.initialize();
        servicesCacheManager.initialize();
        contextInstallationCache.initialize();
        tokenConfigCacheManager.initialize();
    }

    @Override
    public DefaultClientApplicationInstallation getApplicationInstallation(String applicationId, String productId, String clientId, Long tenantId) {
        ArrayList<ClientApplicationInstallation> list = new ArrayList<>();
        listApplicationInstallations(productId, clientId, tenantId, list);
        for(ClientApplicationInstallation installation : list){
            if(installation.getApplication().getId().equals(applicationId)){
                return (DefaultClientApplicationInstallation) installation;
            }
        }
        return null;
    }

    @Override
    public void listApplicationInstallations(String productId, String clientId, Long tenantId, List<ClientApplicationInstallation> list) {
        ProductDomain domain = new ProductDomain(productId, clientId, tenantId);
        DefaultClientApplicationInstallation[] arr;
        do{
            arr = applicationInstallationsCache.getSource(domain);
            if(arr != null){
                for(DefaultClientApplicationInstallation ai : arr){
                    list.add(ai);
                }
            }

            domain = domain.nextLookup();
        }while(domain != null);
    }

    @Override
    public DefaultClientService getClientService(String serviceId, String productId, String clientId, Long tenantId) {
        ArrayList<ClientService> list = new ArrayList<>();
        listClientServices(productId, clientId, tenantId, list);
        for(ClientService clientService : list){
            if(clientService.getService().getId().equals(serviceId)){
                return (DefaultClientService) clientService;
            }
        }
        return null;
    }

    @Override
    public void listClientServices(String productId, String clientId, Long tenantId, List<ClientService> list) {
        ProductDomain domain = new ProductDomain(productId, clientId, tenantId);
        DefaultClientService[] arr;
        do{
            arr = servicesCacheManager.getSource(domain);
            if(arr != null){
                for(DefaultClientService ai : arr){
                    list.add(ai);
                }
            }

            domain = domain.nextLookup();
        }while(domain != null);
    }

    @Override
    public DefaultContextInstallation getContextInstallation(ContextPath contextPath, String productId, String clientId, Long tenantId) {
        ArrayList<ContextInstallation> list = new ArrayList<>();
        listContextInstallations(productId, clientId, tenantId, list);

        do{
            for(ContextInstallation installation : list){
                if(installation.getContextPath().equals(contextPath)){
                    return (DefaultContextInstallation) installation;
                }
            }
            ContextPath next = contextPath.parentTemplate();
            if(next.equals(contextPath)){
                // reach the end
                break;
            }
            contextPath = next;
        }while(true);

        return null;
    }


    @Override
    public void listContextInstallations(String productId, String clientId, Long tenantId, List<ContextInstallation> list) {
        ProductDomain domain = new ProductDomain(productId, clientId, tenantId);
        DefaultContextInstallation[] arr;
        do{
            arr = contextInstallationCache.getSource(domain);
            if(arr != null){
                for(DefaultContextInstallation ai : arr){
                    list.add(ai);
                }
            }
            domain = domain.nextLookup();
        } while(domain != null);
    }

    @Override
    public DefaultTokenConfig getTokenConfig(String clientId, Long tenantId) {
        ClientDomain clientDomain = new ClientDomain(clientId, tenantId);
        DefaultTokenConfig candidate;
        do{
            candidate = tokenConfigCacheManager.getSource(clientDomain);
            if(candidate != null){
                return candidate;
            }

            clientDomain = clientDomain.nextLookup();
        } while (clientDomain != null);

        return null;
    }
}
