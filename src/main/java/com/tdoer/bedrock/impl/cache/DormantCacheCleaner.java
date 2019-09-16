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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class DormantCacheCleaner implements Runnable{

	protected Logger logger = LoggerFactory.getLogger(DormantCacheCleaner.class);
	protected CachePolicy cachePolicy;
	protected ArrayList<CacheManager> cacheManagers;
	protected boolean goon = true;
	protected byte[] lock;

	public DormantCacheCleaner(CachePolicy cachePolicy){
		this.cachePolicy = cachePolicy;
		cacheManagers = new ArrayList<CacheManager>();
		lock = new byte[0];
	}

	public void registerCacheManager(CacheManager cacheManager){
		synchronized(lock){
			cacheManagers.add(cacheManager);

			if(logger.isDebugEnabled()){
				logger.debug("Register cache manager - {}", cacheManager);
			}
		}
	}

	public void unregisterCacheManager(CacheManager cacheManager){
		synchronized(lock){
			if(cacheManagers.remove(cacheManager)){
				if(logger.isDebugEnabled()){
					logger.debug("Unregister cache manager - {}", cacheManager);
				}
			}
		}
	}

	public void listCacheManagers(List<CacheManager> list){
        synchronized(lock){
            for(CacheManager manager : cacheManagers){
                list.add(manager);
            }
        }
	}

	public void cleanAllCache(){
	    ArrayList<CacheManager> list = new ArrayList<>();
	    listCacheManagers(list);
        emptyCache(list);
    }

    public void cleanDormantCache(){
        ArrayList<CacheManager> list = new ArrayList<>();
        listCacheManagers(list);
        cleanDormantCache(list);
    }

	public void stop(){
		synchronized(lock){
			goon = false;
		}
	}

	@Override
	public void run() {
		// at least 1 minute
		long interval = Math.max(60*1000, cachePolicy.getCleanInterval());
		ArrayList<CacheManager> list = new ArrayList<CacheManager>();

		if(logger.isDebugEnabled()){
			logger.debug("Start working ...");
		}

		while(goon){
			if(logger.isDebugEnabled()){
				logger.debug("Cleaning dormant cache ...");
			}

            listCacheManagers(list);
			cleanDormantCache(list);
			list.clear();

			if(logger.isDebugEnabled()){
				logger.debug("Finished cleaning dormant cache successfully.");
			}
			try{
				if(logger.isDebugEnabled()){
					logger.debug("Sleep {} milliseconds after cleaning dormant cache", interval);
				}

				Thread.sleep(interval);
			}catch(Exception ex){
				// ignore
			}
		}

		if(logger.isDebugEnabled()){
			logger.debug("Finished working");
		}
	}


	protected void cleanDormantCache(ArrayList<CacheManager> list){
		//Note, some cache cleaner may have already been destroyed
		for(CacheManager cleaner : list){
			if(cleaner.isValid()){
				try{
					if(logger.isDebugEnabled()){
						logger.debug("Cache manager ({}): cleaning dormant cache ...", cleaner);
					}

					cleaner.cleanDormantCache();

					if(logger.isDebugEnabled()){
						logger.debug("Cache manager ({}): finished cleaning dormant cache successfully.", cleaner);
					}

				}catch(Throwable ex){
					if(logger.isDebugEnabled()){
						logger.debug("Cache manager ({}): Failed to finish cleaning dormant cache.", cleaner);
					}
					logger.trace("Exception occured when cleaning dormant objects", ex);
				}
			}
		}
	}

    protected void emptyCache(ArrayList<CacheManager> list){
        //Note, some cache cleaner may have already been destroyed
        for(CacheManager cleaner : list){
            if(cleaner.isValid()){
                try{
                    if(logger.isDebugEnabled()){
                        logger.debug("Cache manager ({}): cleaning all cache ...", cleaner);
                    }

                    cleaner.cleanAll();

                    if(logger.isDebugEnabled()){
                        logger.debug("Cache manager ({}): finished cleaning all cache successfully.", cleaner);
                    }

                }catch(Throwable ex){
                    if(logger.isDebugEnabled()){
                        logger.debug("Cache manager ({}): Failed to finish cleaning all cache.", cleaner);
                    }
                    logger.trace("Exception occured when cleaning all objects", ex);
                }
            }
        }
    }
}
