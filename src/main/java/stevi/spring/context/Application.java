package stevi.spring.context;

import stevi.spring.config.Config;
import stevi.spring.config.DefaultConfig;
import stevi.spring.factory.ObjectFactory;

import java.util.Map;

public class Application {

    public static ApplicationContext run(String packageToScan, Map<Class, Class> ifcToImplMap) {
        Config config = new DefaultConfig(packageToScan, ifcToImplMap);
        ApplicationContext applicationContext = new ApplicationContext(config);
        ObjectFactory objectFactory = new ObjectFactory(applicationContext);
        applicationContext.setObjectFactory(objectFactory);

        //todo: inti all not lazy singletons
        return applicationContext;
    }
}