## Oracle11g 安装教程

### 目录

[TOC]

参考链接：https://blog.csdn.net/Feng_xiaoqi/article/details/88780749

因为自己一直没有总结，导致因为某些原因再次安装是，会出现很多的问题，也很头疼，网上博客也五花八门，所以记录自己的整个安装过程，以便于自己下次需要的时候，可以查看，同时，也希望能够帮助到被Oracle安装困扰的同学。

如果是卸载重装Oracle，可以看我另一篇博客；

博客地址：<https://blog.csdn.net/qq_40089907/article/details/105662993>

均是自己一步一步来的，自己都成功了的！

### 一、Oracle下载

官网下载

https://www.oracle.com/technetwork/database/enterprise-edition/downloads/index.html

百度网盘链接：https://pan.baidu.com/s/1fCyi-si9KywZIwFw9jCxvg

提取码：aocb

### 二、Oracle安装

1.解压缩文件，将两个压缩包一起选择， 鼠标右击 ->  解压文件 如图

![image-20201104184040073](https://i.loli.net/2020/11/04/yTdELjP2IvGcOtJ.png)

2.双击database中的setup.exe,开始安装

![image-20201104184104708](https://i.loli.net/2020/11/04/UcNZlC4koxhVITt.png)

3.配置安全更新，电子邮件不用输入，取消我希望前面的勾，点击下一步

弹出尚未提供电子邮件地址的窗口，点击是。

![image-20201104184116939](https://i.loli.net/2020/11/04/21avAShBtjXTwHK.png)

4.选择安装选项，默认创建和配置数据库，点击下一步

![image-20201104184137432](https://i.loli.net/2020/11/04/GUxWJ2ADPT39Ewq.png)

5.系统类，选择桌面类，点击下一步。

![image-20201105093716095](https://i.loli.net/2020/11/05/KZLg53EsOeJj6a4.png)

6.典型安装配置，目录中不要出现中文，可以选择默认，管理口令Oracle官方默认是大写字母+小写字母+数字，长度大于8位，你也可以自己选择，密码随意。我的是Oracle123。

==管理口令要记住。==

![image-20201104184153671](https://i.loli.net/2020/11/04/wB1iGTAKcvDPUF6.png)

7.先决条件检查窗口，点击下一步。

![image-20201104184202974](https://i.loli.net/2020/11/04/NfPsb2o9lqjh1yc.png)

8.点击完成，开始安装。

![image-20201104184213132](https://i.loli.net/2020/11/04/hxNRTlezFtBc4bu.png)

9.安装界面：

![image-20201104184233784](https://i.loli.net/2020/11/04/vAMQFhVZeYgz3qo.png)

10.当进度条达到100%时，会创建数据库实例：

![image-20201104184246500](https://i.loli.net/2020/11/04/zBxUiybJMLAwOqW.png)

11.创建实例完成，除了sys和SYStem账户以外，其他账户处于锁定，我们点击口令管理，将SCOTT账户的勾取消，输入管理口令。口令如上一样。

![image-20201104184255754](https://i.loli.net/2020/11/04/TrnQIymzaUfo9Jk.png)

12.回到主界面，点击关闭。安装到此完成。

接下来开始测试是否安装好！

### 三、Oracle安装完成测试

##### 1.最快测试我们安装是否完成的方法

1.进入开始菜单栏，找到Oracle，点击Database Comtrol - orcl,会跳转到<https://localhost:1158/em/console/logon/logon>。

![image-20201104184305896](https://i.loli.net/2020/11/04/zpIxn5ldhRPGEH2.png)

2.登录。

![image-20201104184317222](https://i.loli.net/2020/11/04/RePTjz16IwMg3WS.png)

用户名：sys  

口令：安装时的管理口令，我的时Oracle123，连接选择SYSDBA。

3.查看用户，点击服务器，用户，就可以看到我们自己的用户情况。

![image-20201104184337858](https://i.loli.net/2020/11/04/AaP6TJiDWE4F2Hn.png)

4.可以看到我们的用户

![image-20201104184403893](https://i.loli.net/2020/11/04/8IiBHfaY2JOTAFG.png)

可以看出我们的安装已经没有问题！

接下来第二种测试，也就是SQL Plus连接测试。

##### 2.SQL plus连接测试

1.在开始菜单栏找到SQL Plus，点击运行

![image-20201104184433033](https://i.loli.net/2020/11/04/tOxIdumDCspVq2M.png)

2.输入用户名和口令，登录

如果是普通用户如scott，直接输入用户名scott和密码(就是管理口令)就行。

如果是sys,用户名需要+as SYSDBA,以管理员用户登录，否则报error

![image-20201104184455608](https://i.loli.net/2020/11/04/k8r3tQSaAboHGgI.png)



连接成功，说明安装完成！

### 四、用PLSQL连接Oracle

1.运行PLSQL

![image-20201104184506271](https://i.loli.net/2020/11/04/ayOD4lVUdu2QxKJ.png)

2.输入用户名、管理口令、数据库和登录方式

我解锁的用户有：sys、system、scott

管理口令都为Oracle123

数据库：localhost:1521/ORCL或者127.0.0.1:1521/ORCL

连接为：如果是sys用户，必须是SYSDBA或者SYSOPER，如果普通用户，只能用Normal。

3.登录成功：

![image-20201104184534219](https://i.loli.net/2020/11/04/GXNijOSCVZquHob.png)

### 五、Oracle学习

##### 1.使用PLSQL连接Oracle创建用户和授予权限

1. 首先用sys登录PLSQL

2. 找到users，右键新建

   ![image-20201104184549183](https://i.loli.net/2020/11/04/pKbk9WHTGZORA2i.png)

3. 填写内容，一定要给权限，不然会导致后续出错。

   ![image-20201104184601861](https://i.loli.net/2020/11/04/Nsjmhd4PkLJWaBK.png)

   权限说明：

   connect : 基本操作表的权限，比如增删改查、视图创建等 
resource： 创建一些函数，比如簇、索引，创建表、列等 
   dba : 相当于管理员权限，拥有系统所有权限 
   
4. 点击应用。创建完成。

   ![image-20201105093807738](https://i.loli.net/2020/11/05/LjuYxqngaDiS7e9.png)

   
   
   然后进行登录，用户名emptest,密码自己设置的，我的是Oracle123.端口还是1521，选择normal登录。

后续补充！