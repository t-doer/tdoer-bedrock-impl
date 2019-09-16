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
package com.tdoer.bedrock.impl;

import com.tdoer.bedrock.BedrockErrorCodes;
import com.tdoer.springboot.annotation.ReasonPhrase;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface BedrockImplErrorCodes extends BedrockErrorCodes {
    @ReasonPhrase("Failed to load service of Id: {0}")
    int FAILED_TO_LOAD_SERVICE = 5201;

    @ReasonPhrase("Failed to load tenant client of the client domain {0}")
    int FAILED_TO_LOAD_TENANT_CLIENT_OF_DOMAIN = 5202;

    @ReasonPhrase("Failed to load service method of domain: {0}")
    int FAILED_TO_LOAD_SERVICE_METHODS = 5203;

    @ReasonPhrase("Failed to load service method of Id: {0}")
    int FAILED_TO_LOAD_SERVICE_METHOD = 5204;

    @ReasonPhrase("Failed to load application for application Id: {0}")
    int FAILED_TO_LOAD_APPLICATION = 5205;

    @ReasonPhrase("Failed to load client application installations of product domain ({0})")
    int FAILED_TO_LOAD_CLIENT_APPLICATION_INSTALLATIONS = 5206;

    @ReasonPhrase("Failed to load pages of domain: {0}")
    int FAILED_TO_LOAD_PAGES = 5207;

    @ReasonPhrase("Failed to load actions of domain: {0}")
    int FAILED_TO_LOAD_ACTIONS = 5208;

    @ReasonPhrase("Failed to load context application installations for the context domain {0}")
    int FAILED_TO_LOAD_CONTEXT_APPLICATION_INSTALLATIONS = 5209;

    @ReasonPhrase("Failed to load product Ids of tenant ({0})")
    int FAILED_TO_LOAD_PRODUCT_IDS_OF_TENANT = 5210;

    @ReasonPhrase("Failed to load tenant client from host ({0})")
    int FAILED_TO_LOAD_TENANT_CLIENT_FROM_HOST = 5211;

    @ReasonPhrase("Failed to load client services for the product domain {0}")
    int FAILED_TO_LOAD_CLIENT_SERVICES= 5212;

    @ReasonPhrase("Failed to load context installations for the product domain {0}")
    int FAILED_TO_LOAD_CONTEXT_INSTALLATIONS = 5213;

    @ReasonPhrase("Failed to load client Ids of tenant ({0})")
    int FAILED_TO_LOAD_CLIENT_IDS_OF_TENANT = 5214;

    @ReasonPhrase("Failed to load public authorities for the context domain {0}")
    int FAILED_TO_LOAD_PUBLIC_AUTHORITIES = 5215;

    @ReasonPhrase("Failed to load token config for the client domain {0}")
    int FAILED_TO_LOAD_TOKEN_CONFIG = 5216;

    @ReasonPhrase("Failed to load tenant of Id ({0})")
    int FAILED_TO_LOAD_TENANT_OF_ID = 5217;

    @ReasonPhrase("Failed to load product of Id ({0})")
    int FAILED_TO_LOAD_PRODUCT = 5218;

    @ReasonPhrase("Failed to load context instance of context path ({0})")
    int FAILED_TO_LOAD_CONTEXT_INSTANCE = 5219;

    @ReasonPhrase("Failed to load product rental of product domain ({0})")
    int FAILED_TO_LOAD_PRODUCT_RENTAL = 5220;

    @ReasonPhrase("Failed to load services for the application domain {0}")
    int FAILED_TO_LOAD_SERVICES = 5221;

    @ReasonPhrase("Failed to load tenant of code ({0})")
    int FAILED_TO_LOAD_TENANT_OF_CODE = 5222;

    @ReasonPhrase("Failed to load client of Id ({0})")
    int FAILED_TO_LOAD_CLIENT = 5223;

    @ReasonPhrase("Failed to load context roles for the context domain {0}")
    int FAILED_TO_LOAD_CONTEXT_ROLES = 5224;

    @ReasonPhrase("Service definition not found for Id: {}")
    int SERVICE_NOT_FOUND = 5225;

    @ReasonPhrase("Failed to load service method of Id: {}")
    int SERVICE_METHOD_NOT_FOUND = 5226;

    @ReasonPhrase("Application definition not found of Id: {}")
    int APPLICATION_NOT_FOUND = 5227;

}
