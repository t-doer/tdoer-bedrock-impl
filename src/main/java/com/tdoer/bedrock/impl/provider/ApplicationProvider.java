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
import com.tdoer.bedrock.impl.definition.application.*;

import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface ApplicationProvider {
    
    /**
     * Get available application definition of specific application Id
     *
     * @param applicationId Application Id
     * @return
     */
    ApplicationDefinition getApplicationDefinitionById(Long applicationId);

    ApplicationDefinition getApplicationDefinitionByCode(String applicationCode);

    List<PageDefinition> getAllPageDefinitions(Long applicationId);

    List<Long> getCustomizedPageIds(Long applicationId, Long productId, Long clientId, Long tenantId,
                                               ContextPath contextPath);

    List<PageDefinition> getCommonPageIds(Long applicationId);

    List<ActionDefinition> getAllActionDefinitions(Long pageId);

    List<Long> getCustomizedActionIds(Long applicationId, Long productId, Long clientId, Long tenantId,
                                                   ContextPath contextPath);

    List<Long> getCommonActionIds(Long pageId);


    List<Long> getCustomizedRefereeServiceIds(Long applicationId, Long productId, Long clientId, Long tenantId,
                                             ContextPath contextPath);

    List<Long> getCommonRefereeServiceIds(Long applicationId);

    /**
     * Get all methods of specific page Id
     *
     * @param pageId
     * @return
     */
    List<PageMethodDefinition> getPageMethodDefinitions(Long pageId);

    /**
     * Get all methods of specific action Id
     *
     * @param actionId
     * @return
     */
    List<ActionMethodDefinition> getActionMethodDefinitions(Long actionId);

}
