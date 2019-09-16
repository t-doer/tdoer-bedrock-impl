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
package com.tdoer.bedrock.impl.product;

import com.tdoer.bedrock.impl.application.DefaultApplication;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.definition.product.ClientApplicationDefinition;
import com.tdoer.bedrock.product.ClientApplicationInstallation;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientApplicationInstallation implements ClientApplicationInstallation {

    private ClientApplicationDefinition definition;

    private DefaultApplicationRepository applicationRepository;

    public DefaultClientApplicationInstallation(ClientApplicationDefinition definition, DefaultApplicationRepository applicationRepository) {
        this.definition = definition;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public DefaultApplication getApplication() {
        return applicationRepository.getApplication(definition.getApplicationId());
    }

    @Override
    public Long getTenantId() {
        return definition.getTenantId();
    }

    @Override
    public String getProductId() {
        return definition.getProductId();
    }

    @Override
    public String getClientId() {
        return definition.getClientId();
    }
}
