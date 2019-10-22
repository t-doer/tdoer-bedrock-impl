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
package com.tdoer.bedrock.impl.tenant;

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.product.ClientDomain;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_TENANT_CLIENT_OF_DOMAIN;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TenantClientCacheManagerByDomain extends AbstractCacheManager<ClientDomain, DefaultTenantClient> {

    private TenantLoader tenantLoader;

    public TenantClientCacheManagerByDomain(CachePolicy cachePolicy, DormantCacheCleaner cleaner, TenantLoader tenantLoader) {
        super(cachePolicy, cleaner);
        this.tenantLoader = tenantLoader;
    }

    @Override
    protected DefaultTenantClient loadSource(ClientDomain clientDomain) throws ErrorCodeException {
        try{
            logger.info("Loading tenant client for the client domain {} ...", clientDomain);
            DefaultTenantClient ret = tenantLoader.loadTenantClient(clientDomain.getClientId(), clientDomain.getTenantId());
            logger.info("Loaded tenant client for the client domain {}.", clientDomain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load tenant client for the client domain {}", clientDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_TENANT_CLIENT_OF_DOMAIN, t, clientDomain);
        }
    }

    @Override
    protected void destroySource(DefaultTenantClient defaultTenantClient) {
        // do nothing
    }

    @Override
    protected DefaultTenantClient reloadSource(ClientDomain clientDomain, DefaultTenantClient oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading tenant client for the client domain {} ...", clientDomain);
            DefaultTenantClient ret = tenantLoader.loadTenantClient(clientDomain.getClientId(), clientDomain.getTenantId());
            logger.info("Reloaded tenant client for the client domain {}.", clientDomain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload tenant client for the client domain {}", clientDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_TENANT_CLIENT_OF_DOMAIN, t, clientDomain);
        }
    }
}
