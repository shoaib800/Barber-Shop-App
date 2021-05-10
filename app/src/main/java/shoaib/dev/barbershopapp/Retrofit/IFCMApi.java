package shoaib.dev.barbershopapp.Retrofit;

import android.database.Observable;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import shoaib.dev.barbershopapp.Model.FCMResponse;
import shoaib.dev.barbershopapp.Model.FCMSendData;

public interface IFCMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAl0AwVsA:APA91bHrx9-SGwU_pES02DMK4Vfre98wdL4esuJPQsLJoYQph85MC-XUDiDFhLManQjx39-PK51-j_voE03IQE-sdQTVhAzOm6ANqtur3_vvQn_-MkhQdLooj_jvRZHlFPlbTDveO4Ik"

    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification (@Body FCMSendData body);
}
