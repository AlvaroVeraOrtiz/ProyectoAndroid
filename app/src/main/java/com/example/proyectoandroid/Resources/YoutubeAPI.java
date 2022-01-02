package com.example.proyectoandroid.Resources;

/**
 * Sample Java code for youtube.search.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/guides/code_samples#java
 */

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.List;

public class YoutubeAPI {
    private static final String APPLICATION_NAME = "ProyectoAndroid";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static String API = "AIzaSyDEUQWH4aJOskd6-fcXMnQPSYqbBAGUH40";
    private static String busquedaAPI = "AIzaSyAZD6i5oRnAxj07hC0LdCBmNaBUoJbdARo";
    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = new NetHttpTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {

            }
        })
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static String getAPI () {
        return API;
    }

    public static String getBusquedaAPI() {
        return busquedaAPI;
    }
    public static List<SearchResult> buscaVideos(String titulo) throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list("snippet").setKey(busquedaAPI);
        SearchListResponse response = request.setQ(titulo).setType("video").setMaxResults(20L).execute();
        return response.getItems();
    }

    public static Video getVideo(String id) throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Videos.List request = youtubeService.videos()
                .list("snippet").setKey(API);
        VideoListResponse response = request.setId(id).setMaxResults(1L).execute();
        return response.getItems().get(0);
    }

    public static Channel getCanal(String id) throws IOException, GeneralSecurityException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Channels.List request = youtubeService.channels()
                .list("snippet").setKey(API);
        ChannelListResponse response = request.setId(id).setMaxResults(1L).execute();
        return response.getItems().get(0);
    }

    public static List<Video> getListaVideos(List<String> ids) throws GeneralSecurityException, IOException {


        Iterator<String> it = ids.iterator();
        List<Video> res = null;
        //Iteramos múltiples veces

        for(int i = 0; i <= ids.size()/50;i++) {
            String q = "";
            int cont = 0;
            if (it.hasNext()) {
                cont++;
                q += it.next();
            }

            while(it.hasNext() && cont < 50) {
                cont++;
                q+="," + it.next();
            }
            YouTube youtubeService = getService();

            //Buscamos los elementos contados
            // Define and execute the API request
            YouTube.Videos.List request = youtubeService.videos()
                    .list("snippet");
            VideoListResponse response = request.setKey(API)
                    .setId(q)
                    .setMaxResults((cont>=50) ? 50L : (long) cont)
                    .execute();
            if (res==null) {
                res = response.getItems();
                //Si es otra iteración añadimos todos los elementos al resultado
            } else {
                res.addAll(response.getItems());
            }
        }
        return res;
    }

    /*public static void main(String[] args)
            throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list("snippet");
        SearchListResponse response = request.setQ("titulo").execute();
        System.out.println(response);
    }*/
}