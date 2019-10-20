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
package com.tdoer.bedrock.impl.domain;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public enum DomainType {

    SERVICE, APPLICATION, CONTEXT, PRODUCT, APPLICATION_CONTEXT;

    private static final Map<String, DomainType> mappings = new HashMap<>(5);

    static {
        for (DomainType clientCategory : values()) {
            mappings.put(clientCategory.name(), clientCategory);
        }
    }


    /**
     * Resolve the given type value to an {@code DomainType}.
     * @param type the type value as a String
     * @return the corresponding {@code DomainType}, or {@code null} if not found
     */
    @Nullable
    public static DomainType resolve(@Nullable String type) {
        return (type != null ? mappings.get(type) : null);
    }


    /**
     * Determine whether this {@code DomainType} matches the given
     * type value.
     * @param type the type value as a String
     * @return {@code true} if it matches, {@code false} otherwise
     * @since 4.2.4
     */
    public boolean matches(String type) {
        return (this == resolve(type));
    }
}
