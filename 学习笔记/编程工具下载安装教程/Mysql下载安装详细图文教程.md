# Mysql下载安装详细图文教程

## 1.下载

```http
https://dev.mysql.com/downloads/mysql/
```

![image-20210129015734120](https://i.loli.net/2021/01/29/HL2fp1koDAnU9iO.png)



## 2.安装过程

1.将mysql-8.0.16-winx64文件解压至某盘（不要装在c盘，其路径为E:\mysql-8.0.16-winx64），路径文件夹名称不能包含中文，涉及到字节码问题。图例：

```shell
## 解压安装目录
D:\Program\programTool\mysql8.0\mysql-8.0.23-winx64\mysql-8.0.23-winx64
```



2.设置环境变量

path：

```shell
## mysql安装目录下的bin目录
D:\Program\programTool\mysql8.0\mysql-8.0.23-winx64\mysql-8.0.23-winx64\bin
```



3.安装

管理员身份打开cmd，切换到bin目录

```shell
## 执行命令，开始安装
mysqld --initialize-insecure --user=mysql
注：若提示缺少msvcp120.dll等文件，去网上下载后，重启cmd重复以上安装步骤即可。
```

![image-20210129021530988](https://i.loli.net/2021/01/29/AyiKYBbxh1tmWfR.png)

执行完，在mysql-8.0.23-winx64下会出现data文件夹



```shell
## 安装mysql服务
mysqld --install
```

成功后会提示Service successfully installed

![image-20210129021646672](https://i.loli.net/2021/01/29/W1eULqJIcaBgfAk.png)



```shell
## 启动MySQL服务
net start mysql
```

完成以上步骤已完成mysql安装



## 3.验证是否安装成功

cmd启动mysql

```shell
mysql -uroot -p
```

安装初始密码为空，执行此代码后按两次回车。

![image-20210129022037131](https://i.loli.net/2021/01/29/ZsHMU8CTVf4axi9.png)



初次设置密码

```shel
ALTER USER 'root'@'localhost' IDENTIFIED BY 'yourpass';
```

