# Spring core framework custom implementation
Implementation contains basic core Spring features like: `BeanFactory`, `ApplicationContext`, `BeanPostProcessor`, `Enviroment`, `AOP support`, different annotations. 

### Motivation
To gain deeper understanding how Spring framework works under the hood and which problems solves.


## How to use the framework implementation

- clone this project
- In `build.gradle` there is a task called `fatJar`, running which jar file will be created and placed into `build/libs` folder with name `spring-core-1.0.0.jar`.
- create your own `gradle` project
- add to `gradle` `dependencies`
```
implementation files('C:\\space\\projects\\spring-builder\\build\\libs\\spring-core-1.0.0.jar')
```
- Add to `main` method
```java
ApplicationContext applicationContext = Application.run("org.example");  
BeanTwo bean = applicationContext.getBean(BeanTwo.class);  
bean.test();
```