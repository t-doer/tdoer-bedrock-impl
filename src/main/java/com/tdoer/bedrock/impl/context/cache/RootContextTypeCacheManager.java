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

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.context.ContextLoader;
import com.tdoer.bedrock.impl.context.DefaultContextType;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_APPLICATION;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class RootContextTypeCacheManager extends AbstractCacheManager<Long, DefaultContextType> {
    protected ContextLoader loader;
    protected Logger logger = LoggerFactory.getLogger(RootContextTypeCacheManager.class);

    public RootContextTypeCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ContextLoader loader){
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "Context loader cannot be null");
        this.loader = loader;
        logger.info("RootContextTypeCacheManager is initialized");
    }

    @Override
    protected DefaultContextType loadSource(Long tenantId) throws ErrorCodeException {
        try{
            logger.info("Loading root context type of Id ({}) ...", tenantId);
            DefaultContextType ret = loader.loadRootContextType(tenantId);
            logger.info("Loaded root context type of Id ({}): {}", tenantId, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch(Throwable t){
            logger.error("Failed to load root context type of Id ({})", tenantId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_APPLICATION, t, tenantId);
        }
    }

    @Override
    protected void destroySource(DefaultContextType defaultApplication) {
        // do nothing here
    }

    @Override
    protected DefaultContextType reloadSource(Long tenantId, DefaultContextType oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading root context type of Id ({}) ...", tenantId);
            DefaultContextType ret = loader.loadRootContextType(tenantId);
            logger.info("Reloaded root context type of Id ({}): {}", tenantId, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch(Throwable t){
            logger.error("Failed to reload root context type of Id ({})", tenantId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_APPLICATION, t, tenantId);
        }
    }
}
