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

import com.tdoer.bedrock.impl.application.ApplicationLoader;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.context.*;
import com.tdoer.bedrock.impl.product.DefaultClientConfigCenter;
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
import com.tdoer.bedrock.impl.product.ProductLoader;
import com.tdoer.bedrock.impl.provider.*;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import com.tdoer.bedrock.impl.service.ServiceLoader;
import com.tdoer.bedrock.impl.tenant.DefaultRentalCenter;
import com.tdoer.bedrock.impl.tenant.TenantLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */

@Configuration
@ComponentScan("com.tdoer.bedrock.impl.cache.admin.controller")
public class BeckrockAutoConfiguration {

    // Below services need be declared in application
    @Autowired
    protected ServiceProvider serviceProvider;

    @Autowired
    protected ApplicationProvider applicationProvider;

    @Autowired
    protected ContextProvider contextProvider;

    @Autowired
    protected ProductProvider productProvider;

    @Autowired
    protected TenantProvider tenantProvider;

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
    protected ContextTypeLoader contextTypeLoader(){
        return new ContextTypeLoader(contextProvider);
    }

    @Bean
    protected ServiceLoader serviceLoader(){
        return new ServiceLoader(serviceProvider);
    }

    @Bean
    protected ApplicationLoader applicationLoader(){
        return new ApplicationLoader(applicationProvider, defaultServiceRepository());
    }

    @Bean
    protected ContextConfigLoader contextConfigLoader(){
        return new ContextConfigLoader(contextProvider, defaultContextPathParser(), defaultApplicationRepository());
    }

    @Bean
    protected ContextInstanceLoader contextInstanceLoader(){
        return  new ContextInstanceLoader(contextProvider, defaultContextConfigCenter(), defaultContextTypeRoot());
    }

    @Bean
    protected ProductLoader productLoader(){
        return new ProductLoader(productProvider, defaultClientConfigCenter(), defaultServiceRepository(),  defaultApplicationRepository(), defaultContextTypeRoot(), defaultContextPathParser());
    }

    @Bean
    protected TenantLoader tenantLoader(){
        return new TenantLoader(tenantProvider, defaultProductRepository(), defaultContextTypeRoot(), defaultContextConfigCenter());
    }

    // -----------------------------------------------------------------------------------
    // Below public beans are needed by com.tdoer.bedrock.PlatformConfiguration
    // -----------------------------------------------------------------------------------
    @Bean
    public DefaultContextPathParser defaultContextPathParser(){
        return new DefaultContextPathParser();
    }

    @Bean
    public DefaultRootContextType defaultContextTypeRoot(){
        return new DefaultRootContextType(contextTypeLoader());
    }

    @Bean
    public DefaultServiceRepository defaultServiceRepository(){
        return new DefaultServiceRepository(serviceLoader(), cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    public DefaultApplicationRepository defaultApplicationRepository(){
        return new DefaultApplicationRepository(applicationLoader(), cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    public DefaultContextConfigCenter defaultContextConfigCenter(){
        return new DefaultContextConfigCenter(contextConfigLoader(), cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    public DefaultContextInstanceCenter defaultContextInstanceCenter(){
        return new DefaultContextInstanceCenter(contextInstanceLoader(), defaultContextTypeRoot(), defaultRentalCenter(), cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    public DefaultProductRepository defaultProductRepository(){
        return new DefaultProductRepository(productLoader(), cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    public DefaultClientConfigCenter defaultClientConfigCenter(){
        return new DefaultClientConfigCenter(productLoader(), cachePolicy(), dormantObjectCleaner());
    }

    @Bean
    public DefaultRentalCenter defaultRentalCenter(){
        return new DefaultRentalCenter(tenantLoader(), cachePolicy(), dormantObjectCleaner());
    }

}
