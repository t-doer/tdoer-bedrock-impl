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

import com.tdoer.bedrock.impl.definition.context.ContextTypeDefinition;
import com.tdoer.bedrock.impl.provider.ContextProvider;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextTypeLoader {
    private ContextProvider contextProvider;
    private ContextTypeBuilder contextTypeBuilder;

    public ContextTypeLoader(ContextProvider contextProvider) {

        this.contextProvider = contextProvider;
        this.contextTypeBuilder = new ContextTypeBuilder();
    }

    public DefaultContextType loadRootContextType(){
        List<ContextTypeDefinition> all = contextProvider.getAllContextTypes();
        ContextTypeDefinition definition = findRoot(all);
        DefaultContextType root = contextTypeBuilder.buildContextType(definition);

        buildChild(root, all);

        return root;
    }

    protected ContextTypeDefinition findRoot(List<ContextTypeDefinition> list){
        for(ContextTypeDefinition type : list){
            if(type.getParentType() == null){
                return type;
            }
        }

        // data error
        return null;
    }

    protected List<ContextTypeDefinition> findChildren(List<ContextTypeDefinition> list, Integer parentType){
        ArrayList<ContextTypeDefinition> arr = new ArrayList<>();
        for(ContextTypeDefinition type : list){
            if(type.getParentType().equals(parentType)){
                arr.add(type);
            }
        }
        return arr;
    }

    protected void buildChild(DefaultContextType parent, List<ContextTypeDefinition> all){
        List<ContextTypeDefinition> children = findChildren(all, parent.getType());
        if(children.size() ==0){
            return;
        }

        DefaultContextType child = null;
        for(ContextTypeDefinition definition : children){
            child = contextTypeBuilder.buildContextType(definition);
            parent.addChild(child);
            buildChild(child, all);
        }
    }
}
