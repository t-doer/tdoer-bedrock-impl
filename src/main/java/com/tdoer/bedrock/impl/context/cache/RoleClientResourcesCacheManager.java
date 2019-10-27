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
import com.tdoer.bedrock.impl.context.ContextRoleCacheKey;
import com.tdoer.bedrock.impl.product.DefaultClientResource;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_CONTEXT_APPLICATION_INSTALLATIONS;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class RoleClientResourcesCacheManager extends AbstractCacheManager<ContextRoleCacheKey,
        DefaultClientResource[]> {
    private ContextLoader contextLoader;
    private Logger logger = LoggerFactory.getLogger(RoleClientResourcesCacheManager.class);

    public RoleClientResourcesCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ContextLoader contextLoader) {
        super(cachePolicy, cleaner);
        Assert.notNull(contextLoader, "ContextLoader cannot be null");
        this.contextLoader = contextLoader;
        logger.info("RoleClientResourcesCacheManager is initialized");
    }

    @Override
    protected DefaultClientResource[] loadSource(ContextRoleCacheKey key) throws ErrorCodeException {
        try{
            logger.info("Loading client resources for the context role {} ...", key);
            DefaultClientResource[] ret =
                    contextLoader.loadRoleClientResources(key.getClientId(),
                            key.getTenantId(), key.getContextPath(), key.getRoleId());
            logger.info("Loaded {} client resources for the context role {}.", ret.length,
                    key);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load client resources for the context role {}", key, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_APPLICATION_INSTALLATIONS, t, key);
        }
    }

    @Override
    protected void destroySource(DefaultClientResource[] defaultContextApplicationInstallations) {
        // do nothing
    }

    @Override
    protected DefaultClientResource[] reloadSource(ContextRoleCacheKey key, DefaultClientResource[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading client resources for the context role {} ...", key);
            DefaultClientResource[] ret =
                    contextLoader.loadRoleClientResources(key.getClientId(),
                            key.getTenantId(), key.getContextPath(), key.getRoleId());
            logger.info("Reloaded {} client resources for the context role {}.", ret.length,
                    key);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload client resources for the context role {}", key, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_APPLICATION_INSTALLATIONS, t, key);
        }
    }
}
