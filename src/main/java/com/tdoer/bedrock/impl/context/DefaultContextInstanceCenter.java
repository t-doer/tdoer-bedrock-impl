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

import com.tdoer.bedrock.context.ContextInstance;
import com.tdoer.bedrock.context.ContextInstanceCenter;
import com.tdoer.bedrock.context.ContextInstanceNotFoundException;
import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.tenant.DefaultRentalCenter;
import org.springframework.util.Assert;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextInstanceCenter implements ContextInstanceCenter {

    private ContextInstanceCacheManager  cacheManager;

    private DefaultRootContextType rootContextType;

    private DefaultRentalCenter rentalCenter;

    public DefaultContextInstanceCenter(ContextInstanceLoader instanceLoader, DefaultRootContextType rootContextType, DefaultRentalCenter rentalCenter, CachePolicy cachePolicy, DormantCacheCleaner cleaner) {
        Assert.notNull(instanceLoader, "InstanceLoader cannot be null");
        Assert.notNull(cachePolicy, "CachePolicy cannot be null");
        Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");

        this.cacheManager = new ContextInstanceCacheManager(cachePolicy, cleaner, instanceLoader);
        this.rootContextType = rootContextType;
        this.rentalCenter = rentalCenter;

        // Initialize cache manager
        cacheManager.initialize();
    }

    /**
     * Get context instance of specific context path in specific tenant
     *
     * @param tenantId    Tenant Id, cannot be <code>null</code>
     * @param contextPath Context path, cannot be <code>null</code>
     * @return Context instance if it exists and is enabled
     * @throws ContextInstanceNotFoundException if it is not found
     */
    @Override
    public ContextInstance getContextInstance(Long tenantId, ContextPath contextPath) throws ContextInstanceNotFoundException {
        if(contextPath.getType().equals(rootContextType.getRoot().getType())){
            // root context type, the context instance must be a tenant
            return rentalCenter.getTenant(contextPath.getInstanceId());
        }

        return cacheManager.getSource(contextPath);
    }
}
