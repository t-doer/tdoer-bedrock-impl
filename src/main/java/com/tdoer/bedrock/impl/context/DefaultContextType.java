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

	public void addChild(DefaultContextType child){
		children.add(child);
		child.setParent(this);
	}

	public void setParent(DefaultContextType parent){
	    this.parent = parent;
    }

	@Override
	public Integer getType() {
		return definition.getType();
	}

	@Override
	public String getName() {
		return definition.getName();
	}

	@Override
	public String getCode() {
		return definition.getCode();
	}

	@Override
	public String getCategory() {
		return definition.getCategory();
	}

	@Override
	public DefaultContextType getParent() {
		return parent;
	}

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

	@Override
	public DefaultContextType getRoot(){
		DefaultContextType c = this;
		while(c.getParent() != null) {
			c = c.getParent();
		}
		return c;
	}

	@Override
	public DefaultContextType find(Integer contextType){
		DefaultContextType root = getRoot();
		return search(root, contextType);
	}

	private DefaultContextType search(DefaultContextType current, Integer contextType){
		if(current.getType().equals(contextType)){
			return current;
		}

		DefaultContextType candidate = null;
		for(DefaultContextType child : current.getChildren()){
			candidate = search(child, contextType);
			if(candidate != null){
				return candidate;
			}
		}

		return null;
	}

    @Override
    public int hashCode() {
        return definition.getType();
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
}
