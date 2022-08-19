# seckill
### 项目描述
本系统是基于 SpringBoot 开发的高并发限时秒杀系统，实现了多种注册和登录方式，还包括查看商品列表、秒杀、下单等功
能，项目中针对高并发情况实现了系统缓存、异步和限流等技术。

### 项目结构
``` lua
seckill
├── seckill-common -- 通用模块实现
|          ├── entity -- 实体类
|          ├── execption -- 全局异常处理器。
|          ├── result -- 返回给前端的model封装，包括状态码等信息。
|          ├── utils -- 通用工具包。包括数据库连接、http工具包等。
|          └── vo -- view model，前端表单提交数据的封装
|
├── seckill-service -- 服务端实现
|          ├── mapper -- 数据持久层的工作。
|          ├── rabbitmq -- 消息队列相关操作，包括发送消息，接收消息。
|          ├── redis -- 对redis客户端操作的封装。
|          ├── registry -- 服务注册功能的封装
|          └── service -- 业务逻辑的处理
|
└── seckill-web -- web端实现
      |     ├── config -- webconfig的相关配置。
      |     ├── com.llp.kill.controller -- controller层。给前端返回数据以及接收前端的数据。
      |     └── MainApplication -- 服务器启动类。
      |
      |
      └──────── static -- 前端静态资源
            ├── templates -- 前端html页面
            └── application.properties 配置文件
      
```


### 已实现的功能
- [x] 用户注册登录模块整合了手机号码短信验证服务，以及实现了双重 MD5 密码校验的密码登录；
- [x] 利用 Redis 实现了商品信息的分布式缓存，包括页面缓存和对象缓存，减少对数据库的访问，提高业务吞吐量；
- [x] 通过本地内存标记、库存预热、异步下单等方式优化秒杀服务，抵挡秒杀的高并发流量。
- [x] 通过加密秒杀链接、数字公式验证码、接口限流防刷等方式，维护秒杀接口的安全，防止恶意攻击；
- [x] JSR303自定义校验器，实现对用户账号、密码的验证，使得验证逻辑从业务代码中脱离出来；
- [x] 全局异常统一处理。
  
  

### 运行项目
1. 在数据库中执行sql目录下的脚本，建表，插入数据。
2. 启动项目所需要的redis、rabbitMQ中间件。
3. 在配置文件中配置中间件地址、sms的相关参数等。
4. 运行MainApplication。
5. 访问localhost:8888/register/to_register 注册账号
6. 然后跳转到登录页面进行登录，即可跳转的秒杀商品列表。
