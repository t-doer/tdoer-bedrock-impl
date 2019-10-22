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
package com.tdoer.bedrock.impl.tenant;

import com.tdoer.bedrock.context.ContextConfig;
import com.tdoer.bedrock.context.ContextInstance;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextType;
import com.tdoer.bedrock.impl.context.DefaultContextConfig;
import com.tdoer.bedrock.impl.definition.tenant.TenantDefinition;
import com.tdoer.bedrock.tenant.ProductRental;
import com.tdoer.bedrock.tenant.Tenant;
import com.tdoer.bedrock.tenant.TenantClient;
import org.springframework.util.Assert;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultTenant implements Tenant {

    protected TenantDefinition definition;

    private DefaultRentalCenter rentalCenter;

    private ContextType rootContextType;

    private ContextPath contextPath;

    private DefaultContextConfig contextConfig;

    public DefaultTenant(TenantDefinition definition, ContextPath contextPath, ContextType rootContextType, DefaultContextConfig contextConfig, DefaultRentalCenter rentalCenter) {
        Assert.notNull(definition, "Tenant definition cannot be null");
        Assert.notNull(contextPath, "Context Path cannot be null");
        Assert.notNull(rootContextType, "Root context type cannot be null");
        Assert.notNull(contextConfig, "Context config cannot be null");
        Assert.notNull(rentalCenter, "Rental center cannot be null");

        this.definition = definition;
        this.contextPath = contextPath;
        this.rootContextType = rootContextType;
        this.contextConfig = contextConfig;
        this.rentalCenter = rentalCenter;
    }

    /**
     * List the tenant's product rentals
     *
     * @param list List to hold product rentals, cannot be <code>null</code>
     */
    @Override
    public void listProductRentals(List<ProductRental> list) {
        Assert.notNull(list, "List cannot be null");
        rentalCenter.listProductRentals(getId(), list);
    }

    /**
     * List the tenant's clients
     *
     * @param list List to hold tenant clients, cannot be <code>null</code>
     */
    @Override
    public void listTenantClients(List<TenantClient> list) {
        Assert.notNull(list, "List cannot be null");
        rentalCenter.listTenantClients(getId(), list);
    }

    /**
     * Context instance Id
     *
     * @return Instance Id, must not be <code>null</code>
     */
    @Override
    public Long getId() {
        return definition.getId();
    }

    /**
     * Context instance guid, globally unique
     *
     * @return Instance GUID, must not be blank
     */
    @Override
    public String getGuid() {
        return definition.getGuid();
    }

    /**
     * Context instance name, unique in a tenant
     *
     * @return Instance name, must not be blank
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * Instance code, unique in a tenant
     *
     * @return Instance code, must not be blank
     */
    @Override
    public String getCode() {
        return definition.getCode();
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
     * The top parent of the instance, that's the tenant.
     *
     * @return
     */
    @Override
    public ContextInstance getTopParent() {
        return this;
    }

    /**
     * Context path to the context instance, say, '1.1-20.2-30.3', it's always
     * globally unique.
     *
     * @return Context path, must not be <code>null</code>
     */
    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }

    /**
     * Context type of the context instance. An instance must below to only one
     * context type.
     *
     * @return Context type, must not be <code>null</code>
     */
    @Override
    public ContextType getContextType() {
        return rootContextType;
    }

    /**
     * The instance's configurations, for example, available applications, context roles etc.
     *
     * @return Context configuration, must not be <code>null</code>
     */
    @Override
    public ContextConfig getContextConfig() {
        return contextConfig;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof Tenant){
            return this.getId().equals(((Tenant) obj).getId());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tenant[");
        sb.append(getId()).append(", ");
        sb.append(getCode()).append(", ");
        sb.append(getName()).append(", ");
        sb.append(getGuid()).append(", ");
        sb.append(getContextPath());
        sb.append("]");
        return sb.toString();
    }
}
