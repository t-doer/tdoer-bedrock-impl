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

import com.tdoer.bedrock.product.ClientRole;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultClientRole implements ClientRole {
    private String code;

    public DefaultClientRole(String code) {
        Assert.hasText(code, "Client role's code cannot be blank");
        this.code = code;
    }

    /**
     * Client's role to play with services.
     *
     * @return Client role's code, it must not blank
     */
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getAuthority() {
        return getCode();
    }
}
