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

import java.util.List;
import java.util.Locale;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultProduct implements com.tdoer.bedrock.product.Product {
    private ProductDefinition definition;
    private Locale defaultLanguage;
    private Locale[] languages;
    private DefaultProductRepository productRepository;

    public DefaultProduct(ProductDefinition definition, Locale defaultLanguage, Locale[] languages, DefaultProductRepository productRepository) {
        this.definition = definition;
        this.defaultLanguage = defaultLanguage;
        this.languages = languages;
        this.productRepository = productRepository;
    }

    @Override
    public String getId() {
        return definition.getId();
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public String getDescription() {
        return definition.getDescription();
    }

    /**
     * Get the default language of the product
     *
     * @return
     */
    @Override
    public Locale getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * List the languages the product supports
     *
     * @param list
     */
    @Override
    public void listLanguages(List<Locale> list) {
        if(languages != null){
            for(Locale lang : languages){
                list.add(lang);
            }
        }
    }

    /**
     * Get the client of the product by the specific Id
     *
     * @param clientId
     * @return
     */
    @Override
    public DefaultClient getClient(String clientId) {
        return productRepository.getClient(clientId);
    }

    /**
     * List the product's clients
     *
     * @param list
     */
    @Override
    public void listClients(List<Client> list) {
        DefaultClient[] clients = productRepository.getClients(getId());
        if(clients != null){
            for(DefaultClient client : clients){
                list.add(client);
            }
        }
    }
}
