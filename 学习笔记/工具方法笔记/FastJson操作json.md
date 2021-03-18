# FastJson操作json

使用之前需导入fastjson依赖

```xml
		<dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.54</version>
        </dependency>
```



# 1.序列化

序列化就是指 把JavaBean对象转成JSON格式的字符串。

```java
toJSONString();
```

com.alibaba.fastjson.JSON提供了许多方法（多态）实现序列化。

![image-20210310162223447](https://i.loli.net/2021/03/10/7OzTbWht89XSVmv.png)

1.基本的序列化 

```
String objJson = JSON.toJSONString(Object object);
```



传入一个对象，将对象转成JSON字符串。

## 例1：将Map转成JSON

```java
Map<String, String> hashMap = new HashMap<>();
        hashMap.put("a","111");
        hashMap.put("b","222");
        String s = JSON.toJSONString(hashMap);
        System.out.println(s);
```

输出结果：

```json
{"a":"111","b":"222"}
```



## 例2：将List<Map>转成JSON。

```java
Map<String, String> hashMap = new HashMap<>();
        hashMap.put("a","111");
        hashMap.put("b","222");

        Map<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("c","333");
        hashMap2.put("d","444");

        List<Object> list = new ArrayList<>();
        list.add(hashMap);
        list.add(hashMap2);
        String s = JSON.toJSONString(list);
        System.out.println(s);
```

输出结果：

```json
[{"a":"111","b":"222"},{"c":"333","d":"444"}]
```

## 例3：自定义JavaBean User转成JSON。

```java
		MediaVO mediaVO = new MediaVO();
        mediaVO.setName("zhangsan");
        mediaVO.setAge("18");
        String medioStr = JSON.toJSONString(mediaVO);
```

输出结果：

```json
{"age":"18","name":"zhangsan"}
```

## 格式化输出 JSON 字符串

```java
String objJson = JSON.toJSONString(Object object, boolean prettyFormat);
```

传入一个对象和一个布尔类型（是否格式化），将对象转成格式化后的JSON字符串。

例4：以例2代码为例。

```java
String listJson = JSON.toJSONString(list, true);
```

输出结果为：

```json
[
	{
		"a":"111",
		"b":"222"
	},
	{
		"c":"333",
		"d":"444"
	}
]
```



FastJSON提供了许多特性支持。

```
String objJson = JSON.toJSONString(Object object, SerializerFeature... features) 
```

传入一个对象和SerializerFeature类型的可变变量。SerializerFeature是一个枚举。

com.alibaba.fastjson.serializer.SerializerFeature

![img](http://images.cnitblog.com/i/637684/201405/291336016976905.png)

![img](http://images.cnitblog.com/i/637684/201405/291336163067908.png)

你可以根据自己的情况使用这些特性。

简单说下几个常用的特性：

## 日期格式化：

FastJSON可以直接对日期类型格式化，在缺省的情况下，FastJSON会将Date转成long。

例5：FastJSON将java.util.Date转成long。

```java
String s = JSON.toJSONString(new Date());
        System.out.println(s);
```

输出结果：

```
1401370199040
```

例6：使用SerializerFeature特性格式化日期。

```
String s = JSON.toJSONString(new Date(),SerializerFeature.WriteDateUseDateFormat);
        System.out.println(s);
```

输出结果：

```
"2021-03-10 16:37:43"
```

也可以指定输出日期格式。

例7：指定输出日期格式。

```java
String s = JSON.toJSONStringWithDateFormat(new Date(), "yyyy-MM-dd hh:MM:ss");
        System.out.println(s);
```

输出结果：

```json
"2021-03-10 04:03:16"
```

## 使用单引号

例8：以例2为例。

```
String listJson = JSON.toJSONString(list, SerializerFeature.UseSingleQuotes);
```

输出结果：

```
[{'key1':'One','key2':'Two'},{'key3':'Three','key4':'Four'}]
```

3.JSON格式化。

例9：

```
String listJson = JSON.toJSONString(list, SerializerFeature.PrettyFormat);
```

输出结果：与例4结果一致。

## 输出Null字段

 缺省情况下FastJSON不输入为值Null的字段，可以使用SerializerFeature.WriteMapNullValue使其输出。

例10：

```java
1 Map<String, Object> map = new HashMap<String,Object>();
2         
3 String b = null;
4 Integer i = 1;
5         
6 map.put("a", b);
7 map.put("b", i);
8         
9 String listJson = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
```

输出结果：

```
{"a":null,"b":1}
```

## 序列化时写入类型信息

例11：

```java
MediaVO mediaVO = new MediaVO();
        mediaVO.setAge("18");
        mediaVO.setName("zhangsan");
        String s = JSON.toJSONString(mediaVO,SerializerFeature.WriteClassName);
```

输出结果：

```json
{"@type":"com.lemon.Bean.MediaVO","age":"18","name":"zhangsan"}
```

由于序列化带了类型信息，使得反序列化时能够自动进行类型识别。

例12：将例11反序列化。

```java
ParserConfig.getGlobalInstance().setAutoTypeSupport(true); //必须加上这句，否则报JSONException: autoType is not support. com.lemon.Bean.MediaVO，原因：序列化时将class信息写入，反解析的时候，fastjson默认情况下会开启autoType的检查，相当于一个白名单检查吧，如果序列化信息中的类路径不在autoType中，反解析就会报上面的com.alibaba.fastjson.JSONException: autoType is not support的异常。
        MediaVO parse = (MediaVO) JSON.parse(s);
        System.out.println(parse.getAge());
```

输出结果：

```
18
```

如果User序列化是没有加入类型信息（SerializerFeature.WriteClassName），按照例12的做法就会报错（java.lang.ClassCastException）。

 

# 2.反序列化

反序列化就是把JSON格式的字符串转化为Java Bean对象。

```java
	Object parse(String text)
    
    JSONObject parseObject(String text)
    
    JSONArray parseArray(String text)
```

com.alibaba.fastjson.JSON提供了许多方法（多态）实现反序列化。

![img](http://images.cnitblog.com/i/637684/201405/310118085564159.png)

 

 简单举几个例子。

指定Class信息反序列化。

## 将javaBean反序列化。

```java
 MediaVO mediaVO1 = JSON.parseObject(s, MediaVO.class);
        System.out.println(mediaVO1.toString());
```

输出结果：

```
MediaVO(name=zhangsan, age=18)
```

## 集合反序列化

例14：将例2反序列化。

```
1 List<Map> list1 = JSON.parseArray(listJson, Map.class);
2          
3 for(Map<String, Object> map : list1){
4     System.out.println(map.get("key1"));
5     System.out.println(map.get("key2"));         
6 }
```

输出结果：

```
1 One
2 Two
3 Three
4 Four
```

泛型的反序列化（使用TypeReference传入类型信息）。

例15：将例1反序列化。

```java
List<Map> maps = JSON.parseArray(s, Map.class);
        for (Map map : maps) {
            System.out.println(map);
        }
```

输出结果：

```
{a=111, b=222}
{c=333, d=444}
```

 

\---------------------------------------------------------------------------------------------------------------------------------------------------------

# 3.JSONObject和JSONArray

**JSONObject，JSONArray是JSON的两个子类。**

## JSONObject

相当于Map<String, Object>

方法：

```java
int size()
    boolean isEmpty()
    boolean containsKey(Object key)
    boolean containsValue(Object value)
    Object get(Object key)
    JSONObject getJSONObject(String key)
    void putAll(Map<? extends String, ? extends Object> m)
    void clear()
    Object remove(Object key)
    Set<String> keySet()
    
```



简单方法示例：

例16：将Map转成JSONObject，然后添加元素，输出。

```java
Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("a","111");
        hashMap.put("b","222");

        JSONObject jsonObject = new JSONObject(hashMap);
        jsonObject.put("c","3333");
        System.out.println(jsonObject);
```

输出结果：

```json
{"a":"111","b":"222","c":"3333"}
```

##  

## **JSONArray**

**相当于List<Object>。**

例17：将List对象转成JSONArray，然后输出。

```java
Map<String, String> hashMap = new HashMap<>();
        hashMap.put("a","111");
        hashMap.put("b","222");

        Map<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("c","333");
        hashMap2.put("d","444");

        List<Object> list = new ArrayList<>();
        list.add(hashMap);
        list.add(hashMap2);

        JSONArray array = JSONArray.parseArray(JSON.toJSONString(list));
        System.out.println(array);
```



输出结果：

```
[{"a":"111","b":"222"},{"c":"333","d":"444"}]
```

更多方法:

```java
int size()
     boolean isEmpty()
    boolean contains(Object o)
    Iterator<Object> iterator()
    Object[] toArray()
    boolean add(Object e)
    boolean remove(Object o)
    boolean containsAll(Collection<?> c)
    boolean addAll(Collection<? extends Object> c)
    void clear()
    Object set(int index, Object element) 
    void add(int index, Object element)
    int indexOf(Object o)
    int lastIndexOf(Object o)
    JSONObject getJSONObject(int index)
    JSONArray getJSONArray(int index)
```

