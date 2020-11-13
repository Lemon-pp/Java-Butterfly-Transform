## 1.SpringMVC概述

### MVC三层架构：

M: Model模型，封装和映射数据（Javabean）

V：View视图，界面显示工作（.jsp）

C：Controller控制器，控制整个网站的跳转逻辑（Servlet）

 ![image-20201109103828763](https://i.loli.net/2020/11/09/DhobkrgwmzLF2MJ.png)



### SpringMVC三层架构

![image-20201113113548932](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20201113113548932.png)

## 2.注解

### @RequestMapping

RequestMapping是一个用来处理请求地址映射的注解，可用于类或方法上。用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径。

RequestMapping注解有六个属性：

```shell
## value， method
value：     指定请求的实际地址，指定的地址可以是URI Template 模式（后面将会说明）；

method：  指定请求的method类型， GET、POST、PUT、DELETE等；

## consumes，produces
consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;

produces:    指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；

## params，headers
params： 指定request中必须包含某些参数值或者不包含某些参数值，才让该方法处理。

headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求。
```

Value:

URL地址可以进行通配符匹配：

```bash
?   可以替代任意一个字符,0个多个都不行，模糊和精确同时存在时，精确优先

*	能替代任意多个字符，和一层路径

**	能替代多层路径
```



### @PathVariable  映射URL绑定的占位符

通过 @PathVariable 可以将URL中占位符参数{xxx}绑定到处理器类的方法形参中@PathVariable(“xxx“) 。

然后可以拿到请求地址中的参数。

语法：

```java
@PathVariable("xxx")
通过 @PathVariable 可以将URL中占位符参数{xxx}绑定到处理器类的方法形参中@PathVariable(“xxx“) 
 
@RequestMapping(value=”user/{id}/{name}”)
请求路径：http://localhost:8080/hello/show5/1/james
```

代码示例：

```java
@RequestMapping("/hello/{id}/{name}")
public String hello2(@PathVariable("id") String id, @PathVariable("name") String name){
        System.out.println("id=" + id);
        System.out.println("name=" + name);
        return "success";
}
```

## 3.Restful架构

参考：

Restful例子代码；

通过get、post、put、delete四种不同的请求方式以及不同的RequestMapping区分请求方法。

```java
@GetMapping("/book/{id}")
    public String hello1(@PathVariable("id") String id){
        System.out.println("查询id=" + id);
        return "success";
    }
    @PostMapping("/book")
    public String hello3(){
        System.out.println("添加=");
        return "success";
    }
    @PutMapping("/book/{id}")
    public String hello4(@PathVariable("id") String id){
        System.out.println("修改=" + id);
        return "success";
    }
    @DeleteMapping("/book/{id}")
    public String hello5(@PathVariable("id") String id){
        System.out.println("删除=" + id);
        return "success";
    }
```

## 4.SpringMVC获取请求带来的信息

### @RequestParam

作用：

```
将请求参数绑定到你控制器的方法参数上（是springmvc中接收普通参数的注解）
```

语法：

```bash
语法：@RequestParam(value=”参数名”,required=”true/false”,defaultValue=””)
 
value：参数名
 
required：是否包含该参数，默认为true，表示该请求路径中必须包含该参数，如果不包含就报错。
 
defaultValue：默认参数值，如果设置了该值，required=true将失效，自动为false,如果没有传该参数，就使用默认值
```

例子：

```http
请求地址：http://localhost:8080/test?id=123
```

controller：

```java
@RequestMapping("/test")
public String hello6(@RequestParam(value = "id", required = false, defaultValue = "没带参数的默认值") String id){
    //String id也可以取名为其他，比如String abc,代表
    //abc = request.getParameter("id")
    System.out.println("获取到了：" + id);
    return "success";
}
```

通过@RequestParam获取到了请求中id参数。

**@RequestParam和@PathVariable的区别：**

```HTTP
http://localhost:8080/test/{name}?id=123
@RequestParam获取的是id的值，是请求参数中的值。
@PathVariable获取的是{name}中的name值，是请求路径上占位符的值。
```



### @RequestHeader

作用：

```
获取请求头中某个key的值，能够将请求头中的变量值映射到控制器的参数中
```

参数：

```
1、value：参数名称

2、required：是否必须

3、defaultValue：默认值
```

例子：

```java
@RequestMapping("/test1")
    public String hello7(@RequestHeader(value = "user-agents", required = false) String id){
        System.out.println("拿到请求头：" + id);
        return "success";
    }
```



### @CookieValue

作用：

```
用来获取Cookie中的值
```

参数：

```
1、value：参数名称

2、required：是否必须

3、defaultValue：默认值
```

## 5.数据到页面

除了传入原生request和session，SpringMVC还提供了很多其他方式将数据带给页面。

### 在方法出传入Map、Model、ModelMap

**Map，Model，ModelMap的作用域都是request。**

三者关系：

通过getclass，可以看到`Map`，`Model`，`ModelMap`最终都是`BindingAwareModelMap`在工作；相当于给`BindingAwareModelMap`中保存的东西都会被放在请求域中。

例子：

```java
@RequestMapping("/hello1")
    public String hello1(Map<String, Object> map){
        System.out.println("数据输出，map");
        map.put("msg", "你好");
        System.out.println(map.getClass());
        return "success";
    }

    @RequestMapping("/hello2")
    public String hello2(Model model){
        model.addAttribute("msg", "我是output1()");
        return "success";
    }

    @RequestMapping("/hello3")
    public String hello3(ModelMap modelMap){
        modelMap.addAttribute("msg", "我是output3()");
        return "success";
    }
```

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>success</h1>
${msg}
</body>
</html>
```

```jsp
## 使用JSTL需要导入两个jar包
<dependency>
      <groupId>javax.servlet.jsp.jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>
    <dependency>
      <groupId>taglibs</groupId>
      <artifactId>standard</artifactId>
      <version>1.1.2</version>
    </dependency>
    
在jsp页面导入标签：
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

```

还会有个小问题：
由于Servlet版本原因，会导致EL表达式无法被解析到.，所以需要设置：

```jsp
<%@ page isELIgnored="false"%>
```

或者：修改web.xml

```xml
 <?xml version="1.0" encoding="UTF-8"?>
2 <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
3          xmlns="http://java.sun.com/xml/ns/javaee"
4          xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
5          id="WebApp_ID" version="3.0">
```

### 方法返回值使用ModeAndView

ModeAndView

既包含视图信息（页面地址），也包含模型数据（给页面带的数据）；数据均保存在请求域中。

page域：在页面

session域：在一个session中，运行时间长，数据量大，容易崩。

request域：数据存放在请求域中，请求结束，数据消失，比较利于程序运行。

application域：不仅自己可以看，其他也能看到，信息不安全。

所以使用最多的是request域。

例子：

```Java
@RequestMapping("/hello4")
    public ModelAndView hello4(){
        ModelAndView modelAndView = new ModelAndView("success");
        //或者
        //ModelAndView modelAndView = new ModelAndView();
        //modelAndView.setViewName("success");
        modelAndView.addObject("msg", "hello");
       
        return modelAndView;
    }

```

## 使用@SessionAttributes注解为session域中暂存数据

使用`@SessionAttributes(value = "msg")`注解会在给`BindingAwareModelMap`中保存数据的同时，为session中存放一份。value指定保存数据时要给session中存放的key。

- `value = {"msg"}`：只要保存的是这种key的数据，给session中存放一份。
- `types={String.class}`：只要保存的是这种类型的数据，给session中存放一份。
- 需要放在类上

```Java
@SessionAttributes(value = "msg")
@Controller
public class OutputController {

	@RequestMapping("handler01")
	public String handler01(Map<String, Object> map) {
		map.put("msg", "你好");
		System.out.println("map的类型是：" + map.getClass());
		return "success";
	}
}

```

SpringMVC虽然提供了为session存放数据的@SessionAttributes注解，不过还是推荐使用原始API，因为使用SpringMVC提供的注解可能会引发异常。