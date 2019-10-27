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
import com.tdoer.bedrock.impl.context.UserRolesCacheKey;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_SERVICE_METHODS;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class RoleIdsCacheManager extends AbstractCacheManager<UserRolesCacheKey, Long[]> {
    protected ContextLoader loader;
    protected Logger logger = LoggerFactory.getLogger(RoleIdsCacheManager.class);

    public RoleIdsCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ContextLoader loader) {
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "ContextLoader cannot be null");
        this.loader = loader;
        logger.info("RoleIdsCacheManager is initialized");
    }

    @Override
    protected Long[] loadSource(UserRolesCacheKey cacheKey) throws ErrorCodeException {
        try{
            logger.info("Loading role Ids of user ({}) in the context ({}) ...", cacheKey.getUserId(), cacheKey.getContextPath());
            Long[] ret = loader.loadRoleIdsOfUser(cacheKey.getTenantId(), cacheKey.getContextPath(), cacheKey.getUserId());
            logger.info("Loaded {} role Ids of user ({}) in the context ({}).", ret.length, cacheKey.getUserId(), cacheKey.getContextPath());
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload role Ids of {}", cacheKey, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICE_METHODS, t, cacheKey);
        }
    }

    @Override
    protected void destroySource(Long[] defaultPages) {
        // do nothing here
    }

    @Override
    protected Long[] reloadSource(UserRolesCacheKey cacheKey, Long[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading role Ids of user ({}) in the context ({}) ...", cacheKey.getUserId(), cacheKey.getContextPath());
            Long[] ret = loader.loadRoleIdsOfUser(cacheKey.getTenantId(), cacheKey.getContextPath(), cacheKey.getUserId());
            logger.info("Reloaded {} role Ids of user ({}) in the context ({}).", ret.length, cacheKey.getUserId(), cacheKey.getContextPath());
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload role Ids of {}", cacheKey, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_SERVICE_METHODS, t, cacheKey);
        }
    }
}
