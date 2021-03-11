# Spring Boot 配置和工具使用

## 1.Lombok

`Lombok` 是一种 Java™ 实用工具，可用来帮助开发人员消除 Java 的冗长，尤其是对于简单的 Java 对象（POJO）。它通过注解实现这一目的。

### 1.1 使用

安装插件

> 直接在settings->plugins
> 搜索lombok安装即可

导入依赖

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

**注解：**

```java
@NonNull //方法参数/字段属性 检查是否为空
    
@Getter/@Setter //lombok会自动生成默认的getter / setter    

@Data 
    集成以下注解
        @Getter
        @Setter
        @RequiredArgsConstructor
        @ToString
        @EqualsAndHashCode

@AllArgsConstructor //全参构造器

@NoArgsConstructor //无参构造器

@ToString/@EqualsAndHashCode  //自动重写toString\equals\hashCode方法

@Slf4j  //注解在类上, 为类提供一个属性名为 log 的 log4j 的日志对象
```



## 2.spring boot核心功能

![image-20210301172843745](https://i.loli.net/2021/03/01/4FP9d7p8AEhNcz2.png)

