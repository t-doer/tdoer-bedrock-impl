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
package com.tdoer.bedrock.impl.service;

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ServiceDomain;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_SERVICE_METHODS;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceMethodsCacheManager extends AbstractCacheManager<ServiceDomain, DefaultServiceMethod[]> {
    protected ServiceLoader loader;
    protected Logger logger = LoggerFactory.getLogger(ServiceMethodsCacheManager.class);

    public ServiceMethodsCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ServiceLoader loader) {
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "ServiceLoader cannot be null");
        this.loader = loader;
        logger.info("PagesCacheManager is initialized");
    }

    @Override
    protected DefaultServiceMethod[] loadSource(ServiceDomain serviceDomain) throws ErrorCodeException {
        try{
            logger.info("Loading service methods for the service domain {} ...", serviceDomain);
            DefaultServiceMethod[] ret = loader.loadServiceMethods(serviceDomain);
            logger.info("Loaded {} service methods for the service domain {}.", ret.length, serviceDomain);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load service methods for the service domain {}", serviceDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICE_METHODS, t, serviceDomain);
        }
    }

    @Override
    protected void destroySource(DefaultServiceMethod[] defaultPages) {
        // do nothing here
    }

    @Override
    protected DefaultServiceMethod[] reloadSource(ServiceDomain serviceDomain, DefaultServiceMethod[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading service methods for the service domain {} ...", serviceDomain);
            DefaultServiceMethod[] ret = loader.loadServiceMethods(serviceDomain);
            logger.info("Reloaded {} service methods for the service domain {}.", ret.length, serviceDomain);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload service methods for the service domain {}", serviceDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICE_METHODS, t, serviceDomain);
        }
    }
}
