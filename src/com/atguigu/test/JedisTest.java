package com.atguigu.test;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @Author: Mr.li
 * @Description:
 * @Date: Created in 2021/1/15 13:25
 * @Modified By:
 */
public class JedisTest {
    public static void main(String[] args) {
        // 使用jedis技术连接Redis
        Jedis jedis = new Jedis("192.168.244.129",6379); //connection
        // 测试是否连接redis
        System.out.println(jedis.ping());
        //获取redis中所有的键，返回一个set集合
        Set<String> keys = jedis.keys("*");

        System.out.println("====================");
        // 遍历 set 集合
        // 因为每个key的类型不同，获取键值的方法不同，所以需要判断key的类型
        // type(key) 查询键的类型，所有类型都是 小写字母
        for (String key : keys) {
            // 如果key是 string 类型
            if (jedis.type(key).equals("string")){
                // string类型 ：get( key ) 获取对应键值
                System.out.println(jedis.get(key));
            }

            // 如果key是 set 类型
            if (jedis.type(key).equals("set")){
                // set 类型 ：smembers(key) 获取对应键值，返回的是一个集合
                Set<String> smembers = jedis.smembers(key);
                for (String smember : smembers) {
                    System.out.println(smember);
                }
            }

            if (jedis.type(key).equals("list")){
                // 如果key是 list 类型       range 润之    值域|范围
                // list类型： lrange(key, 0, -1)获取全部键值，返回的是一个集合
                for (String s : jedis.lrange(key, 0, -1)) {
                    System.out.println(s);
                }
            }
        }

    }
}
