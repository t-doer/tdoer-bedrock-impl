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
import com.tdoer.bedrock.impl.product.ClientDomain;
import com.tdoer.bedrock.impl.product.DefaultTokenConfig;
import com.tdoer.bedrock.impl.product.ProductLoader;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_TOKEN_CONFIG;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TokenConfigCacheManager extends AbstractCacheManager<ClientDomain, DefaultTokenConfig> {
    private ProductLoader productLoader;

    public TokenConfigCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ProductLoader productLoader) {
        super(cachePolicy, cleaner);
        this.productLoader = productLoader;
    }

    @Override
    protected DefaultTokenConfig loadSource(ClientDomain clientDomain) throws ErrorCodeException {
        try{
            logger.info("Loading token config for the client domain {} ...", clientDomain);
            DefaultTokenConfig ret = productLoader.loadTokenConfig(clientDomain);
            logger.info("Loaded token config for the client domain {}.", clientDomain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load token config for the client domain {}", clientDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_TOKEN_CONFIG, t, clientDomain);
        }
    }

    @Override
    protected void destroySource(DefaultTokenConfig defaultTokenConfig) {
        // do nothing
    }

    @Override
    protected DefaultTokenConfig reloadSource(ClientDomain clientDomain, DefaultTokenConfig oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading token config for the client domain {} ...", clientDomain);
            DefaultTokenConfig ret = productLoader.loadTokenConfig(clientDomain);
            logger.info("Reloaded token config for the client domain {}.", clientDomain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload token config for the client domain {}", clientDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_TOKEN_CONFIG, t, clientDomain);
        }
    }
}
