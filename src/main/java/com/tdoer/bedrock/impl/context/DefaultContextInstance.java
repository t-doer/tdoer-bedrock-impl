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
package com.tdoer.bedrock.impl.context;

import com.tdoer.bedrock.context.ContextInstance;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextType;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextInstance implements ContextInstance {

    private String instanceName;

    private String code;

    private ContextType contextType;

    private ContextPath contextPath;

    private DefaultContextInstance parent;

    private DefaultContextConfig contextConfig;

    private Long detailObjectId;

    public DefaultContextInstance(ContextPath contextPath, ContextType contextType, String instanceName, String code, Long detailObjectId, DefaultContextConfig contextConfig) {
        this.contextPath = contextPath;
        this.contextType = contextType;
        this.instanceName = instanceName;
        this.code = code;
        this.detailObjectId = detailObjectId;
        this.contextConfig = contextConfig;
    }

    public void setParent(DefaultContextInstance parent){
        this.parent = parent;
    }

    @Override
    public Long getInstanceId() {
        return contextPath.getInstanceId();
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public ContextType getContextType() {
        return contextType;
    }

    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }

    @Override
    public DefaultContextInstance getParent() {
        return parent;
    }

    @Override
    public DefaultContextConfig getContextConfig() {
        return contextConfig;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Long getDetailObjectId() {
        return detailObjectId;
    }

    /**
     * The top parent is always the tenant
     *
     * @return
     */
    @Override
    public DefaultContextInstance getTopParent() {
        DefaultContextInstance c = this;
        while(c.getParent() != null) {
            c = c.getParent();
        }
        return c;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DefaultContextInstance[");
        sb.append(contextPath).append(", ");
        sb.append(code).append(", ");
        sb.append(instanceName).append(", ");
        sb.append(detailObjectId);
        sb.append("]");
        return sb.toString();
    }
}
