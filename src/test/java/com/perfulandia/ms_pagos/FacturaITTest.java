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
class FacturaITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarFacturas_devuelve200() throws Exception {
        mockMvc.perform(get("/api/v1/facturas"))
                .andExpect(status().isOk());
    }

    @Test
    void generarFactura_valida_devuelve201YGenerada() throws Exception {
        String body = """
                {
                  "idPago": 1,
                  "folio": "F-TEMP",
                  "montoTotal": 35000,
                  "estado": "GENERADA"
                }
                """;

        mockMvc.perform(post("/api/v1/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idFactura", notNullValue()))
                .andExpect(jsonPath("$.folio", notNullValue()))
                .andExpect(jsonPath("$.estado").value("GENERADA"))
                .andExpect(jsonPath("$.montoTotal").value(35000));
    }

    @Test
    void buscarFacturaNoExistente_devuelve404() throws Exception {
        mockMvc.perform(get("/api/v1/facturas/999999"))
                .andExpect(status().isNotFound());
    }
}