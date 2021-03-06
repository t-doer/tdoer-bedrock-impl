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

import com.tdoer.bedrock.impl.cache.AbstractCacheManager;
import com.tdoer.bedrock.impl.cache.CachePolicy;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.domain.ContextDomain;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static com.tdoer.bedrock.impl.BedrockImplErrorCodes.FAILED_TO_LOAD_CONTEXT_APPLICATION_INSTALLATIONS;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextApplicationsInstallationCacheManager extends AbstractCacheManager<ContextDomain, DefaultContextApplicationInstallation[]> {
    private ContextConfigLoader contextConfigLoader;
    private Logger logger = LoggerFactory.getLogger(ContextApplicationsInstallationCacheManager.class);

    public ContextApplicationsInstallationCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner, ContextConfigLoader contextConfigLoader) {
        super(cachePolicy, cleaner);
        Assert.notNull(contextConfigLoader, "ApplicationLoader cannot be null");
        this.contextConfigLoader = contextConfigLoader;
        logger.info("ContextApplicationInstallationCacheManager is initialized");
    }

    @Override
    protected DefaultContextApplicationInstallation[] loadSource(ContextDomain contextDomain) throws ErrorCodeException {
        try{
            logger.info("Loading context application installations for the context domain {} ...", contextDomain);
            DefaultContextApplicationInstallation[] ret = contextConfigLoader.loadApplicationInstallations(contextDomain.getContextPath(), contextDomain.getProductId(), contextDomain.getClientId(), contextDomain.getTenantId());
            logger.info("Loaded context application installations for the context domain {}.", contextDomain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to load context application installations for the context domain {}", contextDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_APPLICATION_INSTALLATIONS, t, contextDomain);
        }
    }

    @Override
    protected void destroySource(DefaultContextApplicationInstallation[] defaultContextApplicationInstallations) {
        // do nothing
    }

    @Override
    protected DefaultContextApplicationInstallation[] reloadSource(ContextDomain contextDomain, DefaultContextApplicationInstallation[] oldSource) throws ErrorCodeException {
        try{
            logger.info("Reloading context application installations for the context domain {} ...", contextDomain);
            DefaultContextApplicationInstallation[] ret = contextConfigLoader.loadApplicationInstallations(contextDomain.getContextPath(), contextDomain.getProductId(), contextDomain.getClientId(), contextDomain.getTenantId());
            logger.info("Reloaded context application installations for the context domain {}.", contextDomain, ret);
            return ret;
        } catch (ErrorCodeException ece) {
            throw ece;
        } catch (Throwable t){
            logger.error("Failed to reload context application installation for the context domain {}", contextDomain, t);
            throw new ErrorCodeException(FAILED_TO_LOAD_CONTEXT_APPLICATION_INSTALLATIONS, t, contextDomain);
        }
    }
}
