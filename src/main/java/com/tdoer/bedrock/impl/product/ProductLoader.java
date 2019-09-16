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

import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.context.DefaultRootContextType;
import com.tdoer.bedrock.impl.definition.product.*;
import com.tdoer.bedrock.impl.provider.ProductProvider;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ProductLoader {

    private ProductProvider productProvider;

    private ProductBuilder productBuilder;

    public ProductLoader(ProductProvider productProvider, DefaultClientConfigCenter clientConfigCenter, DefaultServiceRepository serviceRepository, DefaultApplicationRepository applicationRepository, DefaultRootContextType rootContextType, ContextPathParser contextPathParser) {
        this.productProvider = productProvider;
        this.productBuilder = new ProductBuilder(serviceRepository, applicationRepository, rootContextType, contextPathParser, clientConfigCenter);
    }

    void setProductRepository(DefaultProductRepository productRepository){
        productBuilder.setProductRepository(productRepository);
    }

    public DefaultProduct loadProduct(String productId){
        ProductDefinition definition = productProvider.getProductDefinition(productId);
        if(definition == null){
            // todo, thrown exception
        }

        return productBuilder.buildProduct(definition);
    }

    public String[] loadClientIds(String productId){
        List<String> list = productProvider.getClientIds(productId);
        if(list == null || list.size() == 0){
            // todo, thrown exception
        }

        String[] ret = new String[list.size()];
        return list.toArray(ret);
    }

    public DefaultClient loadClient(String clientId){
        ClientDefinition definition = productProvider.getClientDefinition(clientId);
        if(definition == null){
            // todo, throw exception
        }

        return productBuilder.buildClient(definition);
    }

    public DefaultClientApplicationInstallation[] loadApplicationInstallations(String productId, String clientId, Long tenantId){
        List<ClientApplicationDefinition> list = productProvider.getClientApplicationDefinitions(productId, clientId, tenantId);
        if(list == null || list.size() == 0){
            // todo log
            return null;
        }

        ArrayList<DefaultClientApplicationInstallation> installations = new ArrayList<>(list.size());
        for(ClientApplicationDefinition definition : list){
            try{
                installations.add(productBuilder.buildApplicationInstallation(definition));
            }catch(Throwable t){
                // todo, warn
            }
        }

        DefaultClientApplicationInstallation[] ret = new DefaultClientApplicationInstallation[installations.size()];
        return installations.toArray(ret);
    }

    public DefaultClientService[] loadClientServices(String productId, String clientId, Long tenantId){
        List<ClientServiceDefinition> list = productProvider.getClientServiceDefinitions(productId, clientId, tenantId);
        if(list == null || list.size() == 0){
            // todo log
            return null;
        }

        ArrayList<DefaultClientService> clientServices = new ArrayList<>(list.size());
        for(ClientServiceDefinition definition : list){
            try{
                clientServices.add(productBuilder.buildClientService(definition));
            }catch(Throwable t){
                // todo, warn
            }
        }

        DefaultClientService[] ret = new DefaultClientService[clientServices.size()];
        return clientServices.toArray(ret);
    }

    public DefaultContextInstallation[] loadContextInstallations(String productId, String clientId, Long tenantId){
        List<ClientContextDefinition> list = productProvider.getClientContextDefinitions(productId, clientId, tenantId);
        if(list == null || list.size() == 0){
            // todo, log
            return null;
        }

        ArrayList<DefaultContextInstallation> installations = new ArrayList<>(list.size());
        for(ClientContextDefinition definition : list){
            try{
                installations.add(productBuilder.buildContextInstallation(definition));
            }catch(Throwable t){
                // todo, warn
            }
        }

        DefaultContextInstallation[] ret = new DefaultContextInstallation[installations.size()];
        return installations.toArray(ret);
    }

    public DefaultTokenConfig loadTokenConfig(String clientId, Long tenantId){
        ClientTokenDefinition definition = productProvider.getClientTokenDefinition(clientId, tenantId);
        if(definition == null){
            // todo log
            return null;
        }

        return productBuilder.buildTokenConfig(definition);
    }

}
