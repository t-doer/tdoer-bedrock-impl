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
public class ApplicationDomain extends ExtensionDomain<ApplicationDomain>{

    protected String applicationId;
	protected String productId;
	protected String clientId;
	protected Long tenantId;
	protected ContextPath contextPath;

	public ApplicationDomain(String applicationId, String productId, String clientId, Long tenantId, ContextPath contextPath) {
        Assert.notNull(applicationId, "Application ID cannot be null");

	    this.applicationId = applicationId;
	    this.productId = productId;
		this.clientId = clientId;
		this.tenantId = tenantId;
		this.contextPath = contextPath;
	}

    @Override
    public DomainType getType() {
        return DomainType.APPLICATION;
    }

    public String getApplicationId() {
        return applicationId;
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
	 * Suppose current application domain is [applicationId: productId, clientId, tenantId, contextPath]
     * is [user-manager: cc, cc-engineer-app, 1, 1.1-20.1-30.1], next lookup
	 * sequence will be:
	 * <ol>
	 *     <li>[user-manager: cc, cc-engineer-app, 1, 1.1-20.1-30.1]</li>
	 *     <li>[user-manager: cc, cc-engineer-app, 1, 1.1-20.1-30.0]</li>
	 *     <li>[user-manager: cc, cc-engineer-app, 1, 1.1-20.0-30.0]</li>
	 *     <li>[user-manager: cc, cc-engineer-app, 1, 1.0-20.0-30.0]</li>
	 *     <li>[user-manager: cc, cc-engineer-app, 0, 1.0-20.0-30.0]</li>
     *     <li>[user-manager: cc, cc-engineer-app, 0, null]</li>
     *     <li>[user-manager: cc, null, 0, null]</li>
     *     <li>[user-manager: null, null, 0, null]</li>
     *     <li>null</li>
	 * </ol>
	 *
	 * @return
	 */
	@Override
	public ApplicationDomain nextLookup() {
	    if(contextPath != null){
            ContextPath cp = contextPath.parentTemplate();
            if (!cp.equals(contextPath)){
                return new ApplicationDomain(applicationId, productId, clientId, tenantId, cp);
            } else{
                if(tenantId != 0){
                    return new ApplicationDomain(applicationId, productId, clientId,0L, cp);
                } else {
                    return new ApplicationDomain(applicationId, productId, clientId,0L, null);
                }
            }
        }else{
	        if(clientId != null){
	            return new ApplicationDomain(applicationId, productId, null,0L, null);
            }else if(productId != null){
	            return  new ApplicationDomain(applicationId, null, null,0L, null);
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
	  	if(obj instanceof ApplicationDomain){
	  		return toString().equals(obj.toString());
	  	}
	  	return false;
  	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(applicationId).append(": ");
        sb.append(productId);
        sb.append(", ").append(clientId);
        sb.append(", ").append(tenantId);
        sb.append(", ").append(contextPath.getAbsoluteValue());
        sb.append("]");
        return sb.toString();
    }
}
