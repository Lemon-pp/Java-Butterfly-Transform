##  Linux下网关部署（Oracle）

### 目录

[TOC]

### 一、环境说明

Linux：Centos7.3  IP :192.169.1.156  

数据库：Oracle   数据库名:ORCL  数据库用户名：emptest  密码：Oracle123  

IP：192.168.1.172

### 二、数据库配置

1. 创建数据库用户
2. 授予用户权限
3. 执行SQL脚本：从001-004依次执行。

### 三、创建EMP用户组和网关用户empgw

1.创建用户组：groupadd emp

2.创建网关用户empgw，后续的网关部署在该目录下。

​	useradd -m -g emp -d /home/empgw empgw

3.设置网关密码：passwd empgw

​	输入密码：我的是123456

​	提示我的密码短于8位 ’BAD PASSWORD: The password is shorter than 8 characters‘ ，忽略此提示，继续确认密码.

4.将cfgforsmsgw.sh脚本拷贝到/home/empgw目录下

5.赋予此脚本可执行权限

​	chmod a+x cfgforsmsgw.sh

6.执行脚本

​	./cfgforsmsgw.sh

​	![1588056131126](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588056131126.png)



### 四、部署安装

1.在empgw目录下创建empgateway目录（使用empgw登录）

​	su empgw

​	mkdir empgateway

2.将mwsmsgw.x86_64.tar.gz、install.sh文件复制到empgateway目录下

​	使用Xftp复制。

3.给install.sh赋予权限然后执行

​	chmod a+x install.sh

```
如果执行上一个命令出现这个问题：
chmod: changing permissions of ‘install.sh’: Operation not permitted
则更改该目录的所属用户为empgw:chown -R empgw install.sh
```

​	./install.sh

4.部署执行完后，执行目录：source ~/.bashrc(注意bashrc前面有一个点“.”)

5.安装完成后使用service命令检测cu服务是否已经启动。

​	service cu status

​	5.1 如图表示cu运行正常

​	![1588057503423](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588057503423.png)

​	PID[29022]表示进程ID为29022，LISTEN PORT[9901]表示CU监听9901（这个可修改）端口.

​	5.2 如下图表示cu运行停止。

​	![1588057549512](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588057549512.png)

​	service cu stop:停止cu服务

​	service cu start:启动cu服务

​	service cu restart:重启cu服务

​	5.3 如果是如下情况，则可以通过cmd来确认cu端口是否开放

​	![1588057598471](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588057598471.png)

​	在cmd端执行命令 telnet 192.169.1.20(网关所在ip) 9901(cu端口默认是9901) ,

如果能通，表名cu服务启动成功.

​	![1588057618072](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588057618072.png)

​	5.4 安装完后的目录结构应该如下图所示:

![1588057843769](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588057843769.png)	5.5 安装完成后，建议重启一次服务器，因为install.sh中某些修改需要重启服务器后生效。



### 五、安装unixODBC

#####　1.检测系统unixODBC版本

​	两种方法：

​		方法一：（ 适用RPM包安装）检测RPM包安装的unixODBC版本，

执行命令rpm -qa|grep odbc 或者 rpm -qa | grep unixODBC。

​		方法二：（适用于源码安装）通过isql命令检测，

执行isql -v  查看unixODBC版本。

​	如果没有，则不需要卸载，如果有，需要卸载。

##### 2.卸载unixODBC

​	方法一 (适用RPM包安装)通过RPM包检测的结果卸载，使用命令 rpm –e [unixODBC包名]。在卸载RPM包的时候可能会被其他ODBC的RPM包所依赖，请先使用同样的方法卸载其他依赖的RPM包；

​	方法二 (适用源代码安装)unixODBC通过源代码安装的，找到源代码安装的位置执行make uninstall或者手动删除unixODBC所安装的文件。

##### 3.源码unixODBC安装（使用empgw登录）

​	3.1 切换empgw用户

​		su empgw

​	3.2 在登录目录mkdir dir创建local目录

​		mkdir local

​	3.3 复制unixODBC-2.3.0.tar.gz到local目录下

​		使用Xftp复制。

​	3.4 解压

​		tar -zxvf unixODBC-2.3.0.tar.gz

​	3.5 进入解压后的unixODBC-2.3.0目录。在执行配置编译选项前需要安装GCC。		（需要root权限）

​		使用gcc -v可以查看自己是否有。

​		yum **install** -y gcc

​	3.6 配置编译选项（--prefix=/home/empgw/local/unixODBC 注意--prefix 后面的目录不能是unixODBC-2.3.0.tar.gz的解压目录/home/empgw/local/unixODBC-2.3.0）

​		./configure --prefix=/home/empgw/local/unixODBC --sysconfdir=/home/empgw/local/unixODBC/etc

​	3.7 配置编译完成后进行编译安装

​		make && make install

​		unixODBC头文件都被安装到了/home/empgw/local/unixODBC/inlucde下.

​		库文件安装到了/home/empgw/local/unixODBC/lib下。

​		unixODBC可执行文件安装到了/home/empgw/local/unixODBC /bin下。

​		ODBC相关配置文件存放在/home/empgw/local/unixODBC/etc下。

​	3.8 配置unixODBC环境变量

​		切换到empgw用户。

​		打开文件 vi ~/.bashrc(注意bashrc前有一个点".")

​		在文件末尾增加如下配置：

```
export ODBCINI=/home/empgw/local/unixODBC/etc/odbc.ini
export ODBCINST=/home/empgw/local/unixODBC/etc/odbcinst.ini
export PATH=$PATH:/home/empgw/local/unixODBC/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/empgw/local/unixODBC/lib
```

​		执行 source ~/.bashrc使文件生效.

##### 3.进入目录1mwsmsgw下，执行ldd mwsmsgw64_r

​	如果出现下图这种：	![1588059550549](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588059550549.png)

​		not found 代表都是目前缺少的系统库，需要我们进行手动安装：

​		![1588059688323](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588059688323.png)

​	安装时主机需要连接外网。需要检测的文件有安装目录下 1mwsmsgw/mwsmsgw_r、2spgate/spgate64_r、2spgate/libsmei64.so、2spgate/libmfwd64.so、cu/cu。

​	如果有很多文件not found,但是使用命令find / -name xxx又能查到，建议重启机器。

```
问题：
1. 系统库not found:不一定是没有，可能在unixODBC下。
	解决方法：在etc/profile下添加unixODBC的环境变量。
```



### 六、oracle数据库ODBC数据源的配置

1.检测TNS : 执行命令echo $TNS_ADMIN 执行结果如下图一、图二所示，如果TNS_ADMIN为空，或为系统目录/etc普通用户无权修改时，需要增加用户自己的TNS_ADMIN路径。

![1588063222087](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588063222087.png)

2.配置环境变量TNS_ADMIN: 打开文件 vi ~/.bashrc(注意bashrc前有一个点".") 在文件末尾增加如下配置，保存退出后，执行 source ~/.bashrc将环境变量注册生效。完成后再执行步骤1，这会就应该如图三所示了。

​	export TNS_ADMIN=/home/empgw/local/etc

3.配置ORACLE客户端TNS：修改TNS_ADMIN对应目录下（/home/empgw/local/etc）的tnsnames.ora文件(如果没有则创建)。添加内容如下。中EMP_ORACLE为一个标识名，配置文件odbc.ini中的ServerName项要与之相对应，SERVICE_NAME为数据库名，HOST为数据库所在主机IP地址，PORT为数据库监听端口号。

```
EMP_ORACLE=
(DESCRIPTION =
	   (ADDRESS_LIST =
	      (ADDRESS = (PROTOCOL = TCP)(HOST = 192.169.1.156)(PORT = 1521))
	      )
	      (CONNECT_DATA = 
	         (SERVER = DEDICATED)
	         (SERVICE_NAME =ORCL)
	      )
	   )
```

4.配置odbcinst.ini文件：在**/home/emp****gw****/local/unixODBC/etc**目录下，如果存在则直接编辑，不存在则创建文件，添加内容如下。其中$(EMPGW_HOME)为网关的主目录。

```
[Oracle]
Description=ODBC for Oracle
Driver=$(EMPGW_HOME)/lib64/oracle/libsqora.so.11.1
FileUsage= 1
Threading=2
```

$(EMPGW_HOME)这个需要替换成自己的网关主目录。

```
[Oracle]
Description=ODBC for Oracle
Driver=/home/empgw/empgateway/lib64/oracle/libsqora.so.11.1
FileUsage= 1
Threading=2
```



5.配置odbc.ini文件：在**/home/emp****gw****/local/unixODBC/etc**目录下，如果存在则直接编辑，不存在则创建文件，添加内容如下。主标签[ORA_EMPSVR]为配置的ODBC数据源名称，网关程序的配置文件中需要进行添加，Driver = Oracle 中的Oralcle对应的是步骤3.4 odbcinst.ini中配置的主标签；Servername = EMP_ORACLE中的EMP_ORACLE对应的是步骤3.3 tnsnames.ora中配置的主标签。

```
[ORA_EMPSVR]
Description = Data Source to Oracle
Driver = Oracle 
Servername = EMP_ORACLE
```

6.测试数据源是否通畅：

isql -v 数据源名称 数据库用户名 数据库密码

例如：isql -v ORA_EMPSVR emptest Oracle123.

+---------------------------------------+

| Connected!                             |

|                                       |

| sql-statement                           |

| help [tablename]                        |

| quit                                   |

|                                       |

+---------------------------------------+

SQL>

出现上述字样表示已数据库连接OK

注意点：

```
1.不要用isql -v测试，会报错。
2.出现错误检查配置文件和环境变量。
```

问题：

```
1.如果测试数据源是否通畅时候，出现错误，没有办法可以试着修改主机名。
```

### 七、LINUX网关序列号注册

使用工具【序列号注册工具.exe】进行序列号注册，注册时必须保证网络能接入我司认证服务器，认证成功后将生成认证文件【empauthen.dat】。请将该文件拷贝到1mwsmsgw/目录下。并保证LINUX网关服务器能正常接入我司认证服务器，否则网关将只能运行七天，并且目前LINUX网关只支持纯网络序列号验证方式。

![1588059806662](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588059806662.png)

1.输入序列号和公司名称，其他随便填

​	序列号：KRUJ8 D5V7D JCYUX 39FQG  

​	公司名称：开发一部测试

​	序列号：LWW5I X55P2 IWTJK K3XKY  

​	公司名称：质量部测试D

![1588060096101](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588060096101.png)

2.empauthen.dat文件拷贝至1mwsmsgw/目录下

3.使用工具【网关部署辅助工具.exe】

![1588063364258](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588063364258.png)

 需要配置项：

​	服务器IP、数据库类型、数据库名称、数据库IP、端口、数据库用户名、密码、ODBC数据源等。

配置完后依次点击 网关初始部署 网关配置 网关服务控制.

然后将网关类型改为短信SPGATE,再依次执行网关初始部署->网关配置-> 网关服务控制.



4.修改网关数据库,网关配置文件中需要手动增加配置OdbcDS=ORA_EMPSVR，修改后完整的配置文件应该如下(注: DBPwd数据库密码为经过Base64编码的密码)，网关的1mwsmsgw_4000_99和2spgate_3000_xxx都要进行修改。

网关配置文件为后缀为.ini的。

```
[GateNO]
GateNo = 99
[LoginDB]
DBType = 2
DBIP = 192.169.1.39
DBPort = 1521
OdbcDS = ORA_EMPSVR
DBName = empsvr
DBUser = sftest
DBPwd = MTIzNDU2
[DataBase]
DBType = 2
DBIP = 192.169.1.39
DBPort = 1521
OdbcDS = ORA_EMPSVR
DBName = empsvr
DBUser = sftest
DBPwd = MTIzNDU2
```



5.启动网关

进入empgateway

进入1mwsmsgw_4000_99目录

执行mwsmsgw64_r_4000_99 , 加&表示为守护进程，即使CTRL+C也不会停止进程。

​	./mwsmsgw64_r_4000_99 &

```
如果启动报：
error while loading shared libraries: libodbc.so.1: cannot open shared object file: No such file or directory
解决办法：
	查看环境变量是否错误，没有错误可能是环境变量未生效，source etc/profile或者重启机器或者用Xshell启动试一下。
```



执行monitor

​	./monitor

查看更新时间是否随着时间再更新变化，是的话表示没问题，短信网关启动成功

![1588063577489](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588063577489.png)

​	

**6.启动spgate网关**

进入目录/home/empgw/empgateway/2spgate_3000_100下

执行spgate64_r_3000_100，加&表示为守护进程，即使CTRL+C也不会停止进程

​	./mwsmsgw64_r_4000_99 &

执行monitor

​	./monitor

查看更新时间是否随着时间再更新变化，是的话表示没问题，spgate网关启动成功

![1588063684131](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588063684131.png)

注意点：

```
如果出现错误，可以去sysLog下查看日志，看错误的情况。
```

7.停止短信网关

​	ps -ef | grep mw

​	kill -9 进程ID 

8.停止短信spgate

​	ps -ef | grep spgate

​	kill -9 进程ID 

### 八、在emp中配置网关

通信配置（通信管理）
1>短彩SP账号--新建sp账号（前端）
2>通道管理--新建通道（后端）
3>账户通道配置（前端）--SP账号绑定通道账号
4>通道账户管理（后端）--修改EMP网关地址（部署网关的地址）、修改模拟网关地址（开启模拟网关的地址，模拟网关也就是运营商网关）

网关启动顺序：

​	梦网网关mw--短信网关spgate--运营商模拟网关CMPP。

4.发送短信（移动办公）
相同内容群发或者不同内容群发

### 九、重装网关

数据库改了之后如何重新搭建网关?	

1.停止mw短信网关进程，停止spgate进程

2.删除文件1mwsmsgw_4000_99  和2spgate_3000_100

3.重新使用工具【网关部署辅助工具.exe】部署mw短信网关，短信spgate

### 十、遇到的问题

1.如果注册mw网关，注册失败，请查看网络

```bash
##
需要在IPV4网络设置中添加外网地址：192.168.x.x
同时网关地址要改为：192.168.1.1
```

2.如果发送发送，显示refuse，connection timeout

```bash
########
说明IP地址没配好

```

