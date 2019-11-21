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

import com.tdoer.bedrock.CloudEnvironment;
import com.tdoer.bedrock.CloudEnvironmentHolder;
import com.tdoer.bedrock.application.Action;
import com.tdoer.bedrock.application.ApplicationInstallation;
import com.tdoer.bedrock.application.Page;
import com.tdoer.bedrock.context.ContextConfig;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextRole;
import com.tdoer.bedrock.context.PublicAuthority;
import com.tdoer.bedrock.resource.ResourceType;
import com.tdoer.bedrock.security.AuthenticationUtil;
import com.tdoer.bedrock.service.ServiceMethod;
import com.tdoer.bedrock.tenant.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextConfig implements ContextConfig {

    private ContextPath contextPath;

    private DefaultContextConfigCenter configCenter;

    private Logger logger = LoggerFactory.getLogger(DefaultContextConfig.class);

    public DefaultContextConfig(DefaultContextConfigCenter configCenter, ContextPath contextPath) {
        this.configCenter = configCenter;
        this.contextPath = contextPath;
    }

    /**
     * List current login user's roles in current contextPath instance.
     *
     * @param list
     */
    @Override
    public void listCurrentUserRoles(List<ContextRole> list) {
        User user = AuthenticationUtil.getUser();
        listUserRoles(user.getId(), list);
    }

    /**
     * List context roles of given user Id
     *
     * @param userId
     * @param list
     */
    @Override
    public void listUserRoles(Long userId, List<ContextRole> list) {
        // todo, all user roles should be available in the context instance

        configCenter.listUserRoles(contextPath, userId, list);
    }

    /**
     * List available roles in current context instance
     *
     * @param list
     */
    @Override
    public void listContextRoles(List<ContextRole> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();

        // Note use this context path, instead of env's
        configCenter.listContextRoles(contextPath, env.getProductId(), env.getClientId(), env.getTenantId(), list);
    }

    /**
     * Get context role available in the context instance
     *
     * @param roleId
     * @return
     */
    @Override
    public ContextRole getContextRole(Long roleId) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return configCenter.getContextRole(roleId, contextPath, env.getProductId(), env.getClientId(), env.getTenantId());
    }

    /**
     * List public resources in current contextPath instance which all users can access.
     *
     * @param list
     */
    @Override
    public void listPublicAuthorities(List<PublicAuthority> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        configCenter.listPublicAuthorities(contextPath, env.getProductId(), env.getClientId(), env.getTenantId(), list);
    }

    /**
     * Get an application installed in current contextPath instance.
     *
     * @param applicationId
     *
     * @return An application installation or null if not found
     */
    @Override
    public DefaultContextApplicationInstallation getApplicationInstallation(String applicationId) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return configCenter.getApplicationInstallation(applicationId, contextPath, env.getProductId(), env.getClientId(), env.getTenantId());
    }


    /**
     * Check whether an application was installed in current contextPath instance.
     *
     * @param applicationId Application Id
     * @return true if the application is supported
     */
    @Override
    public boolean supportApplication(String applicationId) {
        // Dose the context supports the application?
        return (getApplicationInstallation(applicationId) != null);
    }


    @Override
    public boolean checkServiceMethodAccess(String httpMethod, String path){
        List<ServiceMethod> methods = new ArrayList<>();
        List<PublicAuthority> publicAuthorities = new ArrayList<>();

        //public resource
        listPublicAuthorities(publicAuthorities);
        for(PublicAuthority authority : publicAuthorities){
            if(ResourceType.PAGE == authority.getResource().getType()){
                Page pg = (Page) authority.getResource();

                // check page's loading service methods
                pg.listServiceMethods(methods);
                for(ServiceMethod method : methods){
                    if(method.match(httpMethod, path)){
                        logger.info("HTTP Request ({}, {}) matches Service Method ({}) of Page ({})", httpMethod, path, method, pg);
                        return true;
                    }
                }
                methods.clear();
            }else if(ResourceType.ACTION == authority.getResource().getType()){
                Action action = (Action) authority.getResource();
                // check page's actions' service methods
                action.listServiceMethods(methods);
                for(ServiceMethod method : methods){
                    if(method.match(httpMethod, path)){
                        logger.info("HTTP Request ({}, {}) matches Service Method ({}) of Action({})", httpMethod, path, method, action);
                        return true;
                    }
                }
                methods.clear();
            }
        }

        // check user role's authorities
        List<ContextRole> userRoles = new ArrayList<>();
<<<<<<< HEAD
        listCurentUserRoles(userRoles);
=======
        listCurrentUserRoles(userRoles);
>>>>>>> 3a2ee00e2fd564c26d90643c691b4fddcafa6448
        for (ContextRole role : userRoles) {
            if (role.permitServiceMethod(httpMethod, path)) {
                logger.info("HTTP Request ({}, {}) is permitted by Context Role ({})", httpMethod, path, role);
                return true;
            }
        }

        return false;
    }

    @Override
    public void listApplicationInstallations(List<ApplicationInstallation> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        configCenter.listApplicationInstallation(contextPath, env.getProductId(), env.getClientId(), env.getTenantId(), list);
    }

    @Override
    public String getEntryApplicationId() {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return env.getClientConfig().getContextInstallation(contextPath).getEntryApplicationId();
    }

    @Override
    public String getEntryNavItem() {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return env.getClientConfig().getContextInstallation(contextPath).getEntryNavItem();
    }

    @Override
    public Locale getEntryLanguage() {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return env.getClientConfig().getContextInstallation(contextPath).getEntryLanguage();
    }
}