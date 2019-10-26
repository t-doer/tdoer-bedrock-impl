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
package com.tdoer.bedrock.impl.product;

import org.springframework.util.Assert;

/**
 * Client domain is used for search client configuration
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ClientDomain {

    protected Long clientId;
    protected Long tenantId;

    public ClientDomain(Long clientId, Long tenantId) {
        Assert.notNull(clientId, "Client ID cannot be null");
        Assert.notNull(tenantId, "Tenant Id cannot be null");

        this.clientId = clientId;
        this.tenantId = tenantId;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getTenantId() {
        return tenantId;
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
        if(obj instanceof ClientDomain){
            return toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClientDomain[");
        sb.append(clientId).append(": ");
        sb.append(tenantId);
        sb.append("]");
        return sb.toString();
    }
}

