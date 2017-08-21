package br.com.uol.pslibs.lojinhapagseguro.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DownloadApi {

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
