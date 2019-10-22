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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ProductLoader {

    private static Logger logger = LoggerFactory.getLogger(ProductLoader.class);

    private ProductProvider productProvider;

    private ProductBuilder productBuilder;

    public ProductLoader(ProductProvider productProvider, DefaultClientConfigCenter clientConfigCenter, DefaultServiceRepository serviceRepository, DefaultApplicationRepository applicationRepository, DefaultRootContextType rootContextType, ContextPathParser contextPathParser) {
        this.productProvider = productProvider;
        this.productBuilder = new ProductBuilder(serviceRepository, applicationRepository, rootContextType, contextPathParser, clientConfigCenter);
    }

    void setProductRepository(DefaultProductRepository productRepository){
        productBuilder.setProductRepository(productRepository);
    }

    public DefaultProduct loadProduct(Long productId){
        ProductDefinition definition = null;
        try{
            definition = productProvider.getProductDefinition(productId);
        }catch(Throwable t){
            logger.error("Failed to load product definition of product Id: " + productId, t);
        }
        if(definition != null){
            try{
                return productBuilder.buildProduct(definition);
            }catch (Exception ex){
                logger.error("Invalid product definition: " + definition, ex);
            }
        }

        return null;
    }

    public DefaultProduct loadProduct(String productCode){
        ProductDefinition definition = null;
        try{
            definition = productProvider.getProductDefinition(productCode);
        }catch(Throwable t){
            logger.error("Failed to load product definition of product code: " + productCode, t);
        }
        if(definition != null){
            try{
                return productBuilder.buildProduct(definition);
            }catch (Exception ex){
                logger.error("Invalid product definition: " + definition, ex);
            }
        }

        return null;
    }

    public DefaultClient[] loadClients(Long productId){
        List<ClientDefinition> list = null;
        try{
            list = productProvider.getClientDefinitions(productId);
        }catch (Throwable t){
            logger.error("Failed to load client definitions of product Id: " + productId, t);
        }

        if(list != null){
            ArrayList<DefaultClient> clients = new ArrayList<>(list.size());
            for(ClientDefinition def : list){
                try{
                    clients.add(productBuilder.buildClient(def));
                }catch(Exception ex){
                    logger.error("Invalid client definition: " + def, ex);
                }
            }
            DefaultClient[] ret = new DefaultClient[clients.size()];
            return clients.toArray(ret);
        }

        return new DefaultClient[0];
    }


    public DefaultClientApplicationInstallation[] loadApplicationInstallations(ClientDomain clientDomain){

        List<ClientApplicationDefinition> list = null;
        try{
            ClientDomain cd = clientDomain;
            list = productProvider.getClientApplicationDefinitions(cd.getClientId(),
                    cd.getTenantId());
        }catch(Throwable t){
            logger.error("Failed to load client application definitions of client domain: " + clientDomain, t);
        }
        if(list != null){
            ArrayList<DefaultClientApplicationInstallation> cal = new ArrayList<>(list.size());
            for(ClientApplicationDefinition definition : list){
                try{
                    cal.add(productBuilder.buildApplicationInstallation(definition));
                }catch (Exception ex){
                    logger.error("Invalid client application definition: " + definition, ex);
                }
            }
            DefaultClientApplicationInstallation[] ret = new DefaultClientApplicationInstallation[cal.size()];
            return cal.toArray(ret);
        }

        return new DefaultClientApplicationInstallation[0];
    }

    public DefaultClientServiceInstallation[] loadClientServices(ClientDomain clientDomain){

        List<ClientServiceDefinition> list = null;
        try{
            ClientDomain cd = clientDomain;
            list = productProvider.getClientServiceDefinitions(cd.getClientId(),
                    cd.getTenantId());
        }catch(Throwable t){
            logger.error("Failed to load client service definitions of client domain: " + clientDomain, t);
        }
        if(list != null){
            ArrayList<DefaultClientServiceInstallation> cal = new ArrayList<>(list.size());
            for(ClientServiceDefinition definition : list){
                try{
                    cal.add(productBuilder.buildClientServiceInstallation(definition));
                }catch (Exception ex){
                    logger.error("Invalid client service definition: " + definition, ex);
                }
            }
            DefaultClientServiceInstallation[] ret = new DefaultClientServiceInstallation[cal.size()];
            return cal.toArray(ret);
        }

        return new DefaultClientServiceInstallation[0];
    }

    public DefaultClientContextInstallation[] loadContextInstallations(ClientDomain clientDomain){
        List<ClientContextDefinition> list = null;
        try{
            list = productProvider.getClientContextDefinitions(clientDomain.getClientId(),
                    clientDomain.getTenantId());
        }catch(Throwable t){
            logger.error("Failed to load client context definitions of client domain: " + clientDomain, t);
        }
        if(list != null){
            ArrayList<DefaultClientContextInstallation> cal = new ArrayList<>(list.size());
            for(ClientContextDefinition definition : list){
                try{
                    cal.add(productBuilder.buildClientContextInstallation(definition));
                }catch (Exception ex){
                    logger.error("Invalid client context definition: " + definition, ex);
                }
            }
            DefaultClientContextInstallation[] ret = new DefaultClientContextInstallation[cal.size()];
            return cal.toArray(ret);
        }

        return new DefaultClientContextInstallation[0];
    }

    public DefaultTokenConfig loadTokenConfig(ClientDomain clientDomain){
        ClientTokenDefinition definition = null;
        try{
            definition = productProvider.getClientTokenDefinition(clientDomain.getClientId(), clientDomain.getTenantId());
        }catch(Throwable t){
            logger.error("Failed to load client token definition of client domain: " + clientDomain, t);
        }
        if(definition == null){
            try{
                return productBuilder.buildTokenConfig(definition);
            }catch (Exception ex){
                logger.error("Invalid client token definition: " + definition, ex);
            }
        }

        return null;
    }

}
