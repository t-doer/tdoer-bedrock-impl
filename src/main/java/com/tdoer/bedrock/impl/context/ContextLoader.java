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
package com.tdoer.bedrock.impl.context;

import com.tdoer.bedrock.context.ContextInstance;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.definition.context.*;
import com.tdoer.bedrock.impl.product.DefaultClientResource;
import com.tdoer.bedrock.impl.provider.ContextProvider;
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
public class ContextLoader {
    private final static Logger logger = LoggerFactory.getLogger(ContextLoader.class);

    private ContextProvider contextProvider;

    private ContextBuilder contextBuilder;

    private ContextTypeDefinition rootContextTypeDefinition;

    private static final DefaultContextRole[] EMPTY_CONTEXT_ROLES = new DefaultContextRole[0];

    private static final DefaultContextApplicationInstallation[] EMPTY_CONTEXT_APPLICATIONS = new DefaultContextApplicationInstallation[0];


    public ContextLoader(ContextProvider contextProvider, ContextPathParser contextPathParser,
                         DefaultApplicationRepository applicatinRespository, DefaultServiceRepository serviceRepository) {
        Assert.notNull(contextProvider, "Context provider cannot be null");
        Assert.notNull(contextPathParser, "Context path parser cannot be null");
        Assert.notNull(applicatinRespository, "Application repository cannot be null");
        Assert.notNull(serviceRepository, "Service repository cannot be null");

        generateRootContextDefiniton();
        this.contextProvider = contextProvider;
        this.contextBuilder = new ContextBuilder(contextPathParser, applicatinRespository, serviceRepository);
    }

    void setContextCenter(DefaultContextCenter contextCenter){
        Assert.notNull(contextCenter, "Context center cannot be null");

        this.contextBuilder.setContextCenter(contextCenter);
    }

    void generateRootContextDefiniton(){
        rootContextTypeDefinition = new ContextTypeDefinition();
        rootContextTypeDefinition.setCategory("TENANT");
        rootContextTypeDefinition.setCode("TENANT");
        rootContextTypeDefinition.setId(1L);
        rootContextTypeDefinition.setContextPath("1.0");
        rootContextTypeDefinition.setName("Tenant");
        rootContextTypeDefinition.setParentType(null);
        rootContextTypeDefinition.setTenantId(0L);
    }

    public DefaultContextType loadRootContextType(Long tenantId){
        List<ContextTypeDefinition> all = null;
        try{
            all = contextProvider.getContextTypes(tenantId);
        }catch (Throwable t){
            logger.error("Failed to load context types of tenant Id: " + tenantId, t);
        }

        DefaultContextType root = contextBuilder.buildContextType(rootContextTypeDefinition);
        if(all != null){
            buildChild(root, all);
        }

        return root;
    }

    protected List<ContextTypeDefinition> findChildren(List<ContextTypeDefinition> list, Long parentType){
        ArrayList<ContextTypeDefinition> arr = new ArrayList<>();
        for(ContextTypeDefinition type : list){
            if(parentType.equals(type.getParentType())){
                arr.add(type);
            }
        }
        return arr;
    }

    protected void buildChild(DefaultContextType parent, List<ContextTypeDefinition> all){
        List<ContextTypeDefinition> children = findChildren(all, parent.getType());
        if(children.size() ==0){
            return;
        }

        DefaultContextType child = null;
        for(ContextTypeDefinition definition : children){
            try{
                child = contextBuilder.buildContextType(definition);
                parent.addChild(child);
                buildChild(child, all);
            }catch (Exception ex){
                logger.error("Invalid context type definition: " + definition, ex);
            }
        }
    }

    public ContextInstance loadContextInstance(Long tenantId, String instanceGUID){
        try{
            ContextInstance instance = contextProvider.getContextInstance(tenantId,instanceGUID);
            if(instance != null){
                if(instance.getTenantId().equals(tenantId)){
                    return instance;
                }else{
                    logger.error("The tenant dose not own the context instance, {} - {}", tenantId, instance);
                    return null;
                }
            }
            return null;
        }catch (Throwable t){
            logger.error("Failed to load context instance (tenantId, guid) - (" +
                    tenantId + ", " + instanceGUID + ")", t);
            return null;
        }
    }

    public ContextInstance loadContextInstance(Long tenantId, Long contextType, Long instanceId){
        try{
            ContextInstance instance = contextProvider.getContextInstance(tenantId, contextType, instanceId);
            if(instance != null){
                if(instance.getTenantId().equals(tenantId)){
                    return instance;
                }else{
                    logger.error("The tenant dose not own the context instance, {} - {}", tenantId, instance);
                    return null;
                }
            }
            return null;
        }catch (Throwable t){
            logger.error("Failed to load context instance (tenantId, instanceId) - (" +
                    tenantId + ", " + instanceId + ")", t);
            return null;
        }
    }

    public DefaultContextRole[] loadContextRoles(Long tenantId, ContextPath contextPath){
        List<ContextRoleDefinition> list = null;
        try{
            list = contextProvider.getContextRoles(tenantId,
                    contextPath);
        }catch (Throwable t){
            logger.error("Failed to load context roles of: " + contextPath, t);
        }

        if(list != null){
            ArrayList<DefaultContextRole> roles = new ArrayList<>(list.size());
            for(ContextRoleDefinition definition : list){
                try{
                    roles.add(contextBuilder.buildContextRole(definition));
                }catch (Exception ex){
                    logger.error("Invalid context role definition: " + definition, ex);
                }
            }
            DefaultContextRole[] ret = new DefaultContextRole[roles.size()];
            return roles.toArray(ret);
        }
        return EMPTY_CONTEXT_ROLES;
    }

    public Long[] loadRoleIdsOfUser(Long tenantId, ContextPath contextPath, Long userId){
        List<Long> list = null;
        try{
            contextProvider.getRoleIdsOfUserInContext(tenantId, contextPath, userId);
        }catch (Throwable t){
            logger.error("Failed to load role Ids of user: " + userId, t);
        }

        if(list != null){
            ArrayList<Long> ret = new ArrayList<>(list.size());
            for(Long roleId : list){
                ret.add(roleId);
            }
        }
        return new Long[0];
    }

    public DefaultContextApplicationInstallation[] loadApplicationInstallations(Long clientId, Long tenantId, ContextPath contextPath){

        List<ContextApplicationDefinition> list = null;
        try{
            list = contextProvider.getContextApplications(clientId, tenantId, contextPath);
        }catch (Throwable t){
            logger.error("Failed to load context application installations in [" + clientId + ", " + tenantId + ", " + contextPath + "]", t);
        }
        if(list == null || list.size() ==0){
            return EMPTY_CONTEXT_APPLICATIONS;
        }

        ArrayList<DefaultContextApplicationInstallation> installationList = new ArrayList<>(list.size());
        for(ContextApplicationDefinition definition : list){
            try{
                installationList.add(contextBuilder.buildContextApplicationInstallation(definition));
            }catch(Throwable t){
                logger.error("Invalid context application definition: " + definition, t);
            }
        }

        DefaultContextApplicationInstallation[] ret = new DefaultContextApplicationInstallation[installationList.size()];
        return installationList.toArray(ret);
    }

    public DefaultServiceMethod[] loadRoleServiceMethods(Long clientId, Long tenantId, ContextPath contextPath,
                                                      Long roleId){
        List<ContextRoleMethodDefinition> list = null;
        try{
            list = contextProvider.getContextRoleMethods(clientId, tenantId, contextPath, roleId);
        }catch (Throwable t){
            logger.error("Failed to load role service methods of [" + clientId + ", " + tenantId + ", " + contextPath +
                     ", " + roleId, "]", t);
        }
        if(list == null || list.size() ==0){
            return new DefaultServiceMethod[0];
        }

        ArrayList<DefaultServiceMethod> methods = new ArrayList<>(list.size());
        for(ContextRoleMethodDefinition definition : list){
            try{
                methods.add(contextBuilder.buildRoleServiceMethods(definition));
            }catch(Throwable t){
                logger.error("Invalid role service method definition: " + definition, t);
            }
        }

        DefaultServiceMethod[] ret = new DefaultServiceMethod[methods.size()];
        return methods.toArray(ret);
    }

    public DefaultServiceMethod[] loadPublicServiceMethods(Long clientId, Long tenantId, ContextPath contextPath){
        List<ContextPublicMethodDefinition> list = null;
        try{
            list = contextProvider.getContextPublicMethods(clientId, tenantId, contextPath);
        }catch (Throwable t){
            logger.error("Failed to load public service methods in [" + clientId + ", " + tenantId + ", " + contextPath + "]", t);
        }
        if(list == null || list.size() ==0){
            return new DefaultServiceMethod[0];
        }

        ArrayList<DefaultServiceMethod> methods = new ArrayList<>(list.size());
        for(ContextPublicMethodDefinition definition : list){
            try{
                methods.add(contextBuilder.buildPublicServiceMethods(definition));
            }catch(Throwable t){
                logger.error("Invalid public service method definition: " + definition, t);
            }
        }

        DefaultServiceMethod[] ret = new DefaultServiceMethod[methods.size()];
        return methods.toArray(ret);
    }

    public DefaultClientResource[] loadPublicClientResources(Long clientId, Long tenantId, ContextPath contextPath){
        List<ContextPublicResourceDefinition> list = null;
        try{
            list = contextProvider.getContextPublicResources(clientId, tenantId, contextPath);
        }catch (Throwable t){
            logger.error("Failed to load public client resources in [" + clientId + ", " + tenantId + ", " + contextPath + "]", t);
        }
        if(list == null || list.size() ==0){
            return new DefaultClientResource[0];
        }

        ArrayList<DefaultClientResource> methods = new ArrayList<>(list.size());
        for(ContextPublicResourceDefinition definition : list){
            try{
                methods.add(contextBuilder.buildPublicClientResource(definition));
            }catch(Throwable t){
                logger.error("Invalid public client resource definition: " + definition, t);
            }
        }

        DefaultClientResource[] ret = new DefaultClientResource[methods.size()];
        return methods.toArray(ret);
    }

    public DefaultClientResource[] loadRoleClientResources(Long clientId, Long tenantId, ContextPath contextPath,
                                                         Long roleId){
        List<ContextRoleResourceDefinition> list = null;
        try{
            list = contextProvider.getContextRoleResources(clientId, tenantId, contextPath, roleId);
        }catch (Throwable t){
            logger.error("Failed to load role client resources of [" + clientId + ", " + tenantId + ", " + contextPath +
                    ", " + roleId, "]", t);
        }
        if(list == null || list.size() ==0){
            return new DefaultClientResource[0];
        }

        ArrayList<DefaultClientResource> methods = new ArrayList<>(list.size());
        for(ContextRoleResourceDefinition definition : list){
            try{
                methods.add(contextBuilder.buildRoleClientResource(definition));
            }catch(Throwable t){
                logger.error("Invalid role client resource definition: " + definition, t);
            }
        }

        DefaultClientResource[] ret = new DefaultClientResource[methods.size()];
        return methods.toArray(ret);
    }
}
