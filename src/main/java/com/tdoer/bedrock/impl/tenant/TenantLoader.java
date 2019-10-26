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
package com.tdoer.bedrock.impl.tenant;

import com.tdoer.bedrock.impl.context.DefaultContextCenter;
import com.tdoer.bedrock.impl.definition.tenant.TenantClientDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantDefinition;
import com.tdoer.bedrock.impl.definition.tenant.TenantProductDefinition;
import com.tdoer.bedrock.impl.product.DefaultProductRepository;
import com.tdoer.bedrock.impl.provider.TenantProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class TenantLoader {

    private static Logger logger = LoggerFactory.getLogger(TenantLoader.class);

    private TenantProvider tenantProvider;

    private TenantBuilder tenantBuilder;

    public TenantLoader(TenantProvider tenantProvider, DefaultProductRepository productRepository, DefaultContextCenter contextCenter) {
        Assert.notNull(tenantProvider, "Tenant provider cannot be null");
        Assert.notNull(productRepository, "Product repository cannot be null");
        Assert.notNull(contextCenter, "Context center cannot be null");

        this.tenantProvider = tenantProvider;
        this.tenantBuilder = new TenantBuilder(productRepository, contextCenter);
    }

    public void setRentalCenter(DefaultRentalCenter rentalCenter) {
        this.tenantBuilder.setRentalCenter(rentalCenter);
    }

    public DefaultTenant loadTenantByCode(String tenantCode){
        TenantDefinition definition = null;
        try{
            definition = tenantProvider.getTenantDefinitionByCode(tenantCode);
        }catch (Throwable t){
            logger.error("Failed to load tenant definition of tenant code: " + tenantCode, t);
        }
        if(definition == null){
            try{
                return tenantBuilder.buildTenant(definition);
            }catch(Exception ex){
                logger.error("Invalid tenant definition: " + definition, ex);
            }
        }
        return null;
    }

    public DefaultTenant loadTenantByGuid(String guid){
        TenantDefinition definition = null;
        try{
            definition = tenantProvider.getTenantDefinitionByGuid(guid);
        }catch (Throwable t){
            logger.error("Failed to load tenant definition of tenant guid: " + guid, t);
        }
        if(definition == null){
            try{
                return tenantBuilder.buildTenant(definition);
            }catch(Exception ex){
                logger.error("Invalid tenant definition: " + definition, ex);
            }
        }
        return null;
    }

    public DefaultTenant loadTenantById(Long tenantId){
        TenantDefinition definition = null;
        try{
            definition = tenantProvider.getTenantDefinitionById(tenantId);
        }catch (Throwable t){
            logger.error("Failed to load tenant definition of tenant Id: " + tenantId, t);
        }
        if(definition == null){
            try{
                return tenantBuilder.buildTenant(definition);
            }catch(Exception ex){
                logger.error("Invalid tenant definition: " + definition, ex);
            }
        }
        return null;
    }

    public DefaultTenantClient loadTenantClient(String host){
        TenantClientDefinition definition = null;
        try{
            definition = tenantProvider.getTenantClientDefinition(host);
        }catch (Throwable t){
            logger.error("Failed to load tenant client definition: " + definition, t);
        }
        if(definition == null){
            try{
                return tenantBuilder.buildTenantClient(definition);
            }catch(Exception ex){
                logger.error("Invalid tenant client definition: " + definition, ex);
            }
        }
        return null;
    }

    public DefaultTenantClient[] loadTenantClients(Long tenantId){
        List<TenantClientDefinition> list = null;
        try{
          list = tenantProvider.getTenantClientDefinitions(tenantId);
        } catch (Throwable t){
            logger.error("Failed to load tenant client definitions of tenant Id: " + tenantId, t);
        }
        if(list != null){
            ArrayList<DefaultTenantClient> defs = new ArrayList<>(list.size());
            for(TenantClientDefinition definition : list){
                try{
                    defs.add(tenantBuilder.buildTenantClient(definition));
                }catch (Exception ex){
                    logger.error("Invalid tenant client definition: " + definition, ex);
                }
            }
            DefaultTenantClient[] ret = new DefaultTenantClient[defs.size()];
            return defs.toArray(ret);
        }

        return new DefaultTenantClient[0];
    }

    public DefaultProductRental[] loadProductRendtal(Long tenantId) {
        List<TenantProductDefinition> list = null;
        try{
            list = tenantProvider.getTenantProductDefinitions(tenantId);
        } catch (Throwable t){
            logger.error("Failed to load product rental definitions of tenant Id: " + tenantId, t);
        }
        if(list != null){
            ArrayList<DefaultProductRental> defs = new ArrayList<>(list.size());
            for(TenantProductDefinition definition : list){
                try{
                    defs.add(tenantBuilder.buildProductRental(definition));
                }catch (Exception ex){
                    logger.error("Invalid product rental definition: " + definition, ex);
                }
            }
            DefaultProductRental[] ret = new DefaultProductRental[defs.size()];
            return defs.toArray(ret);
        }

        return new DefaultProductRental[0];
    }
}
