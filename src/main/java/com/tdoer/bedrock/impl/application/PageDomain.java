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
package com.tdoer.bedrock.impl.application;

import com.tdoer.bedrock.context.ContextPath;
import org.springframework.util.Assert;

/**
 * PageDomain is used to search actions
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class PageDomain {

    protected Long pageId;
	protected Long productId;
	protected Long clientId;
	protected Long tenantId;
	protected ContextPath contextPath;

	public PageDomain(Long pageId, Long productId, Long clientId, Long tenantId, ContextPath contextPath) {
        Assert.notNull(pageId, "Page ID cannot be null");

	    this.pageId = pageId;
	    this.productId = productId;
		this.clientId = clientId;
		this.tenantId = tenantId;
		this.contextPath = contextPath;
	}

    public Long getPageId() {
        return pageId;
    }

    public Long getProductId() {
		return productId;
	}

	public Long getClientId() {
		return clientId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public ContextPath getContextPath() {
		return contextPath;
	}

	boolean isExtensionDomain(){
		return ( 0 != productId
				|| 0 != clientId
				|| 0 != tenantId
				|| null != contextPath);
	}

	boolean isCommonDomain(){
		return !isExtensionDomain();
	}

	@Override
  	public int hashCode() {
	  	return toString().hashCode();
  	}

	@Override
  	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(this == obj){
			return true;
		}
	  	if(obj instanceof PageDomain){
	  		return toString().equals(obj.toString());
	  	}
	  	return false;
  	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PageDomain[");
        sb.append(pageId).append(": ");
        sb.append(productId);
        sb.append(", ").append(clientId);
        sb.append(", ").append(tenantId);
        sb.append(", ").append(contextPath);
        sb.append("]");
        return sb.toString();
    }
}