package ar.edu.utn.dds.k3003.controller;


import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {
    private final Fachada fachadaSolicitud;

    @Autowired
    public SolicitudController(Fachada fachadaSolicitud) {
        this.fachadaSolicitud = fachadaSolicitud;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> getSolicitudXHecho (@RequestParam("hecho") String hechoId){
        return ResponseEntity.ok(fachadaSolicitud.buscarSolicitudXHecho(hechoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> getSolicitudXId (@PathVariable("id") String id){
        return ResponseEntity.ok(fachadaSolicitud.buscarSolicitudXId(id));
    }

    @PostMapping
    public ResponseEntity<?> crearSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        try {
            SolicitudDTO solicitudCreada = fachadaSolicitud.agregar(solicitudDTO);
            return ResponseEntity.ok(solicitudCreada);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "No se pudo crear la solicitud");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PatchMapping
    public ResponseEntity<SolicitudDTO> actualizarSolicitud(@RequestBody SolicitudDTO dto) {
        SolicitudDTO actualizada = fachadaSolicitud.modificar(dto.id(), dto.estado(), dto.descripcion());
        return ResponseEntity.ok(actualizada);
    }
}
