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

    /**
     * Client Id
     *
     * @return Client Id, must not be <code>null</code>
     */
    @Override
    public Long getClientId() {
        return definition.getClientId();
    }

    /**
     * Tenant Id
     *
     * @return Tenant Id, must not be <code>null</code>
     */
    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    /**
     * The client's grant types, for example, authorization_code, password etc.
     *
     * @return token grant type, must not be empty
     */
    @Override
    public String[] getGrantTypes() {
        return StringUtils.commaDelimitedListToStringArray(definition.getGrantTypes());
    }

    /**
     * The client's auto approval's scope
     *
     * @return Auto approval, must not be empty
     */
    @Override
    public String[] getAutoApprovals() {
        return StringUtils.commaDelimitedListToStringArray(definition.getAutoApprovals());
    }

    /**
     * The redirection URI once user's access token is granted
     *
     * @return Redirection URI, may be blank
     */
    @Override
    public String getWebRedirectURI() {
        return definition.getWebRedirectURI();
    }

    /**
     * Access token's validity duration in seconds.
     *
     * @return Validity duration, must not be <code>null</code>
     */
    @Override
    public Integer getAccessTokenValidityInSeconds() {
        return definition.getAccessTokenValidity();
    }

    /**
     * Refresh token's validity duration in seconds.
     *
     * @return Validity duration, must not be <code>null</code>
     */
    @Override
    public Integer getRefreshTokenValidityInSeconds() {
        return definition.getRefreshTokenValidity();
    }

    /**
     * Session policy
     *
     * @return Session policy, must not be <code>null</code>
     */
    @Override
    public SessionPolicy getSessionPolicy() {
        return SessionPolicy.resolve(definition.getSessionPolicty());
    }
}
