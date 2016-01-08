package com.abc.zhuhai.tool.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCache {  
	
	@Resource
    private RedisTemplate<String, Object> redisTemplate;  
    private String name;  
  
    public RedisTemplate<String, Object> getRedisTemplate() {  
        return redisTemplate;  
    }  
  
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
      
    public String getName() {  
        // TODO Auto-generated method stub  
        return this.name;  
    }  
  
      
    public Object getNativeCache() {  
        // TODO Auto-generated method stub  
        return this.redisTemplate;  
    }  
  
      
    public ValueWrapper get(Object key) {  
        // TODO Auto-generated method stub  
        final String keyf = (String) key;  
        Object object = null;  
        object = redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
  
                byte[] key = keyf.getBytes();  
                byte[] value = connection.get(key);  
                if (value == null) {  
                    return null;  
                }  
                return toObject(value);  
  
            }  
        });  
        return (object != null ? new SimpleValueWrapper(object) : null);  
    }  
  
      
    public void put(Object key, Object value) {  
        // TODO Auto-generated method stub  
        final String keyf = (String) key;  
        final Object valuef = value;  
        final long liveTime = 86400;  
  
        redisTemplate.execute(new RedisCallback<Long>() {  
            public Long doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                byte[] keyb = keyf.getBytes();  
                byte[] valueb = toByteArray(valuef);  
                connection.set(keyb, valueb);  
                if (liveTime > 0) {  
                    connection.expire(keyb, liveTime);  
                }  
                return 1L;  
            }  
        });  
    }  
  
    /** 
     * 描述 : <Object转byte[]>. <br> 
     * <p> 
     * <使用方法说明> 
     * </p> 
     *  
     * @param obj 
     * @return 
     */  
    private byte[] toByteArray(Object obj) {  
        byte[] bytes = null;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        try {  
            ObjectOutputStream oos = new ObjectOutputStream(bos);  
            oos.writeObject(obj);  
            oos.flush();  
            bytes = bos.toByteArray();  
            oos.close();  
            bos.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }  
        return bytes;  
    }  
  
    /** 
     * 描述 : <byte[]转Object>. <br> 
     * <p> 
     * <使用方法说明> 
     * </p> 
     *  
     * @param bytes 
     * @return 
     */  
    private Object toObject(byte[] bytes) {  
        Object obj = null;  
        try {  
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);  
            ObjectInputStream ois = new ObjectInputStream(bis);  
            obj = ois.readObject();  
            ois.close();  
            bis.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        } catch (ClassNotFoundException ex) {  
            ex.printStackTrace();  
        }  
        return obj;  
    }  
  
      
    public void evict(Object key) {  
        // TODO Auto-generated method stub  
        final String keyf = (String) key;  
        redisTemplate.execute(new RedisCallback<Long>() {  
            public Long doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                return connection.del(keyf.getBytes());  
            }  
        });  
    }
    
	public void evict(List<String> keys) {
		// TODO Auto-generated method stub
		for (final String keyf : keys) {
			redisTemplate.execute(new RedisCallback<Long>() {
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.del(keyf.getBytes());
				}
			});
		}
	}
  
      
    public void clear() {  
        // TODO Auto-generated method stub  
        redisTemplate.execute(new RedisCallback<String>() {  
            public String doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                connection.flushDb();  
                return "ok";  
            }  
        });  
    }
    
    public List<String> keys(String str){
    	final String[] a1={str};
    	List<String> lst=redisTemplate.execute(new RedisCallback<List<String>>() { 
            public List<String> doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                Set<byte[]> tt=connection.keys((a1[0]).getBytes()); 
                List<String> list=new ArrayList<String>();
                for(byte[] t:tt){
                	String obj=new String(t);
                	list.add(obj);
                }
                return list;  
            }  
        });
    	return lst;
    }
  
} 
