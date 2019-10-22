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

import com.tdoer.bedrock.impl.definition.product.*;

import java.util.List;

/**
 * Product provider provides active or enabled product, client and their relationship,
 * including with application, service and context etc.
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface ProductProvider {

    /**
     * Get product definition of specific product
     *
     * @param productId Product Id, cannot be <code>null</code>
     * @return Product definition or <code>null</code>
     */
    ProductDefinition getProductDefinition(Long productId);

    /**
     * Get product definition of specific product
     *
     * @param productCode Product code, cannot be blank
     * @return Product definition or <code>null</code>
     */
    ProductDefinition getProductDefinition(String productCode);

    /**
     * Get all client definitions of specific product
     * @param productId Product Id, cannot be <code>null</code>
     * @return
     */
    List<ClientDefinition> getClientDefinitions(Long productId);

    /**
     * Get client token definition of specific tenant's client
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @return Client token definition or <code>null</code>
     */
    ClientTokenDefinition getClientTokenDefinition(Long clientId, Long tenantId);

    /**
     * Get client application definition of specific tenant's client
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @return List of client application definitions or <code>null</code>
     */
    List<ClientApplicationDefinition> getClientApplicationDefinitions(Long clientId, Long tenantId);

    /**
     * Get client service definition of specific tenant's client
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @return List of client service definitions or <code>null</code>
     */
    List<ClientServiceDefinition> getClientServiceDefinitions(Long clientId, Long tenantId);

    /**
     * Get client context definition of specific tenant's client
     * @param clientId Client Id, cannot be <code>null</code>
     * @param tenantId Tenant Id, cannot be <code>null</code>
     * @return List of client context definitions or <code>null</code>
     */
    List<ClientContextDefinition> getClientContextDefinitions(Long clientId, Long tenantId);
}
