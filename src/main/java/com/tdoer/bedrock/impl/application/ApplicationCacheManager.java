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
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_APPLICATION;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ApplicationCacheManager extends AbstractCacheManager<String, DefaultApplication> {
    protected ApplicationLoader loader;
    protected Logger logger = LoggerFactory.getLogger(ApplicationCacheManager.class);

    public ApplicationCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ApplicationLoader loader){
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "ApplicationLoader cannot be null");
        this.loader = loader;
        logger.info("ApplicationCacheManagerByID is initialized");
    }

    @Override
    protected DefaultApplication loadSource(String applicationId) throws ErrorCodeException {
        try{
            logger.info("Loading application of Id ({}) ...", applicationId);
            DefaultApplication ret = loader.loadApplication(applicationId);
            logger.info("Loaded application of Id ({}): {}", applicationId, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch(Throwable t){
            logger.error("Failed to load application of Id ({})", applicationId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_APPLICATION, t, applicationId);
        }
    }

    @Override
    protected void destroySource(DefaultApplication defaultApplication) {
        logger.info("ApplicationCacheManagerByID is destroyed");
        // do nothing here
    }

    @Override
    protected DefaultApplication reloadSource(String applicationId, DefaultApplication oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading application of Id ({}) ...", applicationId);
            DefaultApplication ret = loader.loadApplication(applicationId);
            logger.info("Reloaded application of Id ({}): {}", applicationId, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch(Throwable t){
            logger.error("Failed to reload application of Id ({})", applicationId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_APPLICATION, t, applicationId);
        }
    }
}
