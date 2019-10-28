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

import com.tdoer.bedrock.impl.definition.tenant.TenantClientDefinition;
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
import com.tdoer.bedrock.product.Client;
import com.tdoer.bedrock.tenant.TenantClient;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultTenantClient implements TenantClient {

    private TenantClientDefinition definition;

    private DefaultRentalCenter rentalCenter;

    private DefaultProductRepository productRepository;

    public DefaultTenantClient(TenantClientDefinition definition, DefaultRentalCenter rentalCenter, DefaultProductRepository productRepository) {
        Assert.notNull(definition, "Tenant client definition cannot be null");
        Assert.notNull(rentalCenter, "Rental center cannot be null");
        Assert.notNull(productRepository, "Product repository cannot be null");

        this.definition = definition;
        this.rentalCenter = rentalCenter;
        this.productRepository = productRepository;
    }

    /**
     * Tenant client's GUID
     *
     * @return Tenant client's GUID, cannot be <code>null</code>
     */
    @Override
    public String getGuid() {
        return definition.getGuid();
    }

    /**
     * Tenant who accesses a product's client
     *
     * @return Tenant, cannot be <code>null</code>
     */
    @Override
    public DefaultTenant getTenant() {
        return rentalCenter.getTenantById(getTenantId());
    }

    /**
     * A product's client
     *
     * @return Client, cannot be <code>null</code>
     */
    @Override
    public Client getClient() {
        return productRepository.getClient(definition.getProductId(), getClientId());
    }

    /**
     * Tenant Id
     *
     * @return Tenant Id, cannot be <code>null</code>
     */
    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    /**
     * Client Id
     *
     * @return Tenant Id, cannot be <code>null</code>
     */
    @Override
    public Long getClientId() {
        return definition.getClientId();
    }

    /**
     * Client secret
     *
     * @return Client secret, cannot be null
     */
    @Override
    public String getSecret() {
        return definition.getSecret();
    }

    /**
     * List a client's access domains for the tenant
     *
     * @param list List to hold access domains, cannot be <code>null</code>
     */
    @Override
    public void listAccessDomains(List<String> list) {
        String[] strs = StringUtils.commaDelimitedListToStringArray(definition.getHosts());
        for(String str : strs){
            list.add(str);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof TenantClient){
            TenantClient other = (TenantClient) obj;

            return this.getTenantId().equals(other.getTenantId())
                    && this.getClientId().equals(other.getClientId());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TenantClient[");
        sb.append(getTenantId()).append(", ");
        sb.append(getClientId()).append(", ");
        sb.append(definition.getHosts());
        sb.append("]");
        return sb.toString();
    }
}
