# Typora通过PicGo实现图片上传

## 前提工具

1.**Typora：**百度或者谷歌下载，建议最新版。因为功能是最新版才有。

2.**PicGo：**开源的图片管理工具，可以自己上传图片到各种图床。

3.**`SM.MS`**: **免费**图床。我选择这个图床的原因主要是免费、功能够用。

功能：

实现自动上传图片到图床、保存 Markdown 格式的链接，直接粘贴插入，非常轻松，减少了复杂的工作。

## 搭建步骤

**1.下载新版Typora**

**2.下载PicGo.**

​	下载地址：[https://picgo.github.io/PicGo-Doc/zh/guide/#%E7%89%B9%E8%89%B2%E5%8A%9F%E8%83%BD](https://picgo.github.io/PicGo-Doc/zh/guide/#特色功能)

windows版本：

![image-20200715085021500](https://i.loli.net/2020/07/15/NHOJdkQe5WKh2tV.png)

**3.注册SM.MS**

先注册，然后在API Token中获取token

![image-20200715085316716](https://i.loli.net/2020/07/15/AIMNFi76SzcVGP2.png)

**4.配置PicGo**

1.插件设置

​	下载smms-user插件，在设置中配置sm的token。

2.上传区选择图片上传-SM.MS图床。

**5.Typroa设置**

![image-20200715085716749](https://i.loli.net/2020/07/15/SgsVLF4e26Y39Ir.png)

根据1-2-3-4，完成设置。

## 测试

1.点击验证图片上传选项，查看是否成功。

2.截图复制到typroa中，查看是否自动上传，由本地路劲变为HTTP路径。