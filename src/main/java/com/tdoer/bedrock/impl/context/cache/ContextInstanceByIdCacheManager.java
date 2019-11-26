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
package com.tdoer.bedrock.impl.context.cache;

import com.tdoer.bedrock.context.ContextInstance;
import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.context.ContextInstanceIdCacheKey;
import com.tdoer.bedrock.impl.context.ContextLoader;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_APPLICATION;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextInstanceByIdCacheManager extends AbstractCacheManager<ContextInstanceIdCacheKey, ContextInstance> {
    protected ContextLoader loader;
    protected Logger logger = LoggerFactory.getLogger(ContextInstanceByIdCacheManager.class);

    public ContextInstanceByIdCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ContextLoader loader){
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "Context loader cannot be null");
        this.loader = loader;
        logger.info("ContextInstanceByIdCacheManager is initialized");
    }

    @Override
    protected ContextInstance loadSource(ContextInstanceIdCacheKey key) throws ErrorCodeException {
        try{
            logger.info("Loading context instance of Id: {} ...", key);
            ContextInstance ret = loader.loadContextInstance(key.getTenantId(), key.getContextType(),
                    key.getInstanceId());
            logger.info("Loaded context instance of Id: {} - {}", key, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch(Throwable t){
            logger.error("Failed to load context instance of Id: {}", key, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_APPLICATION, t, key);
        }
    }

    @Override
    protected void destroySource(ContextInstance defaultApplication) {
        // do nothing here
    }

    @Override
    protected ContextInstance reloadSource(ContextInstanceIdCacheKey key, ContextInstance oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading context instance of Id: {} ...", key);
            ContextInstance ret = loader.loadContextInstance(key.getTenantId(), key.getContextType(),
                    key.getInstanceId());
            logger.info("Reloaded context instance of Id: {} - {}", key, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch(Throwable t){
            logger.error("Failed to reload context instance of Id: {}", key, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_APPLICATION, t, key);
        }
    }
}