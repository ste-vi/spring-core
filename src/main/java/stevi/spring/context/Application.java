package stevi.spring.context;

import stevi.spring.config.Config;
import stevi.spring.config.DefaultConfig;
import stevi.spring.factory.ObjectFactory;

public class Application {

    public static ApplicationContext run(String packageToScan) {
        Config config = new DefaultConfig(packageToScan);
        ApplicationContext applicationContext = new ApplicationContext(config);
        ObjectFactory objectFactory = new ObjectFactory(applicationContext);
        applicationContext.setObjectFactory(objectFactory);

        //todo: inti all not lazy singletons
        return applicationContext;
    }
}
