package stevi.spring.context;

import stevi.spring.config.Config;
import stevi.spring.config.DefaultConfig;
import stevi.spring.factory.ObjectFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static ExecutorService applicationFixedExecutorService;

    public static ApplicationContext run(String packageToScan) {
        Config config = new DefaultConfig(packageToScan);
        AutowiredApplicationContext applicationContext = new AutowiredApplicationContext(config);
        ObjectFactory objectFactory = new ObjectFactory(applicationContext);
        applicationContext.setObjectFactory(objectFactory);
        applicationContext.postInit();

        applicationFixedExecutorService = Executors.newFixedThreadPool(10);

        return applicationContext;
    }
}
