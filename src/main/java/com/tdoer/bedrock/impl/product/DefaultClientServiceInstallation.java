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

import com.tdoer.bedrock.impl.definition.product.ClientServiceDefinition;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import com.tdoer.bedrock.product.ClientServiceInstallation;
import com.tdoer.bedrock.service.Service;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientServiceInstallation implements ClientServiceInstallation {

    private ClientServiceDefinition definition;

    private DefaultServiceRepository serviceRepository;

    public DefaultClientServiceInstallation(ClientServiceDefinition definition, DefaultServiceRepository serviceRepository) {
        this.definition = definition;
        this.serviceRepository = serviceRepository;
    }

    /**
     * The Id of installed service
     *
     * @return Application Id, must not be <code>null</code>
     */
    @Override
    public Long getServiceId() {
        return definition.getServiceId();
    }

    /**
     * The Id of the client in which the service is installed
     *
     * @return Client Id, it must not be <code>null</code>
     */
    @Override
    public Long getClientId() {
        return definition.getClientId();
    }

    /**
     * The Id of the tenant, in which the service is installed specifically
     *
     * @return Tenant Id, it must not be <code>null</code>, but it may be zero.
     */
    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    /**
     * Get the installed service
     *
     * @return The installed service
     */
    @Override
    public Service getService() {
        return serviceRepository.getService(definition.getServiceId());
    }
}
