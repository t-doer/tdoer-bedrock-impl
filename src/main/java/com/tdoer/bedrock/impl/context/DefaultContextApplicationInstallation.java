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

import com.tdoer.bedrock.application.Application;
import com.tdoer.bedrock.context.ContextApplicationInstallation;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.definition.context.ContextApplicationDefinition;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextApplicationInstallation implements ContextApplicationInstallation {

    private ContextApplicationDefinition definition;

    private ContextPath contextPath;

    private DefaultApplicationRepository applicationRepository;


    public DefaultContextApplicationInstallation(ContextApplicationDefinition definition, ContextPath contextPath, DefaultApplicationRepository applicationRepository) {
        Assert.notNull(definition, "Client context application installation cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.notNull(applicationRepository, "Application repository cannot be null");

        this.definition = definition;
        this.contextPath = contextPath;
        this.applicationRepository = applicationRepository;
    }

    /**
     * The path of the context (context type or context instance) in which the
     * application is installed
     *
     * @return Context path, must not be <code>null</code>
     */
    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }

    /**
     * The Id of the client in which the application is installed
     *
     * @return Client Id, must not be <code>null</code>
     */
    @Override
    public Long getClientId() {
        return definition.getClientId();
    }

    /**
     * The Id of the tenant in which the application is installed
     *
     * @return Tenant Id, must not be <code>null</code>, but may be zero
     */
    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    /**
     * The installed application
     *
     * @return the installed application, it must not be <code>null</code>
     */
    @Override
    public Application getApplication() {
        return applicationRepository.getApplication(definition.getApplicationId());
    }

    /**
     * The Id of the installed application
     *
     * @return Application Id, must not be <code>null</code>
     */
    @Override
    public Long getApplicationId() {
        return definition.getApplicationId();
    }
}
