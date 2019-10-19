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
package com.tdoer.bedrock.impl.definition.product;

import java.io.Serializable;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ClientTokenDefinition implements Serializable {
    private Long clientId;

    private Long tenantId;

    private String grantTypes;

    private String autoApprovals;

    private String webRedirectURI;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String sessionPolicty;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getAutoApprovals() {
        return autoApprovals;
    }

    public void setAutoApprovals(String autoApprovals) {
        this.autoApprovals = autoApprovals;
    }

    public String getWebRedirectURI() {
        return webRedirectURI;
    }

    public void setWebRedirectURI(String webRedirectURI) {
        this.webRedirectURI = webRedirectURI;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getSessionPolicty() {
        return sessionPolicty;
    }

    public void setSessionPolicty(String sessionPolicty) {
        this.sessionPolicty = sessionPolicty;
    }
}
