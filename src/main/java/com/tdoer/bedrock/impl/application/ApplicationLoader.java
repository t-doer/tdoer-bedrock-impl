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

import com.tdoer.bedrock.impl.definition.application.*;
import com.tdoer.bedrock.impl.provider.ApplicationProvider;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ApplicationLoader {

    private static Logger logger = LoggerFactory.getLogger(ApplicationLoader.class);

    private ApplicationProvider applicationProvider;

    private DefaultServiceRepository serviceRepository;

    private ApplicationBuilder applicationBuilder;

    public ApplicationLoader(ApplicationProvider applicationProvider, DefaultServiceRepository serviceRepository) {
        Assert.notNull(applicationProvider, "ApplicationProvider cannot be null");
        Assert.notNull(serviceRepository, "ServiceRepository cannot be null");
        this.applicationProvider = applicationProvider;
        this.serviceRepository = serviceRepository;

        // initialize builder
        this.applicationBuilder = new ApplicationBuilder(serviceRepository);
    }

    public void setApplicationRepository(DefaultApplicationRepository applicationRepository){
        Assert.notNull(applicationRepository, "ApplicationRepository cannot be null");
        this.applicationBuilder.setApplicationRepository(applicationRepository);
    }

    public DefaultApplication loadApplicationByCode(String applicationCode){
        ApplicationDefinition applicationDefinition = null;
        try{
            applicationDefinition = applicationProvider.getApplicationDefinitionByCode(applicationCode);
        }catch(Throwable t){
            logger.error("Failed load application of code: " + applicationCode, t);
        }

        if(applicationDefinition != null){
            try{
                return applicationBuilder.buildApplication(applicationDefinition);
            }catch(Exception ex){
                logger.error("Invalid application definition: " + applicationDefinition, ex);
            }
        }

        return null;
    }

    public DefaultApplication loadApplicationById(Long applicationId){
        ApplicationDefinition applicationDefinition = null;
        try{
            applicationDefinition = applicationProvider.getApplicationDefinitionById(applicationId);
        }catch(Throwable t){
            logger.error("Failed to load application of Id: " + applicationId, t);
        }

        if(applicationDefinition != null){
            try{
                return applicationBuilder.buildApplication(applicationDefinition);
            }catch(Exception ex){
                logger.error("Invalid application definition: " + applicationDefinition, ex);
            }
        }

        return null;
    }

    public DefaultPage[] loadAllPages(Long applicationId){
        List<PageDefinition> list = null;
        try{
            list = applicationProvider.getAllPageDefinitions(applicationId);
        }catch(Throwable t){
            logger.error("Failed to load all page of application Id: " + applicationId, t);
        }

        if(list != null){
            ArrayList<DefaultPage> pageList = new ArrayList<>(list.size());
            for(PageDefinition pageDefinition : list){
                try{
                    pageList.add(applicationBuilder.buildPage(pageDefinition));
                }catch(Exception ex){
                    logger.error("Invalid page definition: " + pageDefinition, ex);
                }
            }
            DefaultPage[] ret = new DefaultPage[pageList.size()];
            pageList.toArray(ret);
            return ret;
        }

        return new DefaultPage[0];
    }

   public Long[] loadPageIds(ApplicationDomain applicationDomain){
       Assert.notNull(applicationDomain, "Application domain cannot be null");

       ApplicationDomain ad = applicationDomain;

       List<Long> list = null;
       try{
           if(applicationDomain.isExtensionDomain()){
               list = applicationProvider.getCustomizedPageIds(ad.getApplicationId(), ad.getProductId(), ad.getClientId(),ad.getTenantId(),
                       ad.getContextPath()== null? "void" : ad.getContextPath().getAbsoluteValue());
           }else{
               list = applicationProvider.getCommonPageIds(ad.getApplicationId());
           }
       }catch(Throwable t){
           logger.error("Failed to load page Ids of application domain: " + applicationDomain, t);
       }

       if(list != null){
           Long[] ids = new Long[list.size()];
           return list.toArray(ids);
       }

       return new Long[0];
   }

    public DefaultAction[] loadAllActions(Long pageId){
        List<ActionDefinition> list = null;
        try{
            list = applicationProvider.getAllActionDefinitions(pageId);
        }catch(Throwable t){
            logger.error("Failed to load all action definitions of page Id: " + pageId, t);
        }
        if(list != null){
            ArrayList<DefaultAction> actionList = new ArrayList<>(list.size());
            for(ActionDefinition actionDefinition : list){
                try{
                    actionList.add(applicationBuilder.buildAction(actionDefinition));
                }catch(Exception ex){
                    logger.error("Invalid action definition: " + actionDefinition, ex);
                }
            }
            DefaultAction[] ret = new DefaultAction[actionList.size()];
            actionList.toArray(ret);
            return ret;
        }
        return new DefaultAction[0];
    }

    public Long[] loadActionIds(PageDomain pageDomain){
        Assert.notNull(pageDomain, "Page domain cannot be null");

        PageDomain ad = pageDomain;

        List<Long> list = null;
        try{
            if(pageDomain.isExtensionDomain()){
                list = applicationProvider.getCustomizedActionIds(ad.getPageId(), ad.getProductId(),
                        ad.getClientId(), ad.getTenantId(),
                        ad.getContextPath()== null? "void" : ad.getContextPath().getAbsoluteValue());
            }else{
                list = applicationProvider.getCommonActionIds(ad.getPageId());
            }
        }catch(Throwable t){
            logger.error("Failed to load action Ids of page domain: " + pageDomain, t);
        }

        if(list != null){
            Long[] ids = new Long[list.size()];
            return list.toArray(ids);
        }

        return new Long[0];
    }

    public Long[] loadRefereeServiceIds(ApplicationDomain applicationDomain){
        Assert.notNull(applicationDomain, "Application domain cannot be null");

        ApplicationDomain ad = applicationDomain;

        List<Long> list = null;
        try{
            if(applicationDomain.isExtensionDomain()){
                list = applicationProvider.getCustomizedRefereeServiceIds(ad.getApplicationId(), ad.getProductId(), ad.getClientId(),
                        ad.getTenantId(), ad.getContextPath()== null? "void" : ad.getContextPath().getAbsoluteValue());
            }else{
                list = applicationProvider.getCommonRefereeServiceIds(ad.getApplicationId());
            }
        }catch(Throwable t){
            logger.error("Failed to load referee service Ids of application domain: " + applicationDomain, t);
        }

        if(list != null){
            Long[] ids = new Long[list.size()];
            return list.toArray(ids);
        }

        return new Long[0];
    }

    public Long[] loadAllRefereeServiceIds(Long applicationId){
        Assert.notNull(applicationId, "Application Id cannot be null");
        List<Long> list = null;
        try{
            list = applicationProvider.getAllRefereeServiceIds(applicationId);
        }catch(Throwable t){
            logger.error("Failed to load all referee service Ids of application Id: " + applicationId, t);
        }

        if(list != null){
            Long[] ids = new Long[list.size()];
            return list.toArray(ids);
        }

        return new Long[0];
    }

    public DefaultServiceMethod[] loadServiceMethodsOfPage(Long pageId){
        List<PageMethodDefinition> list = null;
        try{
            list = applicationProvider.getPageMethodDefinitions(pageId);
        }catch (Throwable t){
            logger.error("Failed to load page method definitions of page Id: " + pageId, t);
        }

        if(list != null){
            ArrayList<DefaultServiceMethod> methods = new ArrayList<>(list.size());
            for(PageMethodDefinition def : list){
                try{
                    methods.add(serviceRepository.getServiceMethod(def.getServiceId(), def.getMethodId()));
                }catch (Exception ex){
                    logger.error("Invalid page method definition: " + def, ex);
                }
            }
            DefaultServiceMethod[] ret = new DefaultServiceMethod[methods.size()];
            return methods.toArray(ret);
        }
        return new DefaultServiceMethod[0];
    }

    public DefaultServiceMethod[] loadServiceMethodsOfAction(Long actionId){
        List<ActionMethodDefinition> list = null;
        try{
            list = applicationProvider.getActionMethodDefinitions(actionId);
        }catch (Throwable t){
            logger.error("Failed to load action method definitions of page Id: " + actionId, t);
        }

        if(list != null){
            ArrayList<DefaultServiceMethod> methods = new ArrayList<>(list.size());
            for(ActionMethodDefinition def : list){
                try{
                    methods.add(serviceRepository.getServiceMethod(def.getServiceId(), def.getMethodId()));
                }catch (Exception ex){
                    logger.error("Invalid action method definition: " + def, ex);
                }
            }
            DefaultServiceMethod[] ret = new DefaultServiceMethod[methods.size()];
            return methods.toArray(ret);
        }
        return new DefaultServiceMethod[0];
    }

}
