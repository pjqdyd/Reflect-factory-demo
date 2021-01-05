### SpringBoot利用反射加工厂模式减少代码中的If Else案例

#### 情景描述:
>你的同学是一个练习生(你干嘛~~哎呦喂~),他擅长唱🎤、跳💃、rap👅、篮球🏀，并且希望在学校或班级派对上表演一个才艺。
> 
>他想收集同学的意见，决定是在学校还是班级进行哪一项派对活动才艺表演😄。

#### 通常做法:
##### 1. 创建派对接口:
```
/**
 * 派对活动接口
 */
public interface PartyService {

   public String sing();       // 唱
   public String dance();      // 跳
   public String rap();        // rap
   public String basketball(); // 篮球

}
```

##### 2. 班级派对活动:
```
/**
 * 班级派对
 */
@Service("classPartyService")
public class ClassPartyServiceImpl implements PartyService {
    @Override
    public String sing() {
        System.out.println("在班里唱");
        return "在班里唱";
    }
    @Override
    public String dance() {
        System.out.println("在班里跳");
        return "在班里跳";
    }
    @Override
    public String rap() {
        System.out.println("在班里rap");
        return "在班里rap";
    }
    @Override
    public String basketball() {
        System.out.println("在班里打篮球");
        return "在班里打篮球";
    }
}
```

##### 3. 学校派对活动:
```
/**
 * 学校派对
 */
@Service("schoolPartyService")
public class SchoolPartyServiceImpl implements PartyService {
    @Override
    public String sing() {
        System.out.println("在学校唱");
        return "在学校唱";
    }
    @Override
    public String dance() {
        System.out.println("在学校跳");
        return "在学校跳";
    }
    @Override
    public String rap() {
        System.out.println("在学校rap");
        return "在学校rap";
    }
    @Override
    public String basketball() {
        System.out.println("在学校打篮球");
        return "在学校打篮球";
    }
}
```

##### 4. 派对Controller层：
```
/**
 * 派对Controller
 */
@RestController
@RequestMapping("/party")
public class PartyController {
    @Resource
    private PartyService classPartyService;

    @Resource
    private PartyService schoolPartyService;

    /**
     * 表示才艺地点: 班级Service
     * @param methodName  表示才艺方法: 唱、跳、rap、篮球
     */
    @GetMapping("/classPartyService/{methodName}")
    public String party(@PathVariable(value = "methodName") String methodName) {
        if(methodName == "sing"){
           return classPartyService.sing();
        }else if(methodName == "dance"){
           return classPartyService.dance();
        }else if(methodName == "rap"){
           return classPartyService.rap();
        }else if(methodName == "basketball"){
           return classPartyService.basketball();
        }
        return "没有该派对活动";
    }
    
    /**
     * 表示才艺地点: 学校Service
     * @param methodName  表示才艺方法: 唱、跳、rap、篮球
     */
    @GetMapping("/schoolPartyService/{methodName}")
    public String party(@PathVariable(value = "methodName") String methodName) {
        if(methodName == "sing"){
           return schoolPartyService.sing();
        }else if(methodName == "dance"){
           return schoolPartyService.dance();
        }else if(methodName == "rap"){
           return schoolPartyService.rap();
        }else if(methodName == "basketball"){
           return schoolPartyService.basketball();
        }
        return "没有该派对活动";
    }
}

```

#### 优化后做法：

##### 1. 添加工厂类：
工厂类只负责初始化和生产Service对象和方法(当然这里的Service对象是从Spring进货来的)，方便提供给调用者，满足单一职责原则
```
/**
 * 派对工厂
 */
@Component
public class PartyFactory implements CommandLineRunner {
    @Resource
    private ApplicationContext applicationContext;

    private static Map<String, PartyService> partyServiceMap = new HashMap<>(); // 存放所有partyService
    private static Map<String, Method> partyMethodMap = new HashMap<>();        // 存放所有partyService的方法

    public PartyService getService(String serviceName){ // 提供Service对象
        return partyServiceMap.get(serviceName);
    }

    public Method getServiceMethod(String serviceName, String methodName){ // 提供Service方法
        return partyMethodMap.get(serviceName + ":" + methodName);
    }

    /**
     * 初始方法，启动执行
     */
    @Override
    public void run(String... args) throws Exception {
        partyServiceMap = applicationContext.getBeansOfType(PartyService.class); // 获取所有实现PartyService接口的实现类
        partyServiceMap.forEach((key, value) -> {
            Arrays.stream(value.getClass().getDeclaredMethods()).forEach(method -> { // 反射获取所有的方法
                partyMethodMap.put(key + ":" + method.getName(), method); // 将所有的方法push到map中
            });
        });
        partyMethodMap.keySet().forEach(System.out::println); // 打印所有的方法
    }
}
```

##### 2. 修改Controller层：
这里的控制层也不需要依赖具体的某个Service对象，只从工厂中获取方法执行就行了，做到了解耦
```
/**
 * 派对Controller
 */
@RestController
@RequestMapping("/party")
public class PartyController {
    @Resource
    private PartyFactory partyFactory;

    /**
     * @param serviceName 表示才艺地点: 学校Service或班级Service
     * @param methodName  表示才艺方法: 唱、跳、rap、篮球
     */
    @GetMapping("/{serviceName}/{methodName}")
    public String party(
            @PathVariable( value = "serviceName") String serviceName,
            @PathVariable( value = "methodName") String methodName) {
        try {
            // 获取工厂中的对象和方法，并执行对象的方法
           return partyFactory.getServiceMethod(serviceName, methodName).invoke(partyFactory.getService(serviceName), null).toString();
        } catch (Exception e) {
            return "未找到方法，派对活动不存在!";
        }
    }
}
```

#### 总结：

1. 如果我们需要增加一个家庭派对活动比如HomePartyServiceImpl，那么只要实现PartyService就行了，不需要修改工厂和
   控制层的其他代码，满足了扩展性，符合开闭原则的设计。


2. 上述只是一个简单的例子，可以理解为把所有的方法都存入一个map集合中，然后根据key去调用方法,有点像表驱动设计。


3. 并不是所有的业务都需要抽象成设计模式，一般是业务比较稳定之后可以用设计模式优化代码
   或者是业务的变化比较有规律和通用。
   

4. 如果你的业务是不按套路出牌，甲方和需求也反复横跳，那在现在敏捷开发的模式下，还真不如If Else来的快😂。
   要不然你精心抽象的代码，上的设计模式，突然需求业务变更，那就白干了😥，
   
   
5. 在设计中，代码的可读性和可维护性也是非常重要的。
   