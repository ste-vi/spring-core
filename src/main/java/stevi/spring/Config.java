package stevi.spring;

public interface Config {

    <T> Class<? extends T> getImplementation(Class<T> interfaceType);
}
