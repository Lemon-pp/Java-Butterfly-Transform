## Oracle出现的问题汇总

1.本来好好的突然出现无监听程序：

原因：PROCESS、SESSION数量设置的不够，导致ORACLE在高峰期的时候，没有足够的PROCESS对连接上来的客户服务进行分配。（连接太多崩溃了）

解决办法：

​	1.方法一：增大Process 和 session ,重启数据库服务

​	2.方法二：重启



2.本地可以连接用PL SQL连接无监听程序：

解决办法：

​	１.查看监听服务和程序是否开启。

```bash
1.查看服务---启动或者重启服务
###
2.查看程序：lsnrctl status
```

​	2.Net Manager修改地址2，改为ip

​	3.Net Configration Assistant重新配置监听程序

