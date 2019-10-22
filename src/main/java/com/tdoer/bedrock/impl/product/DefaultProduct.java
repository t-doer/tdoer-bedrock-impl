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

import com.tdoer.bedrock.impl.definition.product.ProductDefinition;
import com.tdoer.bedrock.product.Client;
import com.tdoer.bedrock.product.Product;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Locale;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultProduct implements Product {
    private ProductDefinition definition;
    private Locale[] languages;
    private DefaultProductRepository productRepository;

    public DefaultProduct(ProductDefinition definition, Locale[] languages, DefaultProductRepository productRepository) {
        Assert.notNull(definition, "Product definition cannot be null");
        Assert.notNull(languages, "Languages cannot be null");
        Assert.notNull(productRepository, "Product repository cannot be null");

        this.definition = definition;
        this.languages = languages;
        this.productRepository = productRepository;
    }

    /**
     * Product Id
     *
     * @return Product Id, must not be <code>null</code>
     */
    @Override
    public Long getId() {
        return definition.getId();
    }

    /**
     * Product code
     *
     * @return Product code, must not be <code>null</code>
     */
    @Override
    public String getCode() {
        return definition.getCode();
    }

    /**
     * Product name
     *
     * @return Product name, must not be <code>null</code>
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * Product description
     *
     * @return Product description, may be <code>null</code>
     */
    @Override
    public String getDescription() {
        return definition.getDescription();
    }

    /**
     * Product version
     *
     * @return Product version, must not be <code>null</code>
     */
    @Override
    public String getVersion() {
        return definition.getVersion();
    }

    /**
     * List the languages the product supports
     *
     * @param list List to hold languages
     */
    @Override
    public void listLanguages(List<Locale> list) {
        for(Locale locale : languages){
            list.add(locale);
        }
    }

    /**
     * Get the product's client of specific ID
     *
     * @param clientId Client ID, cannot be <code>null</code>
     * @return Client if found, otherwise return <code>null</code>
     */
    @Override
    public Client getClient(Long clientId) {
        Assert.notNull(clientId, "Client Id cannot be null");

        return productRepository.getClient(getId(), clientId);
    }

    /**
     * Get the product's client of specific client code
     *
     * @param clientCode Client code, cannot be <code>null</code>
     * @return Client if found, otherwise return <code>null</code>
     */
    @Override
    public Client getClient(String clientCode) {
        Assert.hasText(clientCode, "Client code cannot be blank");
        return productRepository.getClient(getId(), clientCode);
    }

    /**
     * List the product's enabled clients
     *
     * @param list List to hold clients, cannot be <code>null</code>
     */
    @Override
    public void listClients(List<Client> list) {
        Assert.notNull(list, "List cannot be null");
        productRepository.listClients(getId(), list);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof Product){
            return this.getId().equals(((Product) obj).getId());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product[");
        sb.append(getId()).append(", ");
        sb.append(getCode()).append(", ");
        sb.append(getName()).append(", ");
        sb.append(getVersion());
        sb.append("]");
        return sb.toString();
    }
}
