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
import com.tdoer.bedrock.context.PublicAuthority;
import com.tdoer.bedrock.impl.application.DefaultApplicationRepository;
import com.tdoer.bedrock.impl.definition.context.ContextApplicationDefinition;
import com.tdoer.bedrock.impl.definition.context.ContextRoleAuthorityDefinition;
import com.tdoer.bedrock.impl.definition.context.ContextRoleDefinition;
import com.tdoer.bedrock.impl.definition.context.PublicAuthorityDefinition;
import com.tdoer.bedrock.impl.provider.ContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class ContextConfigLoader {
    private final static Logger logger = LoggerFactory.getLogger(ContextConfigLoader.class);

    private ContextProvider contextProvider;

    private ContextConfigBuilder contextConfigBuilder;

    private ContextPathParser contextPathParser;

    private static final DefaultContextRole[] EMPTY_CONTEXT_ROLES = new DefaultContextRole[0];

    private static final DefaultContextApplicationInstallation[] EMPTY_CONTEXT_APPLICATIONS = new DefaultContextApplicationInstallation[0];

    private static final DefaultPublicAuthority[] EMPTY_PUBLIC_AUTHORITIES = new DefaultPublicAuthority[0];

    public ContextConfigLoader(ContextProvider contextProvider, ContextPathParser contextPathParser, DefaultApplicationRepository applicatinRespository) {
        this.contextProvider = contextProvider;
        this.contextPathParser = contextPathParser;
        this.contextConfigBuilder = new ContextConfigBuilder(contextPathParser);
        this.contextConfigBuilder.setApplicationRepository(applicatinRespository);
    }

    public DefaultContextRole[] loadUserRoles(ContextPath contextPath, Long userId){
        List<ContextRoleDefinition> roleDefinitionList = contextProvider.getUserRolesInContext(contextPath, userId);
        return buildContextRoles(roleDefinitionList);
    }

    public PublicAuthority[] loadPublicAuthorities(ContextPath contextPath, String productId, String clientId, Long tenantId){
        List<PublicAuthorityDefinition> publicAuthorities = contextProvider.getPublicAuthorities(contextPath, productId, clientId, tenantId);
        if(publicAuthorities != null && publicAuthorities.size() > 0){
            ArrayList<DefaultPublicAuthority> list = new ArrayList<>();
            for(PublicAuthorityDefinition definition  : publicAuthorities){
                try{
                    list.add(contextConfigBuilder.buildPublicAuthority(definition));
                }catch(Throwable t){
                    // todo
                }
            }

            DefaultPublicAuthority[] ret = new DefaultPublicAuthority[list.size()];
            return list.toArray(ret);
        }

        return EMPTY_PUBLIC_AUTHORITIES;
    }

    public DefaultContextRole[] loadContextRoles(ContextPath contextPath, String productId, String clientId, Long tenantId){
        List<ContextRoleDefinition> roleDefinitionList = contextProvider.getContextRoles(contextPath, productId, clientId, tenantId);
        return buildContextRoles(roleDefinitionList);
    }

    protected DefaultContextRole[] buildContextRoles(List<ContextRoleDefinition> roleDefinitionList){
        if(roleDefinitionList != null && roleDefinitionList.size() >0){
            ArrayList<DefaultContextRole> roleList = new ArrayList<>();
            for(ContextRoleDefinition roleDefinition : roleDefinitionList){
                DefaultRoleAuthority[] auths = null;
                List<ContextRoleAuthorityDefinition> authorityDefinitionList = contextProvider.getContextRoleAuthorities(contextPathParser.parse(roleDefinition.getContextPath()), roleDefinition.getId());
                if(authorityDefinitionList != null){
                    ArrayList<DefaultRoleAuthority> authorityList = new ArrayList<>();
                    for(ContextRoleAuthorityDefinition authorityDefinition : authorityDefinitionList){
                        try{
                            authorityList.add(contextConfigBuilder.buildRoleAuthority(authorityDefinition));
                        }catch(Throwable t){
                            // todo
                        }
                    }

                    auths = new DefaultRoleAuthority[authorityList.size()];
                    authorityList.toArray(auths);
                }

                try{
                    roleList.add(contextConfigBuilder.buildContextRole(roleDefinition, auths));
                }catch(Throwable t){
                    // todo
                }
            }

            DefaultContextRole[] roles = new DefaultContextRole[roleList.size()];
            roleList.toArray(roles);
            return roles;
        }

        return EMPTY_CONTEXT_ROLES;
    }

    public DefaultContextApplicationInstallation[] loadApplicationInstallations(ContextPath contextPath, String productId, String clientId, Long tenantId){

        List<ContextApplicationDefinition> list = contextProvider.getContextApplications(contextPath, productId, clientId, tenantId);
        if(list == null || list.size() ==0){
            return EMPTY_CONTEXT_APPLICATIONS;
        }

        ArrayList<DefaultContextApplicationInstallation> installationList = new ArrayList<>(list.size());
        for(ContextApplicationDefinition definition : list){
            try{
                installationList.add(contextConfigBuilder.buildContextApplicationInstallation(definition));
            }catch(Throwable t){
                // todo
            }
        }

        DefaultContextApplicationInstallation[] ret = new DefaultContextApplicationInstallation[installationList.size()];
        return installationList.toArray(ret);
    }

}
