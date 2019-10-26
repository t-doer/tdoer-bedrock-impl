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
import com.tdoer.bedrock.impl.definition.product.*;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import com.tdoer.springboot.util.LocaleUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ProductBuilder {
    private DefaultProductRepository productRepository;

    private DefaultApplicationRepository applicationRepository;

    private DefaultServiceRepository serviceRepository;

     private ContextPathParser contextPathParser;

    public ProductBuilder(DefaultServiceRepository serviceRepository, DefaultApplicationRepository applicationRepository, ContextPathParser contextPathParser) {
        this.serviceRepository = serviceRepository;
        this.applicationRepository = applicationRepository;
        this.contextPathParser = contextPathParser;
    }

    public void setProductRepository(DefaultProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public DefaultProduct buildProduct(ProductDefinition definition){
        // todo check definition

        String[] ls = StringUtils.delimitedListToStringArray(definition.getLanguages(), ",");
        Locale[] languages = new Locale[ls.length];
        for(int i=0; i<ls.length; i++){
            languages[i] = LocaleUtil.getLocale(ls[i].trim());
        }

        return new DefaultProduct(definition, languages, productRepository);
    }

    public DefaultClient buildClient(ClientDefinition definition){
        // todo check definition

        DefaultClientRole[] clientRoles = null;
        String[] arr = StringUtils.delimitedListToStringArray(definition.getRoles(), ",");
        if(arr != null){
            ArrayList<DefaultClientRole> list = new ArrayList<>(arr.length);
            for(String auth : arr){
                list.add(new DefaultClientRole(auth));
            }
            clientRoles = new DefaultClientRole[list.size()];
            list.toArray(clientRoles);
        }else{
            clientRoles = new DefaultClientRole[0];
        }

        DefaultClientConfig clientConfig = new DefaultClientConfig(definition.getId(), productRepository);

        return new DefaultClient(definition, clientRoles, clientConfig);
    }

    public DefaultClientApplicationInstallation buildApplicationInstallation(ClientApplicationDefinition definition){
        // todo check defnition
        return new DefaultClientApplicationInstallation(definition, applicationRepository);
    }

    public DefaultClientServiceInstallation buildClientServiceInstallation(ClientServiceDefinition definition){
        // todo check definition
        return new DefaultClientServiceInstallation(definition, serviceRepository);
    }

    public DefaultClientContextInstallation buildClientContextInstallation(ClientContextDefinition definition){
        // todo check definition
        ContextPath contextPath = contextPathParser.parse(definition.getContextPath());
        return new DefaultClientContextInstallation(definition, contextPath);
    }

    public DefaultTokenConfig buildTokenConfig(ClientTokenDefinition definition){
        // todo check definition

        return new DefaultTokenConfig(definition);
    }
}
