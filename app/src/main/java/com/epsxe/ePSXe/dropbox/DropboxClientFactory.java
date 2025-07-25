package com.epsxe.ePSXe.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;

/* loaded from: classes.dex */
public class DropboxClientFactory {
    private static DbxClientV2 sDbxClient;

    public static void init(String accessToken) {
        if (sDbxClient == null) {
            DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("epsxe").withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient())).build();
            sDbxClient = new DbxClientV2(requestConfig, accessToken);
        }
    }

    public static DbxClientV2 getClient() {
        if (sDbxClient == null) {
            return null;
        }
        return sDbxClient;
    }
}
