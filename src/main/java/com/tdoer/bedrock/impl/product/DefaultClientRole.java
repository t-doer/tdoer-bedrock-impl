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

import com.tdoer.bedrock.product.ClientRole;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientRole implements ClientRole {
    private String clientId;

    private String code;

    private String name;

    public DefaultClientRole(String clientId, String code, String name) {
        this.clientId = clientId;
        this.code = code;
        this.name = name;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    /**
     * If the <code>GrantedAuthority</code> can be represented as a <code>String</code>
     * and that <code>String</code> is sufficient in precision to be relied upon for an
     * access control decision by an {@link AccessDecisionManager} (or delegate), this
     * method should return such a <code>String</code>.
     * <p>
     * If the <code>GrantedAuthority</code> cannot be expressed with sufficient precision
     * as a <code>String</code>, <code>null</code> should be returned. Returning
     * <code>null</code> will require an <code>AccessDecisionManager</code> (or delegate)
     * to specifically support the <code>GrantedAuthority</code> implementation, so
     * returning <code>null</code> should be avoided unless actually required.
     *
     * @return a representation of the granted authority (or <code>null</code> if the
     * granted authority cannot be expressed as a <code>String</code> with sufficient
     * precision).
     */
    @Override
    public String getAuthority() {
        return getCode();
    }
}
