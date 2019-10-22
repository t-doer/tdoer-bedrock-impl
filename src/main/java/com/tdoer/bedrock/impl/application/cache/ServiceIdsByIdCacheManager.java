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
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_SERVICES;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceIdsByIdCacheManager extends AbstractCacheManager<Long, Long[]> {
    protected ApplicationLoader loader;
    protected Logger logger = LoggerFactory.getLogger(ServiceIdsByIdCacheManager.class);

    public ServiceIdsByIdCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ApplicationLoader loader) {
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "ApplicationLoader cannot be null");
        this.loader = loader;
        logger.info("ServiceIdsByIdCacheManager is initialized");
    }

    @Override
    protected Long[] loadSource(Long applicationId) throws ErrorCodeException {
        try{
            logger.info("Loading service Ids for the application Id {} ...", applicationId);
            Long[] ret = loader.loadAllRefereeServiceIds(applicationId);
            logger.info("Loaded {} service Ids for the application Id {}.", ret.length, applicationId);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load service Ids for the application Id {}", applicationId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICES, t, applicationId);
        }
    }

    @Override
    protected void destroySource(Long[] defaultServices) {
        // do nothing here
    }

    @Override
    protected Long[] reloadSource(Long applicationId, Long[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading service Ids for the application Id {} ...", applicationId);
            Long[] ret = loader.loadAllRefereeServiceIds(applicationId);
            logger.info("Reloaded {} service Ids for the application Id {}.", ret.length, applicationId);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload service Ids for the application Id {}", applicationId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICES, t, applicationId);
        }
    }
}
