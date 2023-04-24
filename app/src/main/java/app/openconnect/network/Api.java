package app.openconnect.network;


import app.openconnect.model.Data;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

//    @GET("rGRCzBcL?")
//    Call<Data> getServerData();

//    @GET("GJ9Uk4ru")
//    Call<Data> getServerData();

    @GET("test.json")
    Call<Data> getServerData();


}
