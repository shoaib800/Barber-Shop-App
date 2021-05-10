package shoaib.dev.barbershopapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

import shoaib.dev.barbershopapp.Common.Common;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

public class MyFCMService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Common.updateToken(this, token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        //     dataSend.put("update_done", "true");
        if(remoteMessage.getData() != null)
        {
            if(remoteMessage.getData().get("update_done") != null){
                //updateLastBooking();
                Map<String,String> dataReceived = remoteMessage.getData();
                Paper.init(this);
                Paper.book().write(Common.RATING_INFORMATION_KEY, new Gson().toJson(dataReceived));

            }


            if(remoteMessage.getData().get(Common.TITLE_KEY) != null &&
                    remoteMessage.getData().get(Common.CONTENT_KEY) != null){

                Common.showNotification(this,
                        new Random().nextInt(),
                        remoteMessage.getData().get(Common.TITLE_KEY),
                        remoteMessage.getData().get(Common.CONTENT_KEY),
                        null);
            }
        }
    }
}
