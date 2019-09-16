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
package com.tdoer.bedrock.impl.product;

import com.tdoer.bedrock.impl.definition.product.ClientDefinition;
import com.tdoer.bedrock.product.Client;
import com.tdoer.bedrock.product.ClientCategory;
import com.tdoer.bedrock.product.ClientConfig;
import com.tdoer.bedrock.product.Product;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClient implements Client {
    private ClientDefinition definition;
    private DefaultClientConfig clientConfig;
    private DefaultProductRepository productRepository;

    public DefaultClient(ClientDefinition definition, DefaultClientConfig clientConfig, DefaultProductRepository productRepository) {
        this.definition = definition;
        this.clientConfig = clientConfig;
        this.productRepository = productRepository;
    }

    /**
     * Client Id
     *
     * @return
     */
    @Override
    public String getId() {
        return definition.getId();
    }

    /**
     * Client name
     *
     * @return
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * The product the client belongs to
     *
     * @return
     */
    @Override
    public Product getProduct() {
        return productRepository.getProduct(definition.getProductId());
    }

    /**
     * Clent category
     *
     * @return
     */
    @Override
    public ClientCategory getCategory() {
        return ClientCategory.resolve(definition.getCategory());
    }

    /**
     * Client secret
     *
     * @return
     */
    @Override
    public String getSecret() {
        return definition.getSecret();
    }

    /**
     * The client's access scopes
     *
     * @return
     */
    @Override
    public String[] getScopes() {
        return StringUtils.delimitedListToStringArray(definition.getScopes(), ",");
    }

    /**
     * The client's grant types
     *
     * @return
     */
    @Override
    public String[] getGrantTypes() {
        return StringUtils.delimitedListToStringArray(definition.getGrantTypes(), ",");
    }

    /**
     * The client's auto approval's scope
     *
     * @return
     */
    @Override
    public String[] getAutoApprovals() {
        return StringUtils.delimitedListToStringArray(definition.getAutoApprovals(), ",");
    }

    /**
     * The client's authorities or roles that it can plays as in its product
     *
     * @return
     */
    @Override
    public DefaultClientRole[] getRoles() {
        DefaultClientRole[] ret = null;
        String[] arr = StringUtils.delimitedListToStringArray(definition.getAuthorities(), ",");
        if(arr != null){
            ArrayList<DefaultClientRole> list = new ArrayList<>(arr.length);
            for(String auth : arr){
                list.add(new DefaultClientRole(getId(), auth, auth));
            }
            ret = new DefaultClientRole[list.size()];
            list.toArray(ret);
        }
        return ret;
    }

    /**
     * Whether the client can be trusted or not
     *
     * @return
     */
    @Override
    public boolean isTrusted() {
        return !("N".equalsIgnoreCase(definition.getTrusted()));
    }

    /**
     * Client configuration
     *
     * @return
     */
    @Override
    public ClientConfig getClientConfig() {
        return clientConfig;
    }
}
