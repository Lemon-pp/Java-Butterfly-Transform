# Maven下载安装详细图文教程

## 1.下载

```shell
## 官网地址
https://maven.apache.org/download.cgi
```

![image-20210129033643785](https://i.loli.net/2021/01/29/MUD7v4hfxpWco9Q.png)

下载后直接解压



## 2.配置环境变量

```shell
## 1.MAVEN_HOME
D:\Program\programTool\apache-maven-3.6.3-bin\apache-maven-3.6.3

## 2.Path
%MAVEN_HOME%\bin
```



## 3.验证是否配置成功

打开cmd

```shell
## 输入
mvn -v
```

![image-20210129034039416](https://i.loli.net/2021/01/29/oAptIlELyTdjfuF.png)



## 4.maven设置

```shell
## 1.配置maven仓库
<localRepository>D:\Program\programTool\apache-maven-3.6.3-bin\apache-maven-3.6.3\MavenRepository</localRepository>

## 2.配置阿里云镜像
<mirror>
        <id>alimaven</id>
        <mirrorOf>central</mirrorOf>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
</mirror>

## 打开cmd输入
mvn help:system
```

运行成功后：

![image-20210129034933175](https://i.loli.net/2021/01/29/4mgdqx8rvJek5w3.png)

maven仓库会多很多文件夹：

![image-20210129034957861](https://i.loli.net/2021/01/29/mE9TBdbQRsDAkOq.png)



至此，安装配置完成！