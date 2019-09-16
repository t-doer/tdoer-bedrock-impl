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
package com.tdoer.bedrock.impl.application;

import com.tdoer.bedrock.context.ContextPath;
import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.impl.definition.application.ActionDefinition;
import com.tdoer.bedrock.impl.definition.application.ApplicationDefinition;
import com.tdoer.bedrock.impl.definition.application.PageDefinition;
import com.tdoer.bedrock.impl.service.DefaultServiceMethod;
import org.springframework.util.StringUtils;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ApplicationBuilder {
    private DefaultApplicationRepository applicationRepository;

    private ContextPathParser contextPathParser;

    public ApplicationBuilder(ContextPathParser contextPathParser) {
        this.contextPathParser = contextPathParser;
    }

    public void setApplicationRepository(DefaultApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public DefaultApplication buildApplication(ApplicationDefinition applicationDefinition){
        // todo verify definition
        return new DefaultApplication(applicationDefinition, applicationRepository);
    }

    public DefaultPage buildPage(PageDefinition pageDefinition, DefaultServiceMethod[] serviceMethods){
        // todo verify defnition
        ContextPath contextPath = null;
        if(StringUtils.hasText(pageDefinition.getContextPath())){
            contextPath = contextPathParser.parse(pageDefinition.getContextPath());

        }
        return new DefaultPage(pageDefinition, contextPath, serviceMethods, applicationRepository);
    }

    public DefaultAction buildAction(ActionDefinition actionDefinition, DefaultServiceMethod[] serviceMethods){
        // todo verify defintion
        ContextPath contextPath = null;
        if(StringUtils.hasText(actionDefinition.getContextPath())){
            contextPath = contextPathParser.parse(actionDefinition.getContextPath());

        }
        return new DefaultAction(actionDefinition, contextPath, serviceMethods);
    }
}
