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

import com.tdoer.bedrock.impl.definition.service.ServiceDefinition;
import com.tdoer.bedrock.impl.definition.service.ServiceMethodDefinition;
import com.tdoer.bedrock.impl.provider.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceLoader {
    private ServiceProvider serviceProvider;
    
    private ServiceBuilder serviceBuilder;

    private static Logger logger = LoggerFactory.getLogger(ServiceLoader.class);

    public ServiceLoader(ServiceProvider serviceProvider) {
        Assert.notNull(serviceProvider, "ServiceProvider cannot be null");
        this.serviceProvider = serviceProvider;
        // initialize builder
        this.serviceBuilder = new ServiceBuilder();
    }

    public void setServiceRepository(DefaultServiceRepository serviceRepository){
        Assert.notNull(serviceRepository, "ServiceRepository cannot be null");
        this.serviceBuilder.setServiceRepository(serviceRepository);
    }

    public DefaultService loadService(String serviceCode){
        ServiceDefinition serviceDefinition = null;
        try{
            serviceDefinition = serviceProvider.getServiceDefinition(serviceCode);
        }catch(Throwable t){
            logger.error("Failed to load service definition of service code: " + serviceCode, t);
        }

        if(serviceDefinition != null){
            try{
                return serviceBuilder.buildService(serviceDefinition);
            }catch(Exception ex){
                logger.error("Invalid service definition: " + serviceDefinition, ex);
            }
        }

        return null;
    }

    public DefaultService loadService(Long serviceId){
        ServiceDefinition serviceDefinition = null;
        try{
            serviceDefinition = serviceProvider.getServiceDefinition(serviceId);
        }catch(Throwable t){
            logger.error("Failed to load service definition of service Id: " + serviceId, t);
        }

        if(serviceDefinition != null){
            try{
                return serviceBuilder.buildService(serviceDefinition);
            }catch(Exception ex){
                logger.error("Invalid service definition: " + serviceDefinition, ex);
            }
        }

        return null;
    }

    public DefaultServiceMethod[] loadAllServiceMethods(Long serviceId){
        Assert.notNull(serviceId, "Service Id cannot be null");

        List<ServiceMethodDefinition> list = null;
        try{
            list =serviceProvider.getAllServiceMethodDefinitions(serviceId);
        }catch(Throwable t){
            logger.error("Failed to load all service method definitions of service Id: " + serviceId, t);
        }

        if(list != null){
            ArrayList<DefaultServiceMethod> methodList = new ArrayList<>(list.size());
            for(ServiceMethodDefinition methodDefinition : list){
                try{
                    methodList.add(serviceBuilder.buildServiceMethod(methodDefinition));
                }catch(Exception t){
                    logger.error("Invalid service method definition: " + methodDefinition, t);
                }
            }

            DefaultServiceMethod[] ret = new DefaultServiceMethod[methodList.size()];
            methodList.toArray(ret);
            return ret;
        }

        return new DefaultServiceMethod[0];
    }

    public Long[] loadServiceMethodIds(ServiceDomain serviceDomain){
        Assert.notNull(serviceDomain, "Service domain cannot be null");

        ServiceDomain ad = serviceDomain;

        List<Long> list = null;
        try{
            if(serviceDomain.isExtensionDomain()){
                list = serviceProvider.getCustomizedServiceMethodIds(ad.getServiceId(),
                        ad.getApplicationId(), ad.getProductId(), ad.getClientId(),ad.getTenantId(),
                        ad.getContextPath()== null? "void" : ad.getContextPath().getAbsoluteValue());
            }else{
                list = serviceProvider.getCommonServiceMethodIds(serviceDomain.getServiceId());
            }
        }catch(Throwable t){
            logger.error("Failed to load service method Ids of service domain: " + serviceDomain, t);
        }

        if(list != null){
            Long[] ids = new Long[list.size()];
            return list.toArray(ids);
        }

        return new Long[0];
    }

    public Long[] loadRefererClientIds(Long serviceId){
        Assert.notNull(serviceId, "Service Id cannot be null");

        List<Long> list = null;
        try{
            list = serviceProvider.getRefererClientIds(serviceId);
        }catch (Throwable t){
            logger.error("Failed to load referer client Ids of service Id: " + serviceId, t);
        }

        if(list != null){
            Long[] clientIds = new Long[list.size()];
            list.toArray(clientIds);
            return clientIds;
        }

        return new Long[0];
    }

    public Long[] loadRefererApplicationIds(Long serviceId){
        Assert.notNull(serviceId, "Service Id cannot be null");

        List<Long> list = null;
        try{
            list = serviceProvider.getRefererApplicationIds(serviceId);
        }catch (Throwable t){
            logger.error("Failed to load referer application Ids of service Id: " + serviceId, t);
        }

        if(list != null){
            Long[] appIds = new Long[list.size()];
            list.toArray(appIds);
            return appIds;
        }

        return new Long[0];
    }

    public Long[] loadRefererServiceIds(Long serviceId){
        Assert.notNull(serviceId, "Service Id cannot be null");

        List<Long> list = null;
        try{
            list = serviceProvider.getRefererServiceIds(serviceId);
        }catch (Throwable t){
            logger.error("Failed to load referer service Ids of service Id: " + serviceId, t);
        }

        if(list != null){
            Long[] serviceIds = new Long[list.size()];
            list.toArray(serviceIds);
            return serviceIds;
        }

        return new Long[0];
    }

    public Long[] loadRefereeServiceIds(Long serviceId){
        Assert.notNull(serviceId, "Service Id cannot be null");

        List<Long> list = null;
        try{
            list = serviceProvider.getRefereeServiceIds(serviceId);
        }catch (Throwable t){
            logger.error("Failed to load referee service Ids of service Id: " + serviceId, t);
        }

        if(list != null){
            Long[] serviceIds = new Long[list.size()];
            list.toArray(serviceIds);
            return serviceIds;
        }

        return new Long[0];
    }
}
