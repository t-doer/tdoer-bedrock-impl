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

import com.tdoer.bedrock.ProviderFailedException;
import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.impl.definition.service.ServiceDefinition;
import com.tdoer.bedrock.impl.definition.service.ServiceMethodDefinition;
import com.tdoer.bedrock.impl.domain.ServiceDomain;
import com.tdoer.bedrock.impl.provider.ServiceProvider;
import com.tdoer.bedrock.service.ServiceNotFoundException;
import com.tdoer.springboot.error.ErrorCodeException;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.*;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceLoader {
    private ServiceProvider serviceProvider;
    
    private ContextPathParser contextPathParser;
    
    private ServiceBuilder serviceBuilder;

    public ServiceLoader(ServiceProvider serviceProvider, ContextPathParser contextPathParser) {
        Assert.notNull(serviceProvider, "ServiceProvider cannot be null");
        Assert.notNull(contextPathParser, "ContextPathParser cannot be null");
        this.serviceProvider = serviceProvider;
        this.contextPathParser = contextPathParser;

        // initialize builder
        this.serviceBuilder = new ServiceBuilder(contextPathParser);
    }

    public void setServiceRepository(DefaultServiceRepository serviceRepository){
        Assert.notNull(serviceRepository, "ServiceRepository cannot be null");
        this.serviceBuilder.setServiceRepository(serviceRepository);
    }

    public DefaultService loadService(String serviceId){
        ServiceDefinition serviceDefinition = null;
        try{
            serviceDefinition = serviceProvider.getServiceDefinition(serviceId);
        }catch(Throwable t){
            throw new ProviderFailedException(FAILED_TO_LOAD_SERVICE, t, serviceId);
        }

        if(serviceDefinition == null){
            ServiceNotFoundException anfe = new ServiceNotFoundException(SERVICE_NOT_FOUND, serviceId);
            anfe.setServiceId(serviceId);
            throw anfe;
        }

        return serviceBuilder.buildService(serviceDefinition);
    }

    public DefaultServiceMethod[] loadServiceMethods(ServiceDomain serviceDomain){
        ServiceDomain ad = serviceDomain;
        List<ServiceMethodDefinition> list = serviceProvider.getServiceMethodDefinitions(ad.getServiceId(), ad.getProductId(), ad.getClientId(),ad.getTenantId(), ad.getContextPath());
        ArrayList<DefaultServiceMethod> methodList = new ArrayList<>(list.size());
        if(list != null){
            for(ServiceMethodDefinition methodDefinition : list){
                try{
                    methodList.add(serviceBuilder.buildServiceMethod(methodDefinition));
                }catch(Throwable t){
                    // todo
                }
            }
        }

        DefaultServiceMethod[] ret = new DefaultServiceMethod[methodList.size()];
        methodList.toArray(ret);
        return ret;
    }

    public DefaultServiceMethod loadServiceMethod(Long methodId){
        ServiceMethodDefinition definition = serviceProvider.getServiceMethodDefinition(methodId);
        if(definition == null){
            throw new ErrorCodeException(SERVICE_METHOD_NOT_FOUND, methodId);
        }else{
            return serviceBuilder.buildServiceMethod(definition);
        }
    }
}
