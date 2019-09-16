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
package com.tdoer.bedrock.impl.tenant;

import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ClientDomain;
import com.tdoer.bedrock.impl.domain.ProductDomain;
import com.tdoer.bedrock.tenant.RentalCenter;

import java.util.ArrayList;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultRentalCenter implements RentalCenter {

    private TenantCacheManagerByID tenantCenterByID;

    private TenantCacheManagerByCode tenantCenterByCode;

    private ProductRentalCacheManager rentalCacheManager;

    private TenantClientCacheManagerByHost clientCacheManagerByHost;

    private TenantClientCacheManagerByDomain clientCacheManagerByDomain;

    private ProductIdsCacheManager productIdsCacheManager;

    private ClientIdsCacheManager clientIdsCacheManager;


    public DefaultRentalCenter(TenantLoader tenantLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        tenantLoader.setRentalCenter(this);

        tenantCenterByID = new TenantCacheManagerByID(cachePolicy, cleaner, tenantLoader);
        tenantCenterByCode = new TenantCacheManagerByCode(cachePolicy, cleaner, tenantLoader);
        rentalCacheManager = new ProductRentalCacheManager(cachePolicy, cleaner, tenantLoader);
        clientCacheManagerByHost = new TenantClientCacheManagerByHost(cachePolicy, cleaner, tenantLoader);
        clientCacheManagerByDomain = new TenantClientCacheManagerByDomain(cachePolicy, cleaner, tenantLoader);
        productIdsCacheManager = new ProductIdsCacheManager(cachePolicy, cleaner, tenantLoader);
        clientIdsCacheManager = new ClientIdsCacheManager(cachePolicy, cleaner, tenantLoader);

        // Initialize cache manager
        tenantCenterByID.initialize();
        tenantCenterByCode.initialize();
        rentalCacheManager.initialize();
        clientCacheManagerByHost.initialize();
        clientCacheManagerByDomain.initialize();
        productIdsCacheManager.initialize();
        clientIdsCacheManager.initialize();

    }

    @Override
    public DefaultTenant getTenant(String tenantCode) {
        return tenantCenterByCode.getSource(tenantCode);
    }

    @Override
    public DefaultTenant getTenant(Long tenantId) {
        return tenantCenterByID.getSource(tenantId);
    }

    @Override
    public DefaultProductRental getProductRendtal(Long tenantId, String productId) {
        ProductDomain domain = new ProductDomain(productId, null, tenantId);
        return rentalCacheManager.getSource(domain);
    }

    @Override
    public DefaultProductRental[] getProductRendtal(Long tenantId) {
        String[] productIds = productIdsCacheManager.getSource(tenantId);
        ArrayList<DefaultProductRental> list = new ArrayList<>(productIds.length);
        for(String productId : productIds){
            try{
                list.add(getProductRendtal(tenantId, productId));
            }catch (Throwable t){
                // todo, warn
            }
        }

        DefaultProductRental[] ret = new DefaultProductRental[list.size()];
        return list.toArray(ret);
    }

    @Override
    public DefaultTenantClient getTenantClient(String host) {
        return clientCacheManagerByHost.getSource(host);
    }

    @Override
    public DefaultTenantClient getTenantClient(Long tenantId, String clientId) {
        ClientDomain domain = new ClientDomain(clientId, tenantId);
        return clientCacheManagerByDomain.getSource(domain);
    }

    @Override
    public DefaultTenantClient[] getTenantClients(Long tenantId) {
        String[] clientIds = clientIdsCacheManager.getSource(tenantId);
        ArrayList<DefaultTenantClient> list = new ArrayList<>(clientIds.length);
        for(String clientId : clientIds){
            try{
                list.add(getTenantClient(tenantId, clientId));
            }catch (Throwable t){
                // todo, warn
            }
        }

        DefaultTenantClient[] ret = new DefaultTenantClient[list.size()];
        return list.toArray(ret);
    }
}
