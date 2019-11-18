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

import com.tdoer.bedrock.context.ContextCenter;
import com.tdoer.bedrock.context.ContextConfig;
import com.tdoer.bedrock.context.ContextInstance;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextType;
import com.tdoer.bedrock.impl.definition.context.ContextInstanceDefinition;

/**
 * DefaultContextInstance
 */
public class DefaultContextInstance implements ContextInstance {

    private ContextInstanceDefinition definition;
    private ContextPath contextPath;
    private ContextType contextType;
    private ContextCenter contextCenter;
    private ContextConfig contextConfig;

    public DefaultContextInstance(ContextInstanceDefinition definition, DefaultContextCenter contextCenter) {
        this.definition = definition;
        this.contextCenter = contextCenter;
        this.contextPath = new DefaultContextPathParser().parse(definition.getContextPath());
        this.contextType = new DefaultContextType(definition.getContextType());
        this.contextConfig = new DefaultContextConfig(this, contextCenter);
    }

    @Override
    public Long getId() {
        return definition.getId();
    }

    @Override
    public String getCode() {
        return definition.getCode();
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public String getGuid() {
        return definition.getGuid();
    }

    @Override
    public Long getDetailObjectId() {
        return definition.getDetailObjectId();
    }

    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    @Override
    public ContextConfig getContextConfig() {
        return this.contextConfig;
    }

    @Override
    public ContextPath getContextPath() {
        return this.contextPath;
    }

    @Override
    public ContextType getContextType() {
        return this.contextType;
    }

    @Override
    public ContextInstance getParent() {
        return contextCenter.getContextInstance(getTenantId(), contextPath.getParent());
    }

    @Override
    public ContextInstance getTopParent() {
        return contextCenter.getContextInstance(getTenantId(), contextPath.getRootPath());
    }

    @Override
    public boolean isTenant() {
        return getContextType().isTenant();
    }
}