package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.dto.DatosAgendarConsulta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HorarioDeFuncionamientoClinicaTest {

    private HorarioDeFuncionamientoClinica horarioDeFuncionamientoClinica;
    private DatosAgendarConsulta datosMock;

    @BeforeEach
    void setUp() {
        horarioDeFuncionamientoClinica = new HorarioDeFuncionamientoClinica();
        datosMock = Mockito.mock(DatosAgendarConsulta.class);
    }

    @Test
    void testConsultaDomingo_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 27, 10, 0)); // Domingo a las 10:00
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaAntesDeApertura_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 6, 0)); // Lunes a las 06:00
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaDespuesDeCierre_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 20, 0)); // Lunes a las 20:00
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaEnHorarioPermitido_NoLanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 10, 0)); // Lunes a las 10:00
        assertDoesNotThrow(() -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaSabadoEnHorarioPermitido_NoLanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 26, 9, 0)); // Sábado a las 09:00
        assertDoesNotThrow(() -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaEnHorarioDeApertura_NoLanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 7, 0)); // Lunes a las 07:00
        assertDoesNotThrow(() -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaEnHorarioDeCierre_NoLanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 19, 0)); // Lunes a las 19:00
        assertDoesNotThrow(() -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testFechaNull_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(null);
        assertThrows(NullPointerException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaDomingoHoraValida_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 27, 10, 0)); // Domingo a las 10:00
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaSabadoDespuesDeCierre_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 26, 20, 0)); // Sábado a las 20:00
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }
    @Test
    void testConsultaJustoAntesDeApertura_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 6, 59)); // Lunes a las 06:59
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaJustoDespuesDeCierre_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 19, 1)); // Lunes a las 19:01
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaOtraZonaHoraria_NoAfectaValidacion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 10, 0).atZone(ZoneId.of("America/New_York")).toLocalDateTime()); // Nueva York, lunes a las 10:00
        assertDoesNotThrow(() -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaHorasMadrugada_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 0, 0)); // Lunes a las 00:00
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));

        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 2, 0)); // Lunes a las 02:00
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }
// testConsultaHorasInusualesExactas_LanzaExcepcion() testConsultaJustoDespuesDeCierre_LanzaExcepcion()
    @Test
    void testConsultaHorasInusualesExactas_LanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 6, 59, 59)); // Lunes a las 06:59:59
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));

        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 28, 19, 0, 1)); // Lunes a las 19:00:01
        assertThrows(ValidationException.class, () -> horarioDeFuncionamientoClinica.validar(datosMock));
    }

    @Test
    void testConsultaSabadoLimiteInferiorYSuperior_NoLanzaExcepcion() {
        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 26, 7, 0)); // Sábado a las 07:00
        assertDoesNotThrow(() -> horarioDeFuncionamientoClinica.validar(datosMock));

        Mockito.when(datosMock.fecha()).thenReturn(LocalDateTime.of(2024, 10, 26, 19, 0)); // Sábado a las 19:00
        assertDoesNotThrow(() -> horarioDeFuncionamientoClinica.validar(datosMock));
    }


}
