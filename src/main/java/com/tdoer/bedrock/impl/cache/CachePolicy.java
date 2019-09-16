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
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class CachePolicy{
	
	protected long cleanInterval;
	protected long cacheDuration;
	protected long allowedDormantDuration;
	
	public CachePolicy(long cleanInterval, long cacheDuration, long allowedDormantDuration){
		this.cleanInterval = cleanInterval;
		this.cacheDuration = cacheDuration;
		this.allowedDormantDuration = allowedDormantDuration;
	}
	
	/**
	 * Returns the interval (milliseconds) to clean dormant cache entity.
	 * 
	 * @return  the interval to clean dormant cache entity
	 */
	public long getCleanInterval(){
		return cleanInterval;
	}
	
	/**
	 * Returns the duration (milliseconds) to cache an entity. If an entity is cached longer than the duration,
	 * the entity's cache will be deemed expired and the entity should be reload when it's requested. If the duration is
	 * set less than zero, cache will never be reloaded.
	 * 
	 * @return  the duration to cache an entity
	 */
	public long getCacheDuration(){
		return cacheDuration;
	}
	
	/**
	 * Returns the allowed dormant duration (milliseconds). If an entity is not requested longer than the dormant duration,
	 * it will be deemed dormant and cleaned out of cache manager in a clean interval.
	 * 
	 * @see #getCleanInterval()
	 * @return the dormant duration
	 */
	public long getAllowedDormantDuration(){
		return allowedDormantDuration;
	}
}