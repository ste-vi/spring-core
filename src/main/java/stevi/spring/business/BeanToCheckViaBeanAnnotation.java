package stevi.spring.business;

public class BeanToCheckViaBeanAnnotation {

    private final String value;

    public BeanToCheckViaBeanAnnotation(String value) {
        this.value = value;
    }

    void someMethod() {
        System.out.println(value);
    }
}
