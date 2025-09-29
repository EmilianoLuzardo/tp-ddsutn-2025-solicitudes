package ar.edu.utn.dds.k3003.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HechoVerificadoDTO {
    private String id;
    private boolean existe;
}
