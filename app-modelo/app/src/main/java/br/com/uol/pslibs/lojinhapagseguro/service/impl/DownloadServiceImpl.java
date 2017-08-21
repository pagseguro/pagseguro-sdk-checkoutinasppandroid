package br.com.uol.pslibs.lojinhapagseguro.service.impl;

import br.com.uol.pslibs.lojinhapagseguro.api.Configuration;
import br.com.uol.pslibs.lojinhapagseguro.api.DownloadApi;
import br.com.uol.pslibs.lojinhapagseguro.service.DownloadService;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class DownloadServiceImpl implements DownloadService {

    private DownloadApi downloadApi = Configuration.getApi(DownloadApi.class);

    @Override
    public void downloadFile(String url, Callback<ResponseBody> callback) {
        downloadApi.downloadFile(url).enqueue(callback);
    }
}
