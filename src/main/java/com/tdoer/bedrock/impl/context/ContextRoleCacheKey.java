/*
 * Copyright 2019 T-Doer (tdoer.com).
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
 *
 */
package com.tdoer.bedrock.impl.context;

import com.tdoer.bedrock.context.ContextPath;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-10-28
 */
public class ContextRoleCacheKey {
    protected Long clientId;
    protected Long tenantId;
    protected ContextPath contextPath;
    protected Long roleId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public ContextPath getContextPath() {
        return contextPath;
    }

    public void setContextPath(ContextPath contextPath) {
        this.contextPath = contextPath;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(clientId).append(", ").append(tenantId).append(", ").append(contextPath).append(", ").append(roleId).append("]");
        return sb.toString();
    }

    public static ContextRoleCacheKey getKey(Long clientId, Long tenantId, ContextPath contextPath, Long roleId){
        ContextRoleCacheKey key = new ContextRoleCacheKey();
        key.setClientId(clientId);
        key.setTenantId(tenantId);
        key.setContextPath(contextPath);
        key.setRoleId(roleId);
        return key;
    }
}
