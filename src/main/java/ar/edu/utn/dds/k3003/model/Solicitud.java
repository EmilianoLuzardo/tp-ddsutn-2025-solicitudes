package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @Enumerated(EnumType.ORDINAL)
    private EstadoSolicitudBorradoEnum estado;
    private final String hechoId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaModificacion;

    public Solicitud(
            Long id,
            String descripcion,
            EstadoSolicitudBorradoEnum estado,
            String hechoId
    ) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
        this.hechoId = hechoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoSolicitudBorradoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitudBorradoEnum estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHechoId() {
        return hechoId;
    }

    public LocalDateTime getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) {
        fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        fechaCreacion = fechaCreacion;
    }
}
