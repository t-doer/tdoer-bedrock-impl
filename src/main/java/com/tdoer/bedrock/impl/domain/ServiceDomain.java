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
 * ServiceDomain is used to search service methods
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceDomain extends ExtensionDomain<ServiceDomain>{

    protected String serviceId;
	protected String productId;
	protected String clientId;
	protected Long tenantId;
	protected ContextPath contextPath;

	public ServiceDomain(String serviceId, String productId, String clientId, Long tenantId, ContextPath contextPath) {
        Assert.notNull(serviceId, "Application ID cannot be null");

	    this.serviceId = serviceId;
	    this.productId = productId;
		this.clientId = clientId;
		this.tenantId = tenantId;
		this.contextPath = contextPath;
	}

    @Override
    public DomainType getType() {
        return DomainType.APPLICATION;
    }

    public String getServiceId() {
        return serviceId;
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
	 * Suppose current service domain is [serviceId: applicationId, productId, clientId, tenantId, contextPath]
     * is [user-service: user-manager, cc, cc-engineer-app, 1, 1.1-20.1-30.1], next lookup sequence will be:
	 * <ol>
	 *     <li>[user-service: user-manager, cc, cc-engineer-app, 1, 1.1-20.1-30.1]</li>
	 *     <li>[user-service: user-manager, cc, cc-engineer-app, 1, 1.1-20.1-30.0]</li>
	 *     <li>[user-service: user-manager, cc, cc-engineer-app, 1, 1.1-20.0-30.0]</li>
	 *     <li>[user-service: user-manager, cc, cc-engineer-app, 1, 1.0-20.0-30.0]</li>
	 *     <li>[user-service: user-manager, cc, cc-engineer-app, 0, 1.0-20.0-30.0]</li>
     *     <li>[user-service: user-manager, cc, cc-engineer-app, 0, void]</li>
     *     <li>[user-service: user-manager, cc, null, 0, null]</li>
     *     <li>[user-service: user-manager, null, null, 0, null]</li>
	 *     <li>[user-service: null, null, null, 0, null]</li>
     *     <li>null</li>
	 * </ol>
	 *
	 * @return
	 */
	@Override
	public ServiceDomain nextLookup() {
	    if(contextPath != null){
            ContextPath cp = contextPath.parentTemplate();
            if (!cp.equals(contextPath)){
                return new ServiceDomain(serviceId, productId, clientId, tenantId, cp);
            } else{
                if(tenantId != 0){
                    return new ServiceDomain(serviceId, productId, clientId,0L, cp);
                } else {
                    return new ServiceDomain(serviceId, productId, clientId,0L, null);
                }
            }
        }else{
	        if(clientId != null){
	            return new ServiceDomain(serviceId, productId, null,0L, null);
            }else if(productId != null){
	            return  new ServiceDomain(serviceId, null, null,0L, null);
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
	  	if(obj instanceof ServiceDomain){
	  		return toString().equals(obj.toString());
	  	}
	  	return false;
  	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(serviceId).append(": ");
        sb.append(productId);
        sb.append(", ").append(clientId);
        sb.append(", ").append(tenantId);
        sb.append(", ").append(contextPath.getAbsoluteValue());
        sb.append("]");
        return sb.toString();
    }
}
