package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
public class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Fachada fachada;

    @Test
    @DisplayName("Buscar por hecho")
    public void testBuscarPorHecho() throws Exception {
        SolicitudDTO sol1 = new SolicitudDTO("1", "descripcion 1", null, "hecho123");
        SolicitudDTO sol2 = new SolicitudDTO("2", "descripcion 2", null, "hecho123");

        Mockito.when(fachada.buscarSolicitudXHecho("hecho123")).thenReturn(List.of(sol1, sol2));

        mockMvc.perform(get("/solicitudes")
                        .param("hecho", "hecho123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    @DisplayName("Buscar por id de solicitu")
    public void testBuscarSolicitudPorId() throws Exception {
        SolicitudDTO sol = new SolicitudDTO("1", "descripcion", null, "hecho123");

        Mockito.when(fachada.buscarSolicitudXId("1")).thenReturn(sol);

        mockMvc.perform(get("/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.descripcion").value("descripcion"));
    }

    @Test
    @DisplayName("Crear solicitud")
    public void testCrearSolicitud() throws Exception {
        SolicitudDTO input = new SolicitudDTO(null, "nueva solicitud", null, "hecho123");
        SolicitudDTO output = new SolicitudDTO("5", "nueva solicitud", null, "hecho123");

        Mockito.when(fachada.agregar(any(SolicitudDTO.class))).thenReturn(output);

        mockMvc.perform(post("/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.descripcion").value("nueva solicitud"));
    }

    @Test
    @DisplayName("Modificar solicitud")
    public void testActualizarSolicitud() throws Exception {
        SolicitudDTO input = new SolicitudDTO("5", "actualizada", null, "hecho123");
        SolicitudDTO output = new SolicitudDTO("5", "actualizada", null, "hecho123");

        Mockito.when(fachada.modificar(eq("5"), any(), any())).thenReturn(output);

        mockMvc.perform(patch("/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.descripcion").value("actualizada"));
    }
}
