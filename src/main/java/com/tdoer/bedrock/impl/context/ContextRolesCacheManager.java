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
package com.tdoer.bedrock.impl.context;

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ContextDomain;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_CONTEXT_ROLES;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextRolesCacheManager extends AbstractCacheManager<ContextDomain, DefaultContextRole[]> {
    private ContextConfigLoader contextConfigLoader;

    public ContextRolesCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ContextConfigLoader contextConfigLoader) {
        super(cachePolicy, cleaner);
        this.contextConfigLoader = contextConfigLoader;
    }

    @Override
    protected DefaultContextRole[] loadSource(ContextDomain contextDomain) throws ErrorCodeException {
        try{
            logger.info("Loading context roles for the context domain {} ...", contextDomain);
            ContextDomain cd = contextDomain;
            DefaultContextRole[] ret = contextConfigLoader.loadContextRoles(cd.getContextPath(), cd.getProductId(), cd.getClientId(), cd.getTenantId());
            logger.info("Loaded {} context roles for the context domain {}.", ret.length, contextDomain);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load context roles for the context domain {}", contextDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_ROLES, t, contextDomain);
        }
    }

    @Override
    protected void destroySource(DefaultContextRole[] defaultContextRoles) {
        // do nothing
    }

    @Override
    protected DefaultContextRole[] reloadSource(ContextDomain contextDomain, DefaultContextRole[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading context roles for the context domain {} ...", contextDomain);
            ContextDomain cd = contextDomain;
            DefaultContextRole[] ret = contextConfigLoader.loadContextRoles(cd.getContextPath(), cd.getProductId(), cd.getClientId(), cd.getTenantId());
            logger.info("Reloaded {} context roles for the context domain {}.", ret.length, contextDomain);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload context roles for the context domain {}", contextDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_ROLES, t, contextDomain);
        }
    }
}
