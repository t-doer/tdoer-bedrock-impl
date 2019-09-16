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
import com.tdoer.bedrock.impl.definition.context.ContextInstanceDefinition;
import com.tdoer.bedrock.impl.provider.ContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextInstanceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ContextInstanceLoader.class);

    private ContextProvider contextProvider;

    private DefaultContextConfigCenter configCenter;

    private DefaultRootContextType rootContextType;

    public ContextInstanceLoader(ContextProvider contextProvider, DefaultContextConfigCenter configCenter, DefaultRootContextType rootContextType) {
        this.contextProvider = contextProvider;
        this.configCenter = configCenter;
        this.rootContextType = rootContextType;
    }

    public DefaultContextInstance loadContextInstance(ContextPath contextPath){
        DefaultContextInstance ret = null;
        ContextType contextType = rootContextType.find(contextPath.getType());
        if(contextType == null){
            logger.info("Invalid contextPath type found in the contextPath path: {}",  contextPath);
            return null;
        }
        ContextInstanceDefinition entity = contextProvider.getContextInstance(contextPath);
        if(entity == null){
            logger.info("No contextPath instance found from the contextPath path: {}", contextPath);
        }else{
            ContextPath cp = generateContextPath(entity);
            if(!cp.equals(contextPath)){
                logger.info("Illegal request contextPath path ({}), it dose not match real contextPath path ({})", contextPath, cp);
                return null;
            }

            contextType = rootContextType.find(cp.getType());
            ret = new DefaultContextInstance(cp, contextType, entity.getInstanceName(), entity.getCode(), entity.getDetailObjectId(), new DefaultContextConfig(configCenter, cp));

            entity = entity.getParent();
            DefaultContextInstance instance = ret, parent = null;
            while(entity != null){
                cp = generateContextPath(entity);
                contextType = rootContextType.find(cp.getType());
                parent = new DefaultContextInstance(cp, contextType, entity.getInstanceName(), entity.getCode(), entity.getDetailObjectId(), new DefaultContextConfig(configCenter, cp));
                instance.setParent(parent);

                entity = entity.getParent();
                instance = parent;
            }
        }

        return ret;
    }

    protected ContextPath generateContextPath(ContextInstanceDefinition entity){
        ContextPath ret = new ContextPath(entity.getContextType(), entity.getInstanceId());

        entity = entity.getParent();
        ContextPath path = ret, parentPath = null;
        while(entity != null){
            parentPath = new ContextPath(entity.getContextType(), entity.getInstanceId());
            path.setParentPath(parentPath);
            entity = entity.getParent();
            path = parentPath;
        }

        return ret;
    }
}
