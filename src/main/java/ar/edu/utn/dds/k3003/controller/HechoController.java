package ar.edu.utn.dds.k3003.controller;


import ar.edu.utn.dds.k3003.app.Fachada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hechos")
public class HechoController {

    private final Fachada fachadaSolicitud;

    @Autowired
    public HechoController(Fachada fachadaSolicitud) {
        this.fachadaSolicitud = fachadaSolicitud;
    }


    @GetMapping("/{hechoId}/activo")
    public ResponseEntity<Boolean> getHechoEstaActivo (@PathVariable("hechoId") String id){
        return ResponseEntity.ok(fachadaSolicitud.estaActivo(id));
    }

}
