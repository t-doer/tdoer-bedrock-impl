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
package com.tdoer.bedrock.impl.application;

import com.tdoer.bedrock.context.ContextPath;
import org.springframework.util.Assert;

import java.util.Enumeration;

/**
 * <p>Page extension domain enumerator.</p>
 * <p>
 * Suppose current cloud environment is [pageId: productId, clientId, tenantId, contextPath]
 * is [1: 1, 1, 1, 1.1-2.1], application domain enumerator will enumerate the page extension domains:
 * <ol>
 *     <li>[1:  1, 1, 1, 1.1-2.1]</li>
 *     <li>[1:  1, 1, 1, 1.1-2.0]</li>
 *     <li>[1:  1, 1, 1, 1.0-2.0]</li>
 *     <li>[1:  1, 1, 0, 1.0-2.0]</li>
 *     <li>[1:  1, 0, 0, 1.0-2.0]</li>
 *     <li>[1:  0, 0, 0, 1.0-2.0]</li>
 *     <li>[1:  1, 1, 1, null]</li>
 *     <li>[1:  1, 1, 0, null]</li>
 *     <li>[1:  1, 0, 0, null]</li>
 *     <li>[1:  0, 0, 0, null]</li>
 * </ol>
 * </p>
 * <p>
 * The last domain is a special extension domain, which means no extension at all.
 * </p>
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-10-20
 */
public class PageDomainEnumerator implements Enumeration<PageDomain> {

    protected final Long pageId;
    protected final Long productId;
    protected final Long clientId;
    protected final Long tenantId;
    protected final ContextPath contextPath;

    // Elements
    private static final int PAGE = 0;
    private static final int PRODUCT = 1;
    private static final int CLIENT = 2;
    private static final int TENANT = 3;
    private static final int CONTEXT = 4;
    // Status
    private byte[] markers = new byte[5];

    // Current values
    protected Long proId;
    protected Long cliId;
    protected Long tenId;
    protected ContextPath path;

    /**
     * ServiceDomainEnumerator constructor.
     *
     * @param pageId Application Id must not be <code>null</code>, and larger than zero
     * @param productId Product Id must not be <code>null</code>, and larger than zero
     * @param clientId Client Id must not be <code>null</code>, and larger than zero
     * @param tenantId Tenant Id must not be <code>null</code>, and larger than zero
     * @param contextPath Context Instance Id must not be <code>null</code>, and larger than zero
     */
    public PageDomainEnumerator(Long pageId, Long productId, Long clientId, Long tenantId,
                                ContextPath contextPath) {
        Assert.notNull(pageId, "Application Id cannot be null");
        Assert.notNull(productId, "Product Id cannot be null");
        Assert.notNull(clientId, "Client Id cannot be null");
        Assert.notNull(tenantId, "Tenant Id Id cannot be null");
        Assert.notNull(contextPath, "Context path cannot be null");
        Assert.isTrue(pageId > 0, "Application Id must larger than zero");
        Assert.isTrue(productId > 0, "Product Id must larger than zero");
        Assert.isTrue(clientId > 0, "Client Id must larger than zero");
        Assert.isTrue(tenantId > 0, "Tenant Id must larger than zero");
        Assert.isTrue(contextPath.getInstanceId() > 0, "Context Instance Id must larger than zero");

        this.pageId = pageId;
        this.productId = productId;
        this.clientId = clientId;
        this.tenantId = tenantId;
        this.contextPath = contextPath;

        // Init status
        markers[PAGE] = 1;
        markers[PRODUCT] = 1;
        markers[CLIENT] = 1;
        markers[TENANT] = 1;
        markers[CONTEXT] = 2;
        // Init values
        proId = productId;
        cliId = clientId;
        tenId = tenantId;
        path = contextPath;
    }

    @Override
    public boolean hasMoreElements() {
        if(markers[PAGE] == 0){
            return false;
        }

        // set values

        if(markers[CONTEXT] == 0){
            path = null;
        }

        if(markers[TENANT] == 1){
            tenId = tenantId;
        }else{
            tenId = 0L;
        }

        if(markers[CLIENT] == 1){
            cliId = clientId;
        }else{
            cliId = 0L;
        }

        if(markers[PRODUCT] == 1){
            proId = productId;
        }else{
            proId = 0L;
        }

        return true;
    }

    @Override
    public PageDomain nextElement() {
        PageDomain ret = null;
        if(markers[PAGE] != 0) {
            ret = new PageDomain(pageId, proId, cliId, tenId, path);

            // move status forward
            if (markers[CONTEXT] == 2) {
                path = path.nextLookup();
                if (path.nextLookup() == null) {
                    markers[CONTEXT] = 1;
                }
            } else {
                if (markers[TENANT] == 1) {
                    markers[TENANT] = 0;
                } else {
                    if (markers[CLIENT] == 1) {
                        markers[CLIENT] = 0;
                    } else {
                        if (markers[PRODUCT] == 1) {
                            markers[PRODUCT] = 0;
                        } else {
                            if (markers[CONTEXT] == 1) {
                                markers[CONTEXT] = 0;
                                markers[TENANT] = 1;
                                markers[CLIENT] = 1;
                                markers[PRODUCT] = 1;
                            } else {
                                // reach end
                                markers[PAGE] = 0;
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

}
