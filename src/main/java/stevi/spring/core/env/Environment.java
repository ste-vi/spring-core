package stevi.spring.core.env;

public interface Environment {

    String getActiveProfile();

    String getProperty(String key);
}
