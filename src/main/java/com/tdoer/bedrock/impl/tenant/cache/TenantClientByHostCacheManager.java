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

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_TENANT_CLIENT_FROM_HOST;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TenantClientByHostCacheManager extends AbstractCacheManager<String, DefaultTenantClient> {
    private TenantLoader tenantLoader;

    public TenantClientByHostCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, TenantLoader tenantLoader) {
        super(cachePolicy, cleaner);
        this.tenantLoader = tenantLoader;
    }

    @Override
    protected DefaultTenantClient loadSource(String host) throws ErrorCodeException {
        try{
            logger.info("Loading tenant client from host ({}) ...", host);
            DefaultTenantClient ret = tenantLoader.loadTenantClient(host);
            logger.info("Loaded tenant client from host ({}): {}", host, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load tenant client from host ({})", host, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_TENANT_CLIENT_FROM_HOST, t, host);
        }
    }

    @Override
    protected void destroySource(DefaultTenantClient tenantClient) {
        // do nothing
    }

    @Override
    protected DefaultTenantClient reloadSource(String host, DefaultTenantClient oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading tenant client from host ({}) ...", host);
            DefaultTenantClient ret = tenantLoader.loadTenantClient(host);
            logger.info("Reloaded tenant client from host ({}): {}", host, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload tenant client from host ({})", host, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_TENANT_CLIENT_FROM_HOST, t, host);
        }
    }
}
