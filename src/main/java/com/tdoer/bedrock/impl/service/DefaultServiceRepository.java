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

import com.tdoer.bedrock.application.Application;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.product.Client;
import com.tdoer.bedrock.service.*;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultServiceRepository implements ServiceRepository {

    private ServiceCacheManagerByCode serviceCacheManagerByCode;

    private ServiceCacheManagerById serviceCacheManagerById;

    private ServiceMethodsCacheManager methodsCacheManager;

    private ServiceMethodCacheManager methodCacheManager;
    
    public DefaultServiceRepository(ServiceLoader serviceLoader, CachePolicy cachePolicy, DormantCacheCleaner cleaner){
        Assert.notNull(serviceLoader, "ServiceLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");
        
        serviceLoader.setServiceRepository(this);
        serviceCacheManagerByCode = new ServiceCacheManagerByCode(cachePolicy, cleaner, serviceLoader);
        serviceCacheManagerById = new ServiceCacheManagerById(cachePolicy, cleaner, serviceLoader);
        methodsCacheManager = new ServiceMethodsCacheManager(cachePolicy, cleaner, serviceLoader);
        methodCacheManager = new ServiceMethodCacheManager(cachePolicy, cleaner, serviceLoader);

        // Initialize cache manager
        serviceCacheManagerByCode.initialize();
        serviceCacheManagerById.initialize();
        methodCacheManager.initialize();
        methodCacheManager.initialize();
    }

    /**
     * Get the service of specific id.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @return {@link Service}
     * @throws ServiceNotFoundException if the service dose not exist or is disabled.
     */
    @Override
    public Service getService(Long serviceId) throws ServiceNotFoundException {
        return serviceCacheManagerById.getSource(serviceId);
    }

    /**
     * Get the service according to its code.
     *
     * @param serviceCode Service code, cannot be <code>null</code>
     * @return {@link Service}
     * @throws ServiceNotFoundException if the service dose not exist or is disabled.
     */
    @Override
    public Service getService(String serviceCode) throws ServiceNotFoundException {
        return serviceCacheManagerByCode.getSource(serviceCode);
    }

    /**
     * List all available services in the repository.
     *
     * @param list List to hold services, cannot be <code>null</code>
     */
    @Override
    public void listAllServices(List<Service> list) {

    }

    /**
     * List a service's all referer clients.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param list      List to hold clients, cannot be <code>null</code>
     */
    @Override
    public void listRefererClients(Long serviceId, List<Client> list) {

    }

    /**
     * List a service's all referer applications
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param list      List to hold applications, cannot be <code>null</code>
     */
    @Override
    public void listRefererApplications(Long serviceId, List<Application> list) {

    }

    /**
     * List a service's all referer services.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param list      List to hold services, cannot be <code>null</code>
     */
    @Override
    public void listRefererServices(Long serviceId, List<Service> list) {

    }

    /**
     * List a service's all referee services.
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @param list      List to hold services, cannot be <code>null</code>
     */
    @Override
    public void listRefereeServices(Long serviceId, List<Service> list) {

    }

    /**
     * Get the service method of specific id.
     *
     * @param methodId Method Id, cannot be <code>null</code>
     * @return {@link ServiceMethod}
     * @throws ServiceMethodNotFoundException if the service method dose not exist or is disabled.
     */
    @Override
    public ServiceMethod getServiceMethod(Long methodId) throws ServiceMethodNotFoundException {
        return null;
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
            DefaultServiceMethod[] methods = methodsCacheManager.getSource(enumerator.nextElement());
            if(methods != null){
                for(DefaultServiceMethod method : methods){
                    list.add(method);
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

    }
}
