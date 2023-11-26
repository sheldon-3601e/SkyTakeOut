# 苍穹外卖

## 软件开发整体介绍

### 软件开发流程

<img src="https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311082303723.png" alt="image-20231108230310625" style="zoom:50%;" />

### 角色分工

<img src="https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311082303916.png" alt="image-20231108230349857" style="zoom:50%;" />

### 软件环境

![image-20231108230414644](https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311082304688.png)

### 前后端分离开发流程

<img src="C:\Users\26483\AppData\Roaming\Typora\typora-user-images\image-20231108230935459.png" alt="image-20231108230935459" style="zoom:50%;" />

产品原型：用于展示项目的业务功能——项目经理

技术选型：展示项目中使用到的技术框架和中间件等——架构师

<img src="https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311082306263.png" alt="image-20231108230606223" style="zoom:67%;" />

## 项目介绍

### 模块解读

| **序号** | **名称**     | **说明**                                                     |
| -------- | ------------ | ------------------------------------------------------------ |
| 1        | sky-take-out | maven父工程，统一管理依赖版本，聚合其他子模块                |
| 2        | sky-common   | 子模块，存放公共类，例如：工具类、常量类、异常类等           |
| 3        | sky-pojo     | 子模块，存放实体类、VO、DTO等                                |
| 4        | sky-server   | 子模块，后端服务，存放配置文件、Controller、Service、Mapper等 |

#### 1、common模块

| 名称        | 说明                           |
| ----------- | ------------------------------ |
| constant    | 存放相关常量类                 |
| context     | 存放上下文类                   |
| enumeration | 项目的枚举类存储               |
| exception   | 存放自定义异常类               |
| json        | 处理json转换的类               |
| properties  | 存放SpringBoot相关的配置属性类 |
| result      | 返回结果类的封装               |
| utils       | 常用工具类                     |

#### 2、POJO模块

| **名称** | **说明**                                     |
| -------- | -------------------------------------------- |
| Entity   | 实体，通常和数据库中的表对应                 |
| DTO      | 数据传输对象，通常用于程序中各层之间传递数据 |
| VO       | 视图对象，为前端展示数据提供的对象           |
| POJO     | 普通Java对象，只有属性和对应的getter和setter |

#### 3、service模块

| config         | 存放配置类       |
| -------------- | ---------------- |
| controller     | 存放controller类 |
| interceptor    | 存放拦截器类     |
| mapper         | 存放mapper接口   |
| service        | 存放service类    |
| SkyApplication | 启动类           |

#### 数据库

| **序号** | **表名**      | **中文名**     |
| -------- | ------------- | -------------- |
| 1        | employee      | 员工表         |
| 2        | category      | 分类表         |
| 3        | dish          | 菜品表         |
| 4        | dish_flavor   | 菜品口味表     |
| 5        | setmeal       | 套餐表         |
| 6        | setmeal_dish  | 套餐菜品关系表 |
| 7        | user          | 用户表         |
| 8        | address_book  | 地址表         |
| 9        | shopping_cart | 购物车表       |
| 10       | orders        | 订单表         |
| 11       | order_detail  | 订单明细表     |

### 技术栈

#### 1、Nginx反向代理

<img src="https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311082307352.png" alt="image-20231108230748307" style="zoom: 50%;" />

<img src="https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311082308534.png" alt="image-20231108230813499" style="zoom:67%;" />



#### 2、Swagger

Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务(<https://swagger.io/>)。 它的主要作用是：

1. 使得前后端分离开发更加方便，有利于团队协作

2. 接口的文档在线自动生成，降低后端开发人员编写接口文档的负担

3. 功能测试 

   Spring已经将Swagger纳入自身的标准，建立了Spring-swagger项目，现在叫Springfox。通过在项目中引入Springfox ，即可非常简单快捷的使用Swagger。

knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,前身是swagger-bootstrap-ui,取名kni4j是希望它能像一把匕首一样小巧,轻量,并且功能强悍!

目前，一般都使用knife4j框架

#### 3、jwt验证拦截器

在第一次登陆时，根据用户ID生成Jwt令牌，并且返回

```java
/**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        //.........

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //............

        return Result.success(employeeLoginVO);
    }
```

在后续的操作中，获取请求头中携带的token进行验证

```java
 	/**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌 jwtProperties.getAdminTokenName()获取为token
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id：", empId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
```

#### 4、全局异常处理器

```java
	/**
     * 处理SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
```

#### 5、ThreadLocal

ThreadLocal 并不是一个Thread，而是Thread的局部变量。
ThreadLocal为每个线程提供单独一份存储空间，具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问。

**常用方法：**

- public void set(T value) 	设置当前线程的线程局部变量的值
- public T get() 		返回当前线程所对应的线程局部变量的值
- public void remove()        移除当前线程的线程局部变量

![image-20231126172313934](https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311261723088.png)

```java
package com.sky.context;

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
```

#### 6、pageHelper

在后期的sql查询中，pageHelper会动态的拼接`limit`语句

```java
//开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);//后续定义

        long total = page.getTotal();
        List<Employee> records = page.getResult();
```

#### 7、SpringMVC的消息转化器

```java
	/**
     * 扩展Spring MVC框架的消息转化器
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自己的消息转化器加入容器中
        converters.add(0,converter);
    }
```

#### 8、公共字段填充（AOP

**技术点：**枚举、注解、AOP、反射

**实现步骤：**

1). 自定义注解 AutoFill，用于标识需要进行公共字段自动填充的方法

2). 自定义切面类 AutoFillAspect，统一拦截加入了 AutoFill 注解的方法，通过反射为公共字段赋值

3). 在 Mapper 的方法上加入 AutoFill 注解

- 自定义枚举类，定义注解的值

  ```java
  public enum OperationType {
  
      /**
       * 更新操作
       */
      UPDATE,
      /**
       * 插入操作
       */
      INSERT
  
  }
  ```

- 自定义注解 AutoFill

  ```java
  /**
   * @ClassName AutoFill
   * @Author 26483
   * @Date 2023/11/10 16:23
   * @Version 1.0
   * @Description 自定义注解，表示公共字段填充
   */
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface AutoFill {
      //指定数据库操作类型 INSERT UPDATE
      OperationType value();
  }
  ```
  
-  自定义切面类 AutoFillAspect
  
  ```java
  @Aspect
  @Component
  @Slf4j
  public class AutoFillAspect {
  
      /**
       * 切入点
       */
      @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
      public void autoFillPointCut(){}
  
      /**
       * 前置通知，进行公共字段自动填充
       */
      @Before("autoFillPointCut()")
      public void autoFill(JoinPoint joinPoint){
          log.info("开始进行公共字段自动填充...");
  
          //获取到当前被拦截的方法上的数据库操作类型
          MethodSignature signature = (MethodSignature) joinPoint.getSignature();
          AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
          OperationType value = annotation.value();
  
          //获取到当前拦截方法的参数——实体对象
          Object[] args = joinPoint.getArgs();
          if (args == null || args.length == 0){
              return;
          }
          Object entity = args[0];
  
          //准备赋值的参数
          LocalDateTime localDateTime = LocalDateTime.now();
          Long currentId = BaseContext.getCurrentId();
  
          //根据不同的操作类型，为对应的字段赋值
          if (value == OperationType.INSERT){
              //需要为四个属性赋值
              try {
                  Method create_time = entity.getClass()
                      .getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                  Method create_user = entity.getClass()
                      .getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                  Method update_time = entity.getClass()
                      .getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                  Method update_user = entity.getClass()
                      .getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
  
                  create_time.invoke(entity,localDateTime);
                  create_user.invoke(entity,currentId);
                  update_time.invoke(entity,localDateTime);
                  update_user.invoke(entity,currentId);
  
              } catch (Exception e) {
                  throw new RuntimeException(e);
              }
          } else if (value == OperationType.UPDATE) {
              //需要为两个属性赋值
              try {
                  Method update_time = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                  Method update_user = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
  
                  update_time.invoke(entity,localDateTime);
                  update_user.invoke(entity,currentId);
  
              } catch (Exception e) {
                  throw new RuntimeException(e);
              }
          }
  
      }
  
  }
  ```
  
#### 9、使用阿里OSS实现文件上传存储

- 配置OSS相关配置

  ```java
  sky:
    alioss:
      endpoint: ${sky.alioss.endpoint}
      access-key-id: ${sky.alioss.access-key-id}
      access-key-secret: ${sky.alioss.access-key-secret}
      bucket-name: ${sky.alioss.bucket-name}
  ```

  

- 读取OSS配置

  ```java
  @Component
  @ConfigurationProperties(prefix = "sky.alioss")
  @Data
  public class AliOssProperties {
  
      private String endpoint;
      private String accessKeyId;
      private String accessKeySecret;
      private String bucketName;
  
  }
  ```

  

- 创建OSS配置类

  ```java
  /**
   * 配置类，用于创建AliOssUtil对象
   */
  @Configuration
  @Slf4j
  public class OssConfiguration {
  
      @Bean
      @ConditionalOnMissingBean
      public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
          log.info("开始创建阿里云文件上传工具类对象：{}",aliOssProperties);
          return new AliOssUtil(aliOssProperties.getEndpoint(),
                  aliOssProperties.getAccessKeyId(),
                  aliOssProperties.getAccessKeySecret(),
                  aliOssProperties.getBucketName());
      }
  }
  ```

  

- 创建OSS工具类

  ```java
  @Data
  @AllArgsConstructor
  @Slf4j
  public class AliOssUtil {
  
      private String endpoint;
      private String accessKeyId;
      private String accessKeySecret;
      private String bucketName;
  
      /**
       * 文件上传
       *
       * @param bytes
       * @param objectName
       * @return
       */
      public String upload(byte[] bytes, String objectName) {
  
          // 创建OSSClient实例。
          OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
  
          try {
              // 创建PutObject请求。
              ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
          } catch (OSSException oe) {
              System.out.println("Caught an OSSException, which means your request made it to OSS, "
                      + "but was rejected with an error response for some reason.");
              System.out.println("Error Message:" + oe.getErrorMessage());
              System.out.println("Error Code:" + oe.getErrorCode());
              System.out.println("Request ID:" + oe.getRequestId());
              System.out.println("Host ID:" + oe.getHostId());
          } catch (ClientException ce) {
              System.out.println("Caught an ClientException, which means the client encountered "
                      + "a serious internal problem while trying to communicate with OSS, "
                      + "such as not being able to access the network.");
              System.out.println("Error Message:" + ce.getMessage());
          } finally {
              if (ossClient != null) {
                  ossClient.shutdown();
              }
          }
  
          //文件访问路径规则 https://BucketName.Endpoint/ObjectName
          StringBuilder stringBu
              
              
              ilder = new StringBuilder("https://");
          stringBuilder
                  .append(bucketName)
                  .append(".")
                  .append(endpoint)
                  .append("/")
                  .append(objectName);
  
          log.info("文件上传到:{}", stringBuilder.toString());
  
          return stringBuilder.toString();
      }
  }
  ```

  

- 上传文件

  ```java
  /**
       * 文件上传
       * @param file
       * @return
       */
      @PostMapping("/upload")
      @ApiOperation("文件上传")
      public Result<String> upload(MultipartFile file){
          log.info("文件上传：{}",file);
  
          try {
              //原始文件名
              String originalFilename = file.getOriginalFilename();
              //截取原始文件名的后缀   dfdfdf.png
              String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
              //构造新文件名称
              String objectName = UUID.randomUUID().toString() + extension;
  
              //文件的请求路径
              String filePath = aliOssUtil.upload(file.getBytes(), objectName);
              return Result.success(filePath);
          } catch (IOException e) {
              log.error("文件上传失败：{}", e);
          }
  
          return Result.error(MessageConstant.UPLOAD_FAILED);
      }
  }
  ```

#### 10、Redis

- 配置数据源

  ```yaml
  sky:
    redis:
      host: localhost
      port: 6379
      password: 123456
      database: 10
  ```

  

- 创建配置类

  ```java
  @Configuration
  @Slf4j
  public class RedisConfiguration {
  
      @Bean
      public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
          log.info("开始创建redis模板对象...");
          RedisTemplate redisTemplate = new RedisTemplate();
          //设置redis的连接工厂对象
          redisTemplate.setConnectionFactory(redisConnectionFactory);
          //设置redis key的序列化器
          redisTemplate.setKeySerializer(new StringRedisSerializer());
          return redisTemplate;
      }
  }
  ```

  

#### 11、HttpClient

1. 创建HttpClient对象
2. 创建请求对象
3. 发送请求，接受响应结果
4. 解析结果
5. 关闭资源

**工具类**

```java
package com.sky.utils;

/**
 * Http工具类
 */
public class HttpClientUtil {

    static final  int TIMEOUT_MSEC = 5 * 1000;

    /**
     * 发送GET方式请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String doGet(String url,Map<String,String> paramMap){
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String result = "";
        CloseableHttpResponse response = null;

        try{
            URIBuilder builder = new URIBuilder(url);
            if(paramMap != null){
                for (String key : paramMap.keySet()) {
                    builder.addParameter(key,paramMap.get(key));
                }
            }
            URI uri = builder.build();

            //创建GET请求
            HttpGet httpGet = new HttpGet(uri);

            //发送请求
            response = httpClient.execute(httpGet);

            //判断响应状态
            if(response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 发送POST方式请求
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> paramMap) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 创建参数列表
            if (paramMap != null) {
                List<NameValuePair> paramList = new ArrayList();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }

            httpPost.setConfig(builderRequestConfig());

            // 执行http请求
            response = httpClient.execute(httpPost);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }

    /**
     * 发送POST方式请求
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String doPost4Json(String url, Map<String, String> paramMap) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            if (paramMap != null) {
                //构造json格式数据
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    jsonObject.put(param.getKey(),param.getValue());
                }
                StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
                //设置请求编码
                entity.setContentEncoding("utf-8");
                //设置数据类型
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }

            httpPost.setConfig(builderRequestConfig());

            // 执行http请求
            response = httpClient.execute(httpPost);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }
    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
            .setConnectTimeout(TIMEOUT_MSEC)
            .setConnectionRequestTimeout(TIMEOUT_MSEC)
            .setSocketTimeout(TIMEOUT_MSEC).build();
    }

}

```

#### 12、Spring Cache使用Redis缓存商品

![image-20231126192946359](https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311261929418.png)

在SpringCache中提供了很多缓存操作的注解，常见的是以下的几个：

| **注解**       | **说明**                                                     |
| -------------- | ------------------------------------------------------------ |
| @EnableCaching | 开启缓存注解功能，通常加在启动类上                           |
| @Cacheable     | 在方法执行前先查询缓存中是否有数据，如果有数据，则直接返回缓存数据；如果没有缓存数据，调用方法并将方法返回值放到缓存中 |
| @CachePut      | 将方法的返回值放到缓存中                                     |
| @CacheEvict    | 将一条或多条数据从缓存中删除                                 |

`@CachePut(value = "userCache", key = "#user.id")//key的生成：userCache::1`

**说明：**key的写法如下

- user.id : #user指的是方法形参的名称, id指的是user的id属性 , 也就是使用user的id属性作为key ;

+ result.id : #result代表方法返回值，该表达式 代表以返回对象的id属性作为key ；

- p0.id：#p0指的是方法中的第一个参数，id指的是第一个参数的id属性,也就是使用第一个参数的id属性作为key ;

- a0.id：#a0指的是方法中的第一个参数，id指的是第一个参数的id属性,也就是使用第一个参数的id属性作为key ;

- root.args[0].id:#root.args[0]指的是方法中的第一个参数，id指的是第一个参数的id属性,也就是使用第一个参数的id属性作为key ;

#### 13、Spring Task定时任务

将会使用cron表达式

在线生成器：https://cron.qqe2.com/

1). 导入maven坐标 spring-context（已存在）

<img src="https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311261947067.png" alt="image-20221218193251182" style="zoom:50%;" /> 

2). 启动类添加注解 @EnableScheduling 开启任务调度

3). 自定义定时任务类

```java
/**
 * 自定义定时任务类
 */
@Component
@Slf4j
public class MyTask {

    /**
     * 定时任务 每隔5秒触发一次
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void executeTask(){
        log.info("定时任务开始执行：{}",new Date());
    }
}
```

#### 14、WebSocket

- 导入WebSocket的maven坐标

- 导入WebSocket服务端组件WebSocketServer，用于和客户端通信

  ```java
  /**
   * WebSocket服务
   */
  @Component
  @ServerEndpoint("/ws/{sid}")
  public class WebSocketServer {
  
      //存放会话对象
      private static Map<String, Session> sessionMap = new HashMap();
  
      /**
       * 连接建立成功调用的方法
       */
      @OnOpen
      public void onOpen(Session session, @PathParam("sid") String sid) {
          System.out.println("客户端：" + sid + "建立连接");
          sessionMap.put(sid, session);
      }
  
      /**
       * 收到客户端消息后调用的方法
       * @param message 客户端发送过来的消息
       */
      @OnMessage
      public void onMessage(String message, @PathParam("sid") String sid) {
          System.out.println("收到来自客户端：" + sid + "的信息:" + message);
      }
  
      /**
       * 连接关闭调用的方法
       * @param sid
       */
      @OnClose
      public void onClose(@PathParam("sid") String sid) {
          System.out.println("连接断开:" + sid);
          sessionMap.remove(sid);
      }
  
      /**
       * 群发
       * @param message
       */
      public void sendToAllClient(String message) {
          Collection<Session> sessions = sessionMap.values();
          for (Session session : sessions) {
              try {
                  //服务器向客户端发送消息
                  session.getBasicRemote().sendText(message);
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      }
  }
  ```

  

-  导入配置类WebSocketConfiguration，注册WebSocket的服务端组件

  ```java
  /**
   * WebSocket配置类，用于注册WebSocket的Bean
   */
  @Configuration
  public class WebSocketConfiguration {
  
      @Bean
      public ServerEndpointExporter serverEndpointExporter() {
          return new ServerEndpointExporter();
      }
  
  }
  ```

  

-  导入定时任务类WebSocketTask，定时向客户端推送数据

  ```java
  @Component
  public class WebSocketTask {
      @Autowired
      private WebSocketServer webSocketServer;
  
      /**
       * 通过WebSocket每隔5秒向客户端发送消息
       */
      @Scheduled(cron = "0/5 * * * * ?")
      public void sendMessageToClient() {
          webSocketServer.sendToAllClient("这是来自服务端的消息：" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
      }
  }
  ```



#### 15、Apache ECharts

Apache ECharts 是一款基于 Javascript 的数据可视化图表库，提供直观，生动，可交互，可个性化定制的数据可视化图表。
官网地址：https://echarts.apache.org/zh/index.html

#### 16、Apache POI

Apache POI 是一个处理Miscrosoft Office各种文件格式的开源项目。简单来说就是，我们可以使用 POI 在 Java 程序中对Miscrosoft Office各种文件进行读写操作。
一般情况下，POI 都是用于操作 Excel 文件。

## 微信小程序

**js文件：**必须存在，存放页面业务逻辑代码，编写的js代码。

**wxml文件：**必须存在，存放页面结构，主要是做页面布局，页面效果展示的，类似于HTML页面。

**json文件：**非必须，存放页面相关的配置。

**wxss文件：**非必须，存放页面样式表，相当于CSS文件。

#### 1、微信登录流程

<img src="https://gitee.com/sheldon_kkk/typora-image/raw/master/img/202311261927325.png" alt="image-20231126192723236" style="zoom:50%;" />

1. 小程序端，调用wx.login()获取code，就是授权码。
2. 小程序端，调用wx.request()发送请求并携带code，请求开发者服务器(自己编写的后端服务)。
3. 开发者服务端，通过HttpClient向微信接口服务发送请求，并携带appId+appsecret+code三个参数。
4. 开发者服务端，接收微信接口服务返回的数据，session_key+opendId等。opendId是微信用户的唯一标识。
5. 开发者服务端，自定义登录态，生成令牌(token)和openid等数据返回给小程序端，方便后绪请求身份校验。
6. 小程序端，收到自定义登录态，存储storage。
7. 小程序端，后绪通过wx.request()发起业务请求时，携带token。
8. 开发者服务端，收到请求后，通过携带的token，解析当前登录用户的id。
9. 开发者服务端，身份校验通过后，继续相关的业务逻辑处理，最终返回业务数据。

#### 2、微信支付

https://pay.weixin.qq.com/static/product/product_index.shtml





