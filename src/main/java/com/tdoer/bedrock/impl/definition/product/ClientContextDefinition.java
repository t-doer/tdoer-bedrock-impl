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
package com.tdoer.bedrock.impl.definition.product;

import java.io.Serializable;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ClientContextDefinition implements Serializable {

    private String productId;

    private String clientId;

    private String contextPath;

    private Long tenantId;

    private String entryNavItem;

    private String entryApplicationId;

    private String entryLanguage;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getEntryNavItem() {
        return entryNavItem;
    }

    public void setEntryNavItem(String entryNavItem) {
        this.entryNavItem = entryNavItem;
    }

    public String getEntryApplicationId() {
        return entryApplicationId;
    }

    public void setEntryApplicationId(String entryApplicationId) {
        this.entryApplicationId = entryApplicationId;
    }

    public String getEntryLanguage() {
        return entryLanguage;
    }

    public void setEntryLanguage(String entryLanguage) {
        this.entryLanguage = entryLanguage;
    }
}