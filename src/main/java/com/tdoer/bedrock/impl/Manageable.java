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
package com.tdoer.bedrock.impl;

import com.tdoer.springboot.error.BusinessException;

/**
 * A manageable instance will be created, initialized, destroyed, and accordingly
 * has 3 status: NEW, INITIALIZED and DESTROYED. Only when an instance is
 * initialized successfully, it will be valid and its methods will be ready to
 * be called. Once a manageable instance is destroyed, it will become invalid.
 * Except for the 2 methods: {@link #getStatus()} and {@link #isValid()}, 
 * {@link InvalidStatusException} will be thrown to call any other methods of
 * a manageable instance.
 * <p>
 * The interface is the main interface to support tool's hot-deployment feature
 * in the Bedrock framework. Since a tool will be unloaded and reloaded, any other
 * tool should not keep reference to a tool's provider, page, template, configuration
 * or the tool itself with member attribute or field. All the references to a tool
 * should be released once a method finishes the calling to a tool.
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public interface Manageable{
	int STATUS_NEW = 0;
	int STATUS_INITIALIZED = 1;
	int STATUS_DESTROYED = -1;
	
	/**
	 * Initialize the manageable instance. The method should be called only once. If
	 * the manageable instance is initialized successfully, its status will be switched
	 * from {@link #STATUS_NEW} to {@link #STATUS_INITIALIZED}, otherwise a {@link BusinessException}
	 * will be thrown.
	 * 
	 * @throws BusinessException
	 */
	void initialize() throws BusinessException;
	
	/**
	 * Destroy the manageable instance. Once the method is called, the instance's status
	 * will be switched to {@link #STATUS_DESTROYED}. The method should be called only after
	 * the instance was initialized successfully, otherwise {@link InvalidStatusException} 
	 * will be thrown.
	 */
	void destroy();
	
	/**
	 * Returns the status of the manageable instance, {@link #STATUS_NEW}, 
	 * {@link #STATUS_INITIALIZED}, or {@link #STATUS_DESTROYED}.
	 * 
	 * @return the manageable instance's status
	 */
	int getStatus();
	
	/**
	 * Check whether the manageable instance is valid or not. A manageable instance is
	 * valid only when its status is {@link #STATUS_INITIALIZED}.
	 * 
	 * @return true if valid, otherwise false
	 */
	boolean isValid();
}
