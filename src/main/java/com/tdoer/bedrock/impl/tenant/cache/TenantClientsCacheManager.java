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
package com.tdoer.bedrock.impl.tenant.cache;

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.tenant.DefaultTenantClient;
import com.tdoer.bedrock.impl.tenant.TenantLoader;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_CLIENT_IDS_OF_TENANT;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TenantClientsCacheManager extends AbstractCacheManager<Long, DefaultTenantClient[]> {
    private TenantLoader tenantLoader;

    public TenantClientsCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, TenantLoader tenantLoader) {
        super(cachePolicy, cleaner);
        this.tenantLoader = tenantLoader;
    }

    @Override
    protected DefaultTenantClient[] loadSource(Long tenantId) throws ErrorCodeException {
        try{
            logger.info("Loading tenant clients of tenant ({}) ...", tenantId);
            DefaultTenantClient[] ret = tenantLoader.loadTenantClients(tenantId);
            logger.info("Loaded {} tenant clients of tenant ({}): {}", ret.length, tenantId);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load tenant clients of tenant ({})", tenantId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CLIENT_IDS_OF_TENANT, t, tenantId);
        }
    }

    @Override
    protected void destroySource(DefaultTenantClient[] strings) {
        // do nothing
    }

    @Override
    protected DefaultTenantClient[] reloadSource(Long tenantId, DefaultTenantClient[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading tenant clients of tenant ({}) ...", tenantId);
            DefaultTenantClient[] ret = tenantLoader.loadTenantClients(tenantId);
            logger.info("Reloaded {} tenant clients of tenant ({}): {}", ret.length, tenantId);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload tenant clients of tenant ({})", tenantId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CLIENT_IDS_OF_TENANT, t, tenantId);
        }
    }
}
