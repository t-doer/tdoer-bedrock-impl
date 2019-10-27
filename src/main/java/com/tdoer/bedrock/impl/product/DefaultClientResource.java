/*
 * Copyright 2019 T-Doer (tdoer.com).
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
 *
 */
package com.tdoer.bedrock.impl.product;

import com.tdoer.bedrock.product.ClientResource;
import com.tdoer.bedrock.resource.ResourceType;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-10-28
 */
public class DefaultClientResource implements ClientResource {
    private Long clientId;

    private Long id;

    private ResourceType type;

    public DefaultClientResource(Long clientId, Long resourceId, ResourceType resourceType) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(resourceId, "Resource Id cannot be null");
        Assert.notNull(resourceType, "Resource type cannot be null");

        this.clientId = clientId;
        this.id = resourceId;
        this.type = resourceType;
    }

    /**
     * The Id of the client in which the resource is defined
     *
     * @return Client Id, must not be <code>null</code>
     */
    @Override
    public Long getClientId() {
        return clientId;
    }

    /**
     * Resource Id
     *
     * @return Resource Id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Resource type
     *
     * @return Resource type
     */
    @Override
    public ResourceType getType() {
        return type;
    }
}
