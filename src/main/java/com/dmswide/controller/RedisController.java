package com.dmswide.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class RedisController {
    /**
     * 注入RedisTemplate
     * 如果带泛型参数只有两种参数：String或者Object
     * RedisTemplate<String,String>
     * RedisTemplate<Object,Object>
     */

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //添加数据到redis
    @PostMapping("/redis/add")
    public String addToRedis(@RequestParam(value = "key",required = false) String key,
                            @RequestParam(value = "value",required = false) String value){
        //操作string类型的数据
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //添加数据到redis
        valueOperations.set(key,value);
        return "向redis中添加数据";

    }
    //从redis中获取数据
    @GetMapping("/redis/get")
    public String getValue(@RequestParam(value = "key",required = false) String k){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return "key是: " + k + " 值是: " + valueOperations.get(k);
    }

    @PostMapping("/redis_1/{k1}/{v1}")
    public String addStringKV(@PathVariable(value = "k1") String k,
                              @PathVariable(value = "v1") String v){

        stringRedisTemplate.opsForValue().set(k,v);
        return "使用stringRedisTemplate添加数据";
    }

    @GetMapping("/redis_1/{k1}")
    public String getStringV(@PathVariable(value = "k1") String k){
        return "key是: " + " 值是: " + stringRedisTemplate.opsForValue().get(k);
    }
}
