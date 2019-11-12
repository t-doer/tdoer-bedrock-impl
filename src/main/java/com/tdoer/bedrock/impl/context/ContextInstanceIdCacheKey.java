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

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-10-27
 */
public class ContextInstanceIdCacheKey {
    private Long tenantId;

    private Long contextType;

    private Long instanceId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getContextType() {
        return contextType;
    }

    public void setContextType(Long contextType) {
        this.contextType = contextType;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tenantId).append(", ")
                .append(contextType).append(", ")
                .append(instanceId).append("]");
        return sb.toString();
    }

    public static ContextInstanceIdCacheKey getKey(Long tenantId, Long contextType, Long instanceId){
        ContextInstanceIdCacheKey key = new ContextInstanceIdCacheKey();
        key.setTenantId(tenantId);
        key.setContextType(contextType);
        key.setInstanceId(instanceId);
        return key;
    }
}
