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
import com.tdoer.bedrock.impl.tenant.cache.*;
import com.tdoer.bedrock.tenant.*;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultRentalCenter implements RentalCenter {

    private TenantByIdCacheManager tenantByIdCacheManager;

    private TenantByCodeCacheManager tenantByCodeCacheManager;

    private TenantByGuidCacheManager tenantByGuidCacheManager;

    private TenantClientByHostCacheManager tenantClientByHostCacheManager;

    private ProductRentalsCacheManager productRentalsCacheManager;

    private TenantClientsCacheManager tenantClientsCacheManager;


    public DefaultRentalCenter(TenantLoader tenantLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        tenantLoader.setRentalCenter(this);

        tenantByIdCacheManager = new TenantByIdCacheManager(cachePolicy, cleaner, tenantLoader);
        tenantByCodeCacheManager = new TenantByCodeCacheManager(cachePolicy, cleaner, tenantLoader);
        tenantByGuidCacheManager = new TenantByGuidCacheManager(cachePolicy, cleaner, tenantLoader);
        tenantClientByHostCacheManager = new TenantClientByHostCacheManager(cachePolicy, cleaner, tenantLoader);
        productRentalsCacheManager = new ProductRentalsCacheManager(cachePolicy, cleaner, tenantLoader);
        tenantClientsCacheManager = new TenantClientsCacheManager(cachePolicy, cleaner, tenantLoader);

        // Initialize cache manager
        tenantByIdCacheManager.initialize();
        tenantByCodeCacheManager.initialize();
        tenantByGuidCacheManager.initialize();
        tenantClientByHostCacheManager.initialize();
        productRentalsCacheManager.initialize();
        tenantClientsCacheManager.initialize();
    }

    /**
     * Get tenant of specific tenant code
     *
     * @param tenantCode Tenant code, cannot be blank
     * @return Tenant if found
     * @throws TenantNotFoundException if not found
     */
    @Override
    public DefaultTenant getTenantByCode(String tenantCode) throws TenantNotFoundException {
        Assert.hasText(tenantCode, "Tenant code cannot be blank");

        DefaultTenant tenant = tenantByCodeCacheManager.getSource(tenantCode);
        if(tenant != null){
            return tenant;
        }else{
            throw new TenantNotFoundException(tenantCode);
        }
    }

    /**
     * Get tenant of specific tenant Id
     *
     * @param tenantId Tenant Id, cannot be blank
     * @return Tenant if found
     * @throws TenantNotFoundException if not found
     */
    @Override
    public DefaultTenant getTenantById(Long tenantId) throws TenantNotFoundException {
        Assert.notNull(tenantId, "Tenant Id cannot be blank");

        DefaultTenant tenant = tenantByIdCacheManager.getSource(tenantId);
        if(tenant != null){
            return tenant;
        }else{
            throw new TenantNotFoundException(tenantId);
        }
    }

    /**
     * Get tenant of specific tenant GUID
     *
     * @param guid Tenant GUID, cannot be blank
     * @return Tenant if found
     * @throws TenantNotFoundException if not found
     */
    @Override
    public DefaultTenant getTenantByGUID(String guid) throws TenantNotFoundException {
        Assert.hasText(guid, "Tenant guid cannot be blank");

        DefaultTenant tenant = tenantByGuidCacheManager.getSource(guid);
        if(tenant != null){
            return tenant;
        }else{
            throw new TenantNotFoundException(guid);
        }
    }

    /**
     * Find product rental of tenant Id and product Id
     *
     * @param tenantId  Tenant Id, cannot be <code>null</code>
     * @param productId Product Id, cannot be <code>null</code>
     * @return Product rental or <code>null</code> if not found
     */
    @Override
    public DefaultProductRental getProductRendtal(Long tenantId, Long productId) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(productId, "Product Id cannot be null");

        DefaultProductRental[] rentals =  productRentalsCacheManager.getSource(tenantId);
        if (rentals != null) {
            for(DefaultProductRental rental : rentals){
                if(rental.getProductId().equals(productId)){
                    return rental;
                }
            }
        }

        return null;
    }

    /**
     * List all product rentals of a tenant
     *
     * @param tenantId Tenant Id, cannot be <code>nul</code>
     * @param list
     * @return List of product rental or <code>null</code>
     */
    @Override
    public void listProductRentals(Long tenantId, List<ProductRental> list) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        DefaultProductRental[] rentals =  productRentalsCacheManager.getSource(tenantId);
        if (rentals != null) {
            for(DefaultProductRental rental : rentals){
                list.add(rental);
            }
        }
    }

    /**
     * Find the tenant client of specific access domain
     *
     * @param accessDomain
     * @return Tenant client or <code>null</code> if not found
     */
    @Override
    public DefaultTenantClient getTenantClient(String accessDomain) {
        Assert.notNull(accessDomain, "Access domain cannot be blank");

        return tenantClientByHostCacheManager.getSource(accessDomain);
    }

    /**
     * Get tenant client of specific tenant and client
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @param clientId Client Id, cannot be <code>null</code>
     * @return
     */
    @Override
    public DefaultTenantClient getTenantClient(Long tenantId, Long clientId) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(clientId, "Client Id cannot be null");

        DefaultTenantClient[] clients =  tenantClientsCacheManager.getSource(tenantId);
        if (clients != null) {
            for(DefaultTenantClient client : clients){
                if(client.getClientId().equals(clientId)){
                    return client;
                }
            }
        }

        return null;
    }

    /**
     * Get tenant client of specific tenant and client
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @param clientGUID Client GUID, cannot be blank
     * @return Tenant client or <code>null</code> if not found
     */
    @Override
    public TenantClient getTenantClient(Long tenantId, String clientGUID) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(clientGUID, "Client Id cannot be blank");

        DefaultTenantClient[] clients =  tenantClientsCacheManager.getSource(tenantId);
        if (clients != null) {
            for(DefaultTenantClient client : clients){
                if(client.getClientId().equals(clientGUID)){
                    return client;
                }
            }
        }

        return null;
    }

    /**
     * Get all tenant clients of a tenant
     *
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @param list
     * @return List of tenant client or <code>null</code>
     */
    @Override
    public void listTenantClients(Long tenantId, List<TenantClient> list) {
        Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(list, "List cannot be null");

        DefaultTenantClient[] clients =  tenantClientsCacheManager.getSource(tenantId);
        if (clients != null) {
            for(DefaultTenantClient client : clients){
                list.add(client);
            }
        }
    }
}
