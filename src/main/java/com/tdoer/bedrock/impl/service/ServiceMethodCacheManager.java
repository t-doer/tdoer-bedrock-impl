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
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_SERVICE_METHOD;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceMethodCacheManager extends AbstractCacheManager<Long, DefaultServiceMethod> {
    protected ServiceLoader loader;
    protected Logger logger = LoggerFactory.getLogger(ServiceMethodCacheManager.class);

    public ServiceMethodCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ServiceLoader loader){
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "ServiceLoader cannot be null");
        this.loader = loader;
        logger.info("ServiceMethodCacheManager is initialized");
    }

    @Override
    protected DefaultServiceMethod loadSource(Long methodId) throws ErrorCodeException {
        try{
            logger.info("Loading method of Id ({}) ...", methodId);
            DefaultServiceMethod ret = loader.loadServiceMethod(methodId);
            logger.info("Loaded method of Id ({}): {}", methodId, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load method of Id ({})", methodId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICE_METHOD, t, methodId);
        }
    }

    @Override
    protected void destroySource(DefaultServiceMethod defaultService) {
        // do nothing here
    }

    @Override
    protected DefaultServiceMethod reloadSource(Long methodId, DefaultServiceMethod oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading method of Id ({}) ...", methodId);
            DefaultServiceMethod ret = loader.loadServiceMethod(methodId);
            logger.info("Reloaded method of Id ({}): {}", methodId, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload method of Id ({})", methodId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICE_METHOD, t, methodId);
        }
    }
}
