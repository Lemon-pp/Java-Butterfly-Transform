# node.js的卸载和安装

## 一、卸载

1.打开node.js文件的位置，选择uninstall Node.js

```bash
## 文件位置
C:\ProgramData\Microsoft\Windows\Start Menu\Programs\Node.js
```

2.删除.npmrc文件

```bash
## 文件位置
C:\Users\dell
```



## 二、安装

### 1.下载安装包

```bash
## 下载地址
https://nodejs.org/zh-cn/download/
```

### 2.安装

![image-20200724144102100](https://i.loli.net/2020/07/24/nawADFPc7dtylBC.png)

1.默认安装---到安装完成。默认是配置好环境变量了。

![image-20200724144411044](https://i.loli.net/2020/07/24/5i6QWCJvPXEngqB.png)

2.打开cmd，输入node-v    和 npm -v 会显示版本。

```bash
## 
新版本的node.js会自带npm

## 
npm 包管理工具
```

3.打开安装目录，新建两个文件夹

![image-20200724144643821](https://i.loli.net/2020/07/24/54ZOl8onwQvBF3E.png)

输入命令：

```bash
npm config set prefix "E:\Nodejs\node_global"

npm config set cache "E:\Nodejs\node_cache"
```

4.配置环境变量

```bash
## 1.系统变量配置NODE_PATH
值：E:\Nodejs\node_global\node_modules

## 2.用户变量配置Path---将默认的C盘下APPData\Roaming\npm替换
值：E:\Nodejs\node_global\node_modules
```

5.配置完成，配置淘宝镜像

```bash
npm config set registry http://registry.npm.taobao.org/
```

6.测试

```bash
npm install express -g
```

会在配置的路径中生成node_modules文件夹。

**安装完成！**