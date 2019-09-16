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

import com.tdoer.bedrock.impl.definition.tenant.TenantProductDefinition;
import com.tdoer.bedrock.impl.product.DefaultProduct;
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
import com.tdoer.bedrock.tenant.ProductRental;

import java.util.Date;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultProductRental implements ProductRental {

    private TenantProductDefinition definition;

    private DefaultRentalCenter rentalCenter;

    private DefaultProductRepository productRepository;

    public DefaultProductRental(TenantProductDefinition definition, DefaultRentalCenter rentalCenter, DefaultProductRepository productRepository) {
        this.definition = definition;
        this.rentalCenter = rentalCenter;
        this.productRepository = productRepository;
    }

    @Override
    public DefaultTenant getTenant() {
        return rentalCenter.getTenant(definition.getTenantId());
    }

    @Override
    public DefaultProduct getProduct() {
        return productRepository.getProduct(definition.getProductId());
    }

    @Override
    public Date getStartDate() {
        return definition.getStartDate();
    }

    @Override
    public Date getEndDate() {
        return definition.getEndDate();
    }

    @Override
    public boolean isActive() {

        if(definition.getStartDate() != null && definition.getStartDate().getTime() > System.currentTimeMillis()){
            // not started yet
            return false;
        }

        if(definition.getEndDate() != null && definition.getEndDate().getTime() < System.currentTimeMillis()){
            // expired
            return false;
        }

        return true;
    }
}
