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
package com.tdoer.bedrock.impl.domain;

import com.tdoer.bedrock.impl.application.ApplicationDomain;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ProductDomain extends ExtensionDomain<ProductDomain> {

    protected String productId;
    protected String clientId;
    protected Long tenantId;

    public ProductDomain(String productId, String clientId, Long tenantId) {
        Assert.notNull(productId, "Product ID cannot be null");
        this.productId = productId;
        this.clientId = clientId;
        this.tenantId = tenantId;
    }

    public String getProductId() {
        return productId;
    }

    public String getClientId() {
        return clientId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    @Override
    public DomainType getType() {
        return DomainType.PRODUCT;
    }

    /**
     * Suppose current product domain is [productId: clientId, tenantId]
     * is [cc: cc-engineer-app, 1], next lookup sequence will be:
     * <ol>
     *     <li>[cc: cc-engineer-app, 1]</li>
     *     <li>[cc: cc-engineer-app, 0]</li>
     *     <li>[cc: null, 0]</li>
     *     <li>null</li>
     * </ol>
     *
     * @return
     */
    @Override
    public ProductDomain nextLookup() {
        if(tenantId != null && !tenantId.equals(0L)){
            return new ProductDomain(productId, clientId, 0L);
        }else if(clientId != null){
            return new ProductDomain(productId, null, 0L);
        }else{
            return null;
        }
    }
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(obj instanceof ApplicationDomain){
            return toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(productId).append(": ");
        sb.append(clientId);
        sb.append(", ").append(tenantId);
        sb.append("]");
        return sb.toString();
    }
}

