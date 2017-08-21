package br.com.uol.pslibs.lojinhapagseguro.api;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Configuration {

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl("")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getClient());

    public static Retrofit retrofit = builder.build();

    public static <S> S getApi(Class<S> service) {
        return retrofit.create(service);
    }

    private static okhttp3.OkHttpClient getClient(){
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS);

        return builder.build();
    }
}
