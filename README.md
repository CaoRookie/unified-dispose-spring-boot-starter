# unified-dispose-spring-boot-starter

Springboot统一返回以及统一异常处理

1.使用步骤：
    
    1.直接在Maven中引入starter
        <dependency>
            <groupId>com.cebon.tool</groupId>
            <artifactId>unified-dispose-springboot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>
    2.在启动类上添加 @EnableGlobalDispose注解，表示开启全局异常，全局返回处理
    
    3.在1.0.2版本配置不需要加入全局返回的类和参数
        两种方式：
            1.直接在忽略类或者方法上加入 @IgnoreResponseAdvice注解
            2.在配置文件中spring.igore下配置
                //忽略包集合
                adviceFilterPackage
                //忽略类集合
                adviceFilterClass
                //忽略方法集合
                adviceFilterMethod
      在1.0.3版本默认只有配置文件中spring.igore下配置basePackageScan才会进行全局统一返回，没有配置的都不进行统一返回
2.注意：

    自定义返回参数的枚举需要实现BaseEnum接口

    统一返回的类是: ResponseData<T> 包含：code,message,data三个属性
