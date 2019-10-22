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
package com.tdoer.bedrock.impl.product.cache;

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.product.DefaultProduct;
import com.tdoer.bedrock.impl.product.ProductLoader;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_PRODUCT;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ProductByCodeCacheManager extends AbstractCacheManager<String, DefaultProduct> {

    private ProductLoader productLoader;

    public ProductByCodeCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ProductLoader productLoader) {
        super(cachePolicy, cleaner);

        this.productLoader = productLoader;
    }

    @Override
    protected DefaultProduct loadSource(String productCode) throws ErrorCodeException {
        try{
            logger.info("Loading product of code ({}) ...", productCode);
            DefaultProduct ret = productLoader.loadProduct(productCode);
            logger.info("Loaded product of code ({}): {}", productCode, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load product of code ({})", productCode, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_PRODUCT, t, productCode);
        }
    }

    @Override
    protected void destroySource(DefaultProduct defaultProduct) {
        // do nothing
    }

    @Override
    protected DefaultProduct reloadSource(String productCode, DefaultProduct oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading product of code ({}) ...", productCode);
            DefaultProduct ret = productLoader.loadProduct(productCode);
            logger.info("Reloaded product of code ({}): {}", productCode, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload product of code ({})", productCode, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_PRODUCT, t, productCode);
        }
    }
}
