package ar.edu.utn.dds.k3003.clients;


import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;

import java.io.IOException;
import java.util.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.jackson.JacksonConverterFactory;
@Service
public class HechosProxy implements FachadaFuente {

    //private final String endpoint;
    //private final HechosRetrofitClient service;

    //public HechosProxy(ObjectMapper objectMapper) {

    //    var env = System.getenv();
    //    this.endpoint = env.getOrDefault("URL_HECHOS", "https://two025-tp-entrega-2-mijhwang.onrender.com/api/");

    //    var retrofit =
    //            new Retrofit.Builder()
    //                    .baseUrl(this.endpoint)
    //                    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
    //                    .build();

    //    this.service = retrofit.create(HechosRetrofitClient.class);
    //}


    @Override
    public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
        return null;
    }

    @Override
    public ColeccionDTO buscarColeccionXId(String s) throws NoSuchElementException {
        return null;
    }

    @Override
    public HechoDTO agregar(HechoDTO hechoDTO) {
        return null;
    }

    @Override
    public HechoDTO buscarHechoXId(String s) throws NoSuchElementException {
        // Response<HechoDTO> execute = null;
        // try {
        //     execute = service.get(s).execute();
        // } catch (IOException e) {
        //     throw new RuntimeException(e);
        // }
//
        // if (execute.isSuccessful()) {
        //     return execute.body();
        // }
        // if (execute.code() == HttpStatus.NOT_FOUND.getCode()) {
        //     throw new NoSuchElementException("no se encontro el hecho " + s);
        // }
        throw new RuntimeException("Error conectandose con el componente hechos");
    }

    @Override
    public List<HechoDTO> buscarHechosXColeccion(String s) throws NoSuchElementException {
        return List.of();
    }

    @Override
    public void setProcesadorPdI(FachadaProcesadorPdI fachadaProcesadorPdI) {

    }

    @Override
    public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
        return null;
    }

    @Override
    public List<ColeccionDTO> colecciones() {
        return List.of();
    }
}