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

import com.tdoer.bedrock.impl.definition.product.ClientTokenDefinition;
import com.tdoer.bedrock.product.SessionPolicy;
import com.tdoer.bedrock.product.TokenConfig;
import org.springframework.util.StringUtils;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultTokenConfig implements TokenConfig {

    private ClientTokenDefinition definition;

    public DefaultTokenConfig(ClientTokenDefinition definition) {
        this.definition = definition;
    }

    @Override
    public String[] getWebRedirectURIs() {
        return StringUtils.delimitedListToStringArray(definition.getWebRedirectURI(),",");
    }

    @Override
    public Integer getAccessTokenValidityInSeconds() {
        return definition.getAccessTokenValidity();
    }

    @Override
    public Integer getRefreshTokenValidityInSeconds() {
        return definition.getRefreshTokenValidity();
    }

    @Override
    public SessionPolicy getSessionPolicy() {
        return SessionPolicy.resolve(definition.getSessionPolicty());
    }
}
