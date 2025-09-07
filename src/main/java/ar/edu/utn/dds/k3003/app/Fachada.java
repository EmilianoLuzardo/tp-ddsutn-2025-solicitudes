package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.model.Solicitud;
import ar.edu.utn.dds.k3003.repository.InMemoryColeccionRepo;
import ar.edu.utn.dds.k3003.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaSolicitudes {

    private FachadaFuente fachadaFuente;
    private final SolicitudRepository solicitudRepository;

    protected Fachada() {
        this.solicitudRepository = new InMemoryColeccionRepo();
    }

    @Autowired
    public Fachada(
            FachadaFuente fachadaFuente,
            SolicitudRepository solicitudRepository) {
        this.fachadaFuente = fachadaFuente;
        this.solicitudRepository = solicitudRepository;
    }


    @Override
    public SolicitudDTO agregar(SolicitudDTO solicitudDTO) {
        validarHechoAsociado(solicitudDTO.hechoId());
        SolicitudDTO revisada = revisarDescripcionConAntiSpam(solicitudDTO);
        Solicitud nueva = mapearADominio(revisada);
        nueva.setFechaCreacion(LocalDateTime.now());
        Solicitud persistida = solicitudRepository.save(nueva);
        return mapearADTO(persistida);
    }

    @Override
    public SolicitudDTO modificar(String solicitudId, EstadoSolicitudBorradoEnum estado, String descripcion) {
        Solicitud solicitud = obtenerSolicitud(solicitudId);
        solicitud.setEstado(estado);
        solicitud.setDescripcion(descripcion);
        solicitud.setFechaUltimaModificacion(LocalDateTime.now());
        solicitud = solicitudRepository.save(solicitud);
        return mapearADTO(solicitud);
    }

    @Override
    public List<SolicitudDTO> buscarSolicitudXHecho(String hechoId) {
        return solicitudRepository.findByHechoId(hechoId).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Override
    public SolicitudDTO buscarSolicitudXId(String solicitudId) {
        Solicitud solicitud = this.solicitudRepository
                .findById(Long.valueOf(solicitudId))
                .orElseThrow(() -> new NoSuchElementException("No se encontró ninguna solicitud con id: " + solicitudId));
        return mapearADTO(solicitud);
    }

    @Override
    public boolean estaActivo(String unHechoId) {
        return !this.solicitudRepository.findByHechoId(unHechoId).stream()
                .anyMatch(x -> x.getEstado() == EstadoSolicitudBorradoEnum.ACEPTADA);
    }

    @Override
    public void setFachadaFuente(FachadaFuente fuente) {
        this.fachadaFuente = fuente;
    }

    private void validarHechoAsociado(String hechoId){
        HechoDTO hecho = this.fachadaFuente.buscarHechoXId(hechoId);
        if(hecho == null)
            throw new IllegalArgumentException("El hecho no existe");
    }

    private SolicitudDTO revisarDescripcionConAntiSpam(SolicitudDTO solicitud) {
        //como no hay nada definido respecto al antispam, voy a validar por ahora que tenga una descripcion
        if(solicitud.descripcion().isEmpty()){
            return new SolicitudDTO(
                    solicitud.id(),
                    solicitud.descripcion(),
                    EstadoSolicitudBorradoEnum.EN_DISCUCION,
                    solicitud.hechoId()
            );
        }else
            return solicitud;
    }
    private Solicitud obtenerSolicitud(String id) {
        return solicitudRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new NoSuchElementException("No se encontró la solicitud con id: " + id));
    }

    private SolicitudDTO mapearADTO(Solicitud solicitud) {
        return new SolicitudDTO(
                String.valueOf(solicitud.getId()),
                solicitud.getDescripcion(),
                solicitud.getEstado(),
                solicitud.getHechoId()
        );
    }

    private Solicitud mapearADominio(SolicitudDTO dto) {
        return new Solicitud(
                null,
                dto.descripcion(),
                dto.estado(),
                dto.hechoId()
        );
    }
}
