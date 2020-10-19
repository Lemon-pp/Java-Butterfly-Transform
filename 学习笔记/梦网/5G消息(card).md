### 卡片消息

#### 1.纯文本卡片消息体

![1590399912875](E:\待办\Java-Butterfly-Transform\学习笔记\梦网\assets\1590399912875.png)

```properties
"<contentType>application/vnd.gsma.botmessage.v1.0+json</contentType>
<bodyText>
<![CDATA
[
    { 
        ""message"":{ 
            ""generalPurposeCard"":{    //通用卡
                ""layout"":{          //布局
                    ""cardOrientation"":""VERTICAL""   //卡片定位---垂直
                }, 
                ""content"":{        //内容
                    ""description"":""【hello】。"",    //描述
                    ""suggestions"":          //内置按钮，每一个reply代表一个内置按钮
                    [
                        { 
                            ""reply"":{       
                                ""displayText"":""订单详情"", 
                                ""postback"":{ 
                                    ""data"":""订单详情"" 
                                }
                            } 
                        }, { 
                            ""reply"":{ 
                                ""displayText"":""寄放在代收点"", 
                                ""postback"":{ 
                                    ""data"":""寄放在代收点"" 
                                } 
                            } 
                        }, { 
                            ""reply"":{ 
                                ""displayText"":""指定派送时间"", 
                                ""postback"":{ 
                                    ""data"":""指定派送时间"" 
                                } 
                            } 
                        } 
                    ] 
                } 
            } 
        }
    }
]]>
</bodyText>"
```



#### 2.纯文本卡片带内置按钮加悬浮按钮消息体

```properties
<contentType>multipart/mixed; boundary="next"</contentType>

<bodyText>
<![CDATA
[
    --next
    Content-Type: application/vnd.gsma.botmessage.v1.0+json
    Content-Length: 386

    {
        "message":{
            "generalPurposeCard":{
                "layout":{
                    "cardOrientation":"VERTICAL"
                },
                "content":{
                    "description":"【hello】",
                    "suggestions":        //内置按钮
                    [
                        {
                            "reply":{ 
                                "displayText":"订单详情",
                                "postback":{
                                    "data":"订单详情"
                                }
                            }
                        },{
                            "reply":{
                                "displayText":"寄放在代收点",
                                "postback":{
                                    "data":"寄放在代收点"
                                }
                            }
                        },{
                            "reply":{
                                "displayText":"指定派送时间",
                                "postback":{
                                    "data":"指定派送时间"
                                }
                            }
                        }
                    ]}}}}

    --next
    Content-Type: application/vnd.gsma.botsuggestion.v1.0+json
    Content-Length: 226

    {
        "suggestions":          //悬浮按钮
        [
        	{
                "action":{
                    "dialerAction":{   //拨号
                        "dialPhoneNumber":{  //拨送号码
                            "phoneNumber":"17745632640"
                        }
                    },
                    "displayText":"123213",
                    "postback":{
                        "data":"423"
                     }
                }
            },{
                "reply":{
                    "displayText":"111111111111111111111111",
                    "postback":{
                        "data":"上线"
                    }}}]}
    --next--
]
]>
</bodyText>
```



#### 3.单卡片图片消息体

```properties
<contentType>application/vnd.gsma.botmessage.v1.0+json</contentType>
<bodyText>
<![CDATA
[
    {
        "message":{
            "generalPurposeCard":{
                "content":{
                    "description":"三生三世",
                    "media":{      //媒体
                        "height":"SHORT_HEIGHT",
                        "mediaContentType":"image/jpeg", //媒体文件类型
                        "mediaUrl":"http://124.42.103.156:8089/group1/M00/00/17/CgrQc10ax2OAAkdaABXHLlcU5h8991.png",                  //实际媒体路径
                        "thumbnailContentType":"image/png",   //缩略图类型
                        "thumbnailUrl":"http://124.42.103.156:8089/group1/M00/00/17/CgrQc10ax2OAAkdaABXHLlcU5h8991_small.png"               //缩略图地址
                    },
                    "suggestions":[],
                    "title":"三生三世"
                },
                "layout":{
                    "cardOrientation":"VERTICAL",
                    "cardWidth":"MEDIUM_WIDTH"  //卡片宽度---中等宽度
                }
            }
        }
    }
]
]>
</bodyText>
```



#### 4.单卡片视频消息体

```properties
<contentType>application/vnd.gsma.botmessage.v1.0+json</contentType>
<bodyText>
<![CDATA
[
    {
        "message":{
            "generalPurposeCard":{
                "content":{
                    "description":"视频测试",
                    "media":{
                        "height":"SHORT_HEIGHT",
                        "mediaContentType":"video/mpeg4",
                        "mediaUrl":"http://124.42.103.156:8089/group1/M00/00/35/CgrQc11aCb-AdvJyABu8bsQPdC0906.mp4",
                        "thumbnailContentType":"image/jpg",
                        "thumbnailUrl":"http://124.42.103.156:8089/group1/M00/00/35/CgrQc11aCdeARogcAAC8DrcIIFY575.jpg"
                    },
                    "suggestions":[],"title":"视频测试"
                },
                "layout":{
                    "cardOrientation":"VERTICAL",
                    "cardWidth":"MEDIUM_WIDTH"
                }
            }
        }
    }
]
]>
</bodyText>
```



#### 5.单卡片语音消息体

```properties
"<contentType>application/vnd.gsma.botmessage.v1.0+json</contentType>
<bodyText>
<![CDATA
[
    {
        ""message"":{
            ""generalPurposeCard"":{
                ""content"":{
                    ""description"":""春暖花开"",
                    ""media"":{
                        ""height"":""SHORT_HEIGHT"",
                        ""mediaContentType"":""audio/mp3"",
                        ""mediaUrl"":""http://124.42.103.156:8089/group1/M00/00/35/CgrQc11dFSaAIB3eACNpVzc5Ktg060.mp3"",
                        ""thumbnailContentType"":""image/jpg"",
                        ""thumbnailUrl"":""http://124.42.103.156:8089/group1/M00/00/3C/CgrQc12IPqqAfuiyAAAGW8W8kOM770.png""
                    },
                    ""suggestions"":[],
                    ""title"":""春暖花开""
                },
                ""layout"":{
                    ""cardOrientation"":""VERTICAL"",
                    ""cardWidth"":""MEDIUM_WIDTH""
                }
            }
        }
    }
]
]>
</bodyText>"
```



#### 6.单卡片带内置按钮不加悬浮按钮

```properties
"<contentType>application/vnd.gsma.botmessage.v1.0+json</contentType>
<bodyText>
<![CDATA
[
    {
        ""message"":{
            ""generalPurposeCard"":{
                ""content"":{
                    ""description"":""视频测试"",
                    ""media"":{
                        ""height"":""SHORT_HEIGHT"",
                        ""mediaContentType"":""video/mpeg4"",
                        ""mediaUrl"":""http://124.42.103.156:8089/group1/M00/00/35/CgrQc11aCb-AdvJyABu8bsQPdC0906.mp4"",
                        ""thumbnailContentType"":""image/jpg"",
                        ""thumbnailUrl"":""http://124.42.103.156:8089/group1/M00/00/35/CgrQc11aCdeARogcAAC8DrcIIFY575.jpg""
                    },
                    ""suggestions"":     //内置按钮
                    [
                        {
                            ""reply"":{
                                ""displayText"":""测试1"",
                                ""postback"":{
                                    ""data"":""测试1""
                                }
                            }
                        },{
                            ""reply"":{
                                ""displayText"":""测试2"",
                                ""postback"":{
                                    ""data"":""测试2""
                                }
                            }
                        },{
                            ""action"":{
                                ""displayText"":""ceshi"",
                                ""postback"":{
                                    ""data"":""2432""
                                },
                                ""urlAction"":{
                                    ""openUrl"":{
                                        ""url"":""http://baidu.com""
                                    }
                                }
                            }
                        }
                    ],
                    ""title"":""视频测试""
                },
                ""layout"":{
                    ""cardOrientation"":""VERTICAL"",
                    ""cardWidth"":""MEDIUM_WIDTH""}}}}]]></bodyText>"
```



#### 7.单卡片不带内置按钮加悬浮按钮消息体

```properties
"<contentType>multipart/mixed; boundary=""next""</contentType>
<bodyText>
<![CDATA
[
    --next
    Content-Type: application/vnd.gsma.botmessage.v1.0+json
    Content-Length: 472

    {
        ""message"":{
            ""generalPurposeCard"":{
                ""content"":{
                    ""description"":""春暖花开"",
                    ""media"":{
                        ""height"":""SHORT_HEIGHT"",
                        ""mediaContentType"":""audio/mp3"",
                        ""mediaUrl"":""http://124.42.103.156:8089/group1/M00/00/35/CgrQc11dFSaAIB3eACNpVzc5Ktg060.mp3"",
                        ""thumbnailContentType"":""image/jpg"",
                        ""thumbnailUrl"":""http://124.42.103.156:8089/group1/M00/00/3C/CgrQc12IPqqAfuiyAAAGW8W8kOM770.png""
                    },
                    ""suggestions"":[],
                    ""title"":""春暖花开""
                },
                ""layout"":{
                    ""cardOrientation"":""VERTICAL"",
                    ""cardWidth"":""MEDIUM_WIDTH""
                }}}}
    --next
    Content-Type: application/vnd.gsma.botsuggestion.v1.0+json
    Content-Length: 262

    {
        ""suggestions"":    //悬浮按钮
        [
            {
                ""reply"":{
                    ""displayText"":""测试1"",
                    ""postback"":{
                        ""data"":""测试1""
                    }
                }
            },{
                ""reply"":{
                    ""displayText"":""测试2"",
                    ""postback"":{
                        ""data"":""测试2""
                    }
                }
            },{
                ""action"":{
                    ""displayText"":""ceshi"",
                    ""postback"":{
                        ""data"":""2432""
                    },
                    ""urlAction"":{
                        ""openUrl"":{
                            ""url"":""http://baidu.com""
                        }
                    }
                }}]}
    --next--
]
]>
</bodyText>"
```



#### 8.单卡片带内置按钮加悬浮按钮消息体

```properties
"<contentType>multipart/mixed; boundary=""next""</contentType>
<bodyText><![CDATA[--next
Content-Type: application/vnd.gsma.botmessage.v1.0+json
Content-Length: 657

{
    ""message"":{
        ""generalPurposeCard"":{
            ""content"":{
                ""description"":""三生三世"",
                ""media"":{
                    ""height"":""SHORT_HEIGHT"",
                    ""mediaContentType"":""image/jpeg"",
                    ""mediaUrl"":""http://124.42.103.156:8089/group1/M00/00/17/CgrQc10ax2OAAkdaABXHLlcU5h8991.png"",
                    ""thumbnailContentType"":""image/png"",
                    ""thumbnailUrl"":""http://124.42.103.156:8089/group1/M00/00/17/CgrQc10ax2OAAkdaABXHLlcU5h8991_small.png""
                },
                ""suggestions"":
                [
                    {
                        ""reply"":{
                            ""displayText"":""测试1"",
                            ""postback"":{
                                ""data"":""测试1""
                            }
                        }
                    },{
                        ""action"":{
                            ""displayText"":""ceshi"",
                            ""postback"":{
                                ""data"":""2432""
                            },
                            ""urlAction"":{
                                ""openUrl"":{
                                    ""url"":""http://baidu.com""
                                }
                            }
                        }
                    }
                ],
                ""title"":""三生三世""},
                ""layout"":{""cardOrientation"":""VERTICAL"",""cardWidth"":""MEDIUM_WIDTH""}}}}
--next
Content-Type: application/vnd.gsma.botsuggestion.v1.0+json
Content-Length: 262

{
    ""suggestions"":
    [
        {
            ""reply"":{
                ""displayText"":""测试1"",
                ""postback"":{""data"":""测试1""}
            }
        },{
            ""reply"":{
                ""displayText"":""测试2"",
                ""postback"":{""data"":""测试2""}
            }
        },{
            ""action"":{
                ""displayText"":""ceshi"",
                ""postback"":{""data"":""2432""},
                ""urlAction"":{""openUrl"":{""url"":""http://baidu.com""}}}}]}
--next--]]>
</bodyText>"
```



