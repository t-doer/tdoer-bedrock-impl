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
import com.tdoer.bedrock.Platform;
import com.tdoer.bedrock.application.Application;
import com.tdoer.bedrock.context.ContextInstance;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.product.*;
import com.tdoer.bedrock.service.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientConfig implements ClientConfig {

    private Long clientId;

    private DefaultProductRepository productRepository;


    public DefaultClientConfig(Long clientId, DefaultProductRepository productRepository) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(productRepository, "Product repository cannot be null");
        this.clientId = clientId;
        this.productRepository = productRepository;
    }

    /**
     * Get the application installation of specific application Id in the tenant's client.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return {@link ClientApplicationInstallation} if found, otherwise return <code>null</code>
     */
    @Override
    public ClientApplicationInstallation getApplicationInstallation(Long applicationId) {
        Assert.notNull(applicationId, "Application Id cannot be null");
        CloudEnvironment env = Platform.getCurrentEnvironment();
        return productRepository.getApplicationInstallation(clientId, env.getTenantId(), applicationId);
    }

    /**
     * Check whether the tenant's client supports the application of specific Id,
     * that's, the application is installed in the tenant's client.
     *
     * @param application Application to check, cannot be <code>null</code>
     * @return true if the application is supported by the tenant's client
     */
    @Override
    public boolean supportApplication(Application application) {
        Assert.notNull(application, "Application cannot be null");

        return (getApplicationInstallation(application.getId()) != null);
    }

    /**
     * List application installation in the tenant's client
     *
     * @param list List to hold {@link ClientApplicationInstallation}, cannot be <code>null</code>
     */
    @Override
    public void listApplicationInstallation(List<ClientApplicationInstallation> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        productRepository.listApplicationInstallations(clientId, env.getTenantId(), list);
    }

    /**
     * Get the service installation of specific service Id
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @return {@link ClientServiceInstallation} if it's exists
     */
    @Override
    public ClientServiceInstallation getServiceInstallation(Long serviceId) {
        Assert.notNull(serviceId, "Service Id cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        return productRepository.getClientServiceInstallation(clientId, env.getTenantId(), serviceId);
    }

    /**
     * Check whether the tenant's client can access the service
     *
     * @param service The service to check, it cannot be <code>null</code>
     * @return true if the service is accessible for the client
     */
    @Override
    public boolean isServiceAccessible(Service service) {
        Assert.notNull(service, "Service cannot be null");
        return (getServiceInstallation(service.getId()) != null);
    }

    /**
     * List accessible service for the tenant's client
     *
     * @param list List to hold {@link ClientServiceInstallation}, cannot be <code>null</code>
     */
    @Override
    public void listAccessibleService(List<ClientServiceInstallation> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        productRepository.listClientServiceInstallations(clientId, env.getTenantId(), list);
    }

    /**
     * The context instance or context type installation in the tenant's client of specific context path
     *
     * @param contextPath The context path of a context instance, cannot be <code>null</code>
     * @return {@link ClientContextInstallation} if it exists and is enabled
     */
    @Override
    public ClientContextInstallation getContextInstallation(ContextPath contextPath) {
        Assert.notNull(contextPath, "Context path cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        return productRepository.getContextInstallation(clientId, env.getTenantId(), contextPath);
    }

    /**
     * Check whether the tenant's client supports the context instance, that's,
     * the context instance or context type is installed in the tenant's client
     *
     * @param contextInstance The context instance to check, cannot be <code>null</code>
     * @return true if the client supports the context instance
     */
    @Override
    public boolean supportContext(ContextInstance contextInstance) {
        Assert.notNull(contextInstance, "Context instance cannot be null");

        return (getContextInstallation(contextInstance.getContextPath()) != null);
    }

    /**
     * Lists the context installation in the tenant's client
     *
     * @param list List to hold {@link ClientContextInstallation}, cannot be <code>null</code>
     */
    @Override
    public void listContextInstallation(List<ClientContextInstallation> list) {
        Assert.notNull(list, "List cannot be null");

        CloudEnvironment env = Platform.getCurrentEnvironment();
        productRepository.listContextInstallations(clientId, env.getTenantId(), list);
    }

    /**
     * Get token configuration of the tenant's client
     *
     * @return {@link TokenConfig}, it must not be <code>null</code>
     */
    @Override
    public TokenConfig getTokenConfig() {
        CloudEnvironment env = Platform.getCurrentEnvironment();
        return productRepository.getTokenConfig(clientId, env.getTenantId());
    }
}
