package http

/*
 * Copyright (c) 2009-2018. Authors: see NOTICE file.
 *
 * Licensed under the GNU Lesser General Public License, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.io.IOUtils
import org.apache.http.HttpEntity
import org.apache.http.HttpHost
import org.apache.http.HttpResponse
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.AuthCache
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
//import org.apache.http.client.protocol.ClientContext
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.entity.ContentProducer
import org.apache.http.entity.EntityTemplate
import org.apache.http.entity.InputStreamEntity
import org.apache.http.impl.auth.BasicScheme
import org.apache.http.impl.client.BasicAuthCache
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.impl.client.HttpClientBuilder;
//TODO: Migration https://mkyong.com/java/the-type-defaulthttpclient-is-deprecated/
/**
 * Created by IntelliJ IDEA.
 * User: lrollus
 * Date: 11/02/11
 * Time: 8:18
 * Http client used in test
 */
class HttpClient {

//    DefaultHttpClient client
    HttpClient client

    HttpHost targetHost
    BasicHttpContext localcontext
    URL URL
    HttpResponse response
    int timeout = 2500;


    /**
     * Create a connection to a specific URL
     * @param url Url
     * @param username Login
     * @param password Password
     */
    void connect(String url, String username, String password) {
        URL = new URL(url)
        targetHost = new HttpHost(URL.getHost(), URL.getPort());
        client = HttpClientBuilder.create().build();
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        localcontext = new BasicHttpContext();
//        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
        localcontext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
        // Set credentials
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
        client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
    }

    /**
     * Do get action
     * Response is saved and can be retrieved with getResponseCode()/getResponseData()
     */
    void get() {
        HttpGet httpGet = new HttpGet(URL.toString());
        response = client.execute(targetHost, httpGet, localcontext);
    }

    /**
     * Do get action and get data as byte array
     * Response is saved and can be retrieved with getResponseCode()/getResponseData()
     * @return Data as byte array
     * @throws MalformedURLException
     * @throws IOException
     * @throws Exception
     */
    byte[] getData() throws MalformedURLException, IOException, Exception {
        HttpGet httpGet = new HttpGet(URL.toString());
        httpGet.getParams().setParameter("http.socket.timeout", new Integer(timeout));
        response = client.execute(targetHost, httpGet, localcontext);

        boolean isOK = (response.getStatusLine().statusCode == HttpURLConnection.HTTP_OK);
        boolean isFound = (response.getStatusLine().statusCode == HttpURLConnection.HTTP_MOVED_TEMP);
        boolean isErrorServer = (response.getStatusLine().statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR);

        if (!isOK && !isFound & !isErrorServer) throw new IOException(URL.toString() + " cannot be read: " + response.getStatusLine().statusCode);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (entity != null) {
            entity.writeTo(baos)
        }
        return baos.toByteArray()
    }

    /**
     * Do delete action
     * Response is saved and can be retrieved with getResponseCode()/getResponseData()
     */
    void delete() {
        HttpDelete httpDelete = new HttpDelete(URL.toString());
        response = client.execute(targetHost, httpDelete, localcontext);
    }

    /**
     * Do post action
     * Response is saved and can be retrieved with getResponseCode()/getResponseData()
     * @param data Data for post action
     */
    void post(String data) {
        HttpPost httpPost = new HttpPost(URL.toString());
        //write data
        ContentProducer cp = new ContentProducer() {
            public void writeTo(OutputStream outstream) throws IOException {
                Writer writer = new OutputStreamWriter(outstream, "UTF-8");
                writer.write(data);
                writer.flush();
            }
        };
        HttpEntity entity = new EntityTemplate(cp);
        httpPost.setEntity(entity);

        response = client.execute(targetHost, httpPost, localcontext);
    }

    /**
     * Do put action
     * Response is saved and can be retrieved with getResponseCode()/getResponseData()
     * @param data Data for put action
     */
    void put(String data) {
        HttpPut httpPut = new HttpPut(URL.toString());
        //write data
        ContentProducer cp = new ContentProducer() {
            public void writeTo(OutputStream outstream) throws IOException {
                Writer writer = new OutputStreamWriter(outstream, "UTF-8");
                writer.write(data);
                writer.flush();
            }
        };
        HttpEntity entity = new EntityTemplate(cp);
        httpPut.setEntity(entity);

        response = client.execute(targetHost, httpPut, localcontext);
    }

    /**
     * Do put action
     * Response is saved and can be retrieved with getResponseCode()/getResponseData()
     * @param data Data for put action
     */
    void put(byte[] data) {
        HttpPut httpPut = new HttpPut(URL.getPath()); ;

        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(data), -1);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(false);
        httpPut.setEntity(reqEntity);
        response = client.execute(targetHost, httpPut, localcontext);
    }

    /**
     * Get response data as a String
     * @return response data
     */
    String getResponseData() {
        HttpEntity entityResponse = response.getEntity();
        String content = IOUtils.toString(entityResponse.getContent());
        content
    }

    /**
     * Get response code
     * @return response code
     */
    int getResponseCode() {
        return response.getStatusLine().getStatusCode()
    }

    /**
     * Close connection
     */
    void disconnect() {
        try {client.getConnectionManager().shutdown();} catch (Exception e) {}
    }
}