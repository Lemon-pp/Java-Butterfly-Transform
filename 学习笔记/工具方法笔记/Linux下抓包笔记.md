# Linux下服务器端网络抓包和分析笔记

# 场景





# 抓吧

1.首先登录到服务器

2.安装抓包工具tcpdump

```shell
yum install -y tcpdump
```

![image-20210318174859534](https://i.loli.net/2021/03/18/hOeEVa4LTB7ylA1.png)

3.执行命令查看网卡名

```shell
ip addr
```

![image-20210318174929229](https://i.loli.net/2021/03/18/yr9XM567pN2CbiB.png)

4.执行以下命令即可开始抓eth0网卡的包，并保存到名为weatherservice.cap的文件：

```shell
tcpdump tcp -i eth0 -w ./weatherservice.cap
```

![image-20210318175324308](https://i.loli.net/2021/03/18/UXIbDCQd8uVkqfm.png)

5.开始请求交互，抓包开始了，想要结束的时候使用Ctrl+C，然后weatherservice.cap保存了刚刚抓取的数据。

6.查看数据。打开wireshark，然后用wireshark打开weatherservice.cap文件，就可以看到数据了。

7.过滤数据

+ http 只显示http请求
+ ip.dst=192.168.1.74 只显示目的IP为192.168.1.74的请求。