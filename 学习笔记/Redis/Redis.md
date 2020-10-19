#                                                             Redis

## Redis入门

### 概述

Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、缓存和消息中间件。 它支持多种类型的数据结构，如 [字符串（strings）](http://www.redis.cn/topics/data-types-intro.html#strings)， [散列（hashes）](http://www.redis.cn/topics/data-types-intro.html#hashes)， [列表（lists）](http://www.redis.cn/topics/data-types-intro.html#lists)， [集合（sets）](http://www.redis.cn/topics/data-types-intro.html#sets)， [有序集合（sorted sets）](http://www.redis.cn/topics/data-types-intro.html#sorted-sets) 与范围查询， [bitmaps](http://www.redis.cn/topics/data-types-intro.html#bitmaps)， [hyperloglogs](http://www.redis.cn/topics/data-types-intro.html#hyperloglogs) 和 [地理空间（geospatial）](http://www.redis.cn/commands/geoadd.html) 索引半径查询。 Redis 内置了 [复制（replication）](http://www.redis.cn/topics/replication.html)，[LUA脚本（Lua scripting）](http://www.redis.cn/commands/eval.html)， [LRU驱动事件（LRU eviction）](http://www.redis.cn/topics/lru-cache.html)，[事务（transactions）](http://www.redis.cn/topics/transactions.html) 和不同级别的 [磁盘持久化（persistence）](http://www.redis.cn/topics/persistence.html)， 并通过 [Redis哨兵（Sentinel）](http://www.redis.cn/topics/sentinel.html)和自动 [分区（Cluster）](http://www.redis.cn/topics/cluster-tutorial.html)提供高可用性（high availability）。



**学习：**

```
官网：<https://redis.io/>

中文官网：<http://www.redis.cn/>

视频：B站狂神说
```



### Windows下载

下载地址：<https://github.com/redis/redis/releases>

解压

```java
启动服务：
双击redis-server

打开客户端：
双击redis-cli
```



### Linux下载

下载地址：<http://www.redis.cn/>

```java
解压到-->usr/local

生成程序：
yum install gcc-c++

make

make install

程序路径-->usr/local/bin

启动服务：
在usr/local/bin下输入：redis-server

启动客户端：
在usr/local/bin下输入：redis-cli

```

**后台启动：**

```java
修改redis.conf:
找到：daemonize:no改为yes

启动服务：带上redis.conf一起启动
在usr/local/bin下输入：redis-server /路劲/redis.conf

启动客户端：
在usr/local/bin下输入：redis-cli
```

**性能测试：**

```java
redis-benchmark -h localhost -p 6379 -c 100 -n 100000
```

测试100个线程写入10万数据的时间

### redis常用命令

```java
生成key:
set name zhangsan

拿到value：
get name

切换数据库：
select 数据库号码（默认16个数据库）

查看所有key:
keys *

清除当前数据库：
flushdb

清除全部数据库：
flushall

查看key是否存在：
exists key名字

移除key：
move key 1
    
设置value过期时间：
expire name 时间

查看剩余过期时间：
ttl name

查看key类型：
type key
    
value后追加字符串：
    append key value
        
```

### redis基础知识

>
>
>Redis是单线程的！

Redis是基于内存操作的，CPU不是Redis的瓶颈。

**速度快的原因：**

官方数据：100000+的QPS。

```java
1.核心：redis将所有数据放在内存种，所以单线程去操作效率最高，多线程会引起上下文操作。对于内存系统，没有上下文操作效率是最高的。
```

## 五大数据类型

### String

最常用的最基本的数据类型，就是常用的K-V键值对。

Redis的操作都是原子性的，就是一个事务是不可分割的最小单元，事务中的所有操作要么都完成，要么都失败。不用考虑并发问题。适用于自增自减等。

**命令：**

```java
set key value

get value

append key value #如果key不存在，相当于set key value

keys * #查看所有key
        
Strlen    key #获取字符串长度
    
##i++##
    set num 0  
    incr num   #num++
    decr num   #num--
    incrby num 10   #num+10
    decrby num 10   #num-10
    
##字符串范围##
    set key "lemon,hello"
    
    getrange key 0 3 #截取字符串[0,3]
    
    getrange key 0 -1 #和get key 一样
    
##替换##
    set key "lemon,hello"
    
    setrange key 1 xx  #替换指定位置开始的字符串
    
####
    setex  #(set with expire) 设置过期时间
    
    setnx  #(set if not exist)  不存在则设置（在分布式锁中经常使用）
    
##批量操作##
    mset k1 v1 k2 v2
    mget k1 v1 k2 v2
    msetnx k1 v1 k3 v3 #msetnx是一个原子操作，要么全部成功，要么全部失败。
    
##对象##
    set user: 1 {name:zhangsan,age:18} #设置一个user:1对象，值为json字符
        
####
    getset key value #如果不存在，则返回nil
        			#如果存在，获取原来的值，并设置新的值
        
```

**String的使用场景：**

+ 计数器
+ 统计多单位的数量
+ 粉丝数
+ 对象缓存存储



### List（列表）

列表，按照Sting元素插入顺序排序。可以实现栈、队列功能，可以实现最新消息排行榜功能。

**命令：**

```java
#####
	lpush key value # 将一个值或者多个值，插入列表头部（左）
    
    lrange key 0 -1   #查看key下所有的value
    
    lrange key 0 1   # 查看0-1区间的value
    
    rpush key value  # 将一个值或者多个值，插入列表尾部（右）
    
    lpop key #移除 key的第一个元素
    rpop key #移除 key的最后一个元素
    
    lindex key 0 #通过下标获得list中的某一个值
    
    llen key #返回列表的长度
    
    ltrim key 1 2 #截取[1,2]区间的值留下
    
    rpoplpush key otherkey #移除列表的最后一个元素，移动到新的列表中
    
    lset list 0 item #如果存在，更新当前下标的值，不存在就报错
    
    linsert list before/after value value #将一个值插入到列表中已存在的某个值前后
   
```

**总结：**

+ list实际上是一个链表，左右都可以插入。
+ 如果key不存在，创建新的链表。
+ 如果key存在，新增内容。
+ 如果移除了所有值，空链表，也代表不存在！
+ 在两边插入或者改动值，效率最高。

**使用场景：**

+ 消息队列
+ 栈



### set（集合）

String类型组成的无序集合，通过hash表实现，不允许重复。可以实现数据去重、求并集交集等。

**命令：**

```java
#############
    sadd key value    #添加值
    smembers key      #查看指定set的所有值
    
    sismember key value  #判断某个值是否在set中
    scard key      #获取set集合中的内容元素个数
    
    srem key value   #移除set中的指定元素
    
    srandmember myset   #随机获取set中的一个元素
    
    spop ke   #随机移除一个元素
    
    smove myset myset2 value			#将一个指定的值移动到另一个set集合
    
################################
    查看两个set的交集、并集、差集（不同的）
    sdiff key1 key2    #差集
    sinter key1 key2   #交集
    sunion key1 key2   #并集

```

场景：

+ 微博粉丝、共同关注
+ 实现数据去重、求并集交集



### Hash（哈希）

Sring元素组成的字典，适合存储对象。

**命令：**

```java
###################
    hset myhash field helo  #set一个具体的 key-value
    
    hget myhash field #获取一个字段值
    
    hmset myhash2 ke1 v1 ke2 v2 #set多个键值对
    
    hmget myhash2 ke1  ke2   # 获取多个字段值
    
    hgetall key   #获取所有的键值对
    
    hdel myhash field #删除hash指定key字段
    
    hlen key   #获取hash表的字段数量
    
    hkeys key #获取所有的字段
    
    hvals key  #获取所有的value
    
    hexists key # 判断是否存在
    
###################
    incr decr incrby decrby hsetnx
    
    
```

**场景：**



### Zset（有序集合）

有序唯一，通过分数将集合中的成员进行从小到大排序。可以实现分数排行榜等场景。

在set的基础上，加了一个值。

set k1 v1

zset k1 score v1

**命令：**

```java
###############
    
	zadd myzset 1 one
    
    zrange myzset 0 -1
    
###############
    排序
    zrangebyscore key -inf +inf   #按照value升序排列
    
    zrangebyscore key -inf +inf withscore   #升序并将value也输出出来
    
    zrevrangebyscore key 0 -1 #按照value降序排列
    
    zrange key 0 -1 # 查看所有
    
    zrem key value  # 移除有序集合中的指定元素
    
    zcard key  #获取有序集合中的个数
    
    zcount key 1 2 #获取指定区间的成员个数
    

```

应用场景：

+ 分数排行榜

## Redis的基本事务操作

redis的单条命令都具有原子性，但是事务不保证原子性。

redis事务没有隔离级别的概念。

所有的命令在事务中，并没有直接被执行，只有发起执行命令的时候才会执行！Exec

redis的事务：

+ 开启事务（multi）
+ 命令入队（.....）
+ 执行事务（exec）

**命令：**

```
#开启事务
multi

#命令入队
......

#执行事务
exec

```

异常有两种：

>编译型异常，错误命令会报错，执行事务整个事务也会错误。

```java
###############
    multi
    
    错误命令
    
    正确命令
    
    exec --------------整个事务异常
    
    
```



>运行时异常，输入命令时正常，执行事务时，正确命令正常执行，错误命令抛出异常。

```java
###################
    multi
    
    正确的命令（但是运行会报错）
    
    正确命令
    
    exec---------事务正常执行，错误的地方抛出异常，正确的正常执行。
```

>Redis监视测试  ----- watch（相当于加锁）

```java
正常执行#########
127.0.0.1:6379> set out 0
OK
127.0.0.1:6379> watch money  #监视money
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> decrby money 20
QUEUED
127.0.0.1:6379> incrby out 20
QUEUED
127.0.0.1:6379> exec
1) (integer) 80
2) (integer) 20           #正常执行
127.0.0.1:6379> 
```

 **测试多线程修改值，使用watch可以当作redis的乐观锁操作！**

```java
127.0.0.1:6379> watch money
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> decrby money 20
QUEUED
127.0.0.1:6379> incrby out 20
QUEUED
127.0.0.1:6379> exec  #在执行事务之前另一个线程修改了money的值，被watch观察到，导致失败。
(nil)
127.0.0.1:6379>
```

![image-20200716093400841](https://i.loli.net/2020/07/16/cyxT2Jq4h7PkOAb.png)



### Jedis

Redis官方推荐的Java连接开发工具，使用Java操作redis中间件。

1.导入依赖

```xml
<dependencies>
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>3.3.0</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.72</version>
        </dependency>
    </dependencies>
```

2.编码测试

![image-20200716095433135](https://i.loli.net/2020/07/16/ezUtCrd9nwFI1hT.png)



###  Spring Boot 整合Redis

springboot操作数据：spring-data jpa jdbc mongodb redis

Spring Data 也是和spirng boot齐名的项目

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({ LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class })
public class RedisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

}
```



1.导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```



2.配置连接

**如何了解怎么去配置一个新的东西？**

==看源码==

比如redis:

+ 先去依赖中找到spring-boot-autoconfigure--》spring.factories

+ 然后搜索redis,进入RedisAutoConfiguration类

+ 可以看到@EnableConfigurationProperties({RedisProperties.class})注解

+ 进入RedisProperties.class,可以看到

  ```
  @ConfigurationProperties(
      prefix = "spring.redis"
  )
  以及下面的配置项
  ```

+ 然后在application.properties文件中，配置，前缀为spring.redis+配置项。

配置：

```properties
spring.redis.host=127.0.0.1
spring.redis.port=6379
```

3.测试

