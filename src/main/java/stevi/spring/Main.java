package stevi.spring;

public class Main {

    public static void main(String[] args) {
        NotifierService notifierService = new NotifierService();
        notifierService.sendExchangeNotification();
    }
}