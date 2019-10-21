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
package com.tdoer.bedrock.impl.service;

import com.tdoer.bedrock.impl.definition.service.ServiceDefinition;
import com.tdoer.bedrock.impl.definition.service.ServiceMethodDefinition;
import com.tdoer.bedrock.service.ServiceType;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceBuilder {

    private DefaultServiceRepository serviceRespository;

    public ServiceBuilder() {

    }

    public void setServiceRepository(DefaultServiceRepository serviceRespository) {
        this.serviceRespository = serviceRespository;
    }

    public DefaultService buildService(ServiceDefinition definition) {
        Assert.notNull(definition.getId(), "Service Id cannot be null");
        Assert.hasText(definition.getCode(), "Service code cannot be blank");
        Assert.hasText(definition.getName(), "Service name cannot be blank");
        Assert.hasText(definition.getProvider(), "Service provider cannot be blank");
        Assert.hasText(definition.getVersion(), "Service version cannot be blank");
        Assert.notNull(ServiceType.resolve(definition.getType()), "Service type cannot be null");

        return new DefaultService(definition, serviceRespository);
    }

    public DefaultServiceMethod buildServiceMethod(ServiceMethodDefinition definition){
        Assert.notNull(definition.getId(), "Method Id cannot be null");
        Assert.notNull(definition.getServiceId(), "Method's service Id cannot be null");
        Assert.hasText(definition.getMethod(), "HTTP method cannot be blank" );
        Assert.hasText(definition.getName(), "Method name cannot be blank");
        Assert.hasText(definition.getUri(), "Method URI cannot be blank");

        return new DefaultServiceMethod(definition);
    }
}
