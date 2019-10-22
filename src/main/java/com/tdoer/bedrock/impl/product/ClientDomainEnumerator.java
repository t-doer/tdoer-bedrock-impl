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

import org.springframework.util.Assert;

import java.util.Enumeration;

/**
 * <p>Service extension domain enumerator.</p>
 * <p>
 * Suppose current cloud environment is [client: tenantId]
 * is [1: 1], client domain enumerator will enumerate the client extension domains:
 * <ol>
 *     <li>[1: 1]</li>
 *     <li>[1: 0]</li>
 * </ol>
 * </p>
 * <p>
 * The last domain is a special extension domain, which means no extension at all.
 * </p>
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-10-20
 */
public class ClientDomainEnumerator implements Enumeration<ClientDomain> {
    
    protected final Long clientId;
    protected final Long tenantId;

    // Elements
    private static final int CLIENT = 0;
    private static final int TENANT = 1;
    // Status
    private byte[] markers = new byte[2];

    // Current values
    protected Long tenId;

    /**
     * ClientDomainEnumerator constructor.
     *
     * @param clientId Client Id must not be <code>null</code>, and larger than zero
     * @param tenantId Tenant Id must not be <code>null</code>, and larger than zero
     */
    public ClientDomainEnumerator(Long clientId, Long tenantId) {
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id Id cannot be null");
        Assert.isTrue(clientId > 0, "Client Id must larger than zero");
        Assert.isTrue(tenantId > 0, "Tenant Id must larger than zero");
        
        this.clientId = clientId;
        this.tenantId = tenantId;
        
        markers[CLIENT] = 1;
        markers[TENANT] = 1;

        tenId = tenantId;
    }

    @Override
    public boolean hasMoreElements() {
        if(markers[CLIENT] == 0){
            return false;
        }

        // set values
        if(markers[TENANT] == 1){
            tenId = tenantId;
        }else{
            tenId = 0L;
        }

        return true;
    }

    @Override
    public ClientDomain nextElement() {
        ClientDomain ret = null;
        if(markers[CLIENT] != 0) {
            ret = new ClientDomain(clientId, tenId);

            // move status forward
            if (markers[TENANT] == 1) {
                markers[TENANT] = 0;
            } else {
                // reach end
                markers[CLIENT] = 0;
            }

        }
        return ret;
    }

}
