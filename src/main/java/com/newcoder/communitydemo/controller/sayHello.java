package com.newcoder.communitydemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/hello")
public class sayHello {

    @RequestMapping("/test1")
    @ResponseBody
    public String test1() {
        return "test1";
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求中的数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " : " + value);
        }
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        // 返回网页
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter writer = response.getWriter()){
            writer.write("<h1>new</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/http2/{id}")
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        return id + " ";
    }

    @PostMapping("/http3")
    @ResponseBody
    public String postMethod(String name, int age) {
        System.out.println(name);
        return "success";
    }

    // 响应html
    @GetMapping("/http4")
    public ModelAndView getData() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "张三");
        mav.addObject("age", 30);
        mav.setViewName("/demo/view");
        return mav;
    }

    @GetMapping("/http5")
    public String getData2(Model model) {
        model.addAttribute("name", "李四");
        model.addAttribute("age", 20);
        return "/demo/view";
    }

    @GetMapping("/json")
    @ResponseBody
    public Map<String, Object> getJson() {
        HashMap<String, Object> map = new HashMap<String, Object>(){{
            put("张三", 2);
            put("李四", 3);
        }};
        String[] words = new String[]{"abc", "bcs"};
        String [] msdm={".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."};
        // 返回的是json格式
        return map;
    }
}
