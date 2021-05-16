package com.shoaib.barbershopapp.Retrofit;

import android.database.Observable;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import com.shoaib.barbershopapp.Model.FCMResponse;
import com.shoaib.barbershopapp.Model.FCMSendData;

public interface IFCMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAQY1pRZw:APA91bENImwHGp36TPcewEZD4eAba-Qhwx2owAqeESWrw1hLJ0WsCnHZWptTTpuIo0E5L-tlsnMEGvlR21t6JxajQ6J6vSzkNuj9M9JTR4ARZGRss9Lw79QJLB1m5qfplPO7-qg_U2-n"

    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification (@Body FCMSendData body);
}
