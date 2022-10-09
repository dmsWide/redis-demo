package com.dmswide.controller;

import com.dmswide.entity.Student;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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

    /**
     * 设置RedisTemplate的序列化机制
     * 可以单独设置key或value的序列化
     * 也可以同时设置key和value的序列化
     */

    @PostMapping("/redis_2/{k}/{v}")
    public String addStringToRedis(@PathVariable(value = "k") String key,
                                   @PathVariable(value = "v") String value){
        //设置key和value的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key,value);
        return "设置redisTemplate的key和value的系列化机制";
    }

    /**
     * 使用json序列化，将javabean对象序列化为json字符串
     */

    @PostMapping("/redis_3/json")
    public String addJson(){
        Student student = new Student();
        student.setId(0001);
        student.setName("sunquan");
        student.setAge(20);

        //设置序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Student.class));
        redisTemplate.opsForValue().set("stu",student);
        return "使用了json序列化机制";
    }

    @GetMapping("/redis_3/getJson/{key}")
    public String getJson(@PathVariable(value = "key") String key){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Student.class));
        return "反序列化的结果是: " + redisTemplate.opsForValue().get(key);
    }
}
