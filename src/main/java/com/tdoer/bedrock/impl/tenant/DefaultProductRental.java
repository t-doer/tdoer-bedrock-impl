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
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
import com.tdoer.bedrock.product.Product;
import com.tdoer.bedrock.tenant.ProductRental;
import com.tdoer.bedrock.tenant.Tenant;
import com.tdoer.springboot.util.LocaleUtil;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Locale;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultProductRental implements ProductRental {

    private TenantProductDefinition definition;

    private DefaultRentalCenter rentalCenter;

    private DefaultProductRepository productRepository;

    public DefaultProductRental(TenantProductDefinition definition, DefaultRentalCenter rentalCenter, DefaultProductRepository productRepository) {
        Assert.notNull(definition, "Tenant product definition cannot be null");
        Assert.notNull(rentalCenter, "Rental center cannot be null");
        Assert.notNull(productRepository, "Product repository cannot be null");

        this.definition = definition;
        this.rentalCenter = rentalCenter;
        this.productRepository = productRepository;
    }

    /**
     * Tenant who rents the product
     *
     * @return Tenant, must not be <code>null</code>
     */
    @Override
    public Tenant getTenant() {
        return rentalCenter.getTenantById(getTenantId());
    }

    /**
     * Product which is rented by tenant
     *
     * @return Product, must not be <code>null</code>
     */
    @Override
    public Product getProduct() {
        return productRepository.getProduct(getProductId());
    }

    /**
     * Tenant Id
     *
     * @return Tenant Id, must not be <code>null</code>
     */
    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    /**
     * Product Id
     *
     * @return Product Id, must not be <code>null</code>
     */
    @Override
    public Long getProductId() {
        return definition.getProductId();
    }

    /**
     * Default language the tenant uses
     *
     * @return Language, must not be <code>null</code>
     */
    @Override
    public Locale getDefaultLanguage() {
        return LocaleUtil.getLocale(definition.getDefaultLanguage());
    }

    /**
     * Rental start date
     *
     * @return Date, must not be <code>null</code>
     */
    @Override
    public Date getStartDate() {
        return definition.getStartDate();
    }

    /**
     * Rental end date
     *
     * @return Date, must not be <code>null</code>
     */
    @Override
    public Date getEndDate() {
        return definition.getEndDate();
    }

    /**
     * Check if the rental is still active, not due
     *
     * @return true if still active
     */
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof ProductRental){
            ProductRental other = (ProductRental) obj;

            return this.getTenantId().equals(other.getTenantId())
                    && this.getProductId().equals(other.getProductId());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProductRental[");
        sb.append(getTenantId()).append(", ");
        sb.append(getProductId());
        sb.append("]");
        return sb.toString();
    }
}
