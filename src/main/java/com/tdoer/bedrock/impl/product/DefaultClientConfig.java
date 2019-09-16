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
package com.tdoer.bedrock.impl.product;

import com.tdoer.bedrock.CloudEnvironment;
import com.tdoer.bedrock.CloudEnvironmentHolder;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.product.ClientApplicationInstallation;
import com.tdoer.bedrock.product.ClientConfig;
import com.tdoer.bedrock.product.ClientService;
import com.tdoer.bedrock.product.ContextInstallation;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientConfig implements ClientConfig {

    private String productId;

    private String clientId;

    private DefaultClientConfigCenter configCenter;


    public DefaultClientConfig(String productId, String clientId, DefaultClientConfigCenter configCenter) {
        this.productId = productId;
        this.clientId = clientId;
        this.configCenter = configCenter;
    }

    @Override
    public DefaultClientApplicationInstallation getApplicationInstallation(String applicationId) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return configCenter.getApplicationInstallation(applicationId, productId, clientId, env.getTenantId());
    }

    @Override
    public boolean supportApplication(String applicationId) {
        return (getApplicationInstallation(applicationId) != null);
    }

    /**
     * List a application installation for a tenant
     *
     * @param list
     */
    @Override
    public void listApplicationInstallation(List<ClientApplicationInstallation> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        configCenter.listApplicationInstallations(productId, clientId, env.getTenantId(), list);
    }

    /**
     * Get the accessible service of specific Id
     *
     * @param serviceId
     * @return
     */
    @Override
    public DefaultClientService getService(String serviceId) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return configCenter.getClientService(serviceId, productId, clientId, env.getTenantId());
    }

    /**
     * Check whether the tenant's client can access the service of specific Id or not
     *
     * @param serviceId
     * @return
     */
    @Override
    public boolean isServiceAccessible(String serviceId) {
        return (getService(serviceId) != null);
    }

    /**
     * List accessible service for the tenant's client
     *
     * @param list
     */
    @Override
    public void listAccessibleService(List<ClientService> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        configCenter.listClientServices(productId, clientId, env.getTenantId(), list);
    }

    @Override
    public DefaultContextInstallation getContextInstallation(ContextPath contextPath) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return configCenter.getContextInstallation(contextPath, productId, clientId, env.getTenantId());
    }


    @Override
    public boolean supportContext(ContextPath contextPath) {
        return (getContextInstallation(contextPath) != null);
    }

    @Override
    public void listContextInstallation(List<ContextInstallation> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        configCenter.listContextInstallations(productId, clientId, env.getTenantId(), list);
    }

    /**
     * Get token configuration of the tenant's client
     *
     * @return
     */
    @Override
    public DefaultTokenConfig getTokenConfig() {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        return configCenter.getTokenConfig(clientId, env.getTenantId());
    }
}
