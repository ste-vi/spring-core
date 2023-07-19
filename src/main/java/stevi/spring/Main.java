package stevi.spring;

import stevi.spring.business.NotifierService;
import stevi.spring.core.context.Application;
import stevi.spring.core.context.ApplicationContext;

//todo: remove when framework is done
public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = Application.run("stevi.spring");

        NotifierService notifierService = applicationContext.getBean(NotifierService.class);
        notifierService.sendExchangeNotification();
    }
}