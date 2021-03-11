# hexo+github搭建个人博客教程

[toc]

## 1、github创建个人仓库

创建仓库

![img](https://img-blog.csdnimg.cn/20190512104941199.png)

![img](https://img-blog.csdnimg.cn/20190512105152927.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpbnRvbjE=,size_16,color_FFFFFF,t_70)

访问https://lemon-pp.github.io/，就可以看到自己的个人GitHub学习博客了。

## 2、配置SSH key

参考博客：https://zhuanlan.zhihu.com/p/26625249

## 3、准备环境

需要环境：

```shell
node.js
npm
git
```



## 4、安装hexo

```shell
## 下载安装hexo
npm install -g hexo-cli 

## 初始化我们的博客
hexo init blog

## 生成页面
hexo g

## 启动本地服务器,访问http://localhost:4000查看
hexo s

```



## 5、部署到GitHub

找到_config.yml

修改deploy

```yml
deploy:
  type: git
  repository: https://github.com/Lemon-pp/Lemon-pp.github.io.git
  branch: master
```

访问https://lemon-pp.github.io/即可

建议上传到github，用git管理。



## 6、博客个性化

**具体参考butterfly官方文档：**https://butterfly.js.org/posts/dc584b87/#Front-matter

### 更换主题

```shell
# 下载主题
git clone -b master https://github.com/jerryc127/hexo-theme-butterfly.git themes/butterfly

# 配置主题
修改站点配置文件_config.yml中的theme，把主题改为butterfly
## Themes: https://hexo.io/themes/
theme: butterfly
```



### Front-matter

Front-matter是档案最上方以---分隔的区域，用于指定个别档案的变数。

使用方法：在markdown的最上方输入---即可

参考文档即可

官方文档：

https://butterfly.js.org/posts/ceeb73f/#%E7%B6%B2%E7%AB%99%E8%83%8C%E6%99%AF

优秀作者：

https://www.antmoe.com/posts/75a6347a/#%E7%BD%91%E7%AB%99%E8%83%8C%E6%99%AF

#### Page Front-matter

```bash
# 常用
title：【必需】页面标题
date：【必需】页面创建日期
type：【必需】标签、分类和友情链接三个页面需要配置
updated：【可选】页面更新日期
```

####　Post Front-matter

```bash
# 常用
title: 【必需】文章标题
date: 【必需】文章创建日期
updated: 【可选】文章更新日期
tags: 【可选】文章标签
categories: 【可选】文章分类
top_img: 【可选】文章顶部图片
```

### 标签页

前往你的Hexo 博客的根目录

输入hexo new page tags

你会找到source/tags/index.md这个文件

修改这个文件：

```bash
--- 
title:标签
date: 2018-01-05 00:00:00 
type: "tags" 
---
```

### 分类页

前往你的Hexo 博客的根目录

输入hexo new page categories

你会找到source/categories/index.md这个文件

修改这个文件：

```
--- 
title:分类
date: 2018-01-05 00:00:00 
type: "categories" 
---
```

### 友情链接

创建友情链接页面
前往你的Hexo 博客的根目录
输入 hexo new page link
你会找到source/link/index.md这个文件
修改这个文件：

```
--- 
title:友情链接
date: 2018-06-07 22:17:49 
type: "link" 
---
```

友情链接添加:

在Hexo博客目录中的source/_data，创建一个文件link.yml

```yml
class:
  class_name: 友情链接
  link_list:
    1:
      name: 小康博客
      link: https://www.antmoe.com
      avatar: https://img.antmoe.com/avatar.png
      descr: 每天进步一点点
    2:
      name: test
      link: https://www.xxxxxxcn/
      avatar: https://xxxxx/avatar.png
      descr: test
```

### 音乐

参考:https://github.com/MoePlayer/hexo-tag-aplayer/blob/master/docs/README-zh_cn.md

### 电影

参考:https://github.com/mythsman/hexo-douban

### 图库

图库页面只是普通的页面，你只需要hexo n page xxxxx创建你的页面就行

然后使用标签外挂galleryGroup，具体用法请查看对应的内容。

https://butterfly.js.org/posts/4aa8abbe/#Gallery%E7%9B%B8%E5%86%8A%E5%9C%96%E5%BA%AB

#### 子页面

参考:https://butterfly.js.org/posts/4aa8abbe/#Gallery%E7%9B%B8%E5%86%8A

### 404页面

修改主题配置文件:

```yml
error_404: 
    enable: true 
    subtitle: "页面没有找到" 
    background:
```

### 导航菜单

修改主题配置文件:

```yml
menu:
  首页: / || fas fa-home
  时间轴: /archives/ || fas fa-archive
  标签: /tags/ || fas fa-tags
  分类: /categories/ || fas fa-folder-open
  清单|| fa fa-heartbeat: 
    -音乐|| /music/ || fas fa-music 
    -照片|| /Gallery/ || fas fa-images 
    -电影|| /movies/ || fas fa-video
  友链: /link/ || fas fa-link
  关于: /about/ || fas fa-heart

```

### 顶部图

```
配置	解释
index_img	 主页的top_img
default_top_img	  默认的top_img，当页面的top_img 没有配置时，会显示default_top_img
archive_img	  归档页面的top_img
tag_img	tag   子页面的默认top_img
tag_per_img	tag   子页面的top_img，可配置每个tag 的top_img
category_img	  category 子页面的默认top_img
category_per_img	category 子页面的top_img，可配置每个category 的top_img
```

### 文章置顶

你可以直接在文章的front-matter区域里添加sticky: 1属性来把这篇文章置顶。数值越大，置顶的优先级越大。

### 文章封面

文章的markdown文档上,在Front-matter添加cover,并填上要显示的图片地址。
如果不配置cover,可以设置显示默认的cover.

如果不想在首页显示cover,可以设置为false

**修改主题配置文件**

```
cover: #是否显示文章封面index_enable: true aside_enable: true archives_enable: true #封面显示的位置#三个值可配置left , right , both position: both #当没有设置cover时，默认的封面显示default_cover:
  
 
```

当配置多张图片时,会随机选择一张作为cover.此时写法应为

```yml
default_cover: - https://cdn.jsdelivr.net/gh/jerryc127/CDN@latest/cover/default_bg.png - https://cdn.jsdelivr.net/gh/jerryc127/CDN@latest/cover/default_bg2.png - https://cdn.jsdelivr.net/gh/jerryc127/CDN@latest/cover/default_bg3.png
   
```

![image-20201104160915517](https://i.loli.net/2020/11/04/8JigH9MTyA6N43B.png)

### 文章页相关配置

#### 文章meta显示

修改主题配置文件

post_meta

#### 文章版权

post_copyright

#### 文章打赏

reward

#### TOC

在文章页，会有一个目录，用于显示TOC。

toc

#### 相关文章

相关文章推荐的原理是根据文章tags的比重来推荐

修改 主题配置文件

```yml
related_post: enable: true limit: 6 #显示推荐文章数目date_type: created # or created or updated文章日期显示创建日或者更新日
```

#### 头像

修改 主题配置文件

```yml
avatar: img: /img/avatar.png effect: true #头像会一直转圈
```

![image-20201104162336189](https://i.loli.net/2020/11/04/CeAuHr8aWlZLjTI.png)

#### 侧边栏设置

可自行决定哪个项目需要显示，可决定位置，也可以设置不显示侧边栏。

修改 ==主题配置文件==

```
aside
```

#### 最新评论

在侧边栏显示最新评论板块

修改 主题配置文件

```
newest_comments
```

### 评论

参考:

https://butterfly.js.org/posts/ceeb73f/#%E8%A9%95%E8%AB%96

### 搜索系统

你需要安装hexo-generator-search .根据它的文档去做相应配置。注意格式只支持xml。

修改 主题配置文件

```
local_search: enable: false
```

### 网站背景

默认显示白色，可设置图片或者颜色

```
#图片格式url(http://xxxxxx.com/xxx.jpg) 
#颜色（HEX值/RGB值/颜色单词/渐变色) 
#留空不显示背景
background:
```

### footer 背景

```
# footer是否显示图片背景(与top_img一致) 
footer_bg:  true

留空/false	显示默认的颜色
img链接	图片的链接，显示所配置的图片
颜色(
HEX值- #0000FF
RGB值- rgb(0,0,255)
颜色单词- orange
渐变色- linear-gradient( 135deg, #E2B0FF 10%, #9F44D3 100%)
）	对应的颜色
true	显示跟top_img 一样

```

### 背景特效

canvas_fluttering_ribbon

### 页面美化

```
#美化页面显示
beautify: enable: true field: site # site/post title-prefix-icon: '\f0c1' title-prefix-icon-color: "#F47466"
 
field配置生效的区域
post 只在文章页生效
site 在全站生效
```

### 网站副标题

可设置主页中显示的网站副标题或者喜欢的座右铭。

修改 主题配置文件

```
#主页subtitle 
subtitle: enable: true #打字效果effect: true #循环或者只打字一次loop: false # source调用第三方服务# source: false关闭调用# source: 1调用搏天api的随机语录（简体） https ://api.btstu.cn/ # source: 2调用一言网的一句话（简体） https://hitokoto.cn/ # source: 3调用一句网（简体） http://yijuzhan.com/ # source: 4调用今日诗词（简体） https://www.jinrishici.com/ # subtitle会先显示source ,再显示sub的内容source: false #如果有英文逗号' , ',请使用转义字元& #44; #如果有英文双引号' " ',请使用转义字元" #开头不允许转义字元，如需要，请把整个句子用双引号包住#例如”"Never put off till tomorrow what you can do today""#如果关闭打字效果，subtitle只会显示sub的第一行文字sub:
   
  
    - 今日事,今日毕- Never put off till tomorrow what you can do today
              

```

### 字数统计

打开hexo 工作目录

npm install hexo-wordcount --save or yarn add hexo-wordcount

修改主题配置文件:wordcount

### 图片大图查看模式

fancybox



## 7、写博客

```shell
# 生成文章
hexo new 'my-first-blog'

# 生成页面
hexo new page '名字'
```

**提交多个页面：**

```shell
## 清除页面
hexo clean

## 生成页面
hexo g

## 打开本地服务
hexo s

## 推送到github
hexo d
```

