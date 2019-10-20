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
package com.tdoer.bedrock.impl.service;

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.impl.domain.DomainType;
import com.tdoer.bedrock.impl.domain.ExtensionDomain;
import org.springframework.util.Assert;

/**
 * ServiceDomain is used to search service methods
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceDomain {

    protected Long serviceId;
    protected Long applicationId;
	protected Long productId;
	protected Long clientId;
	protected Long tenantId;
	protected ContextPath contextPath;

	public ServiceDomain(Long serviceId, Long applicationId, Long productId, Long clientId, Long tenantId,
						 ContextPath contextPath) {
        Assert.notNull(serviceId, "Service Id cannot be null");
		Assert.notNull(applicationId, "Application Id cannot be null");
		Assert.notNull(productId, "Product Id cannot be null");
		Assert.notNull(clientId, "Client Id cannot be null");
		Assert.notNull(tenantId, "Tenant Id Id cannot be null");

		this.serviceId = serviceId;
		this.applicationId = applicationId;
	    this.productId = productId;
		this.clientId = clientId;
		this.tenantId = tenantId;
		this.contextPath = contextPath;
	}

    public Long getServiceId() {
        return serviceId;
    }

    public Long getApplicationId(){
		return applicationId;
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
		return (0 != applicationId
		|| 0 != productId
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
        sb.append(applicationId).append(", ");
        sb.append(productId).append(", ");
        sb.append(clientId).append(", ");
        sb.append(tenantId).append(", ");
        sb.append(contextPath);
        sb.append("]");
        return sb.toString();
    }
}
