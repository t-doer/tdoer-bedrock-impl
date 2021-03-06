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
package com.tdoer.bedrock.impl.application;

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ApplicationDomain;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_PAGES;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class PagesCacheManager extends AbstractCacheManager<ApplicationDomain, DefaultPage[]> {
    protected ApplicationLoader loader;
    protected Logger logger = LoggerFactory.getLogger(PagesCacheManager.class);

    public PagesCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ApplicationLoader loader) {
        super(cachePolicy, cleaner);

        Assert.notNull(loader, "ApplicationLoader cannot be null");
        this.loader = loader;
        logger.info("PagesCacheManager is initialized");
    }

    @Override
    protected DefaultPage[] loadSource(ApplicationDomain applicationDomain) throws ErrorCodeException {
        try{
            logger.info("Loading pages for the application domain {} ...", applicationDomain);
            DefaultPage[] ret = loader.loadPages(applicationDomain);
            logger.info("Loaded {} pages for the application domain {}.", ret.length, applicationDomain);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load pages for the application domain {}", applicationDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_PAGES, t, applicationDomain);
        }
    }

    @Override
    protected void destroySource(DefaultPage[] defaultPages) {
        // do nothing here
    }

    @Override
    protected DefaultPage[] reloadSource(ApplicationDomain applicationDomain, DefaultPage[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading pages for the application domain {} ...", applicationDomain);
            DefaultPage[] ret = loader.loadPages(applicationDomain);
            logger.info("Reloaded {} pages for the application domain {}.", ret.length, applicationDomain);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload pages for the application domain {}", applicationDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_PAGES, t, applicationDomain);
        }
    }
}
