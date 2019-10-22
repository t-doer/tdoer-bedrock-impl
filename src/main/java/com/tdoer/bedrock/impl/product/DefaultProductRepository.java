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
import com.tdoer.bedrock.impl.product.cache.*;
import com.tdoer.bedrock.product.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultProductRepository implements ProductRepository {

    private ProductLoader productLoader;

    private ProductByIdCacheManager productByIdCacheManager;

    private ProductByCodeCacheManager productByCodeCacheManager;

    private TokenConfigCacheManager tokenConfigCacheManager;

    private ClientsCacheManager clientsCacheManager;

    private ClientApplicationInstallationsCacheManager applicationInstallationsCacheManager;

    private ClientContextInstallationsCacheManager contextInstallationsCacheManager;

    private ClientServiceInstallationsCacheManager serviceInstallationsCacheManager;


    public DefaultProductRepository(ProductLoader productLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        productLoader.setProductRepository(this);

        this.productLoader = productLoader;
        productByIdCacheManager = new ProductByIdCacheManager(cachePolicy, cleaner, productLoader);
        productByCodeCacheManager = new ProductByCodeCacheManager(cachePolicy, cleaner, productLoader);
        tokenConfigCacheManager = new TokenConfigCacheManager(cachePolicy, cleaner, productLoader);
        clientsCacheManager = new ClientsCacheManager(cachePolicy, cleaner, productLoader);
        applicationInstallationsCacheManager = new ClientApplicationInstallationsCacheManager(cachePolicy, cleaner,
                productLoader);
        contextInstallationsCacheManager = new ClientContextInstallationsCacheManager(cachePolicy, cleaner,
                productLoader);
        serviceInstallationsCacheManager = new ClientServiceInstallationsCacheManager(cachePolicy, cleaner, productLoader);

        // Initialize cache manager
        productByIdCacheManager.initialize();
        productByCodeCacheManager.initialize();;
        tokenConfigCacheManager.initialize();;
        clientsCacheManager.initialize();
        applicationInstallationsCacheManager.initialize();
        contextInstallationsCacheManager.initialize();
        serviceInstallationsCacheManager.initialize();
    }

    /**
     * Get product of specific Id
     *
     * @param productId Product Id, cannot be <code>null</code>
     * @return Product if found
     * @throws ProductNotFoundException if the product dose not exist or is disabled
     */
    @Override
    public DefaultProduct getProduct(Long productId) throws ProductNotFoundException {
        Assert.notNull(productId, "Product Id cannot be null");

        DefaultProduct ret = productByIdCacheManager.getSource(productId);
        if(ret != null){
            return ret;
        }else{
            throw new ProductNotFoundException(productId);
        }
    }

    /**
     * Get product of specific product code
     *
     * @param productCode Product code, cannot be <code>null</code>
     * @return Product if found
     * @throws ProductNotFoundException if the product dose not exist or is disabled
     */
    @Override
    public Product getProduct(String productCode) throws ProductNotFoundException {
        Assert.hasText(productCode, "Product code cannot be null");

        DefaultProduct ret = productByCodeCacheManager.getSource(productCode);
        if(ret != null){
            return ret;
        }else{
            throw new ProductNotFoundException(productCode);
        }
    }

    /**
     * Get specific client of specific product
     *
     * @param productId Product Id, cannot be <code>null</code>
     * @param clientId  Client Id, cannot be <code>null</code>
     * @return Client if found
     * @throws ClientNotFoundException if the client dose not exist or is disabled
     */
    @Override
    public DefaultClient getClient(Long productId, Long clientId) throws ClientNotFoundException {
        DefaultClient[] clients = clientsCacheManager.getSource(productId);
        for(DefaultClient client : clients){
            if(client.getId().equals(clientId)){
                return client;
            }
        }
        throw new ClientNotFoundException(clientId);
    }

    /**
     * Get specific client of specific product
     *
     * @param productId  Product Id, cannot be <code>null</code>
     * @param clientCode Client code, cannot be <code>null</code>
     * @return Client if found
     * @throws ClientNotFoundException if the client dose not exist or is disabled
     */
    @Override
    public Client getClient(Long productId, String clientCode) throws ClientNotFoundException {
        DefaultClient[] clients = clientsCacheManager.getSource(productId);
        for(DefaultClient client : clients){
            if(client.getCode().equals(clientCode)){
                return client;
            }
        }
        throw new ClientNotFoundException(clientCode);
    }

    /**
     * List all client of specific product
     *
     * @param productId Product Id, cannot be <code>null</code>
     * @param list      List to hold clients, cannot be <code>null</code>
     */
    @Override
    public void listClients(Long productId, List<Client> list) {
        Assert.notNull(productId, "Product Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        DefaultClient[] ret = clientsCacheManager.getSource(productId);
        if(ret != null){
            for(DefaultClient client : ret){
                list.add(client);
            }
        }
    }

    /**
     * The application installation of specific application in the tenant's client
     *
     * @param clientId      Client Id, cannot be <code>null</code>
     * @param tenantId      Tenant Id, cannot be <code>null</code>, but can be zero
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return {@link ClientApplicationInstallation} if it exists or is enabled, otherwise return <code>null</code>
     */
    @Override
    public DefaultClientApplicationInstallation getApplicationInstallation(Long clientId, Long tenantId, Long applicationId) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(applicationId, "Application Id cannot be null");

        ArrayList<ClientApplicationInstallation> list = new ArrayList<>();
        listApplicationInstallations(clientId, tenantId, list);
        for(ClientApplicationInstallation installation : list){
            if(installation.getApplicationId().equals(applicationId)){
                return (DefaultClientApplicationInstallation) installation;
            }
        }
        return null;
    }

    /**
     * List application installations which are installed in the tenant's client
     *
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param list     List to hold application installations, cannot be <code>null</code>
     */
    @Override
    public void listApplicationInstallations(Long clientId, Long tenantId, List<ClientApplicationInstallation> list) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ClientDomainEnumerator enumerator = new ClientDomainEnumerator(clientId, tenantId);
        DefaultClientApplicationInstallation[] arr = null;
        while(enumerator.hasMoreElements()){
            arr = applicationInstallationsCacheManager.getSource(enumerator.nextElement());
            if(arr != null){
                for(DefaultClientApplicationInstallation ins : arr){
                    list.add(ins);
                }
            }
        }
    }

    /**
     * Get the service installation of specific service in the tenant's client
     *
     * @param clientId  Client Id, cannot be <code>null</code>
     * @param tenantId  Tenant Id, cannot be <code>null</code>, but can be zero
     * @param serviceId Service Id, cannot be <code>null</code>
     * @return {@link ClientServiceInstallation} if it exists or is enabled, otherwise return <code>null</code>
     */
    @Override
    public DefaultClientServiceInstallation getClientServiceInstallation(Long clientId, Long tenantId, Long serviceId) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(serviceId, "Service Id cannot be null");

        ArrayList<ClientServiceInstallation> list = new ArrayList<>();
        listClientServiceInstallations(clientId, tenantId, list);
        for(ClientServiceInstallation ins : list){
            if(ins.getServiceId().equals(serviceId)){
                return (DefaultClientServiceInstallation) ins;
            }
        }
        return null;
    }

    /**
     * List service installations which are installed in the tenant's client
     *
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param list     List to hold service installations, cannot be <code>null</code>
     */
    @Override
    public void listClientServiceInstallations(Long clientId, Long tenantId, List<ClientServiceInstallation> list) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ClientDomainEnumerator enumerator = new ClientDomainEnumerator(clientId, tenantId);
        DefaultClientServiceInstallation[] arr = null;
        while(enumerator.hasMoreElements()){
            arr = serviceInstallationsCacheManager.getSource(enumerator.nextElement());
            if(arr != null){
                for(DefaultClientServiceInstallation ins : arr){
                    list.add(ins);
                }
            }
        }
    }

    /**
     * Get the context (context type or context instance) installation of specific context path in the tenant's client
     *
     * @param clientId    Client Id, cannot be <code>null</code>
     * @param tenantId    Tenant Id, cannot be <code>null</code>, but can be zero
     * @param contextPath Context path, cannot be <code>null</code>
     * @return {@link ClientContextInstallation} if it exists or is enabled, otherwise return <code>null</code>
     */
    @Override
    public ClientContextInstallation getContextInstallation(Long clientId, Long tenantId, ContextPath contextPath) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");

        ArrayList<ClientContextInstallation> list = new ArrayList<>();
        listContextInstallations(clientId, tenantId, list);

        ContextPath cp = contextPath;
        do{
            for(ClientContextInstallation installation : list){
                if(installation.getContextPath().equals(cp)){
                    return (DefaultClientContextInstallation) installation;
                }
            }
            ContextPath next = contextPath.nextLookup();
            if(next == null){
                // reach the end
                break;
            }
            cp = next;
        }while(true);

        return null;
    }

    /**
     * List context installations which are installed in the tenant's client
     *
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @param list     List to hold context installations, cannot be <code>null</code>
     */
    @Override
    public void listContextInstallations(Long clientId, Long tenantId, List<ClientContextInstallation> list) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        ClientDomainEnumerator enumerator = new ClientDomainEnumerator(clientId, tenantId);
        DefaultClientContextInstallation[] arr = null;
        while(enumerator.hasMoreElements()){
            arr = contextInstallationsCacheManager.getSource(enumerator.nextElement());
            if(arr != null){
                for(DefaultClientContextInstallation ins : arr){
                    list.add(ins);
                }
            }
        }
    }

    /**
     * The token configuration
     *
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>, but can be zero
     * @return The token configuration, must not be <code>null</code>
     */
    @Override
    public DefaultTokenConfig getTokenConfig(Long clientId, Long tenantId) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");

        ClientDomainEnumerator enumerator = new ClientDomainEnumerator(clientId, tenantId);
        DefaultTokenConfig config = null;
        while(enumerator.hasMoreElements()){
            config = tokenConfigCacheManager.getSource(enumerator.nextElement());
            if(config != null){
                return config;
            }
        }

        return null;
    }
}
