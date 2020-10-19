# HttpClient使用教程

## 1.HttpClient介绍

 HTTP 协议可能是现在 Internet 上使用得最多、最重要的协议了，越来越多的 Java 应用程序需要直接通过 HTTP 协议来访问网络资源。虽然在 JDK 的 java net包中已经提供了访问 HTTP 协议的基本功能，但是对于大部分应用程序来说，JDK 库本身提供的功能还不够丰富和灵活。HttpClient 是 Apache Jakarta Common 下的子项目，用来提供高效的、最新的、功能丰富的支持 HTTP 协议的客户端编程工具包，并且它支持 HTTP 协议最新的版本和建议。

        HTTP和浏览器有点像，但却不是浏览器。很多人觉得既然HttpClient是一个HTTP客户端编程工具，很多人把他当做浏览器来理解，但是其实HttpClient不是浏览器，它是一个HTTP通信库，因此它只提供一个通用浏览器应用程序所期望的功能子集，最根本的区别是HttpClient中没有用户界面，浏览器需要一个渲染引擎来显示页面，并解释用户输入，例如鼠标点击显示页面上的某处，有一个布局引擎，计算如何显示HTML页面，包括级联样式表和图像。javascript解释器运行嵌入HTML页面或从HTML页面引用的javascript代码。来自用户界面的事件被传递到javascript解释器进行处理。除此之外，还有用于插件的接口，可以处理Applet，嵌入式媒体对象（如pdf文件，Quicktime电影和Flash动画）或ActiveX控件（可以执行任何操作）。HttpClient只能以编程的方式通过其API用于传输和接受HTTP消息。
------------------------------------------------
##  2.HttpClient功能

- 实现了所有 HTTP 的方法（GET、POST、PUT、HEAD、DELETE、HEAD、OPTIONS 等）
- 支持 HTTPS 协议
- 支持代理服务器（Nginx等）等
- 支持自动（跳转）转向
- ……

## 3.HTTPClient使用

**环境说明：JDK1.8、SpringBoot**

1.导入依赖：

```xml
		<dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.54</version>
        </dependency>
        
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.2</version>
        </dependency>
        
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
```

#### 3.1 HttpClientUtil工具类

```java
package com.montnets.chatbot.utils;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhoup
 * @date 2020/6/5 11:35
 * @describe
 */
public class HttpClientUtil {

    //HttpClient客户端
    static CloseableHttpClient httpClient = null;
    //响应模型
    static CloseableHttpResponse response = null;
    /**
     * Get 有参
     */
    public static String doGet(String url, Map<String, String> params){
        //获取http客户端
        httpClient = HttpClientBuilder.create().build();
        String resultString = "";
        //创建uri
        try {
            URIBuilder builder = new URIBuilder(url);
            if (params != null){
                for (String key: params.keySet()) {
                    builder.addParameter(key, params.get(key));
                }
            }
            URI uri = builder.build();
            //创建get请求
            HttpGet httpGet = new HttpGet(uri);
            //由客户端发送get请求
            response = httpClient.execute(httpGet);
            //从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            //处理响应
            if (response.getStatusLine().getStatusCode() == 200){
                resultString = EntityUtils.toString(responseEntity, "UTF-8");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            close();
        }
        return resultString;
    }
    /**
     * Get 无参
     */
    public static String doGet(String url){
        return doGet(url, null);
    }

    /**
     * Post 有参
     */
    public static String doPost(String url, Map<String, String> params){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        String resultString = "";
        //给定参数
        if (params != null){
            List<BasicNameValuePair> list = new ArrayList<>();
            for (String key : params.keySet()) {
                list.add(new BasicNameValuePair(key , params.get(key)));
            }
            //将参数做字符串的转换
            StringEntity stringEntity = null;
            try {
                stringEntity = new UrlEncodedFormEntity(list, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //向请求中绑定参数
            httpPost.setEntity(stringEntity);
        }
        //发送请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            //处理响应
            if (response.getStatusLine().getStatusCode() == 200){
                resultString = EntityUtils.toString(responseEntity, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return resultString;
    }

    /**
     * POST  无参
     */
    public static String doPost(String url){
       return doPost(url, null);
    }

    /**
     * Post Json
     */
    public static String doPostJson(String url, String json) {
        //定义返回结果
        String resultString = "";
        //获取httpClient客户端
        httpClient = HttpClientBuilder.create().build();
        //创建post请求
        HttpPost httpPost = new HttpPost(url);
        //创建请求内容
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        //向请求中绑定参数
        httpPost.setEntity(stringEntity);
        //发送请求
        try {
            response = httpClient.execute(httpPost);
            //处理请求
            int code = response.getStatusLine().getStatusCode();
            //if (response.getStatusLine().getStatusCode() == 200){
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return resultString;
    }

    /**
     * 关闭连接
     */
    public static void close(){
        try {
            if (httpClient != null){
                httpClient.close();
            }
            if (response != null){
                response.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```

#### 3.2 Get、Post发送

```java
package utils;

import com.montnets.chatbot.ChatbotApplication;
import com.montnets.chatbot.utils.HttpClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoup
 * @date 2020/6/5 15:26
 * @describe
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatbotApplication.class)
public class HttpClientUtilTest {

    @Test
    public void doGet(){
        String url = "http://localhost:37598/doGet";
        String result = HttpClientUtil.doGet(url);
        System.out.println(result);
    }

    @Test
    public void doGetParam(){
        String url = "http://localhost:37598/doGetParam";
        Map<String, String> params = new HashMap<>();
        params.put("name", "张三");
        params.put("age", "18");
        String result = HttpClientUtil.doPost(url, params);
        System.out.println(result);
    }

    @Test
    public void doPost(){
        String url = "http://localhost:37598/doPost";
        String result = HttpClientUtil.doPost(url);
        System.out.println(result);
    }

    @Test
    public void doPostParam(){
        String url = "http://localhost:37598/doPostParam";
        Map<String, String> params = new HashMap<>();
        params.put("name", "张三");
        params.put("pwd", "zhangsanfeng");
        String result = HttpClientUtil.doPost(url, params);
        System.out.println(result);
    }

    @Test
    public void doPostJson(){
        String url = "http://localhost:37598/doPostJson";
        String json = "{\"name\":\"张三\",\"pwd\":\"zhangsanfeng\"}";
        String result = HttpClientUtil.doPostJson(url, json);
        System.out.println(result);
    }
}

```

#### 3.3 controller

```java
package com.montnets.chatbot.controller;

import com.montnets.chatbot.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoup
 * @date 2020/6/4 15:11
 * @describe
 */
@RestController
public class HttpClientControllerDemo {

    @RequestMapping("/doGet")
    public String doGetController(){
        return "123";
    }

    @RequestMapping("/doGetParam")
    public String doGetParamController(String name, Integer age) {
        return "[" + name + "]" + age + "岁了!";
    }

    @RequestMapping(value = "/doPost", method = RequestMethod.POST)
    @ResponseBody  //将java对象转为json格式的数据
    public Map<String, String> doPostController() {
        Map<String, String> map = new HashMap<>();
        map.put("msg", "happy!");
        return map;
    }

    @RequestMapping(value = "/doPostParam", method = RequestMethod.POST)
    public Map<String, String> doPostParamController(String name, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("pwd", pwd);
        return map;
    }

    @RequestMapping(value = "/doPostJson", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> doPostJsonController(@RequestBody User user) {
        System.out.println(user.getName() +"---json----" + user.getPwd());
        Map<String, String> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("pwd", user.getPwd());
        return map;
    }
}

```

