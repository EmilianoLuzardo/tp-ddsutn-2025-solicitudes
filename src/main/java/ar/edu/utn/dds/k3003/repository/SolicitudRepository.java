//package ar.edu.utn.dds.k3003.repository;
//
//import ar.edu.utn.dds.k3003.controller.SolicitudController;
//import ar.edu.utn.dds.k3003.model.Solicitud;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface SolicitudRepository {
//
//    Optional<SolicitudController> findById(String id);
//    Solicitud save(Solicitud col);
//    List<Solicitud> findAll();
//
//}

package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Solicitud;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {

    Optional<Solicitud> findById(Long id);

    Solicitud save(Solicitud solicitud);

    List<Solicitud> findAll();

    List<Solicitud> findByHechoId(String hechoId);

}
