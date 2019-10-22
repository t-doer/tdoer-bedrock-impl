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
package com.tdoer.bedrock.impl.application.cache;

import com.tdoer.bedrock.impl.application.ApplicationLoader;
import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_SERVICES;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class MethodsByActionIdCacheManager extends AbstractCacheManager<Long, DefaultServiceMethod[]> {
    protected ApplicationLoader loader;
    protected Logger logger = LoggerFactory.getLogger(MethodsByActionIdCacheManager.class);

    public MethodsByActionIdCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ApplicationLoader loader) {
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "ApplicationLoader cannot be null");
        this.loader = loader;
        logger.info("MethodsByActionIdCacheManager is initialized");
    }

    @Override
    protected DefaultServiceMethod[] loadSource(Long actionId) throws ErrorCodeException {
        try{
            logger.info("Loading service methods for the action Id: {} ...", actionId);
            DefaultServiceMethod[] ret = loader.loadServiceMethodsOfAction(actionId);
            logger.info("Loaded {} service methods for the action Id: {}.", ret.length, actionId);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load service methods for the action Id: {}", actionId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICES, t, actionId);
        }
    }

    @Override
    protected void destroySource(DefaultServiceMethod[] serviceMethods) {
        // do nothing here
    }

    @Override
    protected DefaultServiceMethod[] reloadSource(Long actionId, DefaultServiceMethod[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading service methods for the action Id: {} ...", actionId);
            DefaultServiceMethod[] ret = loader.loadServiceMethodsOfAction(actionId);
            logger.info("Reloaded {} service methods for the action Id: {}.", ret.length, actionId);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload service Ids for the application: Id {}", actionId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICES, t, actionId);
        }
    }
}
