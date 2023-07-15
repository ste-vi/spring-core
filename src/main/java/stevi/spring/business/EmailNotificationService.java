package stevi.spring.business;

import stevi.spring.anotations.Service;

@Service
public class EmailNotificationService {

    public void notifyUser(Long userId, String message) {
        System.out.printf("New notification is here for userId %s : %s%n", userId, message);
    }

}
