# T-Doer Bedrock SaaS Framework Implementation

The project provides the standard implementation of Bedrock SaaS core interfaces:

- com.tdoer.bedrock.application.ApplicationRepository
- com.tdoer.bedrock.context.ContextCenter
- com.tdoer.bedrock.context.ContextPathParser
- com.tdoer.bedrock.product.ProductRepository
- com.tdoer.bedrock.service.ServiceRepository
- com.tdoer.bedrock.tenant.RentalCenter

The applications, which run on T-Doer framework, need to implements the service providers:

- com.tdoer.cloudfw.impl.provider.ApplicationProvider
- com.tdoer.cloudfw.impl.provider.ContextProvider
- com.tdoer.cloudfw.impl.provider.ProductProvider
- com.tdoer.cloudfw.impl.provider.ServiceProvider
- com.tdoer.cloudfw.impl.provider.TenantProvider

## Todo

- XXX Builder's definition verification
- Error codes review
- Read Cache Policy from local configuration
- CacheManager messages
- ExtensionDomains, set status according to init values
