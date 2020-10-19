# 5G消息URL

1.发送消息--群发

```java
HTTPS：POST
https://{serverRoot}/messaging/group/{apiVersion}/outbound/{chatbotURI}/requests

https://117.161.4.174/maap/messaging/group/v1/outbound/sip%3A12599%40botplatform.rcs.chinamobile.com/requests
```



2.发送消息（交互）

```java
HTTPS：POST
https://{serverRoot}/messaging/interaction/{apiVersion}/outbound/{chatbotURI}/requests
```



3.消息接收通知

```java
HTTPS：POST
URL：https://{notifyURL}/InboundMessageNotification/{chatbotURI}

https:///notifications/InboundMessageNotification/sip%3A12599%40botplatform.rcs.chinamobile.com
```



4.状态报告通知

```java
HTTPS：POST
URL：https://{notifyURL}/DeliveryInfoNotification/{chatbotURI}
```



5.发送撤回消息

```java
HTTPS：PUT
https://{serverRoot}/messaging/{apiVersion}/outbound/{chatbotURI}/requests/{messageId}/status

messageId	string	M	待撤回消息的ID。
```



6.消息撤回结果通知

```java
HTTPS：POST
URL：https://{notifyURL}/MessageStatusNotification/{chatbotURI}
```



7.媒体文件上传

```java
HTTPS POST
URL：https://{serverRoot}/Content
带文件指纹时：
https://{serverRoot}/Content?hash=sha256<sha256_value>&SIZE=<file size>
```



8.媒体审核通知

```java
HTTPS：POST
URL：https://{notifyURL}/InboundMessageNotification/{ChatbotURI}
```



9.媒体文件删去

```java
HTTPS DELETE
URL：https://{serverRoot}/Content
```



10.媒体文件下载

```java
GET <FILE_URI> HTTP/1.1
注：这里的URI为文件URI。
```

