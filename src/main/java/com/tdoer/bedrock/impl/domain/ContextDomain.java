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
package com.tdoer.bedrock.impl.domain;

import com.tdoer.bedrock.context.ContextPath;
import org.springframework.util.Assert;

/**
 * ApplicationDomain is used to search application resources (pages or actions)
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextDomain extends ExtensionDomain<ContextDomain> {

	protected ContextPath contextPath;
	protected String productId;
	protected String clientId;
	protected Long tenantId;

	public ContextDomain(ContextPath contextPath, String productId, String clientId, Long tenantId) {
        Assert.notNull(contextPath, "ContextPath cannot be null");

		this.contextPath = contextPath;
		this.productId = productId;
		this.clientId = clientId;
		this.tenantId = tenantId;
	}

    @Override
    public DomainType getType() {
        return DomainType.CONTEXT;
    }

    public String getProductId() {
		return productId;
	}

	public String getClientId() {
		return clientId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public ContextPath getContextPath() {
		return contextPath;
	}


	/**
	 * Suppose current product domain is [contextPath: productId, clientId, tenantId]
     * is [1.1-20.1-30.1: cc, cc-engineer-app, 1], next lookup sequence will be:
	 * <ol>
	 *     <li>[1.1-20.1-30.1: cc, cc-engineer-app, 1]</li>
	 *     <li>[1.1-20.1-30.0: cc, cc-engineer-app, 1]</li>
	 *     <li>[1.1-20.0-30.0: cc, cc-engineer-app, 1]</li>
	 *     <li>[1.0-20.0-30.0: cc, cc-engineer-app, 1]</li>
	 *     <li>[1.0-20.0-30.0: cc, cc-engineer-app, 0]</li>
	 *     <li>[1.0-20.0-30.0: cc, null, 0]</li>
	 *     <li>[1.0-20.0-30.0: null, null, 0]</li>
     *     <li>null</li>
	 * </ol>
	 *
	 * @return
	 */
	@Override
	public ContextDomain nextLookup() {
		if(tenantId != null && !tenantId.equals(0)){
			ContextPath cp = contextPath.parentTemplate();
			if (!cp.equals(contextPath)){
				return new ContextDomain(cp, productId, clientId, tenantId);
			} else{
				return new ContextDomain(cp, productId, clientId,0L);
			}
		}else{
			if(clientId != null){
				return new ContextDomain(contextPath, productId, null,0L);
			}else if(productId != null){
				return  new ContextDomain(contextPath, null, null,0L);
			}else{
				return null;
			}
		}
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
	  	if(obj instanceof ContextDomain){
	  		return toString().equals(obj.toString());
	  	}
	  	return false;
  	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(contextPath).append(": ");
        sb.append(productId);
        sb.append(", ").append(clientId);
        sb.append(", ").append(tenantId);
        sb.append("]");
        return sb.toString();
    }
}
