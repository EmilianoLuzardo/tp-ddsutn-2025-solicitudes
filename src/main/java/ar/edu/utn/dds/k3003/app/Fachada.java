package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.HechosProxy;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.model.Solicitud;

import ar.edu.utn.dds.k3003.repository.JpaSolicitudRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class Fachada {
    private HechosProxy hechosProxy;
    private JpaSolicitudRepository solicitudRepository;

    @Autowired
    public Fachada(JpaSolicitudRepository solicitudRepository)
    {
        this.solicitudRepository = solicitudRepository;
        this.hechosProxy = new HechosProxy(new ObjectMapper());
    }

    //Para que los tests puedan usar constructor vacío y no se cacheen entre sí
    private Fachada() {
        ConfigurableApplicationContext ctx =
                new SpringApplicationBuilder(ar.edu.utn.dds.k3003.Application.class)
                        .properties(
                                "spring.main.web-application-type=none",
                                "spring.jpa.hibernate.ddl-auto=create-drop",
                                // pedido de tests
                                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
                        )
                        .profiles("test")
                        .run();
        this.solicitudRepository = ctx.getBean(JpaSolicitudRepository.class);
        this.hechosProxy = new HechosProxy(new ObjectMapper());
    }

    @Transactional
    public SolicitudDTO agregar(SolicitudDTO solicitudDTO) {
        //validarHechoAsociado(solicitudDTO.hechoId());
        SolicitudDTO revisada = revisarDescripcionConAntiSpam(solicitudDTO);
        Solicitud nueva = mapearADominio(revisada);
        nueva.setFechaCreacion(LocalDateTime.now());
        Solicitud persistida = solicitudRepository.save(nueva);
        return mapearADTO(persistida);
    }


    public SolicitudDTO modificar(String solicitudId, EstadoSolicitudBorradoEnum estado, String descripcion) {
        Solicitud solicitud = obtenerSolicitud(solicitudId);
        solicitud.setEstado(estado);
        solicitud.setDescripcion(descripcion);
        solicitud.setFechaUltimaModificacion(LocalDateTime.now());
        solicitud = solicitudRepository.save(solicitud);
        //solicitud = solicitudRepository.save(solicitud);
        return mapearADTO(solicitud);
    }


    public List<SolicitudDTO> buscarSolicitudXHecho(String hechoId) {
        return solicitudRepository.findByHechoId(hechoId).stream()
                .map(this::mapearADTO)
                .toList();
    }


    public SolicitudDTO buscarSolicitudXId(String solicitudId) {
        Solicitud solicitud = solicitudRepository
                .findById(Long.valueOf(solicitudId))
                .orElseThrow(() -> new NoSuchElementException("No se encontró ninguna solicitud con id: " + solicitudId));
        return mapearADTO(solicitud);
    }


    public boolean estaActivo(String unHechoId) {
        return !this.solicitudRepository.findByHechoId(unHechoId).stream()
                .anyMatch(x -> x.getEstado() == EstadoSolicitudBorradoEnum.ACEPTADA);
    }




    private void validarHechoAsociado(String hechoId){
        HechoDTO hecho = this.hechosProxy.buscarHechoXId(hechoId);
        if(hecho == null)
            throw new IllegalArgumentException("El hecho no existe");
    }

    public String borrarTodo() {
        solicitudRepository.deleteAllInBatch();
        return "borradas todos las solicitudes";
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
