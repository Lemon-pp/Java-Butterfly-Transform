# Linux下部署nginx

### 1、安装make

```shell
yum -y install gcc automake autoconf libtool make
```



### 2、安装g++

```shell
yum install gcc gcc-c++
```



### 3、安装库

```shell
## 安装PCRE库
tar -zxvf pcre-8.43.tar.gz
cd pcre-8.43
./configure
make
make install


## 安装zlib库
wget http://zlib.net/zlib-1.2.11.tar.gz
tar -zxvf zlib-1.2.11.tar.gz
cd zlib-1.2.11
./configure
make
make install

## 安装ssl
cd /usr/local/src
wget https://www.openssl.org/source/openssl-1.0.2s.tar.gz
tar -zxvf openssl-1.0.2s.tar.gz

## 安装nginx
cd /usr/local/src
wget http://nginx.org/download/nginx-1.14.2.tar.gz
tar -zxvf nginx-1.14.2.tar.gz
cd nginx-1.14.2

./configure --prefix=/usr/local/nginx --conf-path=/usr/local/nginx/nginx.conf --pid-path=/usr/local/nginx/nginx.pid --with-http_stub_status_module --with-http_ssl_module --with-openssl=/usr/local/src/openssl-1.0.2t --with-pcre=/usr/local/src/pcre-8.43 --with-zlib=/usr/local/src/zlib-1.2.11 --with-http_realip_module

make
make install

```



### 4、启动

```bash
## 启动nginx
cd nginx/sbin
./nginx

## 停止nginx
nginx -s stop

nginx -s reload
```



### 5、配置nginx.conf

```shell

```



