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
package com.tdoer.bedrock.impl.provider;

import com.tdoer.bedrock.impl.definition.service.ServiceDefinition;
import com.tdoer.bedrock.impl.definition.service.ServiceMethodDefinition;

import java.util.List;

/**
 * Service provider provides active or enabled service, service method and their
 * relationship, including with client, application etc.
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface ServiceProvider {

    /**
     * Get the definition of specific service
     *
     * @param serviceId Service Id, cannot be <code>null</code>
     * @return Service definition or <code>null</code>
     */
    ServiceDefinition getServiceDefinition(Long serviceId);

    /**
     * Get the definition of specific service
     *
     * @param serviceCode Service code, cannot be blank
     * @return Service definition or <code>null</code>
     */
    ServiceDefinition getServiceDefinition(String serviceCode);

    /**
     * Get all service method defifnitions of specific service
     *
     * @param serviceId Service Id, cannot  be <code>null</code>
     * @return List of service method definition or <code>null</code>
     */
    List<ServiceMethodDefinition> getAllServiceMethodDefinitions(Long serviceId);

    /**
     * Get the Ids of customized service methods for specific service domain
     *
     * @param serviceId     Service Id, cannot be <code>null</code>
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param productId     Product Id, cannot be <code>null</code>
     * @param clientId      Client Id, cannot be <code>null</code>
     * @param tenantId      Tenant Id, cannot be <code>null</code>
     * @param contextPath   Context path, cannot be <code>null</code>
     * @return List of service method Ids or <code>null</code>
     */
    List<Long> getCustomizedServiceMethodIds(
            Long serviceId, Long applicationId, Long productId, Long clientId, Long tenantId, String contextPath);

    /**
     * Get the Ids of common service methods of specific service
     * @param serviceId     Service Id, cannot be <code>null</code>
     * @return List of service method Ids or <code>null</code>
     */
    List<Long> getCommonServiceMethodIds(Long serviceId);

    /**
     * Get the Ids of referer clients of specific service
     * @param serviceId     Service Id, cannot be <code>null</code>
     * @return List of client Ids or <code>null</code>
     */
    List<Long> getRefererClientIds(Long serviceId);

    /**
     * Get the Ids of referer application of specific service
     * @param serviceId     Service Id, cannot be <code>null</code>
     * @return List of application Ids or <code>null</code>
     */
    List<Long> getRefererApplicationIds(Long serviceId);

    /**
     * Get the Ids of referer service of specific service
     * @param serviceId     Service Id, cannot be <code>null</code>
     * @return List of referer service Ids or <code>null</code>
     */
    List<Long> getRefererServiceIds(Long serviceId);

    /**
     * Get the Ids of referee service of specific service
     * @param serviceId     Service Id, cannot be <code>null</code>
     * @return List of referee service Ids or <code>null</code>
     */
    List<Long> getRefereeServiceIds(Long serviceId);
}
