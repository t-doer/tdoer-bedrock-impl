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

import com.tdoer.bedrock.context.ContextConfig;
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

    /**
     * Context instance Id
     *
     * @return Instance Id, must not be <code>null</code>
     */
    @Override
    public Long getId() {
        return null;
    }

    /**
     * Context instance guid, globally unique
     *
     * @return Instance GUID, must not be blank
     */
    @Override
    public String getGuid() {
        return null;
    }

    /**
     * Context instance name, unique in a tenant
     *
     * @return Instance name, must not be blank
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Instance code, unique in a tenant
     *
     * @return Instance code, must not be blank
     */
    @Override
    public String getCode() {
        return null;
    }

    /**
     * Get instance's detail information object's ID, say, class's Id, user's Id etc.
     *
     * @return associated detail object Id, may be <code>null</code>
     */
    @Override
    public Long getDetailObjectId() {
        return null;
    }

    /**
     * Is the instance a tenant, the root context instance?
     *
     * @return true if the instance is a tenant
     */
    @Override
    public boolean isTenant() {
        return false;
    }

    /**
     * Parent context instance. If the instance is a tenant, its parent context instance
     * is <code>null</code>
     *
     * @return The context instance's parent instance, may be <code>null</code>
     */
    @Override
    public ContextInstance getParent() {
        return null;
    }

    /**
     * The top parent of the instance, that's the tenant.
     *
     * @return
     */
    @Override
    public ContextInstance getTopParent() {
        return null;
    }

    /**
     * Context path to the context instance, say, '1.1-20.2-30.3', it's always
     * globally unique.
     *
     * @return Context path, must not be <code>null</code>
     */
    @Override
    public ContextPath getContextPath() {
        return null;
    }

    /**
     * Context type of the context instance. An instance must below to only one
     * context type.
     *
     * @return Context type, must not be <code>null</code>
     */
    @Override
    public ContextType getContextType() {
        return null;
    }

    /**
     * The instance's configurations, for example, available applications, context roles etc.
     *
     * @return Context configuration, must not be <code>null</code>
     */
    @Override
    public ContextConfig getContextConfig() {
        return null;
    }
}
