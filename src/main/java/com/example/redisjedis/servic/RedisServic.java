package com.example.redisjedis.servic;/**
 * Created with IntelliJ IDEA.
 * User: 张渡ssss
 * Date: 2020/3/11
 * Time: 18:10
 * Description: No Description
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version 1.0
 * @author： 张渡
 * @date 2020/3/11 18:10
 * Modified By： 修改人姓名(如果有其他人修改时增加这三项)
 * Modified Date: 修改日期
 * jedis 企业开发工具类封装
 */
@Service
public class RedisServic {

    /*注入连接池*/
    @Autowired
    private JedisPool jedisPool;

    /*判断一个 Key 是否存在*/
    public boolean exists(String key){
           Jedis jedis=null;
           boolean result;
        try {
            jedis=jedisPool.getResource();
            result= jedis.exists(key);//看连接池是否有这个key
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }
    /**
    * @author         张渡
    * @version        1.0
    * @date           2020/3/11 19:10
    * Modified By    修改人姓名(如果有其他人修改时增加这三项)
    * Modified Date: 修改日期
    * 删除key
    */

    public Long del(String... keys){
        Jedis jedis=null;
        Long result;
        try {
            jedis=jedisPool.getResource();//封装
            result=jedis.del(keys);//删除这个key
        } finally {
            jedis.close();
        }
        return result;
    }
    /**
     * KEYS pattern 通配符模式匹配
     * 查找所有符合给定模式 pattern 的 key 。
     * KEYS * 匹配数据库中所有 key 。
     * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
     * KEYS h*llo 匹配 hllo 和 heeeeello 等。
     * KEYS 的速度非常快，但在一个大的数据库中使用它仍然可能造成性能问题，如果你需要从一个数据集中查找特定的 key ，
     * 你最好还是用 Redis 的集合结构(set)来代替。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param pattern
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        Set<String> result ;
        try {
            jedis = jedisPool.getResource();
            result = jedis.keys(pattern);
        }  finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }
    /**
     * 设置过期时间
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param seconds
     * @return       Long 1：表示设置成功，0：设置失败
     * @throws
     */
    public Long expire(String key,int seconds){
        Jedis jedis=null;
        Long result=0L;
        try {
            jedis=jedisPool.getResource();
            if(seconds>0){
                result=jedis.expire(key,seconds);
            }
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }
    /**
     * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(永不过期的 key )
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Long 当生存时间移除成功时，返回 1 .如果 key 不存在或 key 没有设置生存时间，返回 0
     * @throws
     */
    public Long persist(String key) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result=jedis.persist(key);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Long 当 key 不存在时，返回 -2 。当 key 存在但没有设置剩余生存时间时，返回 -1 。否则，以秒为单位，返回 key
     *      *         的剩余生存时间
     * @throws
     */
    public Long ttl(String key) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result=jedis.ttl(key);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }
    //**************String数据类型********************
    /**
     * 获取指定Key的Value。如果与该Key关联的Value不是string类型，Redis将抛出异常，
     * 因为GET命令只能用于获取string Value，如果该Key不存在，返回null
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       成功返回value 失败返回null
     * @throws
     */
    public String get(String key) {
        Jedis jedis = null;
        String value ;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return value;
    }

    /**
     * 设定该Key持有指定的字符串Value，如果该Key已经存在，则覆盖其原有值。总是返回"OK"。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param value
     * @param expire 过期时间秒
     * @return       void
     * @throws
     */
    public String set(String key, String value,int expire) {

        Jedis jedis = null;
        String result;
        try {
            jedis = jedisPool.getResource();
            result=jedis.set(key,value);
            if(expire>0){
                jedis.expire(key, expire);
            }
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }
    /**
     * 加锁操作：jedis.set(key,value,"NX","EX",timeOut)【保证加锁的原子操作】
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key key就是redis的key值作为锁的标识，
     * @param value  value在这里作为客户端的标识，
     * @param nxxx NX：只有这个key不存才的时候才会进行操作，if not exists；
     * @param nxxx XX：只有这个key存才的时候才会进行操作，if it already exist；
     * @param expx EX：设置key的过期时间为秒，具体时间由第5个参数决定
     * @param expx PX：设置key的过期时间为毫秒，具体时间由第5个参数决定
     * @param time 通过timeOut设置过期时间保证不会出现死锁【避免死锁】
     * @return       java.lang.String 成功OK不成功null
     * @throws
     */
/*
    public String  set(String key, String value, String nxxx, String expx, long time){
        Jedis jedis=null;
        String result;
        try {
            jedis=jedisPool.getResource();
            result = jedis.set(key, value, nxxx, expx, time);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }*/

    /**
     * 将指定Key的Value原子性的递增1。如果该Key不存在，其初始值为0，在incr之后其值为1。
     * 如果Value的值不能转换为整型值，如Hi，该操作将执行失败并抛出相应的异常。
     * 注意：该操作的取值范围是64位有符号整型；返回递增后的Value值。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Long 加值后的结果
     * @throws
     */
    public Long incr(String key) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.incr(key);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }
    /**
     * 将指定Key的Value原子性的递增1。如果该Key不存在，其初始值为0，在decr之后其值为-1。
     * 如果Value的值不能转换为整型值，如Hi，该操作将执行失败并抛出相应的异常。
     * 注意：该操作的取值范围是64位有符号整型；返回递减后的Value值。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Long 加值后的结果
     * @throws
     */
    public Long decr(String key) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.decr(key);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    //******************hash数据类型*********************
    /**
     * 通过key 和 field 获取指定的 value
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param field
     * @return       java.lang.String
     * @throws
     */
    public String hget(String key, String field) {
        Jedis jedis = null;
        String result ;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, field);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 为指定的Key设定Field/Value对，如果Key不存在，该命令将创建新Key以用于存储参数中的Field/Value对，
     * 如果参数中的Field在该Key中已经存在，则用新值覆盖其原有值。
     * 返回1表示新的Field被设置了新值，0表示Field已经存在，用新值覆盖原有值。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param field
     * @param value
     * @return       java.lang.Long
     * @throws
     */
    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hset(key, field, value);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 判断指定Key中的指定Field是否存在，返回true表示存在，false表示参数中的Field或Key不存在。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param field
     * @return       java.lang.Boolean
     * @throws
     */
    public Boolean hexists(String key, String field) {

        Jedis jedis = null;
        Boolean result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hexists(key, field);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 从指定Key的Hashes Value中删除参数中指定的多个字段，如果不存在的字段将被忽略，
     * 返回实际删除的Field数量。如果Key不存在，则将其视为空Hashes，并返回0。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param fields
     * @return       java.lang.Long
     * @throws
     */
    public Long hdel(String key, String... fields) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hdel(key, fields);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 通过key获取所有的field和value
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.util.Map<java.lang.String,java.lang.String>
     * @throws
     */
    public Map<String, String> hgetall(String key) {
        Jedis jedis = null;
        Map<String, String> result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        } finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 逐对依次设置参数中给出的Field/Value对。如果其中某个Field已经存在，则用新值覆盖原有值。
     * 如果Key不存在，则创建新Key，同时设定参数中的Field/Value。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param hash
     * @return       java.lang.String
     * @throws
     */
    public String hmset(String key, Map<String, String> hash) {

        Jedis jedis = null;
        String result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hmset(key, hash);
        } finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 对应key的字段自增相应的值
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param field
     * @param increment
     * @return       java.lang.Long
     * @throws
     */
    public Long hIncrBy(String key,String field,long increment){

        Jedis jedis=null;
        Long result;
        try {
            jedis=jedisPool.getResource();
            return jedis.hincrBy(key, field, increment);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    //***************List数据类型***************
    /**
     * 向列表左边添加元素。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，之后再将数据从链表的头部插入。
     * 如果该键的Value不是链表类型，该命令将将会抛出相关异常。操作成功则返回插入后链表中元素的数量。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return       java.lang.Long 返回操作的value个数
     * @throws
     */
    public Long lpush(String key, String... strs) {

        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lpush(key, strs);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 向列表右边添加元素。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，之后再将数据从链表的尾部插入。
     * 如果该键的Value不是链表类型，该命令将将会抛出相关异常。操作成功则返回插入后链表中元素的数量。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return       java.lang.Long 返回操作的value个数
     * @throws
     */
    public Long rpush(String key, String... strs) {

        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.rpush(key, strs);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }
    /**
     * 返回并弹出指定Key关联的链表中的第一个元素，即头部元素。如果该Key不存在，
     * 返回nil。LPOP命令执行两步操作：第一步是将列表左边的元素从列表中移除，第二步是返回被移除的元素值。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.String
     * @throws
     */
    public String lpop(String key) {

        Jedis jedis = null;
        String result ;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lpop(key);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 返回并弹出指定Key关联的链表中的最后一个元素，即头部元素。如果该Key不存在，返回nil。
     * RPOP命令执行两步操作：第一步是将列表右边的元素从列表中移除，第二步是返回被移除的元素值。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.String
     * @throws
     */
    public String rpop(String key) {

        Jedis jedis = null;
        String result ;
        try {
            jedis = jedisPool.getResource();
            result = jedis.rpop(key);
        } finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     *该命令的参数start和end都是0-based。即0表示链表头部(leftmost)的第一个元素。
     * 其中start的值也可以为负值，-1将表示链表中的最后一个元素，即尾部元素，-2表示倒数第二个并以此类推。
     * 该命令在获取元素时，start和end位置上的元素也会被取出。如果start的值大于链表中元素的数量，
     * 空链表将会被返回。如果end的值大于元素的数量，该命令则获取从start(包括start)开始，链表中剩余的所有元素。
     * 注：Redis的列表起始索引为0。显然，LRANGE numbers 0 -1 可以获取列表中的所有元素。返回指定范围内元素的列表。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param start
     * @param end
     * @return       java.util.List<java.lang.String>
     * @throws
     */
    public List<String> lrange(String key, long start, long end) {

        Jedis jedis = null;
        List<String> result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lrange(key, start, end);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }



    /**
     * 该命令将返回链表中指定位置(index)的元素，index是0-based，表示从头部位置开始第index的元素，
     * 如果index为-1，表示尾部元素。如果与该Key关联的不是链表，该命令将返回相关的错误信息。 如果超出index返回这返回nil。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param index
     * @return       java.lang.String
     * @throws
     */
    public String lindex(String key, long index) {

        Jedis jedis = null;
        String result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lindex(key, index);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }


    //***************Set数据类型*************
    /**
     * 如果在插入的过程用，参数中有的成员在Set中已经存在，该成员将被忽略，而其它成员仍将会被正常插入。
     * 如果执行该命令之前，该Key并不存在，该命令将会创建一个新的Set，此后再将参数中的成员陆续插入。返回实际插入的成员数量。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return       java.lang.Long 添加成功的个数
     * @throws
     */
    public Long sadd(String key, String... members) {

        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.sadd(key, members);
        } finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }



    /**
     * 判断参数中指定成员是否已经存在于与Key相关联的Set集合中。返回1表示已经存在，0表示不存在，或该Key本身并不存在。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param member
     * @return       java.lang.Boolean
     * @throws
     */
    public Boolean sismember(String key, String member) {

        Jedis jedis = null;
        Boolean result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.sismember(key, member);
        }  finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 通过key获取set中所有的value
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<String> smembers(String key) {

        Jedis jedis = null;
        Set<String> result ;
        try {
            jedis = jedisPool.getResource();
            result = jedis.smembers(key);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }


    //**********Sorted Set 数据类型********************
    /**
     *添加参数中指定的所有成员及其分数到指定key的Sorted Set中，在该命令中我们可以指定多组score/member作为参数。
     * 如果在添加时参数中的某一成员已经存在，该命令将更新此成员的分数为新值，同时再将该成员基于新值重新排序。
     * 如果键不存在，该命令将为该键创建一个新的Sorted Set Value，并将score/member对插入其中。
     * 如果该键已经存在，但是与其关联的Value不是Sorted Set类型，相关的错误信息将被返回。添加成功返回实际插入的成员数量。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param score
     * @param member
     * @return       java.lang.Long
     * @throws
     */
    public Long zadd(String key, double score, String member) {

        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zadd(key, score, member);
        } finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 返回Sorted Set中的成员数量，如果该Key不存在，返回0。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Long
     * @throws
     */
    public Long zcard(String key) {

        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zcard(key);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 该命令将为指定Key中的指定成员增加指定的分数。如果成员不存在，该命令将添加该成员并假设其初始分数为0，
     * 此后再将其分数加上increment。如果Key不存在，该命令将创建该Key及其关联的Sorted Set，
     * 并包含参数指定的成员，其分数为increment参数。如果与该Key关联的不是Sorted Set类型，
     * 相关的错误信息将被返回。如果不报错则以串形式表示的新分数。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param score
     * @param member
     * @return       java.lang.Double
     * @throws
     */
    public Double zincrby(String key, double score, String member) {
        Jedis jedis = null;
        Double result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zincrby(key, score, member);
        }  finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 如果该成员存在，以字符串的形式返回其分数，否则返回null
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param member
     * @return       java.lang.Double
     * @throws
     */
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        Double result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zscore(key, member);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 该命令返回顺序在参数start和stop指定范围内的成员，这里start和stop参数都是0-based，即0表示第一个成员，-1表示最后一个成员。如果start大于该Sorted
     * Set中的最大索引值，或start > stop，此时一个空集合将被返回。如果stop大于最大索引值，
     * 该命令将返回从start到集合的最后一个成员。如果命令中带有可选参数WITHSCORES选项，
     * 该命令在返回的结果中将包含每个成员的分数值，如value1,score1,value2,score2...。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param min
     * @param max
     * @return       java.util.Set<java.lang.String> 指定区间内的有序集成员的列表。
     * @throws
     */
    public Set<String> zrange(String key, long start, long stop) {
        Jedis jedis = null;
        Set<String> result;
        try {
            jedis = jedisPool.getResource();
            result= jedis.zrange(key, start, stop);
        }  finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return result;
    }
    /**
     * 该命令的功能和ZRANGE基本相同，唯一的差别在于该命令是通过反向排序获取指定位置的成员，
     * 即从高到低的顺序。如果成员具有相同的分数，则按降序字典顺序排序。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param start
     * @param end
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        Set<String> result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zrevrange(key, start, end);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }


    /**
     * 该命令除了排序方式是基于从高到低的分数排序之外，其它功能和参数含义均与ZRANGEBYSCORE相同。
     * 需要注意的是该命令中的min和max参数的顺序和ZRANGEBYSCORE命令是相反的。
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param key
     * @param max
     * @param min
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Jedis jedis = null;
        Set<String> result ;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zrevrangeByScore(key, max, min);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return result;
    }

}
