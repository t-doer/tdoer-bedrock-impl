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
 * @create 2019-10-27
 */
public class UserRolesCacheKey {
    private Long tenantId;

    private ContextPath contextPath;

    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tenantId).append(", ").append(contextPath).append(", ").append(userId).append("]");
        return sb.toString();
    }

    public static UserRolesCacheKey getKey(Long tenantId, ContextPath contextPath, Long userId){
        UserRolesCacheKey key = new UserRolesCacheKey();
        key.setTenantId(tenantId);
        key.setContextPath(contextPath);
        key.setUserId(userId);
        return key;
    }
}
