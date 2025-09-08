package ar.edu.utn.dds.k3003.clients;


import ar.edu.utn.dds.k3003.model.HechoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.HttpStatus;

import java.io.IOException;
import java.util.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class HechosProxy{


    private final String endpoint;
    private final HechosRetrofitClient service;

    public HechosProxy(ObjectMapper objectMapper) {

        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_HECHOS", "https://tp-dds-2025-fuente-grupo2-2-fuentes-2.onrender.com");

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.endpoint)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(HechosRetrofitClient.class);
    }


    public HechoDTO buscarHechoXId(String s) throws NoSuchElementException {
         Response<HechoDTO> execute = null;
         try {
             execute = service.get(s).execute();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }

         if (execute.isSuccessful()) {
             return execute.body();
         }
         if (execute.code() == HttpStatus.NOT_FOUND.getCode()) {
             throw new NoSuchElementException("no se encontro el hecho " + s);
         }
        throw new RuntimeException("Error conectandose con el componente hechos");
    }

}