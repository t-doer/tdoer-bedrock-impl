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

import com.tdoer.bedrock.impl.context.DefaultContextConfigCenter;
import com.tdoer.bedrock.impl.context.DefaultRootContextType;
import com.tdoer.bedrock.impl.definition.tenant.TenantClientDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantProductDefinition;
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
import com.tdoer.bedrock.impl.provider.TenantProvider;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TenantLoader {
    private TenantProvider tenantProvider;

    private TenantBuilder tenantBuilder;

    public TenantLoader(TenantProvider tenantProvider, DefaultProductRepository productRepository, DefaultRootContextType rootContextType, DefaultContextConfigCenter configCenter) {
        this.tenantProvider = tenantProvider;
        this.tenantBuilder = new TenantBuilder(productRepository, rootContextType, configCenter);
    }

    public void setRentalCenter(DefaultRentalCenter rentalCenter) {
        this.tenantBuilder.setRentalCenter(rentalCenter);
    }

    public DefaultTenant loadTenant(String tenantCode){
        TenantDefinition definition = tenantProvider.getTenantDefinition(tenantCode);
        if(definition == null){
            // todo throw exception
        }
        return tenantBuilder.buildTenant(definition);
    }

    public DefaultTenant loadTenant(Long tenantId){
        TenantDefinition definition = tenantProvider.getTenantDefinition(tenantId);
        if(definition == null){
            // todo throw exception
        }
        return tenantBuilder.buildTenant(definition);
    }


    public DefaultTenantClient loadTenantClient(String host){
        TenantClientDefinition definition = tenantProvider.getTenantClientDefinition(host);
        if(definition == null){
            // todo, throw exception
            return null;
        }

        return tenantBuilder.buildTenantClient(definition);
    }

    public DefaultTenantClient loadTenantClient(String clientId, Long tenantId){
        TenantClientDefinition definition = tenantProvider.getTenantClientDefinition(clientId, tenantId);
        if(definition == null){
            // todo, throw exception
            return null;
        }

        return tenantBuilder.buildTenantClient(definition);
    }

    public DefaultProductRental loadProductRendtal(String productId, Long tenantId) {
        TenantProductDefinition definition = tenantProvider.getTenantProductDefinition(productId, tenantId);
        if(definition == null){
            // todo, throw exception
        }

        return tenantBuilder.buildProductRental(definition);
    }

    public String[] loadProductIds(Long tenantId){
        List<String> list = tenantProvider.getProductIds(tenantId);
        if(list == null || list.size() == 0){
            // todo, throw exception
        }

        String[] ret = new String[list.size()];
        return list.toArray(ret);
    }

    public String[] loadClientIds(Long tenantId){
        List<String> list = tenantProvider.getClientIds(tenantId);
        if(list == null || list.size() == 0){
            // todo, throw exception
        }

        String[] ret = new String[list.size()];
        return list.toArray(ret);
    }
}
