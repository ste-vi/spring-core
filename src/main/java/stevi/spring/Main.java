package stevi.spring;

import stevi.spring.business.NotifierService;
import stevi.spring.factory.ObjectFactory;

public class Main {

    public static void main(String[] args) {
        NotifierService notifierService = ObjectFactory.getInstance().createObject(NotifierService.class);
        notifierService.sendExchangeNotification();
    }
}