package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.controller.SolicitudController;
import ar.edu.utn.dds.k3003.model.Solicitud;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("test")
public class InMemoryColeccionRepo implements SolicitudRepository {

    private List<Solicitud> solicitudes;

    public InMemoryColeccionRepo() {
        this.solicitudes = new ArrayList<>();
    }


    @Override
    public Optional<Solicitud> findById(Long id) {
        return this.solicitudes.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public Solicitud save(Solicitud sol) {
        this.solicitudes.add(sol);
        return sol;
    }

    @Override
    public List<Solicitud> findAll() {
        return this.solicitudes.stream().toList();
    }

    @Override
    public List<Solicitud> findByHechoId(String hechoId) {
        return this.solicitudes.stream().filter(x -> x.getHechoId().equals(hechoId)).toList();
    }
}