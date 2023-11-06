package com.hideandseekapps.Notesolves;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            // Handle the notification data here
            String messageTitle = remoteMessage.getNotification().getTitle();
            String messageBody = remoteMessage.getNotification().getBody();
            // You can customize the notification handling here
            showNotification(messageTitle, messageBody, webPage.class);

        }
    }

    private boolean isUserSignedIn() {
        // Implement your sign-in check logic here
        // You can use Firebase Authentication or your own custom logic
        // Return true if the user is signed in, otherwise return false
        return false; // Change this to your actual sign-in check
    }

    private void showNotification(String title, String body, Class<?> targetActivityClass) {
        NotificationHandler notificationHandler = new NotificationHandler(this);
        notificationHandler.showNotification(title, body, targetActivityClass);
    }


}
