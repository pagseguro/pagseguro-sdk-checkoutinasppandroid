package br.com.uol.pslibs.lojinhapagseguro.service;

import okhttp3.ResponseBody;
import retrofit2.Callback;


public interface DownloadService {

    void downloadFile(String url, Callback<ResponseBody> callback);
}
