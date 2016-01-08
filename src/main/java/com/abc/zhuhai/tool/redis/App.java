package com.abc.zhuhai.tool.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Hello world!
 *
 */
@ContextConfiguration(locations = "classpath:spring/spring-config-redis.xml")  
@RunWith(SpringJUnit4ClassRunner.class)
public class App 
{
	@Resource
	RedisCache redisCache;
	
	@Resource
    private RedisTemplate<String, Object> redisTemplate;
    /*public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
    }*/
    
    @Test
    public void testRedis(){
    	redisCache.put("foo", "abc");
    	ValueWrapper obj=redisCache.get("foo");
        System.out.println((String)obj.get());
    }
    
    @Test
    public void testTemplate(){
    	// String读写
        redisTemplate.delete("myStr");
        redisTemplate.opsForValue().set("myStr", "http://yjmyzz.cnblogs.com/");
        System.out.println(redisTemplate.opsForValue().get("myStr"));
        System.out.println("---------------");
        
     // List读写
        redisTemplate.delete("myList");
        redisTemplate.opsForList().rightPush("myList", "A");
        redisTemplate.opsForList().rightPush("myList", "B");
        redisTemplate.opsForList().leftPush("myList", "0");
        List<Object> listCache = redisTemplate.opsForList().range("myList", 0, -1);
        for (Object s : listCache) {
            System.out.println(s);
        }
        System.out.println("---------------");

        // Set读写
        redisTemplate.delete("mySet");
        redisTemplate.opsForSet().add("mySet", "A");
        redisTemplate.opsForSet().add("mySet", "B");
        redisTemplate.opsForSet().add("mySet", "C");
        Set<Object> setCache = redisTemplate.opsForSet().members(
                "mySet");
        for (Object s : setCache) {
            System.out.println(s);
        }
        System.out.println("---------------");

        // Hash读写
        redisTemplate.delete("myHash");
        redisTemplate.opsForHash().put("myHash", "PEK", "北京");
        redisTemplate.opsForHash().put("myHash", "SHA", "上海虹桥");
        redisTemplate.opsForHash().put("myHash", "PVG", "浦东");
        Map<Object, Object> hashCache = redisTemplate.opsForHash()
                .entries("myHash");
        for (Map.Entry<Object, Object> entry : hashCache.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

        System.out.println("---------------");
    }
    
    @Test
    public void testKeys(){
    	List<String> lst=this.redisCache.keys("f*");
    	for(String str:lst){
    		System.out.println(str);
    	}
    }
    
    @Test
    public void testEvict(){
    	redisCache.put("foo", "abc");
    	ValueWrapper obj=redisCache.get("foo");
        System.out.println((String)obj.get());
        List<String> lst=this.redisCache.keys("fo*");
        this.redisCache.evict(lst);
        lst=this.redisCache.keys("fo*");
        for(String str:lst){
    		System.out.println(str);
    	}
    }
    
    public static void main(String[] args) {  
        
        String s = "<pre class=\"brush: java;\">";  
        //System.out.println(StringEscapeUtils.escapeHtml(s));  
          
//        String u = "<pre class="brush: java;">";  
//        System.out.println(StringEscapeUtils.unescapeHtml(u));  
    }
}
