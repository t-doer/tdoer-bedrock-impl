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
package com.tdoer.bedrock.impl.definition.context;

/**
 * @description ContextInstanceDefinition
 * @author fly_once(654126198@qq.com)
 * @create 2019-11-15
 */
public class ContextInstanceDefinition {

    private Long id;

    private String code;

    private String guid;

    private String name;

    private Long tenantId;

    private Long detailObjectId;

    private String contextPath;

    private ContextTypeDefinition contextType;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDetailObjectId() {
        return this.detailObjectId;
    }

    public void setDetailObjectId(Long detailObjectId) {
        this.detailObjectId = detailObjectId;
    }

    public String getContextPath() {
        return this.contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public ContextTypeDefinition getContextType() {
        return this.contextType;
    }

    public void setContextType(ContextTypeDefinition contextType) {
        this.contextType = contextType;
    }
}