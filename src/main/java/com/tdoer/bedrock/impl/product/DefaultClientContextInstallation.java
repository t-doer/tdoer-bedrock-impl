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

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.definition.product.ClientContextDefinition;
import com.tdoer.bedrock.product.ClientContextInstallation;
import com.tdoer.springboot.util.LocaleUtil;

import java.util.Locale;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientContextInstallation implements ClientContextInstallation {
    private ClientContextDefinition definition;

    private ContextPath contextPath;

    public DefaultClientContextInstallation(ClientContextDefinition definition, ContextPath contextPath) {
        this.definition = definition;
        this.contextPath = contextPath;
    }

    /**
     * The Id of the client in which the context is installed
     *
     * @return Client Id, it must not be <code>null</code>
     */
    @Override
    public Long getClientId() {
        return definition.getClientId();
    }

    /**
     * The Id of the tenant, in which the context is installed specifically
     *
     * @return Tenant Id, it must not be <code>null</code>, but it may be zero.
     */
    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    /**
     * Default entry application code
     *
     * @return Application code, it must not be blank
     */
    @Override
    public String getEntryApplicationCode() {
        return definition.getEntryAppCode();
    }

    /**
     * Default entry nav item
     *
     * @return Navigation item's node Id, it must not be blank
     */
    @Override
    public String getEntryNavItem() {
        return definition.getEntryNavItem();
    }

    /**
     * Default entry language
     *
     * @return Language, it must not be <code>null</code>
     */
    @Override
    public Locale getEntryLanguage() {
        return LocaleUtil.getLocale(definition.getEntryLanguage());
    }

    /**
     * The context type which is installed
     *
     * @return Context path of context type, must not be <code>null</code>
     */
    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }
}
