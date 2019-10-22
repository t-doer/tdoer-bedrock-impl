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
import com.tdoer.bedrock.impl.product.DefaultClient;
import com.tdoer.bedrock.impl.product.ProductLoader;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_CLIENT;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ClientsCacheManager extends AbstractCacheManager<Long, DefaultClient[]> {
    private ProductLoader productLoader;

    public ClientsCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ProductLoader productLoader) {
        super(cachePolicy, cleaner);
        this.productLoader = productLoader;
    }

    @Override
    protected DefaultClient[] loadSource(Long productId) throws ErrorCodeException {
        try{
            logger.info("Loading clients of product Id ({}) ...", productId);
            DefaultClient[] ret = productLoader.loadClients(productId);
            logger.info("Loaded {} clients of product Id ({})", ret.length, productId);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load clients of product Id ({})", productId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CLIENT, t, productId);
        }
    }

    @Override
    protected void destroySource(DefaultClient[] defaultClient) {
        // do nothing
    }

    @Override
    protected DefaultClient[] reloadSource(Long productId, DefaultClient[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading clients of product Id ({}) ...", productId);
            DefaultClient[] ret = productLoader.loadClients(productId);
            logger.info("Reloaded {} clients of product Id ({})", ret.length, productId);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload clients of product Id ({})", productId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CLIENT, t, productId);
        }
    }
}
