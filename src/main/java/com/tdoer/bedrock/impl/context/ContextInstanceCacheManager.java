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

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.springboot.error.ErrorCodeException;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_CONTEXT_INSTANCE;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextInstanceCacheManager extends AbstractCacheManager<ContextPath, DefaultContextInstance> {
    private ContextInstanceLoader instanceLoader;

    public ContextInstanceCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ContextInstanceLoader instanceLoader) {
        super(cachePolicy, cleaner);
        this.instanceLoader = instanceLoader;
    }

    @Override
    protected DefaultContextInstance loadSource(ContextPath contextPath) throws ErrorCodeException {
        try{
            logger.info("Loading context instance of context path ({}) ...", contextPath);
            DefaultContextInstance ret = instanceLoader.loadContextInstance(contextPath);
            logger.info("Loaded context instance of context path ({}): {}", contextPath, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load context instance of context path ({})", contextPath, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_INSTANCE, t, contextPath);
        }
    }

    @Override
    protected void destroySource(DefaultContextInstance defaultContextInstance) {
        // do nothing
    }

    @Override
    protected DefaultContextInstance reloadSource(ContextPath contextPath, DefaultContextInstance oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading context instance of context path ({}) ...", contextPath);
            DefaultContextInstance ret = instanceLoader.loadContextInstance(contextPath);
            logger.info("Reloaded context instance of context path ({}): {}", contextPath, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload context instance of context path ({})", contextPath, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_INSTANCE, t, contextPath);
        }
    }
}
