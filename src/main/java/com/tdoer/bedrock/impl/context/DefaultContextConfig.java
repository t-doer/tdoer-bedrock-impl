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
import com.tdoer.bedrock.Platform;
import com.tdoer.bedrock.application.Application;
import com.tdoer.bedrock.context.*;
import com.tdoer.bedrock.service.ServiceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextConfig implements ContextConfig {

    private ContextInstance contextInstance;

    private DefaultContextCenter configCenter;

    private static Logger logger = LoggerFactory.getLogger(DefaultContextConfig.class);

    public DefaultContextConfig(ContextInstance contextInstance, DefaultContextCenter configCenter) {
        Assert.notNull(contextInstance, "Context instance cannot be null");
        Assert.notNull(configCenter, "Context config center cannot be null");

        this.configCenter = configCenter;
        this.contextInstance = contextInstance;
    }

    /**
     * List current user's roles in the context instance. Current user
     * is the user who is sending the request, if the user dose not log
     * in yet, current user is <code>null</code>.
     *
     * @param list List to hold context roles, cannot be <code>null</code>
     */
    @Override
    public void listCurrentUserRoles(List<ContextRole> list) {
        Assert.notNull(list, "List cannot be null");

        listUserRoles(Platform.getCurrentUser().getId(), list);
    }

    /**
     * List context roles of specific user Id
     *
     * @param userId User Id, must not be <code>null</code>
     * @param list   List to hold context roles, cannot be <code>null</code>
     */
    @Override
    public void listUserRoles(Long userId, List<ContextRole> list) {
        Assert.notNull(userId, "User Id cannot be null");
        Assert.notNull(list, "List cannot be null");
        configCenter.listUserRoles(contextInstance.getTenantId(), contextInstance.getContextPath(), userId, list);
    }

    /**
     * List all enabled roles in current context instance, including
     * system ones and custom ones
     *
     * @param list List to hold context roles, cannot be <code>null</code>
     */
    @Override
    public void listContextRoles(List<ContextRole> list) {
        Assert.notNull(list, "List cannot be null");

        configCenter.listContextRoles(contextInstance.getTenantId(), contextInstance.getContextPath(), list);
    }

    /**
     * Get context role of specific role Id, which is available in the context instance
     *
     * @param roleId Role Id, cannot be <code>null</code>
     * @return Context role if it exists and is enabled and belows to the context,
     * otherwise return <code>null</code>
     */
    @Override
    public ContextRole getContextRole(Long roleId) {
        Assert.notNull(roleId, "Role Id cannot be null");

        ArrayList<ContextRole> list = new ArrayList<>();
        listContextRoles(list);
        for(ContextRole role : list){
            if(role.getId().equals(roleId)){
                return role;
            }
        }

        return null;
    }

    /**
     * Get context role of specific role code, which is available in the context instance.
     *
     * @param roleCode Role code, cannot be blank
     * @return Context role if it exists and is enabled and belows to the context,
     * otherwise return <code>null</code>
     */
    @Override
    public ContextRole getContextRole(String roleCode) {
        Assert.hasText(roleCode, "Role code cannot be blank");

        ArrayList<ContextRole> list = new ArrayList<>();
        listContextRoles(list);
        for(ContextRole role : list){
            if(role.getCode().equals(roleCode)){
                return role;
            }
        }

        return null;
    }

    /**
     * List all applications installed in current client and current context instance
     *
     * @param list List to hold {@link ContextApplicationInstallation}, cannot be <ode>null</ode>
     */
    @Override
    public void listApplicationInstallations(List<ContextApplicationInstallation> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        configCenter.listApplicationInstallation(env.getClientId(), contextInstance.getTenantId(), contextInstance.getContextPath(), list);
    }

    /**
     * Get an application installed in current client and current context instance
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return An application installation or <code>null</code> if not found
     */
    @Override
    public ContextApplicationInstallation getApplicationInstallation(Long applicationId) {
        Assert.notNull(applicationId, "Application Id cannot be null");

        ArrayList<ContextApplicationInstallation> list = new ArrayList<>();
        listApplicationInstallations(list);
        for(ContextApplicationInstallation ins : list){
            if(ins.getApplicationId().equals(applicationId)){
                return ins;
            }
        }
        return null;
    }

    /**
     * Get an application installed in current client and current context instance
     *
     * @param applicationCode Application code, cannot be blank
     * @return An application installation or <code>null</code> if not found
     */
    @Override
    public ContextApplicationInstallation getApplicationInstallation(String applicationCode) {
        Assert.notNull(applicationCode, "Application code cannot be blank");

        ArrayList<ContextApplicationInstallation> list = new ArrayList<>();
        listApplicationInstallations(list);
        for(ContextApplicationInstallation ins : list){
            if(ins.getApplication().getCode().equals(applicationCode)){
                return ins;
            }
        }
        return null;
    }

    /**
     * Check if an application was installed and enabled in current client and
     * current context instance.
     *
     * @param application Application
     * @return true if the application is supported
     */
    @Override
    public boolean supportApplication(Application application) {
        Assert.notNull(application, "Application cannot be null");

        ContextApplicationInstallation ins = getApplicationInstallation(application.getId());
        return (ins != null);
    }

    /**
     * List public resources in current client and current context instance
     * which resources all users can access.
     *
     * @param list List to hold public authorities, cannot be <code>null</code>
     */
    @Override
    public void listPublicResources(List<ClientResource> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        configCenter.listPublicResources(env.getClientId(),contextInstance.getTenantId(), contextInstance.getContextPath(), list);
    }

    /**
     * List public service methods which are associated with public resources
     * in current client and current context instance
     *
     * @param list List to hold pubic service methods, cannot be <code>null</code>
     */
    @Override
    public void listPublicMethods(List<ServiceMethod> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        configCenter.listPublicMethods(env.getClientId(), contextInstance.getTenantId(), contextInstance.getContextPath(), list);
    }

    /**
     * Check if the user's request is permitted in current client and current context instance
     * according to user's role and public authorities
     *
     * @param httpMethod Http method, cannot be blank
     * @param URI        Request URI, cannot be blank
     * @return true if the request passes access checking
     */
    @Override
    public boolean permitServiceMethodAccess(String httpMethod, String URI) {
        Assert.notNull(HttpMethod.resolve(httpMethod), "Unknown HTTP method: " + httpMethod);
        Assert.hasText(URI, "Request URI cannot be blank");

        // go through public service method
        ArrayList<ServiceMethod> list = new ArrayList<>();
        listPublicMethods(list);
        for(ServiceMethod method : list){
            if(method.match(httpMethod, URI)){
                return true;
            }
        }
        // go through user roles
        ArrayList<ContextRole> lst = new ArrayList<>();
        listCurrentUserRoles(lst);
        for(ContextRole role : lst){
            if(role.permitServiceMethodAccess(httpMethod, URI)){
                return true;
            }
        }

        return false;
    }

    /**
     * Default entry application code in current client and current context instance
     *
     * @return Application code, it must not be blank
     */
    @Override
    public String getEntryApplicationCode() {

        return Platform.getCurrentEnvironment().getClientConfig().getContextInstallation(contextInstance.getContextPath()).getEntryApplicationCode();
    }

    /**
     * Default entry nav item in current client and current context instance
     *
     * @return Navigation item's node Id, it must not be blank
     */
    @Override
    public String getEntryNavItem() {
        return Platform.getCurrentEnvironment().getClientConfig().getContextInstallation(contextInstance.getContextPath()).getEntryNavItem();
    }

    /**
     * Default entry language in current client and current context instance
     *
     * @return Language, it must not be <code>null</code>
     */
    @Override
    public Locale getEntryLanguage() {
        return Platform.getCurrentEnvironment().getClientConfig().getContextInstallation(contextInstance.getContextPath()).getEntryLanguage();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContextConfig[");
        sb.append(contextInstance.getContextPath());
        sb.append("]");
        return sb.toString();
    }
}