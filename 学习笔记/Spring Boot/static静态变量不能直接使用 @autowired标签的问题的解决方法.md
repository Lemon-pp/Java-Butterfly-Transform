# static静态变量不能直接使用 @autowired标签的问题的解决方法.md

## 1.原因：

```bash
被static修饰变量，是部署于任何实例化的对象拥有，spring的依赖注入只能在对象层级上进行依赖注入，所以不能直接使用@autowired标签进行注入。
```

## 2.解决方法

方法一：使用set方法注入（这个没成功，注入获得的为空）

```java
	private static Confsing confsing;

    @Autowired
    public static void setConfsing(){
        MainHelper.confsing = confsing;
    }
```

方法二：把static类和需要加载的类对调 （OK）

```java
	需要在当前类上面加入注解@Component 使当前类成为一个bean对象。
	@Autowired
    private Confsing confsing;
    private static Confsing confsings;

    @PostConstruct
    public void inits() {
        confsings = confsing;
        //这里需要把static类 和需要加载的类对调，就可以使用static加载readApplicationUntil对应的类方法了
    }
```

方法三：新建一个类，通过私有静态类获取普通类的对象（🆗）

```java
public class Conf {
    private static Conf conf;

    @Autowired
    private Confsing confsing;

    public static Conf getInstance(){
        return conf;
    }

    @PostConstruct
    public void init(){
        conf = this;
        conf.confsing = this.confsing;
    }
}
```

