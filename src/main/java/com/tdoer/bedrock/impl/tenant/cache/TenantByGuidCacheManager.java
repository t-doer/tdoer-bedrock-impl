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
import com.tdoer.bedrock.impl.tenant.DefaultTenant;
import com.tdoer.bedrock.impl.tenant.TenantLoader;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_TENANT_OF_CODE;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TenantByGuidCacheManager extends AbstractCacheManager<String, DefaultTenant> {

    private TenantLoader tenantLoader;

    public TenantByGuidCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, TenantLoader tenantLoader) {
        super(cachePolicy, cleaner);
        this.tenantLoader = tenantLoader;
    }

    @Override
    protected DefaultTenant loadSource(String tenantGuid) throws ErrorCodeException {
        try{
            logger.info("Loading tenant of guid ({}) ...", tenantGuid);
            DefaultTenant ret = tenantLoader.loadTenantByCode(tenantGuid);
            logger.info("Loaded tenant of guid ({}): {}", tenantGuid, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t) {
            logger.error("Failed to load tenant of guid ({})", tenantGuid, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_TENANT_OF_CODE, t, tenantGuid);
        }
    }

    @Override
    protected void destroySource(DefaultTenant tenant) {
        // do nothing
    }

    @Override
    protected DefaultTenant reloadSource(String tenantGuid, DefaultTenant oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading tenant of guid ({}) ...", tenantGuid);
            DefaultTenant ret = tenantLoader.loadTenantByCode(tenantGuid);
            logger.info("Reloaded tenant of guid ({}): {}", tenantGuid, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t) {
            logger.error("Failed to reload tenant of code ({})", tenantGuid, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_TENANT_OF_CODE, t, tenantGuid);
        }
    }
}
