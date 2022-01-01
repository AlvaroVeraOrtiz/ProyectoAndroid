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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class YoutubeAPI {
    private static final String APPLICATION_NAME = "ProyectoAndroid";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static String API = "AIzaSyCoEz_Uk6fPutUgIpi8YzY6QCiZidLJP74";
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
    public static List<SearchResult> buscaVideos(String titulo) throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list("snippet").setKey(API);
        SearchListResponse response = request.setQ(titulo).setType("video").setMaxResults(20L).execute();
        return response.getItems();
    }

    public static SearchResult getVideo(String id) {
        return null;
    }

    public static Channel getCanal(String id) throws IOException, GeneralSecurityException {
        YouTube youtubeService = getService();
        // Define and execute the API request
        YouTube.Channels.List request = youtubeService.channels()
                .list("snippet").setKey(API);
        ChannelListResponse response = request.setId(id).setMaxResults(1L).execute();
        return response.getItems().get(0);
    }

    public static List<SearchResult> getListaVideos(List<String> ids) throws GeneralSecurityException, IOException {


        Iterator<String> it = ids.iterator();
        List<SearchResult> res = null;
        //El tamaño máximo de los resultados es 50, iteramos múltiples veces
        for (int i = 0; i <= ids.size()/50; i++) {
            //inicializamos la q y el contador de los elementos
            String q = "";
            int cont  = 0;
            if (it.hasNext()) {
                cont++;
                q += it.next();
            }
            //Contamos cuantos elementos hay en la iteración
            while (it.hasNext() && cont < 50) {
                cont++;
                q+= "|" + it.next();
            }
            YouTube youtubeService = getService();
            // Define and execute the API request
            YouTube.Search.List request = youtubeService.search()
                    .list("snippet");
            //Buscamos los elementos contados
            SearchListResponse response = request.setKey(API)
                    .setMaxResults( (cont>=50) ? 50L : (long) cont)
                    .setQ(q)
                    .setType("video")
                    .execute();
            //Si es la primera iteración no hay lista creada, la creamos
            if (i==0) {
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