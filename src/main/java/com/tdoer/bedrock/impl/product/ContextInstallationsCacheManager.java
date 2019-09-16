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
package com.tdoer.bedrock.impl.product;

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ProductDomain;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_CONTEXT_INSTALLATIONS;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextInstallationsCacheManager extends AbstractCacheManager<ProductDomain, DefaultContextInstallation[]> {
    private ProductLoader productLoader;

    public ContextInstallationsCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ProductLoader productLoader) {
        super(cachePolicy, cleaner);
        this.productLoader = productLoader;
    }

    @Override
    protected DefaultContextInstallation[] loadSource(ProductDomain domain) throws ErrorCodeException {
        try{
            logger.info("Loading context installations for the product domain {} ...", domain);
            DefaultContextInstallation[] ret = productLoader.loadContextInstallations(domain.getProductId(), domain.getClientId(), domain.getTenantId());
            logger.info("Loaded context installations for the product domain {}: {}", domain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load context installations for the product domain {}", domain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_INSTALLATIONS, t, domain);
        }
    }

    @Override
    protected void destroySource(DefaultContextInstallation[] defaultContextInstallations) {
        // do nothing
    }

    @Override
    protected DefaultContextInstallation[] reloadSource(ProductDomain domain, DefaultContextInstallation[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading context installations for the product domain {} ...", domain);
            DefaultContextInstallation[] ret = productLoader.loadContextInstallations(domain.getProductId(), domain.getClientId(), domain.getTenantId());
            logger.info("Reloaded context installations for the product domain {}: {}", domain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload context installations for the product domain {}", domain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_INSTALLATIONS, t, domain);
        }
    }
}
