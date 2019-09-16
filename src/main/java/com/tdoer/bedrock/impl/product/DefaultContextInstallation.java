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
import com.tdoer.bedrock.product.ContextInstallation;
import com.tdoer.springboot.util.LocaleUtil;

import java.util.Locale;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextInstallation implements ContextInstallation {
    private ClientContextDefinition definition;

    private ContextPath contextPath;

    public DefaultContextInstallation(ClientContextDefinition definition, ContextPath contextPath) {
        this.definition = definition;
        this.contextPath = contextPath;
    }

    @Override
    public String getProductId() {
        return definition.getProductId();
    }

    @Override
    public String getClientId() {
        return definition.getClientId();
    }

    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    @Override
    public ContextPath getContextPath() {
        return contextPath;
    }

    @Override
    public String getEntryApplicationId() {
        return definition.getEntryApplicationId();
    }

    @Override
    public String getEntryNavItem() {
        return definition.getEntryNavItem();
    }

    @Override
    public Locale getEntryLanguage() {
        return LocaleUtil.getLocale(definition.getEntryLanguage());
    }
}
