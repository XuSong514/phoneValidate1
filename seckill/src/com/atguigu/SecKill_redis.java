package com.atguigu;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;


public class SecKill_redis {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SecKill_redis.class);

    public static void main(String[] args) {

        Jedis jedis = new Jedis("192.168.244.129", 6379);

        for (int i = 100; i > 0; i--) {
            jedis.rpush("sk:0101:qt", String.valueOf(i));
        }


        jedis.close();
    }

    public static boolean doSecKill(String uid, String prodid){
        Jedis jedis = new Jedis("192.168.244.129",6379);
        // 库存的key second kill
        String qtKey ="sk:"+prodid+":qt";
        // 人的key
        String usrKey = "sk:"+prodid+":usr";

        // 使用list集合 集合中元素吐完，集合为null
        String lpop = jedis.lpop(qtKey);
        if (lpop==null){
            System.out.println("活动还没开始");
            jedis.close();
            return false;
        }

/*
        jedis.watch(qtKey);// 库存的初始版本

        // set集合中1 2 3 4 5
        if (jedis.sismember(usrKey,uid)){
            System.out.println("不能重复秒杀");
            jedis.close();
            return false;
        }

        String qtStr = jedis.get(qtKey);// 获取库存数量
        if (qtStr==null){// 没有库存
            System.out.println("莫着急，活动还没开始");
            jedis.close();
            return false;
        }
        int qt = Integer.parseInt(qtStr);
        if (qt<=0){
            System.out.println("活动结束");
            jedis.close();
            return false;
        }

        Transaction multi = jedis.multi();
        // 减库存
        multi.decr(qtKey);
        // 加人
        multi.sadd(usrKey,uid);
        List<Object> exec = multi.exec();// 提交事务
        if (exec==null||exec.size()==0){
            System.out.println("秒杀失败");
            jedis.close();
            return false;
        }
        System.out.println("秒杀成功");*/
        return true;

        // ab -n 1000 -c 200 -p /postfile -T "application/x-www-form-urlencoded" http://192.168.20.32:8080/seckill/doseckill

    }

}
















