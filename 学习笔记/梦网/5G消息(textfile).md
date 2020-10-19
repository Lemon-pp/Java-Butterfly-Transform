## 5G消息

5G消息分为：

1.P2P消息---用户与用户

2.A2P消息---应用通知用户

  + Native消息
  + 富媒体卡片消息

3.P2A消息----用户上行出发应用

![1590393145034](E:\待办\Java-Butterfly-Transform\学习笔记\梦网\assets\1590393145034.png)

![1590390559834](E:\待办\Java-Butterfly-Transform\学习笔记\梦网\assets\1590390559834.png)

![1590392901291](E:\待办\Java-Butterfly-Transform\学习笔记\梦网\assets\1590392901291.png)

![1590393378105](E:\待办\Java-Butterfly-Transform\学习笔记\梦网\assets\1590393378105.png)

### 一、文本消息

#### 1.纯文本消息

```properties
<contentType>text/plain</contentType>
<bodyText>
<![CDATA
	[Test bureau test!!]
]>
</bodyText>
</request>

```

#### 2.带浮动菜单文本消息体

```properties
<contentType>multipart/mixed; boundary="next"</contentType>  //通过next作为边界
<bodyText>
<![CDATA
[
    --next
    Content-Type: text/plain
    Content-Length: 4

    test
    
    --next
    Content-Type: application/vnd.gsma.botsuggestion.v1.0+json
    Content-Length: 96

    {
        "suggestions":
        [
            {
                "reply":{                                 //回复
                    "displayText":"Yes",                         //显示文本
                    "postback":{                                 //回发
                        "data":"set_by_chatbot_reply_yes"
                    }
                }
            }
        ]
    }
    --next--
]
]>
</bodyText>
```





### 二、文件消息

#### 1.图片文件消息体

```properties
"<contentType>application/vnd.gsma.rcs-ft-http+xml</contentType>
<bodyText>
<![CDATA
[
    <?xml version=""1.0"" encoding=""UTF-8""?>
    <file xmlns=""urn:gsma:params:xml:ns:rcs:rcs:fthttp"" xmlns:e=""urn:gsma:params:xml:ns:rcs:rcs:up:fthttpext"">
        <file-info type=""thumbnail"">   //缩略图
        <file-size>9307</file-size>
        <content-type>image/jpg</content-type>
        <data url=""http://124.42.103.156:8089/group1/M00/00/18/CgrQc10dbDSAUbLLAB9-LgNBkpE811_small.png"" until=""2019-09-04T15:04:44Z""/>  //缩略图路劲
        </file-info>
        
        <file-info type=""file"">
        <file-size>2063918</file-size>
        <file-name>test图片.png</file-name>
        <content-type>image/jpg</content-type>
        <data url=""http://124.42.103.156:8089/group1/M00/00/18/CgrQc10dbDSAUbLLAB9-LgNBkpE811.png"" until=""2019-09-04T15:04:44Z""/>     //实际图的路劲
        </file-info>
    </file>
]
]>
</bodyText>"
```



#### 2.视频文件消息体

```properties
"<contentType>application/vnd.gsma.rcs-ft-http+xml</contentType>
<bodyText>
<![CDATA
[
    <?xml version=""1.0"" encoding=""UTF-8""?>
        <file xmlns=""urn:gsma:params:xml:ns:rcs:rcs:fthttp"" xmlns:e=""urn:gsma:params:xml:ns:rcs:rcs:up:fthttpext"">
        <file-info type=""thumbnail"">  
        <file-size>7231</file-size>
        <content-type>image/jpg</content-type>
        <data url=""http://124.42.103.156:8089/group1/M00/00/3B/CgrQc12B1WuAGzp7AAAcP6HFWRY852.jpg"" until=""2019-09-04T15:04:44Z""/>
        </file-info>
        
        <file-info type=""file"">
        <file-size>2057548</file-size>
        <file-name>test视频.mp4</file-name>
        <content-type>video/mpeg4</content-type>   //实际文件类型
        <data url=""http://124.42.103.156:8089/group1/M00/00/3B/CgrQc12B1WuAMkC8AB9lTNbc9sU052.mp4"" until=""2019-09-04T15:04:44Z""/>
        </file-info>
    </file>
]
]>
</bodyText>"
```



#### 3.语音文件消息体

```properties
<contentType>application/vnd.gsma.rcs-ft-http+xml</contentType>
<bodyText>
<![CDATA
[
		<?xml version="1.0" encoding="UTF-8"?>
        <file xmlns="urn:gsma:params:xml:ns:rcs:rcs:fthttp" xmlns:e="urn:gsma:params:xml:ns:rcs:rcs:up:fthttpext">
            <file-info type="thumbnail">
            <file-size>1627</file-size>
            <content-type>image/png</content-type>
            <data url="http://124.42.103.156:8089/group1/M00/00/3C/CgrQc12IPqqAfuiyAAAGW8W8kOM770.png" until="2019-09-04T15:04:44Z"/>
            </file-info>
            
            <file-info type="file">
            <file-size>2320727</file-size>
            <file-name>test音频.mp3</file-name>
            <file-disposition>timelen=58000</file-disposition>  //时间线
            <content-type>audio/mp3</content-type>
            <data url="http://124.42.103.156:8089/group1/M00/00/35/CgrQc11dFSaAIB3eACNpVzc5Ktg060.mp3" until="2019-09-04T15:04:44Z"/>
            </file-info>
        </file>
]
]>
</bodyText>
```



#### 4.图片文件消息体带建议回复/建议操作

```properties
<contentType>multipart/mixed; boundary="next"</contentType>
<bodyText>
<![CDATA
[
	--next
    Content-Type: application/vnd.gsma.rcs-ft-http+xml
    Content-Length: 653

    <?xml version="1.0" encoding="UTF-8"?>
    <file xmlns="urn:gsma:params:xml:ns:rcs:rcs:fthttp" xmlns:e="urn:gsma:params:xml:ns:rcs:rcs:up:fthttpext">
        <file-info type="thumbnail">
        <file-size>9307</file-size>
        <content-type>image/jpg</content-type>
        <data url="http://124.42.103.156:8089/group1/M00/00/18/CgrQc10dbDSAUbLLAB9-LgNBkpE811_small.png" until="2019-09-04T15:04:44Z"/>
        </file-info>
        
        <file-info type="file">
        <file-size>2063918</file-size>
        <file-name>test图片.png</file-name>
        <content-type>image/jpg</content-type>
        <data url="http://124.42.103.156:8089/group1/M00/00/18/CgrQc10dbDSAUbLLAB9-LgNBkpE811.png" until="2019-09-04T15:04:44Z"/>
        </file-info>
    </file>

    --next
    Content-Type: application/vnd.gsma.botsuggestion.v1.0+json
    Content-Length: 262

    {
        "suggestions":    //reply代表内置按钮
        [
            {
                "reply":{    //回复     
                    "displayText":"测试1",
                    "postback":{
                        "data":"测试1"
                    }
                }
            },{
                "reply":{
                    "displayText":"测试2",
                    "postback":{
                        "data":"测试2"
                    }
                }
            },{
                "action":{    //操作
                    "displayText":"ceshi",
                    "postback":{
                        "data":"2432"
                    },
                    "urlAction":{    //跳转操作
                        "openUrl":{
                            "url":"http://baidu.com"   //跳转路径
                        }
                    }
                }
        	}
        ]
	}
    --next--
]
]>
</bodyText>
```



#### 5.视频文件消息体带建议回复/建议操作

```properties
<contentType>multipart/mixed; boundary="next"</contentType>
<bodyText>
<![CDATA
[
	--next
    Content-Type: application/vnd.gsma.rcs-ft-http+xml
    Content-Length: 649

    <?xml version="1.0" encoding="UTF-8"?>
    <file xmlns="urn:gsma:params:xml:ns:rcs:rcs:fthttp" xmlns:e="urn:gsma:params:xml:ns:rcs:rcs:up:fthttpext">
        <file-info type="thumbnail">
        <file-size>7231</file-size>
        <content-type>image/jpg</content-type>
        <data url="http://124.42.103.156:8089/group1/M00/00/3B/CgrQc12B1WuAGzp7AAAcP6HFWRY852.jpg" until="2019-09-04T15:04:44Z"/>
        </file-info>
        
        <file-info type="file">
        <file-size>2057548</file-size>
        <file-name>test视频.mp4</file-name>
        <content-type>video/mpeg4</content-type>
        <data url="http://124.42.103.156:8089/group1/M00/00/3B/CgrQc12B1WuAMkC8AB9lTNbc9sU052.mp4" until="2019-09-04T15:04:44Z"/>
        </file-info>
    </file>

    --next
    Content-Type: application/vnd.gsma.botsuggestion.v1.0+json
    Content-Length: 262

    {
        "suggestions":
        [
            {
                "reply":{
                    "displayText":"测试1",
                    "postback":{"data":"测试1"
                    }
                }
            },{
                "reply":{
                    "displayText":"测试2",
                    "postback":{"data":"测试2"
                    }
                }
            },{
                "action":{
                    "displayText":"ceshi",
                    "postback":{
                        "data":"2432"
                    },
                    "urlAction":{
                        "openUrl":{
                            "url":"http://baidu.com"
                        }   
                    }
                }
            }
        ]
	}
    --next--
]
]>
</bodyText>
```



#### 6.音频文件消息体带建议回复/建议操作

```properties
<contentType>multipart/mixed; boundary="next"</contentType>
<bodyText>
<![CDATA
[
        --next
        Content-Type: application/vnd.gsma.rcs-ft-http+xml
        Content-Length: 690

        <?xml version="1.0" encoding="UTF-8"?>
        <file xmlns="urn:gsma:params:xml:ns:rcs:rcs:fthttp" xmlns:e="urn:gsma:params:xml:ns:rcs:rcs:up:fthttpext">
            <file-info type="thumbnail">
            <file-size>1627</file-size>
            <content-type>image/jpg</content-type>
            <data url="http://124.42.103.156:8089/group1/M00/00/3C/CgrQc12IPqqAfuiyAAAGW8W8kOM770.png" until="2019-09-04T15:04:44Z"/>
            </file-info>
            
            <file-info type="file">
            <file-size>2320727</file-size>
            <file-name>test音频.mp3</file-name>
            <file-disposition>timelen=58000</file-disposition>
            <content-type>audio/mp3</content-type>
            <data url="http://124.42.103.156:8089/group1/M00/00/35/CgrQc11dFSaAIB3eACNpVzc5Ktg060.mp3" until="2019-09-04T15:04:44Z"/>
            </file-info>
        </file>

        --next
        Content-Type: application/vnd.gsma.botsuggestion.v1.0+json
        Content-Length: 262

        {
            "suggestions":[{
                "reply":{
                    "displayText":"测试1",
                    "postback":{"data":"测试1"
                    }
                }
            },{
                "reply":{
                    "displayText":"测试2",
                    "postback":{
                        "data":"测试2"
                    }
                }
            },{
                "action":{
                    "displayText":"ceshi",
                    "postback":{"data":"2432"},
                    "urlAction":{
                        "openUrl":{
                            "url":"http://baidu.com"
                }}}}]}
        --next--
    ]]>
</bodyText>


```



