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

import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.product.ProductRepository;

import java.util.ArrayList;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultProductRepository implements ProductRepository {

    private ProductLoader productLoader;

    private ProductCacheManager productCacheManager;

    private ClientCacheManager clientCacheManager;

    public DefaultProductRepository(ProductLoader productLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        productLoader.setProductRepository(this);

        this.productLoader = productLoader;
        this.productCacheManager = new ProductCacheManager(cachePolicy, cleaner, productLoader);
        this.clientCacheManager = new ClientCacheManager(cachePolicy, cleaner, productLoader);

        // Initialize cache manager
        productCacheManager.initialize();
        clientCacheManager.initialize();
    }

    public DefaultClient getClient(String clientId){
        return clientCacheManager.getSource(clientId);
    }

    public DefaultClient[] getClients(String productId){
        String[] clientIds = productLoader.loadClientIds(productId);
        ArrayList<DefaultClient> list = new ArrayList<>(clientIds.length);

        DefaultClient client = null;
        for(String clientId : clientIds){
            client = clientCacheManager.getSource(clientId);
            if(client != null){
                list.add(client);
            }
        }

        DefaultClient[] clients = new DefaultClient[list.size()];
        return list.toArray(clients);
    }

    @Override
    public DefaultProduct getProduct(String productId) {
        return productCacheManager.getSource(productId);
    }


}
