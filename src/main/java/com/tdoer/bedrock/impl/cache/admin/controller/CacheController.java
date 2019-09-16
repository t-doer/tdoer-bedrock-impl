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
package com.tdoer.bedrock.impl.cache.admin.controller;

import com.tdoer.bedrock.impl.cache.CacheManager;
import com.tdoer.bedrock.impl.cache.DormantCacheCleaner;
import com.tdoer.bedrock.impl.cache.admin.vo.CacheManagerVo;
import com.tdoer.springboot.rest.GenericResponseData;
import com.tdoer.springboot.rest.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2017-09-19
 */

@RequestMapping("/cache")
@RestController
public class CacheController {

    @Autowired
    private DormantCacheCleaner cacheCleaner;

    @GetMapping("/managers")
    public GenericResponseData<List<CacheManagerVo>> listCacheManager(){
        ArrayList<CacheManager> list = new ArrayList<>();
        cacheCleaner.listCacheManagers(list);
        ArrayList<CacheManagerVo> ret = new ArrayList<>();
        CacheManagerVo vo = null;
        for(CacheManager manager : list){
            vo = new CacheManagerVo();
            vo.setName(manager.getClass().getName());
            vo.setStatus(manager.getStatus());
            vo.setCacheSize(manager.getCacheSize());
            vo.setCacheKeys(manager.getKeys());
            ret.add(vo);
        }
        return new GenericResponseData<>(ret);
    }

    @GetMapping("/manager/{name}")
    public GenericResponseData<Map> showCache(
            @PathVariable String name
    ) {
        HashMap map = null;
        ArrayList<CacheManager> list = new ArrayList<>();
        cacheCleaner.listCacheManagers(list);
        for(CacheManager manager : list){
            if(manager.getClass().getName().equals(name)){
                List keys = manager.getKeys();
                map = new HashMap(keys.size());
                for(Object key : keys){
                    map.put(key, manager.getSource(key));
                }
            }
        }

        return new GenericResponseData(map);
    }

    @PostMapping("/cleanDormantCache")
    public ResponseData cleanDormant(){
        try{
            cacheCleaner.cleanDormantCache();
            return ResponseData.ok();
        }catch (Throwable t){
            return ResponseData.internalServerError().data(t);
        }
    }

    @PostMapping("/emptyCache")
    public ResponseData emptyCache(){
        try{
            cacheCleaner.cleanAllCache();
            return ResponseData.ok();
        }catch (Throwable t){
            return ResponseData.internalServerError().data(t);
        }
    }
}
