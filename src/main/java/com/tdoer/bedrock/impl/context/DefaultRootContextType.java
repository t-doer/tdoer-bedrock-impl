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
import com.tdoer.bedrock.context.RootContextType;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultRootContextType implements RootContextType {

    private ContextTypeLoader contextTypeLoader;

    private DefaultContextType rootContextType;

    public DefaultRootContextType(ContextTypeLoader contextTypeLoader) {
        this.contextTypeLoader = contextTypeLoader;
    }

    @Override
    public DefaultContextType getRoot(){
        if(rootContextType == null){
            rootContextType = contextTypeLoader.loadRootContextType();
        }

        return rootContextType;
    }

    @Override
    public DefaultContextType find(Integer contextType){
        return searchByType(getRoot(), contextType);
    }

    @Override
    public ContextType find(String code) {
        return searchByCode(getRoot(), code);
    }

    private DefaultContextType searchByType(DefaultContextType current, Integer contextType){
        if(current.getType().equals(contextType)){
            return current;
        }

        DefaultContextType candidate = null;
        for(DefaultContextType child : current.getChildren()){
            candidate = searchByType(child, contextType);
            if(candidate != null){
                return candidate;
            }
        }

        return null;
    }

    private DefaultContextType searchByCode(DefaultContextType current, String code){
        if(current.getCode().equals(code)){
            return current;
        }

        DefaultContextType candidate = null;
        for(DefaultContextType child : current.getChildren()){
            candidate = searchByCode(child, code);
            if(candidate != null){
                return candidate;
            }
        }

        return null;
    }
}
