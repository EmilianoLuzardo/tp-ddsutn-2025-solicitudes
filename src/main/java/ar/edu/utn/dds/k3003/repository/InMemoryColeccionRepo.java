package ar.edu.utn.dds.k3003.repository;


import ar.edu.utn.dds.k3003.model.Solicitud;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


@Repository
@Profile("test")
public class InMemoryColeccionRepo implements SolicitudRepository {

    //private final List<Solicitud> solicitudes = new ArrayList<>();
    private final Map<Long, Solicitud> solicitudes = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);


    @Override
    public Optional<Solicitud> findById(String id) {
        return Optional.ofNullable(solicitudes.get(id));
    }

    public Solicitud save(Solicitud sol) {
        solicitudes.put(sol.getId(), sol);
        return sol;
    }


    public List<Solicitud> findByHechoId(String hechoId) {
        return solicitudes.values().stream().filter(x -> x.getHechoId().equals(hechoId)).toList();
    }
}