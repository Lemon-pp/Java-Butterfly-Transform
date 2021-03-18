# Spring Boot Web开发

![image-20210310175838384](https://i.loli.net/2021/03/10/vSP29JhmAgNMpRI.png)

# 1.静态资源规则和定制化

静态资源默认路径：

```java
/static（/public或/resources或/META-INF/resources）
```

访问路径：

```java
当前项目根路径+静态资源名
```



原理：静态资源/**

请求进来，先去找Controller看能不能处理，不能处理的所有请求又都交给静态资源处理器处理。静态资源能找不到则会出现404.



静态资源访问前缀：

可以通过配置修改静态资源的访问前缀

```java
spring.mvc.static-path-pattern=/res/**
```



改变默认的静态资源路径：

例：改到类路劲下的lemon文件夹

```java
spring.resources.static-locations=classpath:/lemon
```



欢迎页/首页

两种：

+ 静态资源路径下 index.html
  + 可以自定义配置静态资源路径，但不能配置访问前缀。否则会导致访问404.
+ controller能处理的/index请求