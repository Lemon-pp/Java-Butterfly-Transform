## Linux下设置静态IP

### 目录

[TOC]

参考地址：<https://blog.csdn.net/u014466635/article/details/80284792>

### 一、设置虚拟网络编辑器

在菜单栏选择编辑→ 虚拟网络编辑器，打开虚拟网络编辑器对话框，选择Net网络连接方式，随意设置子网IP，点击NAT设置页面，查看子网掩码和网关，后面修改静态IP会用到。

![1588039459471](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588039459471.png)

![1588039468135](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588039468135.png)

注：上面的“使用本地DHCP服务……”这一项是没有钩选的

![1588039498506](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588039498506.png)

### 二、检查物理主机 网卡设置

打开网络和共享中心→ 更改适配器设置→，在VMware Network Adapter VMnet8上单击右键，选择属性按钮打开属性对话框。  

![1588039528168](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588039528168.png)

修改IP地址注意填写和第一步设置的一样

![1588039543917](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588039543917.png)

### 三、进入虚拟机，设置固定IP地址。

输入如下命令：vi /etc/sysconfig/network-scripts/ifcfg-eth33

注：修改网络设置必须是root用户或是sudo 

![1588039624014](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588039624014.png)

DEVICE="eth0"
BOOTPROTO="static"
HWADDR="00:0C:29:F4:7E:C9"
IPV6INIT="yes"
NM_CONTROLLED="yes"
ONBOOT="yes"
TYPE="Ethernet"
UUID="2a76c2f8-cd47-44af-936d-11559b3a498d"
IPADDR="192.168.73.100"
NETMASK="255.255.255.0"

GATEWAY="192.168.73.1"

下一步，保存退出

为了使地址生效，需要重新启动网络配置，如下图输入：service network restart

![1588039645702](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1588039645702.png)

### 四、测试网络是否通畅

在Linux下和Windows下互相ping一下。