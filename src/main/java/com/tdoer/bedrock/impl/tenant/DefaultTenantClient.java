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
import com.tdoer.bedrock.tenant.Tenant;
import com.tdoer.bedrock.tenant.TenantClient;
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
        this.definition = definition;
        this.rentalCenter = rentalCenter;
        this.productRepository = productRepository;
    }

    public String[] getHosts() {
        String[] hosts = StringUtils.delimitedListToStringArray(definition.getHosts(), ",");
        return hosts;
    }

    @Override
    public void listHosts(List<String> list) {
        String[] hosts = getHosts();
        if(hosts != null){
            for(String host : hosts){
                list.add(host);
            }
        }
    }

    @Override
    public Tenant getTenant() {
        return rentalCenter.getTenant(definition.getTenantId());
    }

    @Override
    public Client getClient() {
        return productRepository.getClient(definition.getClientId());
    }
}
