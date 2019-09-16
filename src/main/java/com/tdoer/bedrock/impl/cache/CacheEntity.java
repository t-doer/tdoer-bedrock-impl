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

import com.tdoer.springboot.error.ErrorCodeException;

import java.util.Calendar;

/**
 * The class is used to cache entity with a duration. It will be expired
 * when the cache duration from it's created is longer than allowed cache
 * duration. It will never be expired if its allowed cache duration is less
 * than zero. The class is also allow setting the allowed dormant duration.
 * If its source is not queried longer than the allowed dormant duration,
 * it will exceed dormant duration, and cache manager can clean such dormant
 * cache entity.
 * <p>
 * Note, the class must be multi-thread safe.
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class CacheEntity <T> {
	protected final ErrorCodeException error;
	protected final T source;
	protected final CachePolicy cachePolicy;
    protected long cachedAt;
    protected long lastQueriedAt;

	public CacheEntity(T source, ErrorCodeException error, CachePolicy cachePolicy){
		this.source = source;
		this.error = error;
		this.cachePolicy = cachePolicy;
		cachedAt = System.currentTimeMillis();
		lastQueriedAt = cachedAt;
	}

	/**
	 * Returns dormancy duration
	 * 
	 * @return the dormancy duration
	 */
	public long getDormancyDuration(){
		return cachePolicy.getAllowedDormantDuration();
	}

    /**
     * Returns cache duration, if it's over, the cache entity will be expired
     * @return cache duration
     */
	public long getCacheDuration() {
	    return cachePolicy.getCacheDuration();
    }
	
	/**
	 * Return the source cached, it will be <code>null</source>
	 * if there is error.
	 * 
	 * @return the source
	 * @see #getError()
	 */
	public T getSource() {
		setLastQueriedAt();
		return source;
	}
	
	/**
	 * Return the error which caused cache source to be <code>null</source>, it will be <code>null</source>
	 * if cache source is not <code>null</source>.
	 * 
	 * @return the error
	 * @see #getSource()
	 */
	public ErrorCodeException getError() {
		return error;
	}
	
	public boolean isDormant(){
		if(cachePolicy.getAllowedDormantDuration() < 0){
			return false;
		}
		
		long elapse = System.currentTimeMillis() - getLastQueriedAt();
		return cachePolicy.getAllowedDormantDuration() < elapse;
	}

    /**
     * <p>
     * Is the cache entity expired? If its cache duration is not over, it's <code>true</code>,
     * otherwise <code>false</code>.
     * </p>
     * <p>
     * Note if its cache duration is set less zero, the definition will never expired.
     * </p>
     *
     * @return <code>true</code> or <code>false</code>
     */
    public boolean isExpired(){
        if(cachePolicy.getCacheDuration() < 0){
            return false;
        }

        long elapse = System.currentTimeMillis() - cachedAt;
        return cachePolicy.getCacheDuration() < elapse;
    }

    synchronized public long getCacheTime(){
		return cachedAt;
	}
	
	synchronized public long getLastQueriedAt(){
		return lastQueriedAt;
	}
	
	synchronized public void setLastQueriedAt(){
		lastQueriedAt = System.currentTimeMillis();
	}

    /**
     * Refresh cache time and cache expiration will be calculated in a new rough
     */
    synchronized public void refreshCacheTime(){
        cachedAt =  System.currentTimeMillis();
    }

	@Override
    public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("CacheEntity[");

		if(isDormant()){
			sb.append("Dormant / ");
		}

        if(isDormant()){
            sb.append("Expired / ");
        }

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getCacheTime());
		sb.append(cal.getTime());
		
		sb.append(" / ");
		
		cal.setTimeInMillis(getLastQueriedAt());
		sb.append(cal.getTime());
		
		sb.append(" / ");
		
		if(error != null){
			sb.append("\"");
			sb.append(error.getMessage());
			sb.append("\"");
		}else{
			sb.append(source);
		}
		
		sb.append("]");
		
		return sb.toString();
    }
}
