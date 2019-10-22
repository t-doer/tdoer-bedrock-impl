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
import com.tdoer.bedrock.service.ServiceMethod;
import com.tdoer.bedrock.service.ServiceMethodNotFoundException;
import com.tdoer.bedrock.service.ServiceNotFoundException;
import com.tdoer.bedrock.service.ServiceRepository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultServiceRepository implements ServiceRepository {

    private ServiceByCodeCacheManager serviceByCodeCacheManager;

    private ServiceByIdCacheManager serviceByIdCacheManager;

    private ServiceMethodsCacheManager methodsCacheManager;

    private ServiceMethodIdsCacheManager methodIdsCacheManager;

    private RefererClientIdsCacheManager refererClientIdsCacheManager;

    private RefererApplicationIdsCacheManager refererApplicationIdsCacheManager;

    private RefererServiceIdsCacheManager refererServiceIdsCacheManager;

    private RefereeServiceIdsCacheManager refereeServiceIdsCacheManager;

    private ServiceLoader serviceLoader;

    public DefaultServiceRepository(ServiceLoader serviceLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner){
        Assert.notNull(serviceLoader, "ServiceLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");
        
        serviceLoader.setServiceRepository(this);
        this.serviceLoader = serviceLoader;

        serviceByCodeCacheManager = new ServiceByCodeCacheManager(cachePolicy, cleaner, serviceLoader);
        serviceByIdCacheManager = new ServiceByIdCacheManager(cachePolicy, cleaner, serviceLoader);
        methodsCacheManager = new ServiceMethodsCacheManager(cachePolicy, cleaner, serviceLoader);
        methodIdsCacheManager = new ServiceMethodIdsCacheManager(cachePolicy, cleaner, serviceLoader);
        refererClientIdsCacheManager = new RefererClientIdsCacheManager(cachePolicy, cleaner,serviceLoader);
        refererApplicationIdsCacheManager = new RefererApplicationIdsCacheManager(cachePolicy, cleaner, serviceLoader);
        refererServiceIdsCacheManager = new RefererServiceIdsCacheManager(cachePolicy, cleaner, serviceLoader);
        refereeServiceIdsCacheManager = new RefereeServiceIdsCacheManager(cachePolicy, cleaner, serviceLoader);

        // Initialize cache manager
        serviceByCodeCacheManager.initialize();
        serviceByIdCacheManager.initialize();
        methodsCacheManager.initialize();
        methodIdsCacheManager.initialize();
        refererClientIdsCacheManager.initialize();
        refererApplicationIdsCacheManager.initialize();
        refererServiceIdsCacheManager.initialize();
        refereeServiceIdsCacheManager.initialize();
    }

    /**
     * Get the service of specific id.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @return {@link DefaultService}
     * @throws ServiceNotFoundException if the service dose not exist or is disabled.
     */
    @Override
    public DefaultService getService(Long serviceId) throws ServiceNotFoundException {
        Assert.notNull(serviceId, "Service Id cannot be null");

        DefaultService service = serviceByIdCacheManager.getSource(serviceId);
        if(service != null){
            return service;
        }else{
            throw new ServiceNotFoundException(serviceId);
        }
    }

    /**
     * Get the service according to its code.
     *
     * @param serviceCode Service code, cannot be <code>null</code>
     * @return {@link DefaultService}
     * @throws ServiceNotFoundException if the service dose not exist or is disabled.
     */
    @Override
    public DefaultService getService(String serviceCode) throws ServiceNotFoundException {
        Assert.hasText(serviceCode, "Service code cannot be blank");

        DefaultService service = serviceByCodeCacheManager.getSource(serviceCode);
        if(service != null){
            return service;
        }else{
            throw new ServiceNotFoundException(serviceCode);
        }
    }

    /**
     * List a service's all referer client Ids.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param list      List to hold client Ids, cannot be <code>null</code>
     */
    @Override
    public void listRefererClientIds(Long serviceId, List<Long> list) {
        Long[] ret = refererClientIdsCacheManager.getSource(serviceId);
        if(ret != null){
            for(Long id : ret){
                list.add(id);
            }
        }
    }

    /**
     * List a service's all referer application Ids
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param list      List to hold application Ids, cannot be <code>null</code>
     */
    @Override
    public void listRefererApplicationIds(Long serviceId, List<Long> list) {
        Long[] ret = refererApplicationIdsCacheManager.getSource(serviceId);
        if(ret != null){
            for(Long id : ret){
                list.add(id);
            }
        }
    }

    /**
     * List a service's all referer services.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param list      List to hold service Ids, cannot be <code>null</code>
     */
    @Override
    public void listRefererServiceIds(Long serviceId, List<Long> list) {
        Long[] ret = refererServiceIdsCacheManager.getSource(serviceId);
        if(ret != null){
            for(Long id : ret){
                list.add(id);
            }
        }
    }

    /**
     * List a service's all referee service Ids.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param list      List to hold service Ids, cannot be <code>null</code>
     */
    @Override
    public void listRefereeServiceIds(Long serviceId, List<Long> list) {
        Long[] ret = refereeServiceIdsCacheManager.getSource(serviceId);
        if(ret != null){
            for(Long id : ret){
                list.add(id);
            }
        }
    }

    /**
     * Get the service method of specific id.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param methodId Method Id, cannot be <code>null</code>
     * @return {@link DefaultServiceMethod} if found
     * @throws ServiceMethodNotFoundException if the service method dose not exist or is disabled.
     */
    @Override
    public DefaultServiceMethod getServiceMethod(Long serviceId, Long methodId) throws ServiceMethodNotFoundException {
        Assert.notNull(serviceId, "Service Id cannot be null");
        Assert.notNull(methodId, "Service method Id cannot be null");

        DefaultServiceMethod ret = null;
        DefaultServiceMethod[] methods = methodsCacheManager.getSource(serviceId);
        if(methods != null){
            for(DefaultServiceMethod method : methods){
                if(methodId.equals(method.getId())){
                    ret = method;
                    break;
                }
            }
        }

        if(ret != null){
            return ret;
        }else{
            throw new ServiceMethodNotFoundException(methodId);
        }
    }

    /**
     * List all service methods of specific service Id which are available for current environment.
     *
     * @param serviceId     Service Id, cannot be <code>null</code>
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param productId     Product Id, cannot be <code>null</code>
     * @param clientId      Client Id, cannot be <code>null</code>
     * @param tenantId      Tenant Id, cannot be <code>null</code>
     * @param contextPath   Context path, cannot be <code>null</code>
     * @param list          List to hold service methods, cannot be <code>null</code>
     */
    @Override
    public void listCurrentServiceMethods(Long serviceId, Long applicationId, Long productId, Long clientId, Long tenantId, ContextPath contextPath, List<ServiceMethod> list) {
        ServiceDomainEnumerator enumerator = new ServiceDomainEnumerator(serviceId, applicationId, productId, clientId
                , tenantId, contextPath);
        while(enumerator.hasMoreElements()){
            Long[] methodIds = methodIdsCacheManager.getSource(enumerator.nextElement());
            if(methodIds != null){
                for(Long methodId : methodIds){
                    list.add(getServiceMethod(serviceId, methodId));
                }
            }
        }
    }

    /**
     * List a service's all available service methods, including common and customized ones
     *
     * @param serviceId
     * @param list      List to hold service methods, cannot be <code>null</code>
     */
    @Override
    public void listAllServiceMethods(Long serviceId, List<ServiceMethod> list) {
        DefaultServiceMethod[] methods = methodsCacheManager.getSource(serviceId);
        if(methods != null){
            for(DefaultServiceMethod method : methods){
                list.add(method);
            }
        }
    }
}
