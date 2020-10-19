# Spring Boot项目jar包打包步骤

1.修改pom.xm文件

```xaml
	<groupId>org.montnets</groupId>
    <artifactId>zhonglujj</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
```



2.在build标签内添加内容如下:

```xml
		<plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                        <mainClass>com.montnets.chatbot.ChatbotApplication</mainClass>
                    </configuration>
                </plugin>
        </plugins>
```



3. 定义spring boot 的入口类的方法

   ```java
   // Spring Boot 应用的标识
   //主要就是继承SpringBootServletInitializer 和重写 configure方法
   @SpringBootApplication
   public class Application extends SpringBootServletInitializer{
   
       public static void main(String[] args) {
           // 程序启动入口
           // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
           SpringApplication.run(Application.class,args);
       }
       
       @Override//为了打包springboot项目
       protected SpringApplicationBuilder configure(
               SpringApplicationBuilder builder) {
           return builder.sources(this.getClass());
       }
   }
   ```

4. 打包

+ （第一种方式）点击IDEA中的maven窗口的package按钮打包：

![image-20200924180451311](https://i.loli.net/2020/09/24/Tjs56DHvUuEoV1n.png)

+ （第二种方式）在IDEA中的终端中：

  到项目的pom平级的目录下面

  使用mvn clean package 命令打包成jar

  然后再到 target的目录下面执行

  java -jar xxx.jar

  即可执行 项目了

  ![image-20200927095300495](https://i.loli.net/2020/09/27/fVxqT1au8P6p9mW.png)



5.Linux下执行jar包：

```bash
1.复制jar包到Linux中
2.jar包只包含src下的main，需要自己复制resources下的文件、以及其他文件到同级目录，可以修改配置文件。
3.使用命令：nohup java -jar xxx.jar & 可以后台执行项目了。
```

