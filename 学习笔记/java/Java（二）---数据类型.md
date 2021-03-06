# Java（二）---数据类型

## 1.数据类型分类

Java提供了两类数据类型：一种是基本数据(原始类型)，一种是引用类型。

数据类型图：

![image-20200831142635865](https://i.loli.net/2020/08/31/tMwGr8LkvuoUa76.png)

## 2.**基本数据类型：**

(一个字节==8位)

|       数据类型       | 字节大小 | 位   |                         范围                          | 默认值   | 包装类    |
| :------------------: | :------: | ---- | :---------------------------------------------------: | :------- | --------- |
|     Byte（字节）     |    1     | 8    |                    -2^7^---2^7^-1                     | 0        | Byte      |
|   short（短整型）    |    2     | 16   |                   -2^15^---2^15^-1                    | 0        | Short     |
|     int （整型）     |    4     | 32   |                   -2^31^---2^31^-1                    | 0        | Integer   |
|   long （长整型）    |    8     | 64   |                   -2^63^---2^63^-1                    | 0        | Long      |
|   float （浮点型）   |    4     | 32   |                   -2^128^ ~ +2^127^                   | 0.0f     | Float     |
|  double  （双精度）  |    8     | 64   |                  -2^1024^ ~ +2^1023^                  | 0.0d     | Double    |
|  char   （字符型）   |    2     | 16   | 0-255（ascill编码格式）或者\u0000 - \uffff（Unicode） | ‘\u0000’ | Character |
| boolean   （布尔型） |   1位    | 1    |                      true/false                       | false    | Boolean   |

float：
    1bit（符号位） 8bits（指数位） 23bits（尾数位）

double：
    1bit（符号位） 11bits（指数位） 52bits（尾数位）

float的指数范围为-128~+127，而double的指数范围为-1024--+1023，并且指数位是按补码的形式来划分的。
其中负指数决定了浮点数所能表达的绝对值最小的非零数；而正指数决定了浮点数所能表达的绝对值最大的数，也即决定了浮点数的取值范围。

float的范围为-2^128 ~ +2^127

double的范围为-2^1024 ~ +2^1023



**基本数据类型**——**类型转换**：

   1):小类型的变量赋值给大类型，会自动转换

   2):大类型的变量赋值给小类型，强制转换

​     语法：在变量前添加要转换的类型



**常用转义字符：**

| 转义字符 | 说明   |
| :------: | ------ |
|    \b    | 退格符 |
|    \n    | 换行符 |
|    \r    | 回车符 |
|    \t    | 制表符 |
|   \\"    | 双引号 |
|   \\'    | 单引号 |
|   \\\    | 反斜线 |

