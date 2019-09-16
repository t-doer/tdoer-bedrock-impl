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

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextType;
import com.tdoer.bedrock.impl.context.DefaultContextConfig;
import com.tdoer.bedrock.impl.context.DefaultContextConfigCenter;
import com.tdoer.bedrock.impl.context.DefaultRootContextType;
import com.tdoer.bedrock.impl.definition.tenant.TenantClientDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantProductDefinition;
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TenantBuilder {
    private DefaultRentalCenter rentalCenter;

    private DefaultProductRepository productRepository;

    private DefaultRootContextType rootContextType;

    private DefaultContextConfigCenter configCenter;

    public TenantBuilder(DefaultProductRepository productRepository, DefaultRootContextType rootContextType, DefaultContextConfigCenter configCenter) {
        this.productRepository = productRepository;
        this.rootContextType = rootContextType;
        this.configCenter = configCenter;
    }

    public void setRentalCenter(DefaultRentalCenter rentalCenter) {
        this.rentalCenter = rentalCenter;
    }

    public DefaultTenant buildTenant(TenantDefinition definition){
        // todo check defnition
        ContextType contextType = rootContextType.getRoot();
        ContextPath contextPath = new ContextPath(contextType.getType(), definition.getId());
        DefaultContextConfig contextConfig = new DefaultContextConfig(configCenter, contextPath);

        return new DefaultTenant(definition, contextPath, contextType, contextConfig, rentalCenter);
    }

    public DefaultProductRental buildProductRental(TenantProductDefinition definition){
        // todo check definition

        return new DefaultProductRental(definition, rentalCenter, productRepository);
    }

    public DefaultTenantClient buildTenantClient(TenantClientDefinition definition){
        // todo check definition

        return new DefaultTenantClient(definition, rentalCenter, productRepository);
    }
}
