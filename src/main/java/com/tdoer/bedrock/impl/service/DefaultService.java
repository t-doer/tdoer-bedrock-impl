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
import com.tdoer.bedrock.Platform;
import com.tdoer.bedrock.application.Application;
import com.tdoer.bedrock.impl.definition.service.ServiceDefinition;
import com.tdoer.bedrock.product.Client;
import com.tdoer.bedrock.service.Service;
import com.tdoer.bedrock.service.ServiceMethod;
import com.tdoer.bedrock.service.ServiceType;

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
     * @return Service Id, must not be <code>null</code>
     */
    @Override
    public Long getId() {
        return definition.getId();
    }

    /**
     * Service code
     *
     * @return Service code, must not be blank
     */
    @Override
    public String getCode() {
        return definition.getCode();
    }

    /**
     * Service type
     *
     * @return Service type, must not be <code>null</code>
     */
    @Override
    public ServiceType getType() {
        return ServiceType.resolve(definition.getType());
    }

    /**
     * Service provider's name
     *
     * @return Service provider, must not be blank
     */
    @Override
    public String getProvider() {
        return definition.getProvider();
    }

    /**
     * Service name
     *
     * @return Service name, must not be blank
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * Service description
     *
     * @return Service description, maybe be blank
     */
    @Override
    public String getDescription() {
        return definition.getDescription();
    }

    /**
     * Service version
     *
     * @return Service version, must not be blank
     */
    @Override
    public String getVersion() {
        return definition.getVersion();
    }

    /**
     * List the service's methods which are available in current cloud environment
     * {@link CloudEnvironment}.
     *
     * @param list List to hold service methods, cannot be <code>null</code>.
     */
    @Override
    public void listCurrentMethods(List<ServiceMethod> list) {
        CloudEnvironment env = Platform.getCurrentEnvironment();
        serviceRespository.listCurrentServiceMethods(getId(), env.getApplicationId(), env.getProductId(),
                env.getClientId(),
                env.getTenantId(),
                env.getContextPath(), list);
    }

    /**
     * List the service's all enabled service methods, including common and customized
     * ones.
     *
     * @param list List to hold service methods, cannot be <code>null</code>.
     */
    @Override
    public void listAllMethods(List<ServiceMethod> list) {

    }

    /**
     * Get available service method of specific method Id in the service
     *
     * @param methodId Method Id
     * @return Service method if found
     */
    @Override
    public ServiceMethod getMethod(Long methodId) {
        return null;
    }

    /**
     * List the clients which refer to or call the service
     *
     * @param list List to hold clients, cannot be <code>null</code>
     */
    @Override
    public void listRefererClients(List<Client> list) {

    }

    /**
     * List applications which refer to or call the service
     *
     * @param list List to hold applications, cannot be <code>null</code>
     */
    @Override
    public void listRefererApplications(List<Application> list) {

    }

    /**
     * List other services which refer to or call the service
     *
     * @param list List to hold service, cannot be <code>null</code>
     */
    @Override
    public void listRefererServices(List<Service> list) {

    }

    /**
     * List other services to which the service refers
     *
     * @param list List to hold service, cannot be <code>null</code>
     */
    @Override
    public void listRefereeServices(List<Service> list) {

    }

    /**
     * Check if the service permits the access from the referer service?
     *
     * @param referer The referer service to check, cannot be <code>null</code>
     * @return true if the service permits the access
     */
    @Override
    public boolean permitAccessFromService(Service referer) {
        return false;
    }

    /**
     * Check if the service permits the access from the referer application?
     *
     * @param application The referer application to check, cannot be <code>null</code>
     * @return true if the service permits the access
     */
    @Override
    public boolean permitAccessFromApplication(Application application) {
        return false;
    }

    /**
     * Check if the service permits the access from the referer client?
     *
     * @param client The client to check, cannot be <code>null</code>
     * @return true if the service permits the access
     */
    @Override
    public boolean permitAccessFromClient(Client client) {
        return false;
    }

    /**
     * Check if the service has a method which matchs the HTTP request
     *
     * @param httpMethod Http method
     * @param requestURI Request URI
     * @return true if there is one matched service method
     */
    @Override
    public boolean matchRequest(String httpMethod, String requestURI) {
        return false;
    }
}
