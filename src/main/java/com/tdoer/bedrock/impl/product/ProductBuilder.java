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

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.context.DefaultRootContextType;
import com.tdoer.bedrock.impl.definition.product.*;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import com.tdoer.springboot.util.LocaleUtil;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ProductBuilder {
    private DefaultProductRepository productRepository;

    private DefaultApplicationRepository applicationRepository;

    private DefaultServiceRepository serviceRepository;

    private DefaultRootContextType rootContextType;

    private ContextPathParser contextPathParser;

    private DefaultClientConfigCenter clientConfigCenter;

    public ProductBuilder(DefaultServiceRepository serviceRepository, DefaultApplicationRepository applicationRepository, DefaultRootContextType rootContextType, ContextPathParser contextPathParser, DefaultClientConfigCenter clientConfigCenter) {
        this.serviceRepository = serviceRepository;
        this.applicationRepository = applicationRepository;
        this.rootContextType = rootContextType;
        this.contextPathParser = contextPathParser;
        this.clientConfigCenter = clientConfigCenter;
    }

    public void setProductRepository(DefaultProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public DefaultProduct buildProduct(ProductDefinition definition){
        // todo check definition

        Locale defaultLanguage = LocaleUtil.getLocale(definition.getDefaultLanguage());
        String[] ls = StringUtils.delimitedListToStringArray(definition.getLanguages(), ",");
        Locale[] languages = new Locale[ls.length];
        for(int i=0; i<ls.length; i++){
            languages[i] = LocaleUtil.getLocale(ls[i].trim());
        }

        return new DefaultProduct(definition, defaultLanguage, languages, productRepository);
    }

    public DefaultClient buildClient(ClientDefinition definition){
        // todo check definition
        DefaultClientConfig clientConfig = new DefaultClientConfig(definition.getProductId(), definition.getId(), clientConfigCenter);
        return new DefaultClient(definition, clientConfig, productRepository);
    }

    public DefaultClientApplicationInstallation buildApplicationInstallation(ClientApplicationDefinition definition){
        // todo check defnition
        return new DefaultClientApplicationInstallation(definition, applicationRepository);
    }

    public DefaultClientService buildClientService(ClientServiceDefinition definition){
        // todo check definition
        return new DefaultClientService(definition, serviceRepository);
    }

    public DefaultContextInstallation buildContextInstallation(ClientContextDefinition definition){
        // todo check definition
        ContextPath contextPath = contextPathParser.parse(definition.getContextPath());
        return new DefaultContextInstallation(definition, contextPath);
    }

    public DefaultTokenConfig buildTokenConfig(ClientTokenDefinition definition){
        // todo check definition

        return new DefaultTokenConfig(definition);
    }
}
