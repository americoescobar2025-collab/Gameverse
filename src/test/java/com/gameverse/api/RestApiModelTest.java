package com.gameverse.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameverse.model.dto.Videojuego;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestApiModelTest {

    private final ObjectMapper json = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    @Test
    void convierteElPayloadEnElModeloDeNegocioSinAceptarIdDelCuerpo() {
        VideojuegoRequest request = new VideojuegoRequest();
        request.setTitulo("Juego válido");
        request.setDescripcion("Descripción suficientemente larga");
        request.setPrecio(new BigDecimal("19.99"));
        request.setStock(4);
        request.setImagenUrl("https://example.com/juego.jpg");
        request.setCategoria("Acción");

        Videojuego videojuego = request.toVideojuego(27);

        assertEquals(27, videojuego.getId());
        assertEquals("Juego válido", videojuego.getTitulo());
        assertEquals(19.99, videojuego.getPrecio());
        assertEquals(4, videojuego.getStock());
    }

    @Test
    void rechazaPayloadSinPrecioOStock() {
        VideojuegoRequest sinPrecio = new VideojuegoRequest();
        sinPrecio.setStock(1);
        assertThrows(IllegalArgumentException.class, () -> sinPrecio.toVideojuego(0));

        VideojuegoRequest sinStock = new VideojuegoRequest();
        sinStock.setPrecio(new BigDecimal("1.00"));
        assertThrows(IllegalArgumentException.class, () -> sinStock.toVideojuego(0));
    }

    @Test
    void respuestaNoExponePropiedadesInternasDelDto() throws Exception {
        Videojuego videojuego = new Videojuego(
                3, "Juego", "Descripción válida", 9.99, 2, null, "Acción"
        );

        String payload = json.writeValueAsString(VideojuegoResponse.from(videojuego));

        assertEquals(3, json.readTree(payload).get("id").asInt());
        assertFalse(payload.contains("categoriaObjeto"));
    }

    @Test
    void rechazaCamposDesconocidosParaMantenerElContratoExplicito() {
        assertThrows(Exception.class, () -> json.readValue(
                "{\"titulo\":\"Juego\",\"campoNoPermitido\":true}",
                VideojuegoRequest.class
        ));
    }
}
