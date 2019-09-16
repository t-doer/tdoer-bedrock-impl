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
package com.tdoer.bedrock.impl.application;

import com.tdoer.bedrock.ProviderFailedException;
import com.tdoer.bedrock.application.ApplicationNotFoundException;
import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.impl.definition.application.ActionDefinition;
import com.tdoer.bedrock.impl.definition.application.ApplicationDefinition;
import com.tdoer.bedrock.impl.definition.application.ApplicationServiceDefinition;
import com.tdoer.bedrock.impl.definition.application.PageDefinition;
import com.tdoer.bedrock.impl.domain.ApplicationDomain;
import com.tdoer.bedrock.impl.provider.ApplicationProvider;
import com.tdoer.bedrock.impl.service.DefaultService;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.APPLICATION_NOT_FOUND;
import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_APPLICATION;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ApplicationLoader {

    private ApplicationProvider applicationProvider;

    private ContextPathParser contextPathParser;

    private DefaultServiceRepository serviceRepository;

    private ApplicationBuilder applicationBuilder;

    public ApplicationLoader(ApplicationProvider applicationProvider, ContextPathParser contextPathParser, DefaultServiceRepository serviceRepository) {
        Assert.notNull(applicationProvider, "ApplicationProvider cannot be null");
        Assert.notNull(contextPathParser, "ContextPathParser cannot be null");
        Assert.notNull(serviceRepository, "ServiceRepository cannot be null");
        this.applicationProvider = applicationProvider;
        this.contextPathParser = contextPathParser;
        this.serviceRepository = serviceRepository;

        // initialize builder
        this.applicationBuilder = new ApplicationBuilder(contextPathParser);
    }

    public void setApplicationRepository(DefaultApplicationRepository applicationRepository){
        Assert.notNull(applicationRepository, "ApplicationRepository cannot be null");
        this.applicationBuilder.setApplicationRepository(applicationRepository);
    }

    public DefaultApplication loadApplication(String applicationId){
        ApplicationDefinition applicationDefinition = null;
        try{
            applicationDefinition = applicationProvider.getApplicationDefinition(applicationId);
        }catch(Throwable t){
            throw new ProviderFailedException(FAILED_TO_LOAD_APPLICATION, t, applicationId);
        }

        if(applicationDefinition == null){
            ApplicationNotFoundException anfe = new ApplicationNotFoundException(APPLICATION_NOT_FOUND, applicationId);
            anfe.setApplicationId(applicationId);
            throw anfe;
        }

        return applicationBuilder.buildApplication(applicationDefinition);
    }

    public DefaultPage[] loadPages(ApplicationDomain applicationDomain){
        ApplicationDomain ad = applicationDomain;
        List<PageDefinition> list = applicationProvider.getPageDefinitions(ad.getApplicationId(), ad.getProductId(), ad.getClientId(),ad.getTenantId(), ad.getContextPath());
        ArrayList<DefaultPage> pageList = new ArrayList<>(list.size());
        if(list != null){
            for(PageDefinition pageDefinition : list){
                List<Long> smds = applicationProvider.getServiceMethodIdsOfPage(ad.getApplicationId(), pageDefinition.getId());
                DefaultServiceMethod[] methods = null;
                if(smds != null){
                    methods = parseServiceMethods(smds);
                }

                try{
                    pageList.add(applicationBuilder.buildPage(pageDefinition, methods));
                }catch(Throwable t){
                    // todo
                }
            }
        }

        DefaultPage[] ret = new DefaultPage[pageList.size()];
        pageList.toArray(ret);
        return ret;
    }

    public DefaultAction[] loadActions(ApplicationDomain applicationDomain){
        ApplicationDomain ad = applicationDomain;
        List<ActionDefinition> list = applicationProvider.getActionDefinitions(ad.getApplicationId(), ad.getProductId(), ad.getClientId(),ad.getTenantId(), ad.getContextPath());
        ArrayList<DefaultAction> actionList = new ArrayList<>(list.size());
        if(list != null){
            for(ActionDefinition actionDefinition : list){
                List<Long> smds = applicationProvider.getServiceMethodIdsOfAction(ad.getApplicationId(), actionDefinition.getPageId(), actionDefinition.getId());
                DefaultServiceMethod[] methods = null;
                if(smds != null){
                    methods = parseServiceMethods(smds);
                }

                try{
                    actionList.add(applicationBuilder.buildAction(actionDefinition, methods));
                }catch(Throwable t){
                    // todo
                }
            }
        }

        DefaultAction[] ret = new DefaultAction[actionList.size()];
        actionList.toArray(ret);
        return ret;
    }


    protected DefaultServiceMethod[] parseServiceMethods(List<Long> smds){
        ArrayList<DefaultServiceMethod> methodList = new ArrayList<>(smds.size());
        for(Long methodId : smds){
            DefaultServiceMethod method = serviceRepository.getServiceMethod(methodId);
            if(method == null){
                // todo
            }else{
                methodList.add(method);
            }
        }

        DefaultServiceMethod[] methods = new DefaultServiceMethod[methodList.size()];
        methodList.toArray(methods);
        return methods;
    }

    public DefaultService[] loadServices(ApplicationDomain applicationDomain){
        ApplicationDomain ad = applicationDomain;
        List<ApplicationServiceDefinition> list = applicationProvider.getApplicationServiceDefinitions(ad.getApplicationId(), ad.getProductId(), ad.getClientId(),ad.getTenantId(), ad.getContextPath());
        ArrayList<DefaultService> serviceList = new ArrayList<>(list.size());
        if(list != null){
            for(ApplicationServiceDefinition applicationServiceDefinition : list){
                DefaultService service = serviceRepository.getService(applicationServiceDefinition.getServiceId());
                if(service == null){
                    // todo
                }else{
                    serviceList.add(service);
                }
            }
        }

        DefaultService[] ret = new DefaultService[serviceList.size()];
        serviceList.toArray(ret);
        return ret;
    }
}
