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
import com.tdoer.bedrock.application.Application;
import com.tdoer.bedrock.application.ApplicationInstallation;
import com.tdoer.bedrock.application.Page;
import com.tdoer.bedrock.context.*;
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

    @Override
    public String toString() {
        return super.toString();
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

    }

    /**
     * List context roles of specific user Id
     *
     * @param userId User Id, must not be <code>null</code>
     * @param list   List to hold context roles, cannot be <code>null</code>
     */
    @Override
    public void listUserRoles(Long userId, List<ContextRole> list) {

    }

    /**
     * List all enabled roles in current context instance, including
     * system ones and custom ones
     *
     * @param list List to hold context roles, cannot be <code>null</code>
     */
    @Override
    public void listContextRoles(List<ContextRole> list) {

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
        return null;
    }

    /**
     * List public authorities in current context instance which resources all users can access.
     *
     * @param list List to hold public authorities, cannot be <code>null</code>
     */
    @Override
    public void listPublicAuthorities(List<PublicAuthority> list) {

    }

    /**
     * Check if the user's request is permitted in the context instance according to
     * user's role and public authorities
     *
     * @param httpMethod Http method, cannot be blank
     * @param URI        Request URI, cannot be blank
     * @return true if the request passes access checking
     */
    @Override
    public boolean checkServiceMethodAccess(String httpMethod, String URI) {
        return false;
    }

    /**
     * List all applications installed in current context instance
     *
     * @param list List to hold {@link ContextApplicationInstallation}, cannot be <ode>null</ode>
     */
    @Override
    public void listApplicationInstallations(List<ContextApplicationInstallation> list) {

    }

    /**
     * Get an application installed in current context instance.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return An application installation or <code>null</code> if not found
     */
    @Override
    public ContextApplicationInstallation getApplicationInstallation(String applicationId) {
        return null;
    }

    /**
     * Check if an application was installed and enabled in current context instance.
     *
     * @param application Application
     * @return true if the application is supported
     */
    @Override
    public boolean supportApplication(Application application) {
        return false;
    }

    /**
     * Default entry application code
     *
     * @return Application code, it must not be blank
     */
    @Override
    public String getEntryApplicationCode() {
        return null;
    }

    /**
     * Default entry nav item
     *
     * @return Navigation item's node Id, it must not be blank
     */
    @Override
    public String getEntryNavItem() {
        return null;
    }

    /**
     * Default entry language
     *
     * @return Language, it must not be <code>null</code>
     */
    @Override
    public Locale getEntryLanguage() {
        return null;
    }
}