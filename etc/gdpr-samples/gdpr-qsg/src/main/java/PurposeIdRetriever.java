/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * The class which outputs the purpose Id for 'Pickup promotion'.
 *
 * TODO : Pass the needed purpose to the program rather than hard coding.
 *
 */
public class PurposeIdRetriever {

    public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException {

        // This stdout intentionally added to be read by the script
        System.out.println(getPurposeId());
    }

    public static int getPurposeId() throws IOException, NoSuchAlgorithmException, KeyManagementException {

        JSONObject json;
        int purposeId = 0;

        HttpUriRequest request = RequestBuilder
                .get()
                .setUri("https://localhost:9443/api/identity/consent-mgt/v1.0/consents/purposes?limit=10&offset=0")
                .addHeader("Accept","application/json")
                .addHeader("Authorization","Basic YWRtaW46YWRtaW4=")
                .addHeader("Content-Type","application/json")
                .build();

        CloseableHttpClient httpClient = Util.newClient();
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(request);
        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8);

        JSONArray jsonArray = new JSONArray(responseString);
        for (int i = 0; i < jsonArray.length(); i++) {
            json = jsonArray.getJSONObject(i);
            if ((json.getString("purpose")).equalsIgnoreCase("Pickup promotion")) {
                purposeId = json.getInt("purposeId");
            }
        }

        return purposeId;
    }
}
