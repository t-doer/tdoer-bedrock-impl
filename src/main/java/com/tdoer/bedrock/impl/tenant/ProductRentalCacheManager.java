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
import com.tdoer.bedrock.impl.domain.ProductDomain;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_PRODUCT_RENTAL;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ProductRentalCacheManager extends AbstractCacheManager<ProductDomain, DefaultProductRental> {
    private TenantLoader tenantLoader;

    public ProductRentalCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, TenantLoader tenantLoader) {
        super(cachePolicy, cleaner);
        this.tenantLoader = tenantLoader;
    }

    @Override
    protected DefaultProductRental loadSource(ProductDomain domain) throws ErrorCodeException {
        try{
            logger.info("Loading product rental of product domain ({}) ...", domain);
            DefaultProductRental ret = tenantLoader.loadProductRendtal(domain.getProductId(), domain.getTenantId());
            logger.info("Loaded product rental of product domain ({}): {}", domain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load product rental of product domain ({})", domain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_PRODUCT_RENTAL, t, domain);
        }
    }

    @Override
    protected void destroySource(DefaultProductRental defaultProductRental) {
        // do nothing
    }

    @Override
    protected DefaultProductRental reloadSource(ProductDomain domain, DefaultProductRental oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading product rental of product domain ({}) ...", domain);
            DefaultProductRental ret = tenantLoader.loadProductRendtal(domain.getProductId(), domain.getTenantId());
            logger.info("Reloaded product rental of product domain ({}): {}", domain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload product rental of product domain ({})", domain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_PRODUCT_RENTAL, t, domain);
        }
    }
}
