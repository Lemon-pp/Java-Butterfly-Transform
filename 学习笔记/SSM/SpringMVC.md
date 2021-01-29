## 1.SpringMVC概述

### MVC三层架构：

M: Model模型，封装和映射数据（Javabean）

V：View视图，界面显示工作（.jsp）

C：Controller控制器，控制整个网站的跳转逻辑（Servlet）

 ![image-20201109103828763](https://i.loli.net/2020/11/09/DhobkrgwmzLF2MJ.png)



### SpringMVC三层架构

![image-20201113113548932](https://i.loli.net/2020/12/17/nyJXMvQTB1oYAc5.png)

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

### @ResponseBody

作用：

```
作用：将方法的返回值，以特定的格式写入到response的body区域，进而将数据返回给客户端。

当方法上面没有写ResponseBody,底层会将方法的返回值封装为ModelAndView对象。

如果返回值是字符串，那么直接将字符串写到客户端；如果是一个对象，会将对象转化为json串，然后写到客户端。
```

### @RequestBody

获取请求的请求体。

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



### HttpEntity

作用：接收发过来的请求的整个请求信息。包括请求头和请求题。

例子：

```Java
<a href="/hello12?username=zhangsan">HttpEntity接收请求信息</a><br>

@RequestMapping("/hello12")
    public String hello12(HttpEntity<String> body){
        System.out.println(body);
        return "success";
    }
```



### ResponseEntity

作用：响应数据到页面。可以设置响应头、响应体、响应状态码。

例子：

```Java
<a href="/hello13">ResponseEntity返回响应数据</a><br>

@RequestMapping("/hello13")
    public ResponseEntity<String> hello13(){
        String body = "<h1>succssssssssssssss";
        HttpHeaders headers = new HttpHeaders();
        headers.add("abc", "abc");
        ResponseEntity responseEntity = new ResponseEntity<String>(body, headers, HttpStatus.OK);
        System.out.println(responseEntity);
        return responseEntity;
    }
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

### 使用@SessionAttributes注解为session域中暂存数据

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

## 6.视图解析

### **forward转发**

```Java
<!--创建视图解析器对象-->
    <bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <!--表示文件所在位置-->
        <property name="prefix" value="/WEB-INF/pages/"></property>
        <!--表示文件后缀名-->
        <property name="suffix" value=".jsp"></property>
    </bean>
```

一般配置了视图解析器，在返回页面的时候会根据返回值进行拼接跳转到页面，但是如果页面放在了其他地方，而不是视图解析器配置的地方，则可以使用forward前缀指定转发或者使用相对路径。

语法：

```bash
forward:转发的路径
```

两种作用：

1. 转发到指定页面
2. 转发到指定请求

```java
//转发到指定页面
@RequestMapping("/hello5")
    public String hello5(){
        System.out.println("转发");
        return "forward:/forwardtest.jsp";
    }

//转发到指定请求
@RequestMapping("/hello6")
    public String hello6(){
        System.out.println("转发2");
        return "forward:/hello5";
    }
```

### **redirect重定向**

语法：

```
redirect:重定向路径
```

两种作用：

1. 转发到指定页面
2. 转发到指定请求

```java
@RequestMapping("/hello7")
    public String hello7(){
        System.out.println("转发");
        return "redirect:/forwardtest.jsp";
    }

    @RequestMapping("/hello8")
    public String hello8(){
        System.out.println("转发2");
        return "redirect:/hello7";
    }
```

### **转发和重定向区别：**

```
区别：
	1.转发是浏览器上的网址不变，重定向时 浏览器上的网址改变为重定向的路径。
	2.转发只有一次请求，重定向实际上产生了两次请求。
	3.转发的网址必须是本站点的网址，重定向时的网址可以是任何网址。
	4.转发：以前的request中存放的变量不会失效，就像把两个页面拼到了一起。重定向：以前的request中存放的变量全部失效，并进入一个新的request作用域。
	
转发：
　　发送请求 -->服务器运行-->进行请求的重新设置，例如通过request.setAttribute(name,value)-->根据转发的地址，获取该地址的网页-->响应请求给浏览器
　　
重定向：
　　发送请求 -->服务器运行-->响应请求，返回给浏览器一个新的地址与响应码-->浏览器根据响应码，判定该响应为重定向，自动发送一个新的请求给服务器，请求地址为之前返回的地址-->服务器运行-->响应请求给浏览器
　　
四个注意点：
	1.重定向两次请求比较慢
	2.重定向可以避免在用户重新加载页面时两次调用相同的动作。
	3.要访问到另外一个WEB站点上的资源的情况，使用重定向。
	4.转发传值更方便。
```

**浏览器控制台过滤请求**：

```
使用-xx
例如：过滤js和css请求
-js -css  中间用空格隔开
```

### **如何解析视图：**

不论控制器返回一个String,ModelAndView,View都会转换为ModelAndView对象，由视图解析器解析视图，然后，进行页面的跳转。

![image-20201124102139649](https://i.loli.net/2020/11/24/gKYlpj7JRiuyzv8.png)

视图解析源码分析：重要的两个接口**ViewResolver和View。**

### 视图和视图解析器

- 请求处理方法执行完成后，最终返回一个 ModelAndView 对象。对于那些返回 String，View 或 ModeMap 等类型的处理方法，***\*Spring MVC\**** ***\*也会在内部将它们装配成一个\** \**ModelAndView\**** ***\*对象\****，它包含了逻辑名和模型对象的视图
- Spring MVC 借助***\*视图解析器\****（***\*ViewResolver\****）得到最终的视图对象（View），最终的视图可以是 JSP ，也可能是 Excel、JFreeChart等各种表现形式的视图
- 对于最终究竟采取何种视图对象对模型数据进行渲染，处理器并不关心，处理器工作重点聚焦在生产模型数据的工作上，从而实现 MVC 的充分解耦

### 视图

- ***\*视图\****的作用是渲染模型数据，将模型里的数据以某种形式呈现给客户,主要就是完成转发或者是重定向的操作.
- 为了实现视图模型和具体实现技术的解耦，Spring 在 org.springframework.web.servlet 包中定义了一个高度抽象的 ***\*View\**** 接口：

### 视图解析器

- SpringMVC 为逻辑视图名的解析提供了不同的策略，可以在 Spring WEB 上下文中***\*配置一种或多种解析策略\****，***\*并指定他们之间的先后顺序\****。每一种映射策略对应一个具体的视图解析器实现类。
- 视图解析器的作用比较单一：将逻辑视图解析为一个具体的视图对象。
- 所有的视图解析器都必须实现 ViewResolver 接口：

## 7.数据转换&数据格式话&数据校验

**参考：https://www.cnblogs.com/blknemo/p/13498035.html**

### 数据绑定流程

1. Spring MVC 主框架将 ServletRequest 对象及目标方法的入参实例传递给 WebDataBinderFactory 实例，以创建 DataBinder 实例对象
2. DataBinder 调用装配在 Spring MVC 上下文中的 ConversionService 组件进行数据类型转换、数据格式化工作。将 Servlet 中的请求信息填充到入参对象中
3. 调用 Validator 组件对已经绑定了请求消息的入参对象进行数据合法性校验，并最终生成数据绑定结果 BindingData 对象
4. Spring MVC 抽取 BindingResult 中的入参对象和校验错误对象，将它们赋给处理方法的响应入参

Spring MVC 通过反射机制对目标处理方法进行解析，将请求消息绑定到处理方法的入参中。数据绑定的核心部件是 DataBinder，运行机制如下：

![image-20201125103010177](https://i.loli.net/2020/11/25/TZ5Q93l6AgvrPOe.png)

### 数据转换

Spring MVC 上下文中内建了很多转换器，可完成大多数 Java 类型的转换工作。

```
ConversionService converters =

java.lang.Boolean -> java.lang.String :
org.springframework.core.convert.support.ObjectToStringConverter@f874ca
java.lang.Character -> java.lang.Number : CharacterToNumberFactory@f004c9
java.lang.Character -> java.lang.String : ObjectToStringConverter@68a961
java.lang.Enum -> java.lang.String : EnumToStringConverter@12f060a
java.lang.Number -> java.lang.Character : NumberToCharacterConverter@1482ac5
java.lang.Number -> java.lang.Number : NumberToNumberConverterFactory@126c6f
java.lang.Number -> java.lang.String : ObjectToStringConverter@14888e8
java.lang.String -> java.lang.Boolean : StringToBooleanConverter@1ca6626
java.lang.String -> java.lang.Character : StringToCharacterConverter@1143800
java.lang.String -> java.lang.Enum : StringToEnumConverterFactory@1bba86e
java.lang.String -> java.lang.Number : StringToNumberConverterFactory@18d2c12
java.lang.String -> java.util.Locale : StringToLocaleConverter@3598e1
java.lang.String -> java.util.Properties : StringToPropertiesConverter@c90828
java.lang.String -> java.util.UUID : StringToUUIDConverter@a42f23
java.util.Locale -> java.lang.String : ObjectToStringConverter@c7e20a
java.util.Properties -> java.lang.String : PropertiesToStringConverter@367a7f
java.util.UUID -> java.lang.String : ObjectToStringConverter@112b07f
……
```

**自定义转换器类**

总结三步：

1. 写一个自己的转换器类，并实现Converter<S, T>接口，将S类型转换为T类型

2. 将自己的converter配置到ConversionService中

   ```xml
   ## 在springmvc.xml中配置
   <bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
           <property name="converters">
               <set>
                   <bean class="com.lemon.Util.MyConvert"></bean>
               </set>
           </property>
       </bean>
   ```

3. 告诉SpringMVC使用自己的ConversionService

   ```xml
   <mvc:annotation-driven conversion-service="conversionService"></mvc:annotation-driven>
   ```

例子:

index.jsp:

```jsp
<a href="/hello9?userInfo=root-123456">数据转换</a><br>
```

MyConvert:

```java
public class MyConvert implements Converter<String, User> {
    @Override
    public User convert(String s) {
        User user = new User();
        if (!s.isEmpty() && s!=null){
            String[] split = s.split("-");
            user.setUsername(split[0]);
            user.setPasswrod(split[1]);
        }
        return user;
    }
}
```

Controller:

```java
@RequestMapping("/hello9")
    public String hello9(@RequestParam("userInfo") User user){
        System.out.println("用户：" + user);
        return "success";
    }
```

### 关于 mvc:annotation-driven

+ `<mvc:annotation-driven/>` 会自动注册RequestMappingHandlerMapping、RequestMappingHandlerAdapter 与 ExceptionHandlerExceptionResolver 三个bean。

+ 还将提供以下支持：

  - 支持使用 ConversionService 实例对表单参数进行类型转换
  - 支持使用 @NumberFormat annotation、@DateTimeFormat 注解完成数据类型的格式化
  - 支持使用 @Valid 注解对 JavaBean 实例进行 JSR 303 验证
  - 支持使用 @RequestBody 和 @ResponseBody 注解

  

### 数据格式化

 对属性对象的输入/输出进行格式化，从其本质上讲依然属于 “类型转换” 的范畴。

Spring 在格式化模块中定义了一个实现 ConversionService 接口的 FormattingConversionService 实现类，该实现类扩展了 GenericConversionService，因此它既具有类型转换的功能，又具有格式化的功能

FormattingConversionService 拥有一个 FormattingConversionServiceFactroyBean 工厂类，后者用于在 Spring 上下文中构造前者

FormattingConversionServiceFactroyBean 内部已经注册了:

- NumberFormatAnnotationFormatterFactroy：支持对数字类型的属性使用 @NumberFormat 注解
- JodaDateTimeFormatAnnotationFormatterFactroy：支持对日期类型的属性使用 @DateTimeFormat 注解

装配了 FormattingConversionServiceFactroyBean 后，就可以在 Spring MVC 入参绑定及模型数据输出时使用注解驱动了。
 `<mvc:annotation-driven/>` 默认创建的 ConversionService 实例即为 FormattingConversionServiceFactroyBean



#### **日期格式化**

@DateTimeFormat 注解可对 java.util.Date、java.util.Calendar、java.long.Long 时间类型进行标注：

- pattern 属性：类型为字符串。指定解析/格式化字段数据的模式，如：”yyyy-MM-dd hh:mm:ss”

- iso 属性：类型为 DateTimeFormat.ISO。指定解析/格式化字段数据的ISO模式，包括四种：ISO.NONE（不使用） -- 默 认、ISO.DATE(yyyy-MM-dd) 、ISO.TIME(hh:mm:ss.SSSZ)、ISO.DATE_TIME(yyyy-MM-dd hh:mm:ss.SSSZ)

- style 属性：字符串类型。通过样式指定日期时间的格式，由两位字符组成，第一位表示日期的格式，第二位表示时间的格式：S：短日期/时间格式、M：中日期/时间格式、L：长日期/时间格式、F：完整日期/时间格式、-：忽略日期或时间格式

  

#### 数值格式化

@NumberFormat 可对类似数字类型的属性进行标注，它拥有两个互斥的属性：

- style：类型为 NumberFormat.Style。用于指定样式类型，包括三种：Style.NUMBER（正常数字类型）、Style.CURRENCY（货币类型）、 Style.PERCENT（百分数类型）
- pattern：类型为 String，自定义样式，如patter="#,###"；



例子：

```xml
<mvc:annotation-driven></mvc:annotation-driven>
```

```java
public class User {
    @DateTimeFormat(pattern="yyyy/MM/dd")
    private Date birthday;

    @NumberFormat(pattern="#,###.###")
    private Double wages;
}
```

```java
@RequestMapping("/handle19")
public String handle19(@ModelAttribute("user") User user) {
    user.setId(1000);
    System.out.println(user);
    return "success";
}
```

### 数据校验

#### JSR303

+ JSR 303 是 Java 为 Bean 数据合法性校验提供的标准框架，它已经包含在 JavaEE 6.0 中

+ JSR 303 通过在 Bean 属性上标注类似于 @NotNull、@Max 等标准的注解指定校验规则，并通过标准的验证接口对 Bean 进行验证

  | 注解                      | 功能说明                                                 |
  | ------------------------- | -------------------------------------------------------- |
  | @Null                     | 被注解的元素必须为null                                   |
  | @NotNull                  | 被注解的元素必须不为null                                 |
  | @AssertTure               | 被注解的元素必须为true                                   |
  | @AssertFalse              | 被注解的元素必须为false                                  |
  | @Min(value)               | 被注解的元素必须是一个数字，其值必须大于等于指定的最小值 |
  | @Max(value)               | 被注解的元素必须是一个数字其值必须小于等于指定的最大值   |
  | @DecimalMin(value)        | 被注解的元素必须是一个数字其值必须大于等于指定的最小值   |
  | @DecimalMax(value)        | 被注解的元素必须是一个数字其值必须小于等于指定的最大值   |
  | @Size(max,min)            | 被注解的元素的大小必须在指定的范围内                     |
  | @Digits(integer,fraction) | 被注解的元素必须是一个数字，其值必须在可接受的范围内     |
  | @Past                     | 被注解的元素必须是一个过去的日期                         |
  | @Future                   | 被注解的元素必须是一个将来的日期                         |
  | @Pattern(value)           | 被注解的元素必须符合指定的正则表达式                     |

  

#### Hibernate Validator 扩展注解

Hibernate Validator 是 JSR 303 的一个参考实现，除支持所有标准的校验注解外，它还支持以下的扩展注解

| 注解      | 功能说明                               |
| --------- | -------------------------------------- |
| @Email    | 被注解的元素必须是电子邮箱地址         |
| @Length   | 被注解的字符串的大小必须在指定的范围内 |
| @NotEmpty | 被注解的字符串必须非空                 |
| @Range    | 被注解的元素必须在合适的范围内         |

#### Spring MVC 数据校验

+ Spring 4.0 拥有自己独立的数据校验框架，同时支持 JSR 303 标准的校验框架。

+ Spring 在进行数据绑定时，可同时调用校验框架完成数据校验工作。在 Spring MVC 中，可直接通过注解驱动的方式进行数据校验

+ Spring 的 LocalValidatorFactroyBean 既实现了 Spring 的 Validator 接口，也实现了 JSR 303 的 Validator 接口。只要在 Spring 容器中定义了一个 LocalValidatorFactoryBean，即可将其注入到需要数据校验的 Bean 中。

+ Spring 本身并没有提供 JSR303 的实现，所以必须将 JSR303 的实现者的 jar 包放到类路径下。

+ `<mvc:annotation-driven/>` 会默认装配好一个 LocalValidatorFactoryBean，通过在处理方法的入参上标 注 @valid 注解即可让 Spring MVC 在完成数据绑定后执行数据校验的工作

+ 在已经标注了 JSR303 注解的表单/命令对象前标注一个 @Valid，Spring MVC 框架在将请求参数绑定到该入参对象后，就会调用校验框架根据注解声明的校验规则实施校验

+ Spring MVC 是通过对处理方法签名的规约来保存校验结果的：前一个表单/命令对象的校验结果保存到随后的入参中，这个保存校验结果的入参必须是 BindingResult 或 Errors 类型，这两个类都位于 org.springframework.validation 包中

+ 需校验的 Bean 对象和其绑定结果对象或错误对象时成对出现的，它们 之间不允许声明其他的入参

+ Errors 接口提供了获取错误信息的方法，如 getErrorCount() 或 getFieldErrors(String field)
+ BindingResult 扩展了 Errors 接口



**使用步骤：**

1. 导入依赖

2. 给Javabean的属性添加校验注解

3. 在springmvc封装对象的时候,使用@Valid告诉springmvc这个Javabean需要校验

4. 获取校验结果----BindingResult

   ```Java
   在表单/命令对象类的属性中标注校验注解，在处理方法对 应的入参前添加 @Valid，Spring MVC 就会实施校验并将校验结果保存在被校验入参对象之后的 BindingResult 或 Errors 入参中。
   常用方法：
       FieldError getFieldError(String field)
       List getFieldErrors()
       Object getFieldValue(String field)
       Int getErrorCount()
   
   //页面显示错误信息
   Spring MVC 除了会将表单/命令对象的校验结果保存到对应的 BindingResult 或 Errors 对象中外，还会将所有校验 结果保存到 “隐含模型”
   即使处理方法的签名中没有对应于表单/命令对象的结果入参，校验结果也会保存在 “隐含对象” 中。
   隐含模型中的所有数据最终将通过 HttpServletRequest 的属性列表暴露给 JSP 视图对象，因此在 JSP 中可以获取错误信息
   方法:
   	1.从BindingResult 中获取
       2.直接将错误信息写在Javabean的属性上
       //@Length(min = 6, max = 16, message = "密码需要6-16位")
   ```

5. 根据不同的校验结果进行处理

#### 例子

1.导入依赖：

```xml
<dependency>
      <groupId>org.hibernate.validator</groupId>
      <artifactId>hibernate-validator</artifactId>
      <version>6.1.6.Final</version>
    </dependency>
```

2.index.jsp

```jsp
<form action="/hello9">
    用户名：<input type="text" name="username"><br>
    密码：<input type="text" name="passwrod"><br>
    <input type="submit" value="提交">
</form>
```

User

```java
	@NonNull
    private String username;

    @Length(min = 6, max = 16)
    private String passwrod;
```

3.controller

```java
@RequestMapping("/hello9")
    public String hello9(@Valid User user, BindingResult bindingResult, Model model){
        boolean b = bindingResult.hasErrors();
        if (b){
            System.out.println("has error");
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            HashMap<String, String> errorMap = new HashMap<>();
            for (FieldError field : allErrors) {
                System.out.println(field.getField());
                errorMap.put(field.getField(), field.getDefaultMessage());
            }
            model.addAttribute("errors",errorMap);
            return "error";
        }else {
            System.out.println("用户：" + user);
            return "success";
        }
    }
```

4.error.jsp

```jsp
error
${errors.username}
${errors.passwrod}
```

#### 提示消息国际化

每个属性在数据绑定和数据校验发生错误时，都会生成一个对应的 FieldError 对象。

当一个属性校验失败后，校验框架会为该属性生成 4 个消息代码，这些代码以校验注解类名为前缀，结合 modleAttribute、属性名及属性类型名生成多个对应的消息代码：例如 User 类中的 password 属性标准了一个 @Pattern 注解，当该属性值不满足 @Pattern 所定义的规则时, 就会产生以下 4 个错误代码：

- Pattern.user.password
- Pattern.password
- Pattern.java.lang.String
- Pattern

当使用 Spring MVC 标签显示错误消息时， Spring MVC 会查看 WEB 上下文是否装配了对应的国际化消息，如果没有，则显示默认 的错误消息，否则使用国际化消息。

若数据类型转换或数据格式转换时发生错误，或该有的参数不存在，或调用处理方法时发生错误，都会在隐含模型中创建错误消息。其错误代码前缀说明如下：

- required：必要的参数不存在。如 @RequiredParam(“param1”) 标注了一个入参，但是该参数不存在

- typeMismatch：在数据绑定时，发生数据类型不匹配的问题

- methodInvocation：Spring MVC 在调用处理方法时发生了错误

  

**步骤**:

1. 编写国际化的文件

2. 让Springmvc管理国际化资源文件

   ```xml
   <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">     
       <property name="basename" value="i18n"></property>       
   </bean>
   value值为文件基础名:如il8n_error.properties
   ```

3. 页面取值

   

## 8.Ajax

1.导依赖

2.写配置

3.测试



## 9.文件上传与下载

1.导jar包

```xml
<dependency>
      <groupId>commons-fileupload</groupId>
      <artifactId>commons-fileupload</artifactId>
      <version>1.3.3</version>
    </dependency>
```

2.在springmvc.xml中配置multiPartResolver：

```xml
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="123456456123"></property>
        <property name="defaultEncoding" value="UTF-8"></property>
    </bean>
```

3.写页面发请求

```jsp
<form action="/upload" enctype="multipart/form-data" method="post">
    <table>
        <tr>
            <td>请选择文件：</td>
            <td><input type="file" name="file"></td>
        </tr>
        <tr>
            <td>开始上传</td>
            <td><input type="submit" value="上传"></td>
        </tr>
    </table>
</form>
```

4.控制器上传图片

步骤：

```
1.MultipartFile获取文件地址

2.将文件写入目标文件地址（transferTo）
```

多文件上传步骤：

```
1.MultipartFile[]获取多个文件地址

2.遍历数组，将文件写入目标文件地址（transferTo）
```



```java
@RequestMapping(value="/upload")
    public String upload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            String storePath= "E:\\编程\\学习项目\\SpringMVC01\\upload";//存放我们上传的文件路径
            String fileName = file.getOriginalFilename();
            File filepath = new File(storePath, fileName);
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();//如果目录不存在，创建目录
            }
            try {
                file.transferTo(new File(storePath+File.separator+fileName));//把文件写入目标文件地址
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
            return "success";//返回到成功页面
        }else {
            return "error";//返回到失败的页面
        }

    }
```

**文件下载：**

步骤：

```
1.获得下载文件的路径

2.获得字节输入流或者文件输入流

3.创建byte数组接收流 available:获取输入流所读取的文件的最大字节数

4.把字节读取到数组中

5.返回响应到浏览器
```

例子：

```java
@GetMapping("download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename) throws IOException {
        //获得下载文件的路径(这里绝对路径)
        String filepath= "E:\\编程\\学习项目\\SpringMVC01\\upload\\"+filename + ".png";
        System.out.println(filename);
        File file =new File(filepath);
        //获得字节输入流或者文件输入流
        InputStream in = new FileInputStream(file);
        //available:获取输入流所读取的文件的最大字节数
        byte[] body = new byte[in.available()];
        //把字节读取到数组中
        in.read(body);
        //设置请求头
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + file.getName());
        //设置响应状态
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        in.close();
        return entity;//返回
    }
```

## 10.拦截器

参考博客：https://www.cnblogs.com/black-spike/p/7813238.html



