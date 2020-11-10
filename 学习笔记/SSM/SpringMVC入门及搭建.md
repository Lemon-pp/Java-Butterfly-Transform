# SpringMVC入门及搭建

## 1、新建项目

选择maven，勾选，以及选择后缀为webapp

![image-20201105105539237](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20201105105539237.png)

创建项目骨架

在main目录下新建Java、resources文件夹，并修改为source root和Resources root。

![image-20201103150621767](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20201103150621767.png)

## 2、开始项目搭建

### 导入依赖

```xml
<dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.2.10.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>5.2.10.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
      <version>5.2.10.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>5.2.10.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.2.10.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>5.2.10.RELEASE</version>
    </dependency>
```

### 配置前端控制器DispatcherServlet

配置前端控制器DispatcherServlet，能拦截所有请求，并智能派发，由于DispatcherServlet是一个Servlet,所以需要在web.xml中配置。

```xml
<servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    	
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath*:</param-value>
        </init-param>
        <!--Servlet启动加载，启动级别，值越小，级别越高-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <!--拦截所有请求，交由前端控制器处理，不能使用/*，/*会把页面也拦截，比如jsp-->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

指定springmvc的配置文件位置

```xml
<init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:SpringMVC.xml</param-value>
    </init-param>
```

新建SpringMVC的配置文件--SpringMVC.xml

配置包扫描

```xml
<!--开启注解扫描-->
    <context:component-scan base-package="com.lemon"></context:component-scan>
```

创建视图解析器对象

```xml
<bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <!--表示文件所在位置-->
        <property name="prefix" value="/WEB-INF/pages/"></property>
        <!--表示文件后缀名-->
        <property name="suffix" value=".jsp"></property>
    </bean>
```

开启springmvc框架的注解支持

```xml
<mvc:annotation-driven></mvc:annotation-driven>
```

注意别导错了xml的头部

## 3、开始编码

index.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>入门</h1>
    <a href="/hello">点击跳转</a>
    <!--/hello对应controller中@RequestMapping("/hello")的路径映射-->
</body>
</html>
```

success.jsp(放到视图解析器配置的目录)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>入门案例</h1>
</body>
</html>
```

编写controller

```java
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("hello springmvc!!!");
        return "success";
    }
}
```

## 4、请求流程

![image-20201103163845086](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20201103163845086.png)

hello world运行流程：

1. 客户端点击链接发送请求
2. 请求到达tomcat服务器
3. SpringMVC的前端控制器收到请求
4. 请求地址通过与@RequestMapping注解的value进行匹配，找到对应的类和方法来处理。
5. 前端控制器找到了目标处理器类和方法，直接利用反射执行目标方法。
6. 方法执行完成返回一个值，SpringMVC认为这个返回值就是要去的页面地址。
7. 拿到返回值后，用视图解析器拼串得到完整的页面地址。
8. 拿到页面地址，前端控制器转发到页面。

## 5、细节

1.如果不指定SpringMVC配置文件的位置，也可以，但是需要在web应用下的/WEB-INF下创建一个名叫前端控制器<servlet-name>-Servlet.xml

如：![image-20201110163228725](https://i.loli.net/2020/11/10/lW7wQSdkpUJAHYj.png)

则新建dispatcher-Servlet.xml

