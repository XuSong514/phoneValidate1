package com.atguigu.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.utils.VerifyCodeConfig;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class VerifiCodeServlet
 */
/*
要求：1、输入手机号，点击发送后随机生成6位数字码，2分钟有效
           2、每个手机号每天只能输入3次
           3、输入验证码，点击验证，返回成功或失败
* */
public class CodeSenderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeSenderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Jedis jedis = new Jedis("192.168.244.129",6379);
        // 获取手机号
        String phoneNo = request.getParameter("phone_no");
        if (phoneNo==null){
            return;// 表示直接终止请求
        }

        // 需求3;每个手机号每天只能发送三次
        String countKey = VerifyCodeConfig.PHONE_PREFIX + phoneNo + VerifyCodeConfig.COUNT_SUFFIX;
        String count = jedis.get(countKey);
        if (count==null){// 第一次发送
            jedis.setex(countKey,VerifyCodeConfig.SECONDS_PER_DAY,"1");
        }else {
            int countint = Integer.parseInt(count);
            if (countint<3){
                jedis.incr(countKey);//每次加一
            }else {
                System.out.println("超过3次");
                response.getWriter().write("limit");
                return;
            }
        }

        // 需求1:输入手机号后，点击发送后随机生成6位数字码，2分钟有效

        // 生成6位的验证码
        String code = genCode(6);
        System.out.println(code);// 向手机发送验证码
        //需求2：把手机存到redis中 2分钟有效
        /* jedis.set("code",code);
        jedis.expire("code",120);*/
        // phone:1321412:code
        String codeKey = VerifyCodeConfig.PHONE_PREFIX + phoneNo + VerifyCodeConfig.CODE_SUFFIX;
        jedis.setex(codeKey,120,code);// 保存到redis中，手机号作为key，两分钟有效时间，验证码作为value
        response.getWriter().write("true");

    }

    // 生成随机数，验证码
    private String genCode(int len) {
        String code = "";
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }

}
