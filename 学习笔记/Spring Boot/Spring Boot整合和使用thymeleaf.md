# Spring Boot整合和使用thymeleaf

## 1.thymeleaf介绍

Thymeleaf 是一个服务器端 Java 模板引擎，能够处理 HTML、XML、CSS、JAVASCRIPT 等模板文件。Thymeleaf 模板可以直接当作静态原型来使用，它主要目标是为开发者的开发工作流程带来优雅的自然模板，也是 Java 服务器端 HTML5 开发的理想选择。

## 2.整合过程

1.导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

2.配置thymeleaf

```properties
# thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.check-template-location=true
spring.thymeleaf.suffix=.html
#spring.thymeleaf.encoding=utf-8
spring.thymeleaf.content-type=text/html
spring.thymeleaf.mode=HTML5
spring.thymeleaf.cache=false
```

3.创建controller

```java
@Controller
public class HelloController {
    @RequestMapping("/hello")
    public String hello(HttpServletRequest request) {
        request.setAttribute("name", "中文");
        return "hello";
    }
}
```

4.创建模板文件

>注：使用Thymeleaf模板引擎时，必须在html文件上方添加，thymeleaf命名空间使用支持Thymeleaf。

```html
<html lang="en" xmlns:th="http://www.thymeleaf.org"> 
```

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>springboot-thymeleaf demo</title>
</head>

<body>
<p th:text="'hello, ' + ${name} + '!'" />
</body>
</html>
```

5.效果展示：

![image-20210201110133468](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20210201110133468.png)

## 3.使用教程

>https://fanlychie.github.io/post/thymeleaf.html

### 1.创建模板文件

通过`<html xmlns:th="http://www.thymeleaf.org">`引入 Thymeleaf 命名空间。`th:text`用于处理`p`标签体的文本内容。

该模板文件直接在任何浏览器中正确显示，浏览器会自动忽略它们不能理解的属性`th:text`。但这不是一个真正有效的 HTML5 文档，因为 HTML5 规范是不允许使用`th:*`这些非标准属性的。我们可以切换到 Thymeleaf 的`data-th-*`语法，以此来替换`th:*`语法：

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Index Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <p data-th-text="${message}">Welcome to BeiJing!</p>
</body>
</html>
```

HTML5 规范是允许`data-*`这样自定义的属性的。`th:*`和`data-th-*`这两个符号是完全等效且可以互换的。

### 2.标准表达式语法

| 语法 |              名称              |      描述      |          作用          |
| :--: | :----------------------------: | :------------: | :--------------------: |
| ${…} |      Variable Expressions      |   变量表达式   |   取出上下文变量的值   |
| *{…} | Selection Variable Expressions | 选择变量表达式 | 取出选择的对象的属性值 |
| #{…} |      Message Expressions       |   消息表达式   | 使文字消息国际化，I18N |
| @{…} |      Link URL Expressions      |   链接表达式   | 用于表示各种超链接地址 |
| ~{…} |      Fragment Expressions      |   片段表达式   | 引用一段公共的代码片段 |

```html
<p th:text="${message}"></p>
```

```html
<div th:object="${session.user}">
    <p th:text="*{name}"></p>
    <p th:text="*{sex}"></p>
    <p th:text="*{age}"></p>
</div>

相当于
<div>
    <p th:text="${session.user.name}"></p>
    <p th:text="${session.user.sex}"></p>
    <p th:text="${session.user.age}"></p>
</div>
```

```html
消息表达式#{}是不允许直接处理非静态的文本消息的，但是你可以在资源文件中通过使用占位符{}来处理非静态的文本消息：
<p th:text="#{welcom.message}"></p>
```

链接表达式`@{}`是专门用来处理 URL 链接地址的。

```html
<p th:text="@{https://fanlychie.github.io}"></p>
```

参数使用示例：

```html
<!-- /css/mian.css?v=1.0 -->
<p th:text="@{/css/mian.css(v=1.0)}"></p>

<!-- /user/order?username=fanlychie -->
<p th:text="@{/user/order(username=${session.user.name})}"></p>

<!-- /user/order?username=fanlychie&status=PAIED -->
<p th:text="@{/user/order(username=${session.user.name},status='PAIED')}"></p>

<!-- /user/fanlychie/info -->
<p th:text="@{/user/{username}/info(username=${session.user.name})}"></p>
```

### 3.遍历

遍历（迭代）的语法`th:each="自定义的元素变量名称 : ${集合变量名称}"`：

```HTML
<div>
    <spn>你所在城市：</spn>
    <select name="mycity">
        <option th:each="city : ${cities}" th:text="${city.name}"></option>
    </select>
</div>
```



### 4.条件判断

条件判断语句有三种，分别是：`th:if`、`th:unless`、`th:swith`。

**th:if**

当表达式的评估结果为真时则显示内容，否则不显示：

```html
<a th:href="@{/user/order(uid=${user.id})}" th:if="${user != null}">我的订单</a>
```

**th:unless**

`th:unless`与`th:if`判断恰好相反，当表达式的评估结果为假时则显示内容，否则不显示：

```html
<a th:href="@{/user/order(uid=${user.id})}" th:unless="${user == null}">我的订单</a>
```

 **th:swith**

多路选择语句，它需要搭配`th:case`来使用：

```html
<div th:switch="${user.role}">
    <p th:case="admin">管理员</p>
    <p th:case="user">普通用户</p>
</div>
```

### 5.CSS

```html
<style th:inline="css">
    body {
        background-color:[[${bgColor}]];
    }
</style>
```

### 6.javascript

```html
<script th:inline="javascript">
    var user = [[${user}]];
    alert("用户名：" + user.name);
</script>
```

