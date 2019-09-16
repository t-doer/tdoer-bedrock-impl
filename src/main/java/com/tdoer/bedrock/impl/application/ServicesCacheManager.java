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
package com.tdoer.bedrock.impl.application;

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ApplicationDomain;
import com.tdoer.bedrock.impl.service.DefaultService;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_SERVICES;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServicesCacheManager extends AbstractCacheManager<ApplicationDomain, DefaultService[]> {
    protected ApplicationLoader loader;
    protected Logger logger = LoggerFactory.getLogger(ServicesCacheManager.class);

    public ServicesCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ApplicationLoader loader) {
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "ApplicationLoader cannot be null");
        this.loader = loader;
        logger.info("ServicesCacheManager is initialized");
    }

    @Override
    protected DefaultService[] loadSource(ApplicationDomain applicationDomain) throws ErrorCodeException {
        try{
            logger.info("Loading services for the application domain {} ...", applicationDomain);
            DefaultService[] ret = loader.loadServices(applicationDomain);
            logger.info("Loaded {} services for the application domain {}.", ret.length, applicationDomain);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load services for the application domain {}", applicationDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICES, t, applicationDomain);
        }
    }

    @Override
    protected void destroySource(DefaultService[] defaultServices) {
        // do nothing here
    }

    @Override
    protected DefaultService[] reloadSource(ApplicationDomain applicationDomain, DefaultService[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading services for the application domain {} ...", applicationDomain);
            DefaultService[] ret = loader.loadServices(applicationDomain);
            logger.info("Reloaded {} services for the application domain {}.", ret.length, applicationDomain);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload services for the application domain {}", applicationDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICES, t, applicationDomain);
        }
    }
}
