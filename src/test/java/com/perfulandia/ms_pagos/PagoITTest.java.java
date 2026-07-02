package com.perfulandia.ms_pagos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PagoITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarPagos_devuelve200() throws Exception {
        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk());
    }

    @Test
    void procesarPago_valido_devuelve201YConfirmado() throws Exception {
        String body = """
                {
                  "idPedido": 1,
                  "monto": 35000,
                  "metodoPago": "TARJETA",
                  "estado": "PENDIENTE"
                }
                """;

        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPago", notNullValue()))
                .andExpect(jsonPath("$.estado").value("CONFIRMADO"))
                .andExpect(jsonPath("$.monto").value(35000))
                .andExpect(jsonPath("$.metodoPago").value("TARJETA"));
    }

    @Test
    void procesarPago_montoCero_devuelve402() throws Exception {
        String body = """
                {
                  "idPedido": 2,
                  "monto": 0,
                  "metodoPago": "TARJETA",
                  "estado": "PENDIENTE"
                }
                """;

        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isPaymentRequired());
    }

    @Test
    void buscarPagoNoExistente_devuelve404() throws Exception {
        mockMvc.perform(get("/api/v1/pagos/999999"))
                .andExpect(status().isNotFound());
    }
}