package edu.cnm.deepdive.codebreaker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.codebreaker.BuildConfig;
import edu.cnm.deepdive.codebreaker.model.entity.Match;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CodebreakerWebService {

  @GET("users/me")
  Single<User> getProfile(@Header("Authorization") String bearerToken);

  //@POST("matches")
  //Single<Match> startMatch(@Header("Authorization") String bearerToken, @Body Match match);

  //@GET("matches/{matchId}")
  //Single<Match> getMatch(@Header("Authorization"))

  static CodebreakerWebService getInstance() {
    return InstanceHolder.INSTANCE;
  }


  class InstanceHolder {

    private static final CodebreakerWebService INSTANCE;

    static {

      // create a Gson object but ignore those fields without the expose annotation
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();

      // there are different levels of logging so you can specify what you want to get back
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      //setting how much data we want to get back depending on whether or not we're debugging.
      interceptor.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .build();

      INSTANCE = retrofit.create(CodebreakerWebService.class);
    }

  }
}
