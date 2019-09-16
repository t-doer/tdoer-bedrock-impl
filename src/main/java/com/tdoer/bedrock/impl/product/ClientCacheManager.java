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
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_CLIENT;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ClientCacheManager extends AbstractCacheManager<String, DefaultClient> {
    private ProductLoader productLoader;

    public ClientCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ProductLoader productLoader) {
        super(cachePolicy, cleaner);
        this.productLoader = productLoader;
    }

    @Override
    protected DefaultClient loadSource(String clientId) throws ErrorCodeException {
        try{
            logger.info("Loading client of Id ({}) ...", clientId);
            DefaultClient ret = productLoader.loadClient(clientId);
            logger.info("Loaded client of Id ({}): {}", clientId, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load client of Id ({})", clientId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CLIENT, t, clientId);
        }
    }

    @Override
    protected void destroySource(DefaultClient defaultClient) {
        // do nothing
    }

    @Override
    protected DefaultClient reloadSource(String clientId, DefaultClient oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading client of Id ({}) ...", clientId);
            DefaultClient ret = productLoader.loadClient(clientId);
            logger.info("Reloaded client of Id ({}): {}", clientId, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload client of Id ({})", clientId, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CLIENT, t, clientId);
        }
    }
}
