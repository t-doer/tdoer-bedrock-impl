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
package com.tdoer.bedrock.impl.autoconfigure;

import com.tdoer.bedrock.impl.BedrockImplErrorCodes;
import com.tdoer.bedrock.impl.application.ApplicationLoader;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.context.ContextLoader;
import com.tdoer.bedrock.impl.context.DefaultContextCenter;
import com.tdoer.bedrock.impl.context.DefaultContextPathParser;
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
import com.tdoer.bedrock.impl.product.ProductLoader;
import com.tdoer.bedrock.impl.provider.*;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import com.tdoer.bedrock.impl.service.ServiceLoader;
import com.tdoer.bedrock.impl.tenant.DefaultRentalCenter;
import com.tdoer.bedrock.impl.tenant.TenantLoader;
import com.tdoer.springboot.util.StatusCodeUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Bedrock auto configuration needs the beans:
 * <p>
 *     <ol>{@link com.tdoer.bedrock.impl.provider.ServiceProvider}</ol>
 *     <ol>{@link com.tdoer.bedrock.impl.provider.ApplicationProvider}</ol>
 *     <ol>{@link com.tdoer.bedrock.impl.provider.ProductProvider}</ol>
 *     <ol>{@link com.tdoer.bedrock.impl.provider.TenantProvider}</ol>
 *     <ol>{@link com.tdoer.bedrock.impl.provider.ContextProvider}</ol>
 * </p>
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */

@Configuration
@ComponentScan("com.tdoer.bedrock.impl.cache.admin.controller")
public class BedrockImplAutoConfiguration implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        // Register status codes
        StatusCodeUtil.registerStatusCodes(BedrockImplErrorCodes.class);
    }

    // -----------------------------------------------------------------------------------
    // Cache and Loaders
    // -----------------------------------------------------------------------------------
    @Bean
    protected CachePolicy cachePolicy(){
        return new CachePolicy(2*60*1000,5*60*1000,30*60*1000);
    }

    @Bean
    protected DormantCacheCleaner dormantObjectCleaner(){
        return new DormantCacheCleaner(cachePolicy());
    }

    @Bean
    protected ServiceLoader serviceLoader(ServiceProvider serviceProvider){
        return new ServiceLoader(serviceProvider);
    }

    @Bean
    public DefaultServiceRepository defaultServiceRepository(ServiceLoader serviceLoader){
        return new DefaultServiceRepository(serviceLoader, cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    protected ApplicationLoader applicationLoader(ApplicationProvider applicationProvider,
                                                  DefaultServiceRepository defaultServiceRepository){
        return new ApplicationLoader(applicationProvider, defaultServiceRepository);
    }

    @Bean
    public DefaultApplicationRepository defaultApplicationRepository(ApplicationLoader applicationLoader){
        return new DefaultApplicationRepository(applicationLoader, cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    public DefaultContextPathParser defaultContextPathParser(){
        return new DefaultContextPathParser();
    }

    @Bean
    protected ProductLoader productLoader(ProductProvider productProvider,
                                          DefaultServiceRepository defaultServiceRepository,
                                          DefaultApplicationRepository defaultApplicationRepository,
                                          DefaultContextPathParser defaultContextPathParser){
        return new ProductLoader(productProvider, defaultServiceRepository,  defaultApplicationRepository,  defaultContextPathParser);
    }

    @Bean
    public DefaultProductRepository defaultProductRepository(ProductLoader productLoader){
        return new DefaultProductRepository(productLoader, cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    protected ContextLoader contextLoader(ContextProvider contextProvider,
                                          DefaultContextPathParser defaultContextPathParser,
                                          DefaultApplicationRepository defaultApplicationRepository,
                                          DefaultServiceRepository defaultServiceRepository){
        return new ContextLoader(contextProvider, defaultContextPathParser, defaultApplicationRepository, defaultServiceRepository);
    }

    @Bean
    public DefaultContextCenter defaultContextCenter(ContextLoader contextLoader){
        return new DefaultContextCenter(contextLoader, cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    protected TenantLoader tenantLoader(TenantProvider tenantProvider,
                                        DefaultProductRepository defaultProductRepository,
                                        DefaultContextCenter defaultContextCenter){
        return new TenantLoader(tenantProvider, defaultProductRepository,  defaultContextCenter);
    }

    @Bean
    public DefaultRentalCenter defaultRentalCenter(TenantLoader tenantLoader){
        return new DefaultRentalCenter(tenantLoader, cachePolicy(), dormantObjectCleaner());
    }

}
