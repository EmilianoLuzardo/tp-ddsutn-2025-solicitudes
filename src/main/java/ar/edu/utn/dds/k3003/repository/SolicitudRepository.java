package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {

    Optional<Solicitud> findById(String id);

    Solicitud save(Solicitud solicitud);

    List<Solicitud> findByHechoId(String hechoId);

}
