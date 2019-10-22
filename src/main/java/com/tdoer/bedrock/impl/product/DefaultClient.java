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

import com.tdoer.bedrock.impl.definition.product.ClientDefinition;
import com.tdoer.bedrock.product.Client;
import com.tdoer.bedrock.product.ClientCategory;
import com.tdoer.bedrock.product.ClientConfig;
import com.tdoer.bedrock.service.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClient implements Client {
    private ClientDefinition definition;
    private DefaultClientRole[] clientRoles;
    private DefaultClientConfig clientConfig;

    public DefaultClient(ClientDefinition definition, DefaultClientRole[] clientRoles,
                         DefaultClientConfig clientConfig) {
        Assert.notNull(definition, "Client definition cannot be null");
        Assert.notNull(clientRoles, "Client roles cannot be null");
        Assert.notNull(clientConfig, "Client config cannot be null");

        this.definition = definition;
        this.clientRoles = clientRoles;
        this.clientConfig = clientConfig;
    }

    /**
     * Client Id
     *
     * @return Client Id, must not be <code>null</code>
     */
    @Override
    public Long getId() {
        return definition.getId();
    }

    /**
     * Client code
     *
     * @return Client code, must not be blank
     */
    @Override
    public String getCode() {
        return definition.getCode();
    }

    /**
     * Client name
     *
     * @return Client name, must not be blank
     */
    @Override
    public String getName() {
        return definition.getName();
    }

    /**
     * The Id of the product to which the client belongs
     *
     * @return Product Id, must not be <code>null</code>
     */
    @Override
    public Long getProductId() {
        return definition.getProductId();
    }

    /**
     * Clent category, B_END or C_END
     *
     * @return Client category, must not be <code>null</code>
     */
    @Override
    public ClientCategory getCategory() {
        return ClientCategory.resolve(definition.getCategory());
    }

    /**
     * The client's access scopes
     *
     * @return The client's access scopes
     */
    @Override
    public String[] getScopes() {
        return StringUtils.delimitedListToStringArray(definition.getScopes(), ",");
    }

    /**
     * The client's roles that it can plays to services
     *
     * @return Client's roles
     */
    @Override
    public DefaultClientRole[] getRoles() {
        return Arrays.copyOf(clientRoles, clientRoles.length);
    }

    /**
     * Whether the client can be trusted or not. If a client program
     * is developed by 3rd-party, usually the client should not be trusted,
     * and conduct strict access policy
     *
     * @return true if the client is trusted
     */
    @Override
    public boolean isTrusted() {
        return "Y".equalsIgnoreCase(definition.getTrusted());
    }

    /**
     * Client configuration, for example, installed applications, services,
     * and access token configuration.
     *
     * @return Client configuration, must not be <code>null</code>
     */
    @Override
    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof Service){
            return this.getId().equals(((Client) obj).getId());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client[");
        sb.append(getId()).append(", ");
        sb.append(getProductId()).append(", ");
        sb.append(getCode()).append(", ");
        sb.append(getName()).append(", ");
        sb.append(getCategory()).append(", ");
        sb.append(isTrusted());
        sb.append("]");
        return sb.toString();
    }
}
