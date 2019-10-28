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
package com.tdoer.bedrock.impl.context;

import com.tdoer.bedrock.context.ContextPath;
import org.springframework.util.Assert;

/**
 * ContextDomain is used to search context's configurations, such like
 * context roles.
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextDomain {

	protected Long tenantId;
	protected ContextPath contextPath;

	public ContextDomain(Long tenantId, ContextPath contextPath) {
		Assert.notNull(tenantId, "Tenant Id cannot be null");
        Assert.notNull(contextPath, "ContextPath cannot be null");

		this.tenantId = tenantId;
		this.contextPath = contextPath;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public ContextPath getContextPath() {
		return contextPath;
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
		sb.append(tenantId).append(", ");
		sb.append(contextPath);
		sb.append("]");
        return sb.toString();
    }
}