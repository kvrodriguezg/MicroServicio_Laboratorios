package com.microservicio_laboratorios.microservicio_laboratorios.exception;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorDetailsTest {

    @Test
    void testErrorDetails() {
        LocalDateTime now = LocalDateTime.now();
        String mensaje = "Mensaje de error";
        String detalles = "Detalles del error";

        ErrorDetails errorDetails = new ErrorDetails(now, mensaje, detalles);

        assertEquals(now, errorDetails.getTimestamp());
        assertEquals(mensaje, errorDetails.getMensaje());
        assertEquals(detalles, errorDetails.getDetalles());
        assertNotNull(errorDetails.getTimestamp());
    }
}
