package com.example.redisjedis;

import com.example.redisjedis.servic.RedisServic;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisJedisApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RedisServic redisServic;

    @Test
    public void testRedis(){
        redisServic.exists("name");
        redisServic.del("uu");
    }

}
