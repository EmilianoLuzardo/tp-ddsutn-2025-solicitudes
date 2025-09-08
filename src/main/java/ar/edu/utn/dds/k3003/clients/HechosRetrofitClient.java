package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.model.HechoDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HechosRetrofitClient {

    @GET("hechos/{id}")
    Call<HechoDTO> get(@Path("id") String id);
}
