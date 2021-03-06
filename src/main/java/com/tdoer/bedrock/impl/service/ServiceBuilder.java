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
import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.impl.definition.service.ServiceDefinition;
import com.tdoer.bedrock.impl.definition.service.ServiceMethodDefinition;
import org.springframework.util.StringUtils;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ServiceBuilder {

    private DefaultServiceRepository serviceRespository;

    private ContextPathParser contextPathParser;

    public ServiceBuilder(ContextPathParser contextPathParser) {
        this.contextPathParser = contextPathParser;
    }

    public void setServiceRepository(DefaultServiceRepository serviceRespository) {
        this.serviceRespository = serviceRespository;
    }

    DefaultService buildService(ServiceDefinition definition){
        // todo, check definition
        return new DefaultService(definition, serviceRespository);
    }

    DefaultServiceMethod buildServiceMethod(ServiceMethodDefinition definition){
        // todo verify defintion
        ContextPath contextPath = null;
        if(StringUtils.hasText(definition.getContextPath())){
            contextPath = contextPathParser.parse(definition.getContextPath());

        }
        return new DefaultServiceMethod(definition, contextPath);
    }
}
