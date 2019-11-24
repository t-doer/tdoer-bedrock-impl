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

import com.tdoer.bedrock.Platform;
import com.tdoer.bedrock.context.AbstractContextInstance;
import com.tdoer.bedrock.context.ContextInstance;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextType;
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
public class DefaultTenant extends AbstractContextInstance implements Tenant {

    protected TenantDefinition definition;

    public DefaultTenant(TenantDefinition definition) {
        super(ContextType.TENANT, new ContextPath(ContextType.TENANT.getType(), definition.getId()));
        Assert.notNull(definition, "Tenant definition cannot be null");

        this.definition = definition;
    }

    /**
     * List the tenant's product rentals
     *
     * @param list List to hold product rentals, cannot be <code>null</code>
     */
    @Override
    public void listProductRentals(List<ProductRental> list) {
        Assert.notNull(list, "List cannot be null");
        Platform.getRentalCenter().listProductRentals(getId(), list);
    }

    /**
     * List the tenant's clients
     *
     * @param list List to hold tenant clients, cannot be <code>null</code>
     */
    @Override
    public void listTenantClients(List<TenantClient> list) {
        Assert.notNull(list, "List cannot be null");
        Platform.getRentalCenter().listTenantClients(getId(), list);
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
     * The Id of tenant to which the context instance belongs
     *
     * @return Tenant Id, must not be <code>null</code>
     */
    @Override
    public Long getTenantId() {
        return getId();
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
     * Get specific context type available for the tenant
     *
     * @param contextType Context type, cannot be <code>null</code>
     * @return Context type or <code>null</code>
     */
    @Override
    public ContextType getContextType(Long contextType) {
        Assert.notNull(contextType, "Context type cannot be null");
        return Platform.getContextCenter().getContextType(getId(), contextType);
    }

    /**
     * Get specific context type available for the tenant
     *
     * @param contextCode Context code, cannot be blank
     * @return Context type or <code>null</code>
     */
    @Override
    public ContextType getContextType(String contextCode) {
        Assert.hasText(contextCode, "Context code cannot be null");
        return Platform.getContextCenter().getContextType(getId(), contextCode);
    }

    /**
     * List all context types defined by the tenant.
     * <p>
     * Note the list should not include "TENANT" context type which is
     * the root context type for all.
     *
     * @param list List to hold a tenant's context types.
     */
    @Override
    public void listContextTypes(List<ContextType> list) {
        Assert.notNull(list, "List cannot be null");

        Platform.getContextCenter().listContextTypes(getId(), list);
    }

    /**
     * List context types of specific category defined by the tenant.
     *
     * @param category Context category
     * @param list     List to hold context types, cannot be <code>null</code>
     */
    @Override
    public void listContextTypes(String category, List<ContextType> list) {
        Assert.hasText(category, "Category cannot be blank");
        Assert.notNull(list, "List cannot be null");

        Platform.getContextCenter().listContextTypes(getId(), category, list);
    }

    /**
     * Get context instance by GUID
     *
     * @param guid Context instance GUID, cannot be blank
     * @return Context instance or <code>null</code>
     */
    @Override
    public ContextInstance getContextInstance(String guid) {
        Assert.hasText(guid, "GUID cannot be blank");

        if(getGuid().equals(guid)){
            return this;
        }

        return Platform.getContextCenter().getContextInstance(getId(), guid);
    }

    /**
     * Get context instance by context path
     *
     * @param contextPath Context path, cannot be <code>null</code>
     * @return Context instance or <code>null</code>
     */
    @Override
    public ContextInstance getContextInstance(ContextPath contextPath) {
        Assert.notNull(contextPath, "Context path cannot be null");
        if(getContextPath().equals(contextPath)){
            return this;
        }
        return Platform.getContextCenter().getContextInstance(getId(), contextPath);
    }

    /**
     * Get context instance by context type and instance Id
     *
     * @param contextType Context type, cannot be  <code>null</code>
     * @param instanceId Context instance Id, cannot be <code>null</code>
     * @return Context instance or <code>null</code>
     */
    @Override
    public ContextInstance getContextInstance(Long contextType, Long instanceId) {
        Assert.notNull(contextType, "Context type cannot be null");
        Assert.notNull(instanceId, "Instance Id cannot be null");

        if(ContextType.TENANT.getType().equals(contextType) && getId().equals(instanceId)){
            return this;
        }
        return Platform.getContextCenter().getContextInstance(getId(), contextType, instanceId);
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
