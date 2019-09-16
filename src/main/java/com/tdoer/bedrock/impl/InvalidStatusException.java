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

/**
 * An unchecked exception indicating that an attempt to access
 * a manageable object that is no longer valid.
 * <p>
 * This exception is thrown by methods on manageable
 * objects.
 * </p>
 *
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */
public class InvalidStatusException extends RuntimeException{

  private static final long serialVersionUID = 1L;

	public InvalidStatusException(String msg){
		super(msg);
	}
}
