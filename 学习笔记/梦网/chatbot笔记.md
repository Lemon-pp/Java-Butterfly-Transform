# chatbot笔记

文件上传：

```Java
添加classpath获取的是编译后class下的路径
String s = FileUtils.readFileToString(ResourceUtils.getFile("classpath:cardsAndReply.txt"), "utf-8");
不添加classpath获取的是打成jar下的路径
String s = FileUtils.readFileToString(ResourceUtils.getFile("cardsAndReply.txt"), "utf-8");

```

