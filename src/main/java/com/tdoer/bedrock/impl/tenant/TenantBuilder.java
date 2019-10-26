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

import com.tdoer.bedrock.impl.context.DefaultContextCenter;
import com.tdoer.bedrock.impl.definition.tenant.TenantClientDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantProductDefinition;
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TenantBuilder {
    private DefaultRentalCenter rentalCenter;

    private DefaultProductRepository productRepository;

    private DefaultContextCenter contextCenter;

    public TenantBuilder(DefaultProductRepository productRepository, DefaultContextCenter contextCenter) {
        Assert.notNull(productRepository, "Product repository cannot be null");
        Assert.notNull(contextCenter, "Context center cannot be null");

        this.productRepository = productRepository;
        this.contextCenter = contextCenter;
        this.contextCenter = contextCenter;
    }

    public void setRentalCenter(DefaultRentalCenter rentalCenter) {
        Assert.notNull(rentalCenter, "Rental center cannot be null");

        this.rentalCenter = rentalCenter;
    }

    public DefaultTenant buildTenant(TenantDefinition definition){
        // todo check defnition

        return new DefaultTenant(definition, rentalCenter, contextCenter);
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
