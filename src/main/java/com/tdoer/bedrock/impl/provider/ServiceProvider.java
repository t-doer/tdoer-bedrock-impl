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

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.definition.service.ServiceDefinition;
import com.tdoer.bedrock.impl.definition.service.ServiceMethodDefinition;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface ServiceProvider {

    ServiceDefinition getServiceDefinition(Long serviceId);

    ServiceDefinition getServiceDefinition(String serviceCode);

    List<ServiceMethodDefinition> getCustomizedServiceMethodDefinitions(
            Long serviceId, Long applicationId, Long productId, Long clientId, Long tenantId, String contextPath);

    List<ServiceMethodDefinition> getCommonServiceMethodDefinitions(Long serviceId);

    List<ServiceMethodDefinition> getAllServiceMethodDefinitions(Long serviceId);

    ServiceMethodDefinition getServiceMethodDefinition(Long methodId);
}
