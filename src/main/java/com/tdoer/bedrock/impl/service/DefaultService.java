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

import com.tdoer.bedrock.CloudEnvironment;
import com.tdoer.bedrock.CloudEnvironmentHolder;
import com.tdoer.bedrock.impl.definition.service.ServiceDefinition;
import com.tdoer.bedrock.service.Service;
import com.tdoer.bedrock.service.ServiceMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultService implements Service {
    private ServiceDefinition definition;

    private DefaultServiceRepository serviceRespository;

    public DefaultService(ServiceDefinition definition, DefaultServiceRepository serviceRespository) {
        this.definition = definition;
        this.serviceRespository = serviceRespository;
    }

    /**
     * Service Id
     *
     * @return Service Id
     */
    @Override
    public String getId() {
        return definition.getId();
    }

    /**
     * Service provider's name
     *
     * @return
     */
    @Override
    public String getProvider() {
        return definition.getProvider();
    }

    /**
     * Service name
     *
     * @return Service name
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * Service description
     *
     * @return Service description, maybe be {@code Null}
     */
    @Override
    public String getDescription() {
        return definition.getDescription();
    }

    /**
     * Service author's name
     *
     * @return Service author's name, may be {@code Null}
     */
    @Override
    public String getAuthor() {
        return definition.getAuthor();
    }

    /**
     * Service maintainers, user names delimited by comma
     *
     * @return Service maintainers, may be {@code Null}
     */
    @Override
    public String getMaintainers() {
        return definition.getMaintainers();
    }

    /**
     * Service version
     *
     * @return Service version, maybe be {@code Null}
     */
    @Override
    public String getVersion() {
        return definition.getVersion();
    }

    /**
     * List available service methods in current environment {@link CloudEnvironment},
     * that's, list available service method according to current client, tenant and context instance.
     * <br>
     * Service methods will be appended to the given list.
     *
     * @param list List to hold service methods.
     */
    @Override
    public void listCurrentMethods(List<ServiceMethod> list) {
        CloudEnvironment env = CloudEnvironmentHolder.getEnvironment();
        // Note, use this application's serviceId, instead of env's
        serviceRespository.listServiceMethods(getId(), env.getProductId(), env.getClientId(), env.getTenantId(), env.getContextPath(), list);
    }

    /**
     * Get service method of specific Id available in the service
     *
     * @param methodId
     * @return Service method if found, otherwise {@code null}
     */
    @Override
    public DefaultServiceMethod getMethod(Long methodId) {
        ArrayList<ServiceMethod> list = new ArrayList<>();
        listCurrentMethods(list);
        for(ServiceMethod method : list){
            if(method.getId().equals(methodId)){
                return (DefaultServiceMethod)method;
            }
        }
        return null;
    }
}
