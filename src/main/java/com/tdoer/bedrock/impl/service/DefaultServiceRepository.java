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
package com.tdoer.bedrock.impl.service;

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ServiceDomain;
import com.tdoer.bedrock.service.ServiceMethod;
import com.tdoer.bedrock.service.ServiceRepository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultServiceRepository implements ServiceRepository {

    private ServiceCacheManager serviceCacheManager;
    
    private ServiceMethodsCacheManager methodsCacheManager;

    private ServiceMethodCacheManager methodCacheManager;
    
    public DefaultServiceRepository(ServiceLoader serviceLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner){
        Assert.notNull(serviceLoader, "ServiceLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");
        
        serviceLoader.setServiceRepository(this);
        serviceCacheManager = new ServiceCacheManager(cachePolicy, cleaner, serviceLoader);
        methodsCacheManager = new ServiceMethodsCacheManager(cachePolicy, cleaner, serviceLoader);
        methodCacheManager = new ServiceMethodCacheManager(cachePolicy, cleaner, serviceLoader);

        // Initialize cache manager
        serviceCacheManager.initialize();
        methodCacheManager.initialize();
        methodCacheManager.initialize();
    }
    
    @Override
    public DefaultService getService(String serviceId) {
        return serviceCacheManager.getSource(serviceId);
    }

    @Override
    public void listServiceMethods(String serviceId, String productId, String clientId, Long tenantId, ContextPath contextPath, List<ServiceMethod> list) {
        ServiceDomain domain = new ServiceDomain(serviceId, productId, clientId, tenantId, contextPath);
        do{
            DefaultServiceMethod[] methods = methodsCacheManager.getSource(domain);
            if(methods != null){
                for(DefaultServiceMethod method : methods){
                    list.add(method);
                }
            }
            domain = domain.nextLookup();
        }while(domain != null);K

    }

    @Override
    public DefaultServiceMethod getServiceMethod(Long methodId) {
        return methodCacheManager.getSource(methodId);
    }
}
