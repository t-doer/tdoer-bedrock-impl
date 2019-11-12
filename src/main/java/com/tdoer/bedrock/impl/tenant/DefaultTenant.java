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
import com.tdoer.bedrock.impl.context.DefaultContextCenter;
import com.tdoer.bedrock.impl.context.DefaultContextConfig;
import com.tdoer.bedrock.impl.context.DefaultContextType;
import com.tdoer.bedrock.impl.definition.tenant.TenantDefinition;
import com.tdoer.bedrock.tenant.ProductRental;
import com.tdoer.bedrock.tenant.Tenant;
import com.tdoer.bedrock.tenant.TenantClient;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultTenant implements Tenant {

    protected TenantDefinition definition;

    private DefaultRentalCenter rentalCenter;

    private DefaultContextCenter contextCenter;

    private DefaultContextType rootContextType;

    private ContextPath contextPath;

    private DefaultContextConfig contextConfig;

    public DefaultTenant(TenantDefinition definition, DefaultRentalCenter rentalCenter, DefaultContextCenter contextCenter) {
        Assert.notNull(definition, "Tenant definition cannot be null");
        Assert.notNull(rentalCenter, "Rental center cannot be null");
        Assert.notNull(contextCenter, "Context center cannot be null");

        this.definition = definition;
        this.rentalCenter = rentalCenter;
        this.contextCenter = contextCenter;
        this.rootContextType = contextCenter.getRootContextType(getId());
        this.contextPath = new ContextPath(rootContextType.getType(), definition.getId());;
        this.contextConfig = new DefaultContextConfig(this, contextCenter);
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
     * Get specific context type available for the tenant
     *
     * @param contextType Context type, cannot be <code>null</code>
     * @return Context type or <code>null</code>
     */
    @Override
    public ContextType getContextType(Long contextType) {
        Assert.notNull(contextType, "Context type cannot be null");
        return rootContextType.search(contextType);
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
        return rootContextType.search(contextCode);
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

        for(ContextType child : rootContextType.getChildren()){
            listContextTypes(child, list);
        }
    }

    private void listContextTypes(ContextType contextType, List<ContextType> list){
        list.add(contextType);
        for(ContextType child : contextType.getChildren()){
            listContextTypes(child, list);
        }
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
        if(rootContextType.getCategory().equals(category)){
            list.add(rootContextType);
        }else{
            ArrayList<ContextType> all = new ArrayList<>();
            listContextTypes(all);
            for(ContextType type : all){
                if(type.getCategory().equals(category)){
                    list.add(type);
                }
            }
        }
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

        return contextCenter.getContextInstance(getId(), guid);
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

        return contextCenter.getContextInstance(getId(), contextPath);
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

        return contextCenter.getContextInstance(getId(), contextType, instanceId);
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
