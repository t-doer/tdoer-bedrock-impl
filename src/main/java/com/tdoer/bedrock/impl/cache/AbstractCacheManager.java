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
package com.tdoer.bedrock.impl.cache;

import com.tdoer.bedrock.impl.InvalidStatusException;
import com.tdoer.bedrock.impl.Manageable;
import com.tdoer.bedrock.impl.Modifiable;
import com.tdoer.springboot.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Features of cache manager:
 * <ul>
 * <li>Source is wrapped up with {@link CacheEntity}, cache will become dormant;</li>
 * <li>To promote performance, <code>null</code> or error will also be cached to avoid loading source frequently;</li>
 * <li>Provide {@link #cleanDormantCache()} method, so that dormant cache can be cleaned periodically by outside cleaner;</li>
 * <li>The cache manager is {@link Manageable}, so it will be initialized and destroyed by cache manager holder;</li>  
 * </ul>
 * Note, the implementation must be thread-safe
 * @param <Key>
 * @param <Source>
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public abstract class AbstractCacheManager<Key, Source> implements CacheManager<Key, Source> {
	protected CachePolicy cachePolicy;
	protected HashMap<Key, CacheEntity<Source>> cache;
	protected int status;
	protected DormantCacheCleaner cleaner;
	protected Logger logger = LoggerFactory.getLogger(AbstractCacheManager.class);
	
	public AbstractCacheManager(CachePolicy cachePolicy, DormantCacheCleaner cleaner){
		Assert.notNull(cachePolicy, "CachePolicy cannot be null");
		Assert.notNull(cleaner, "DormantObjectCleaner cannot be null");
		this.cachePolicy = cachePolicy;
		this.cleaner = cleaner;
	}
	
	@Override
    public void initialize() throws ErrorCodeException {
		this.cache = new HashMap<>();
		status = Manageable.STATUS_INITIALIZED;
		
		if(logger.isDebugEnabled()){
			logger.debug("CacheManager ({}) is initialized", this);
		}

		// register the cache manager to DormantObjectCleaner;
		register();
    }
	
	@Override
    public synchronized int getStatus() {
		return status;
  }

	@Override
    public synchronized boolean isValid() {
		return (status == Manageable.STATUS_INITIALIZED);
  }
	
	@Override
    public synchronized void destroy() {

		checkStatus();
		unregister();
  		cleanCache();
		status = Manageable.STATUS_DESTROYED;
		
		if(logger.isDebugEnabled()){
			logger.debug("CacheManager ({}) is destroyed", this);
		}
    }

    @Override
    public synchronized Source getSource(Key key) throws ErrorCodeException {

        checkStatus();

        if(logger.isDebugEnabled()){
            logger.debug("{}: Getting source for the request '{}'", this, key);
        }

        boolean loaded = false;
        Source ret = null;
        CacheEntity<Source> cacheEnt = getCacheEntity(key);
        if(cacheEnt != null && !cacheEnt.isExpired()){
            if(logger.isDebugEnabled()){
                logger.debug("{}: Cache entity was not expired, of request '{}': {}", this, key, cacheEnt);
            }

            if(cacheEnt.getError() != null){
                throw cacheEnt.getError();
            }else{
                ret = cacheEnt.getSource();

                if(logger.isDebugEnabled()){
                    logger.debug("{}: Returned cached source for the request '{}': {}", this, key, ret);
                }

                return ret;
            }
        }else{
            try{
                if(cacheEnt == null){
                    // load it
                    ret = loadSource(key);
                    loaded = true;

                    if(logger.isDebugEnabled()){
                        logger.debug("{}: Loaded source for the request '{}': {}", this, key, ret);
                    }

                }else{ // cache expired
                    if(logger.isDebugEnabled()){
                        logger.debug("{}: Cache entity was expired, of request '{}': {}", this, key, cacheEnt);
                    }

                    ret = cacheEnt.getSource();
                    if(ret != null){
                        if(ret instanceof Modifiable && ((Modifiable)ret).isModified()){

                            if(logger.isDebugEnabled()){
                                logger.debug("{}: Cached source was modified, of request '{}': {}", this, key, ret);
                            }

                            // reload it
                            ret = reloadSource(key, ret);
                            loaded = true;

                            if(logger.isDebugEnabled()){
                                logger.debug("{}: Reloaded source for request '{}': {}", this, key, ret);
                            }

                        }else{
                            // refresh time
                            cacheEnt.refreshCacheTime();

                            if(logger.isDebugEnabled()){
                                logger.debug("{}: Cached source was not modified, refreshed cache time for request '{}': {}", this, key, cacheEnt);
                            }
                        }
                    }else{
                        // load it
                        ret = loadSource(key);
                        loaded = true;

                        if(logger.isDebugEnabled()){
                            logger.debug("{}: Reloaded source, since last failed, for request '{}': {}", this, key, ret);
                        }
                    }
                }

                if(loaded){
                    // ret may be null here
                    cacheEnt = new CacheEntity<Source>(ret, null, cachePolicy);
                    putCacheEntity(key, cacheEnt);
                }
            }catch(ErrorCodeException ex){
                if(logger.isDebugEnabled()){
                    // I18n: {0}: Failed to load source for request \"{1}\", because of the error: {2}.
                    logger.debug("{}: Failed to load source for request '{}', because of the error: {}", this, key, ex);
                }

                cacheEnt = new CacheEntity<Source>(null, ex, cachePolicy);
                putCacheEntity(key, cacheEnt);
                throw ex;
            }
        }

        return ret;
    }

    @Override
	public synchronized List<Key> getKeys(){
		
		checkStatus();
		
		List<Key> list = new ArrayList<Key>();
		for(Key key : cache.keySet()){
			list.add(key);
		}
		return list;
	}

	@Override
	public synchronized long getCacheSize() {
		return cache.size();
	}

    @Override
    public synchronized void cleanAll() {
        cleanCache();
    }

    @Override
	public synchronized void cleanDormantCache(){
		
		checkStatus();
		
		ArrayList<Key> list = new ArrayList<Key>();
		listDormantEntities(list);
	  
		if(logger.isDebugEnabled()){
		  // I18n: Cache cleaner ({0}): total {1} cache entities to clean.
			logger.debug("Cache Manager ({}): Total {} cache entities to clean",  this, list.size());
		}
		
		CacheEntity<Source> ent = null;
		for(Key k : list){
			ent = removeCacheEntity(k);
			
			if(logger.isDebugEnabled()){
			  // I18n: Cache cleaner ({0}): cache entity (key = value) - \"{1}\" = \"{2}\".
				logger.debug("Cache Manager ({}): Cache entity (key, value) - ({}, {}", this, k, ent);
			}
			
	    if(ent != null && ent.getSource() != null){
	    	destroySource(ent.getSource());
	    }
		}
	}
	
	public synchronized void dump(PrintWriter writer){
		
		checkStatus();
		
		dumpCache(writer);
	}
	
	protected void checkStatus() throws InvalidStatusException{
		if(status != Manageable.STATUS_INITIALIZED){
			// I18n: Cache manager is not valid, its current status is \"{0}\".
			throw new InvalidStatusException("Cache Manager (" + this.getClass() + ") is not initialized yet");
		}
	}
	
	protected void register(){
		cleaner.registerCacheManager(this);
	}
	
	protected void unregister(){
		cleaner.unregisterCacheManager(this);
	}
	
    protected CacheEntity<Source> getCacheEntity(Key key) {
		return cache.get(key);
  }

    protected void putCacheEntity(Key key, CacheEntity<Source> entity) {
		cache.put(key, entity);
  }

    protected CacheEntity<Source> removeCacheEntity(Key key) {
	  return cache.remove(key);
  }

    protected void dumpCache(PrintWriter writer) {
		CacheEntity<Source> ent = null;
		int size = cache.size();
		writer.print("Total ");
		writer.print(size);
		if(size > 1){
			writer.println(" entities.");
		}else{
			writer.println(" entity.");
		}

		if(size > 0) {
			for(Key key : cache.keySet()){
				ent = cache.get(key);
				writer.print(key);
				writer.print(" = ");
				writer.print(ent);
			}
		}
    }

    protected void cleanCache() {
	    CacheEntity<Source> cacheEnt = null;
	    for(Key key : cache.keySet()){
		    cacheEnt = cache.get(key);
		    if(cacheEnt.getSource() != null){
			    destroySource(cacheEnt.getSource());
		    }
	    }
	    cache.clear();
		cache = null;
    }

    protected void listDormantEntities(List<Key> keyList) {
		CacheEntity<Source> ent = null;
		for(Key key : cache.keySet()){
			ent = cache.get(key);
		if(ent.isDormant()){
			keyList.add(key);
		}
		}
    }

    abstract protected Source loadSource(Key key) throws ErrorCodeException;

    abstract protected Source reloadSource(Key key, Source oldSource) throws ErrorCodeException;

    abstract protected void destroySource(Source source);
}
