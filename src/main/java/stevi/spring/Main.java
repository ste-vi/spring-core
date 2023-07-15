package stevi.spring;

import stevi.spring.business.NotifierService;
import stevi.spring.context.Application;
import stevi.spring.context.ApplicationContext;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = Application.run("stevi.spring", Map.of());

        NotifierService notifierService = applicationContext.getObect(NotifierService.class);
        notifierService.sendExchangeNotification();
    }
}