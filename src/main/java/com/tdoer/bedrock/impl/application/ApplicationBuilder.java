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

import com.tdoer.bedrock.impl.definition.application.ActionDefinition;
import com.tdoer.bedrock.impl.definition.application.ApplicationDefinition;
import com.tdoer.bedrock.impl.definition.application.PageDefinition;
import com.tdoer.bedrock.impl.service.DefaultServiceRepository;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ApplicationBuilder {
    private DefaultApplicationRepository applicationRepository;

    private DefaultServiceRepository serviceRepository;

    public ApplicationBuilder(DefaultServiceRepository serviceRepository) {
        Assert.notNull(serviceRepository, "Service repository cannot be null");
        this.serviceRepository = serviceRepository;
    }

    public void setApplicationRepository(DefaultApplicationRepository applicationRepository) {
        Assert.notNull(applicationRepository, "Application repository cannot be null");
        this.applicationRepository = applicationRepository;
    }

    public DefaultApplication buildApplication(ApplicationDefinition applicationDefinition){
        // todo verify definition
        return new DefaultApplication(applicationDefinition, applicationRepository);
    }

    public DefaultPage buildPage(PageDefinition pageDefinition){
        // todo verify defnition

        return new DefaultPage(pageDefinition, applicationRepository);
    }

    public DefaultAction buildAction(ActionDefinition actionDefinition){
        // todo verify defintion

        return new DefaultAction(actionDefinition, applicationRepository);
    }
}
