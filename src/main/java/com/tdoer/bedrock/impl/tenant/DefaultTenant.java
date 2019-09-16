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
import com.tdoer.bedrock.tenant.Tenant;
import com.tdoer.bedrock.tenant.TenantClient;

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
        this.definition = definition;
        this.contextPath = contextPath;
        this.rootContextType = rootContextType;
        this.contextConfig = contextConfig;
        this.rentalCenter = rentalCenter;
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
    public void listProductRentals(List<com.tdoer.bedrock.tenant.ProductRental> list) {
        DefaultProductRental[] productRentals = rentalCenter.getProductRendtal(getId());
        if(productRentals != null){
            for(DefaultProductRental pr : productRentals){
                list.add(pr);
            }
        }
    }

    /**
     * List the tenant's clients
     *
     * @param list
     */
    @Override
    public void listTenantClients(List<TenantClient> list) {
        DefaultTenantClient[] clients = rentalCenter.getTenantClients(getId());
        if(clients != null){
            for(DefaultTenantClient client : clients){
                list.add(client);
            }
        }
    }

    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }

    /**
     * Organization Id or User Id
     *
     * @return
     */
    @Override
    public Long getInstanceId() {
        return getId();
    }

    /**
     * Organization Name or User Name
     *
     * @return
     */
    @Override
    public String getInstanceName() {
        return getName();
    }

    /**
     * Get contextPath instance's detail information object's ID, say, tenant's Id, store's Id, userI's Id etc.
     *
     * @return
     */
    @Override
    public Long getDetailObjectId() {
        return null;
    }

    /**
     * Parent contextPath instance. If the instance is a tenant, its parent context instance
     * is null; if the instance is a user, its parent is a tenant; if the instance is an
     * organization, parent context instance is its parent organization.
     *
     * <p>
     * The top parent is always the tenant.
     *
     * @return
     */
    @Override
    public ContextInstance getParent() {
        return null;
    }

    /**
     * The top parent is always the tenant.
     *
     * @return
     */
    @Override
    public ContextInstance getTopParent() {
        return this;
    }

    @Override
    public ContextType getContextType() {
        return rootContextType;
    }

    /**
     * The instance's configurations, for example, available applications, contextPath roles etc.
     *
     * @return
     */
    @Override
    public ContextConfig getContextConfig() {
        return contextConfig;
    }
}
