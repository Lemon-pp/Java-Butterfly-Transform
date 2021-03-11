# Dom4J操作XML

## 1.读取XML

xml文件内容：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<msg:outboundMessageRequest xmlns:msg="urn:oma:xml:rest:netapi:messaging:1">
    <address name="phone">tel:+8619585550103</address>
    <destinationAddress>tel:+8619585550103</destinationAddress>
    <senderAddress>sip:125200401111123@botplatform.rcs.chinamobile.com</senderAddress>
    <outboundIMMessage>
        <contentType>text/plain</contentType>
        <conversationID>1111</conversationID>
        <contributionID>1111</contributionID>
        <serviceCapability>
            <capabilityId>ChatbotSA </capabilityId>
            <version>+g.gsma.rcs.botversion=&quot;#=1&quot;</version>
        </serviceCapability>
        <bodyText>Hello</bodyText>
    </outboundIMMessage>
    <clientCorrelator>567895</clientCorrelator>
</msg:outboundMessageRequest>
```



### 读取xml文档

两种方式：

+ DOMReader
+ SAXReader

因为利用了相同的接口，所以调用方式一致。

```java
String path = ResourceUtils.getURL("classpath:basicInfo.xml").getPath();
path = java.net.URLDecoder.decode(path, "utf-8");
SAXReader saxReader = new SAXReader();
Document document = saxReader.read(new File(path));
```

读取指定的xml文件之后返回一个Document对象，这个对象代表了整个XML文档，用于各种Dom运算。执照XML文件头所定义的编码来转换。



### **获取xml根节点**

根节点是xml分析的开始，任何xml分析工作都需要从根开始

```java
Element rootElement = document.getRootElement();
```



### 获取根节点下的子节点

```java
//方式一
            List<Element> elements = rootElement.elements();
            for (Element element : elements) {
                System.out.println(element.getText());
            }

//方式二
            Iterator iterator = rootElement.elementIterator();
            while (iterator.hasNext()){
                Element next = (Element) iterator.next();
                System.out.println(next.getText());
            }
```



### 根据节点名获取根节点下的某个子节点

```java
Element senderAddress = rootElement.element("senderAddress");
```



### 获取节点的所有属性和根据名字获取某个属性

```java
//所有属性
List attributes = senderAddress.attributes();

//根据名字获取某个属性
Attribute name = senderAddress.attribute("name");
```



### 获取节点的值和属性的值

```java
//获取节点值
element.getText();

//获取属性值
attribute.getValue();
```



## 2.创建新的XML文档

```java
Document document1 = DocumentHelper.createDocument();
            //创建根节点
            Element root = document.addElement("root");
            //创建子节点
            Element childNode1 = root.addElement("childNode1")
                    .addAttribute("shuxing","123")
                    .addAttribute("name","456")
                    .addText("123456789");

```



## 3.将xml文档写入文件

```java
FileWriter out = new FileWriter("foo.xml");
document.write(out);
out.close();
```

如果您希望能够更改输出的格式（例如漂亮的打印或紧凑格式），或者希望能够将`Writer` 对象或`OutputStream`对象用作目标，则可以使用`XMLWriter`该类。

Dom4j通过XMLWriter将Document对象表示的XML树写入指定的文件，并使用OutputFormat格式对象指定写入的风格和编码方法。调用OutputFormat.createPrettyPrint()方法可以获得一个默认的pretty print风格的格式对象。对OutputFormat对象调用setEncoding()方法可以指定XML文件的编码方法。

```java
FileWriter fileWriter = new FileWriter("E:\\编程\\学习项目\\SpringBootDemo\\src\\main\\resources\\output2.xml");
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(fileWriter, format);
            writer.write(document1);
            writer.close();
```



## 4.字符串和XML的转换

```java
//document文档转为String
String s = document.asXML();

//String转为document文档
 Document document2 = DocumentHelper.parseText(s);
```



## 5.xml文档的增加、删除操作

```java
//增加节点
rootElement.addElement("node1");

//设置节点值
rootElement.addElement("node1").setText("111111");

//增加属性、设置属性值
rootElement.addElement("node1").addAttribute("nnnn","123");

//删除节点
rootElement.remove(rootElement.element("address"));

//删除属性
address.remove(address.attribute("name"));

```

