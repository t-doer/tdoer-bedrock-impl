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

import com.tdoer.bedrock.impl.definition.application.*;

import java.util.List;

/**
 * Application provider provides active or enabled application, page, action
 * and their relationship definitions, including with service and service method.
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface ApplicationProvider {
    
    /**
     * Get application definition of specific application, which must be
     * active or enabled
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return Application definition or <code>null</code> if dose not exist or is disabled
     */
    ApplicationDefinition getApplicationDefinitionById(Long applicationId);

    /**
     * Get application definition of specific application, which must be
     * active or enabled
     *
     * @param applicationCode Application code, cannot be <code>null</code>
     * @return Application definition or <code>null</code> if dose not exist or is disabled
     */
    ApplicationDefinition getApplicationDefinitionByCode(String applicationCode);

    /**
     * Get all active or enabled page definitions of specific application, including
     * common pages or customized pages.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return List of page definition or <code>null</code>
     */
    List<PageDefinition> getAllPageDefinitions(Long applicationId);

    /**
     * Get the Ids of customized pages for specific application domain with full match.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param productId Product Id, cannot be <code>null</code>, may be zero
     * @param clientId Client Id, cannot be <code>null</code>, may be zero
     * @param tenantId Tenant Id, cannot be <code>null</code>, may be zero
     * @param contextPath Context path, cannot be <code>null</code>, may be "void"
     * @return List of page Ids or <code>null</code>
     */
    List<Long> getCustomizedPageIds(Long applicationId, Long productId, Long clientId, Long tenantId,
                                               String contextPath);

    /**
     * Get the Ids of common pages of specific application, excluding customized ones
     *
     * @param applicationId @param applicationId Application Id, cannot be <code>null</code>
     * @return List of page Ids or <code>null</code>
     */
    List<Long> getCommonPageIds(Long applicationId);

    /**
     * Get all active or enabled actions' definitions of specific page
     *
     * @param pageId Page Id, cannot be <code>null</code>
     * @return List of action definitions or <code>null</code>
     */
    List<ActionDefinition> getAllActionDefinitions(Long pageId);

    /**
     * Get the Ids of customized actions for specific page domain with full match
     * @param pageId Application Id, cannot be <code>null</code>
     * @param productId Product Id, cannot be <code>null</code>, may be zero
     * @param clientId Client Id, cannot be <code>null</code>, may be zero
     * @param tenantId Tenant Id, cannot be <code>null</code>, may be zero
     * @param contextPath Context path, cannot be <code>null</code>, may be "void"
     * @return List of action Ids or <code>null</code>
     */
    List<Long> getCustomizedActionIds(Long pageId, Long productId, Long clientId, Long tenantId,
                                                   String contextPath);

    /**
     * Get the Ids of common actions of specific page
     *
     * @param pageId Page Id, cannot be <code>null</code>
     * @return List of action Ids or <code>null</code>
     */
    List<Long> getCommonActionIds(Long pageId);

    /**
     * Get the Ids of customized referee services for specific application domain with full match
     * @param applicationId Application Id, cannot be <code>null</code>
     * @param productId Product Id, cannot be <code>null</code>, may be zero
     * @param clientId Client Id, cannot be <code>null</code>, may be zero
     * @param tenantId Tenant Id, cannot be <code>null</code>, may be zero
     * @param contextPath Context path, cannot be <code>null</code>, may be "void"
     * @return List of service Ids or <code>null</code>
     */
    List<Long> getCustomizedRefereeServiceIds(Long applicationId, Long productId, Long clientId, Long tenantId,
                                             String contextPath);

    /**
     * Get the Ids of common referee services of specific application
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return List of service Ids or <code>null</code>
     */
    List<Long> getCommonRefereeServiceIds(Long applicationId);

    /**
     * Get the Ids of all referee services of specific application, including common ones and customized ones.
     *
     * @param applicationId Application Id, cannot be <code>null</code>
     * @return List of service Ids or <code>null</code>
     */
    List<Long> getAllRefereeServiceIds(Long applicationId);

    /**
     * Get all active or enabled service methods of specific page
     *
     * @param pageId Page Id, cannot be <code>null</code>
     * @return List of page method definition or <code>null</code>
     */
    List<PageMethodDefinition> getPageMethodDefinitions(Long pageId);

    /**
     * Get all active or enabled methods of specific action
     *
     * @param actionId Action Id, cannot be <code>null</code>
     * @return List of action method definition or <code>null</code>
     */
    List<ActionMethodDefinition> getActionMethodDefinitions(Long actionId);

}
