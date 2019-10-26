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
import com.tdoer.bedrock.context.ContextType;
import com.tdoer.bedrock.impl.definition.context.ContextTypeDefinition;

import java.util.ArrayList;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public final class DefaultContextType implements ContextType {

	private static final DefaultContextType[] EMPTY_ARRAY = new DefaultContextType[0];

	private ContextTypeDefinition definition;

	private DefaultContextType parent;

	private ArrayList<DefaultContextType> children;

	public DefaultContextType(ContextTypeDefinition  definition){
		this(definition, null);
	}

	public DefaultContextType(ContextTypeDefinition definition, DefaultContextType parent) {
		this.definition = definition;
		this.parent = parent;
		children = new ArrayList<>();
	}

	/**
	 * Context type
	 *
	 * @return Context type, must not be <code>null</code>
	 */
	@Override
	public Long getType() {
		return definition.getId();
	}

	/**
	 * Context name
	 *
	 * @return Context name, must not be blank
	 */
	@Override
	public String getName() {
		return definition.getName();
	}

	/**
	 * Context code
	 *
	 * @return Context code, must not be blank
	 */
	@Override
	public String getCode() {
		return definition.getCode();
	}

	/**
	 * Context category
	 *
	 * @return Context category, must not be blank
	 */
	@Override
	public String getCategory() {
		return definition.getCategory();
	}

	/**
	 * Parent context type
	 *
	 * @return Parent context type or <code>null</code> if the context type is the root context type
	 */
	@Override
	public DefaultContextType getParent() {
		return parent;
	}

	/**
	 * Children context types
	 *
	 * @return Context types or <code>null</code> if the context type is the leaf node
	 */
	@Override
	public DefaultContextType[] getChildren() {
		if(children == null){
			return EMPTY_ARRAY;
		}

		DefaultContextType[] arr = new DefaultContextType[children.size()];
		for(int i=0; i< children.size(); i++){
			arr[i] = children.get(i);
		}
		return arr;
	}

	/**
	 * Root context type, must be "TENANT" context type
	 *
	 * @return Context type, must not be <code>null</code>
	 */
	@Override
	public DefaultContextType getRoot() {
		DefaultContextType c = this;
		while(c.getParent() != null) {
			c = c.getParent();
		}
		return c;
	}

	/**
	 * The path of the context
	 *
	 * @return Context path, must not be <code>null</code>
	 */
	@Override
	public ContextPath getContextPath() {
		if(parent == null){
			return new ContextPath(getType(), 0L);
		}else{
			return new ContextPath(getType(), 0L, getParent().getContextPath());
		}
	}

	/**
	 * Check if the context is "TENANT" context type. "TENANT" context type is
	 * the root context type, is the top parent of all other context types.
	 *
	 * @return true if it's "TENANT" context type
	 */
	@Override
	public boolean isTenant() {
		return (parent == null);
	}

	@Override
	public int hashCode() {
		return definition.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}

		if(this == obj){
			return true;
		}

		if(obj instanceof ContextType && this.getType().equals(((ContextType)obj).getType())){
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ContextType[");
		sb.append(getType()).append(", ");
		sb.append(getCode()).append(", ");
		sb.append(getContextPath());
		sb.append("]");
		return sb.toString();
	}
}
