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
import com.tdoer.bedrock.impl.service.DefaultService;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import com.tdoer.bedrock.product.ClientService;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientService implements ClientService {

    private ClientServiceDefinition definition;

    private DefaultServiceRepository serviceRepository;

    public DefaultClientService(ClientServiceDefinition definition, DefaultServiceRepository serviceRepository) {
        this.definition = definition;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public DefaultService getService() {
        return serviceRepository.getService(definition.getServiceId());
    }

    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    @Override
    public String getProductId() {
        return definition.getProductId();
    }

    @Override
    public String getClientId() {
        return definition.getClientId();
    }
}
