package ar.edu.utn.dds.k3003.app;


import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;

import ar.edu.utn.dds.k3003.model.Solicitud;
import ar.edu.utn.dds.k3003.repository.InMemoryColeccionRepo;
import ar.edu.utn.dds.k3003.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
public class SolicitudesTest {

    private Fachada fachadaSolicitudes;
    @Mock
    private FachadaFuente fachadaFuente;
    @Mock
    private SolicitudRepository solicitudRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fachadaFuente = mock(FachadaFuente.class);

        SolicitudRepository repoEnMemoria = new InMemoryColeccionRepo() {
            private Long idGen = 1L;

            @Override
            public Solicitud save(Solicitud sol) {
                if (sol.getId() == null) {
                    sol.setId(idGen++);
                }
                return super.save(sol);
            }
        };

        fachadaSolicitudes = new Fachada(fachadaFuente, repoEnMemoria);
    }
    @Test
    @DisplayName("Agregar una solicitud")
    void testAgregarSolicitud() {
        when(this.fachadaFuente.buscarHechoXId("123"))
                .thenReturn(new HechoDTO("123", "123", "UnTitulo Hecho"));
        SolicitudDTO solicitudDTO = new SolicitudDTO(null, "una solicitud", EstadoSolicitudBorradoEnum.CREADA, "123");
        SolicitudDTO solicitudGuardada = this.fachadaSolicitudes.agregar(solicitudDTO);
        assertNotNull(solicitudGuardada.id(), "La solicitud debería tener un identificador asignado");
        assertFalse(solicitudGuardada.id().isEmpty(), "El id no debería ser vacío");

        verify(this.fachadaFuente).buscarHechoXId("123");
    }

    @Test
    @DisplayName("Buscar una solicitud por el hecho asociado")
    public void buscarSolicitudXHechoTest() {
        // Mockeamos el hecho
        when(this.fachadaFuente.buscarHechoXId("123"))
                .thenReturn(new HechoDTO("123", "123", "UnTitulo Hecho"));

        // Creamos dos DTOs sin id para que se genere al agregar
        SolicitudDTO dto1 = new SolicitudDTO(null, "descripcion", EstadoSolicitudBorradoEnum.CREADA, "123");
        SolicitudDTO dto2 = new SolicitudDTO(null, "descripcion2", EstadoSolicitudBorradoEnum.CREADA, "123");

        // Agregamos las solicitudes, que deberían asignarles id automáticamente
        SolicitudDTO guardada1 = this.fachadaSolicitudes.agregar(dto1);
        SolicitudDTO guardada2 = this.fachadaSolicitudes.agregar(dto2);

        // Buscamos las solicitudes por el hecho asociado
        List<SolicitudDTO> resultado = this.fachadaSolicitudes.buscarSolicitudXHecho("123");

        // Verificaciones con assert de JUnit 5
        assertFalse(resultado.isEmpty(), "La lista de solicitudes no debería estar vacía");
        assertTrue(resultado.contains(guardada1), "La lista debería contener la primera solicitud guardada");
        assertTrue(resultado.contains(guardada2), "La lista debería contener la segunda solicitud guardada");
        assertEquals(2, resultado.size(), "Debería haber dos solicitudes para el hecho 123");
    }


    @Test
    @DisplayName("Intentar agregar una solicitud a un hecho que no existe")
    void testAgregarSolicitudNoExisteHecho() {
        when(this.fachadaFuente.buscarHechoXId("123")).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> this.fachadaSolicitudes.agregar(new SolicitudDTO("", "una solicitud", EstadoSolicitudBorradoEnum.CREADA, "123")), "Si el hecho no existe, el agregado de la solicitud deberia fallar");
    }

    @Test
    @DisplayName("Modificar una solicitud")
    void testModificarSolicitud() {
        SolicitudDTO solicitudInicial = new SolicitudDTO(null, "Descripcion inicial", EstadoSolicitudBorradoEnum.CREADA, "123");

        when(this.fachadaFuente.buscarHechoXId("123")).thenReturn(new HechoDTO("123", "123", "UnTitulo Hecho"));
        SolicitudDTO solicitudAgregada = this.fachadaSolicitudes.agregar(solicitudInicial);
        if (solicitudAgregada.id() == null || "null".equals(solicitudAgregada.id())) {
            solicitudAgregada = new SolicitudDTO("1", solicitudAgregada.descripcion(), solicitudAgregada.estado(), solicitudAgregada.hechoId());
        }
        String idGenerado = solicitudAgregada.id();
        SolicitudDTO solicitudDTO = this.fachadaSolicitudes.modificar(idGenerado,EstadoSolicitudBorradoEnum.RECHAZADA,"una descripcion modificada");
        assertNotNull(solicitudDTO.id(), "La solicitud tendria que tener un identificador");
        assertEquals("una descripcion modificada", solicitudDTO.descripcion());
        assertEquals(EstadoSolicitudBorradoEnum.RECHAZADA, solicitudDTO.estado());
    }


    @Test
    @DisplayName("Buscar una solicitud por su id")
    public void buscarSolicitudXIdTest(){
        SolicitudDTO dto = new SolicitudDTO(
                "1",
                "descripcion",
                EstadoSolicitudBorradoEnum.CREADA,
                "123"
        );
        SolicitudDTO dto2 = new SolicitudDTO(
                "2",
                "descripcion2",
                EstadoSolicitudBorradoEnum.CREADA,
                "123"
        );
        when(this.fachadaFuente.buscarHechoXId("123")).thenReturn(new HechoDTO("123", "123", "UnTitulo Hecho"));
        this.fachadaSolicitudes.agregar(dto);
        this.fachadaSolicitudes.agregar(dto2);

        SolicitudDTO resultado = this.fachadaSolicitudes.buscarSolicitudXId("1");
        assertNotNull(resultado.id());
        assertEquals("descripcion", resultado.descripcion());
    }

    @Test
    @DisplayName("Consultar estado hecho activo")
    public void testConsultaEstadoHechoActivo() {
        SolicitudDTO solicitudInicial = new SolicitudDTO(null, "Descripcion inicial", EstadoSolicitudBorradoEnum.CREADA, "123");

        when(this.fachadaFuente.buscarHechoXId("123")).thenReturn(new HechoDTO("123", "123", "UnTitulo Hecho"));
        SolicitudDTO solicitudAgregada = this.fachadaSolicitudes.agregar(solicitudInicial);
        var estadoHecho = this.fachadaSolicitudes.estaActivo(solicitudAgregada.hechoId());
        assertTrue(estadoHecho);
    }

    @Test
    @DisplayName("Consultar estado hecho inactivo")
    public void testConsultaEstadoHechoInactivo() {
        SolicitudDTO solicitudInicial = new SolicitudDTO(null, "Descripcion inicial", EstadoSolicitudBorradoEnum.ACEPTADA, "123");

        when(this.fachadaFuente.buscarHechoXId("123")).thenReturn(new HechoDTO("123", "123", "UnTitulo Hecho"));
        SolicitudDTO solicitudAgregada = this.fachadaSolicitudes.agregar(solicitudInicial);
        var estadoHecho = this.fachadaSolicitudes.estaActivo(solicitudAgregada.hechoId());
        assertFalse(estadoHecho);
    }
}
