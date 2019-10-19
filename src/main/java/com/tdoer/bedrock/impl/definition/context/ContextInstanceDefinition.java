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

import java.io.Serializable;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextInstanceDefinition implements Serializable {
    /**
     * Tenant Id, Organization Id or User Id
     */
    private Long id;

    private String guid;

    /**
     * Tenant Name, Organizaiton Name or User Name
     */
    private String name;

    /**
     * Tenant Code, Organization Code or User Login Account
     */
    private String code;

    private Long tenantId;

    /**
     * Context type, for example, 1 - TENANT, 2 - USER
     */
    private Integer contextType;

    private String contextPath;

    private Long parentId;

    /**
     * Context instance's detail information object's ID, say, tenant's Id, store's Id, userI's Id etc.
     */
    private Long detailObjectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getContextType() {
        return contextType;
    }

    public void setContextType(Integer contextType) {
        this.contextType = contextType;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getDetailObjectId() {
        return detailObjectId;
    }

    public void setDetailObjectId(Long detailObjectId) {
        this.detailObjectId = detailObjectId;
    }
}
