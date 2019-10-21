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
import com.tdoer.bedrock.context.ContextPathParser;
import com.tdoer.bedrock.context.InvalidContextPathException;
import org.springframework.util.StringUtils;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DefaultContextPathParser implements ContextPathParser {

    /**
     * Parse context path string, say "1.1-2.1-3.1" into {@link ContextPath}
     *
     * @param contextPath Context path string, must not be <code>null</code>
     * @return {@link ContextPath} if the context path string is of the format
     * @throws InvalidContextPathException if the context path string is invalid
     */
    @Override
    public ContextPath parse(String contextPath) throws InvalidContextPathException {

        String[] arr = StringUtils.delimitedListToStringArray(contextPath, "-");

        ContextPath context = null;
        ContextPath parent = null;
        for (String str : arr) {
            String[] ta = StringUtils.delimitedListToStringArray(str, ".");

            if(ta.length != 2){
                throw new InvalidContextPathException(contextPath);
            }

            Long type = Long.parseLong(ta[0]);
            Long xid = Long.parseLong(ta[1]);

            context = new ContextPath(type, xid, parent);

            parent = context;
        }

        return context;
    }
}
