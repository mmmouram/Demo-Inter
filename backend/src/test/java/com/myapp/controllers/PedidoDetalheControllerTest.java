package com.myapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.models.dto.PedidoDetalheDto;
import com.myapp.services.PedidoDetalheService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoDetalheController.class)
public class PedidoDetalheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoDetalheService pedidoDetalheService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/pedidos/{id} - sucesso ao carregar detalhes do pedido")
    public void testCarregarDetalhesPedido() throws Exception {
        PedidoDetalheDto dto = new PedidoDetalheDto();
        dto.setNumeroPedido("12345");
        dto.setCnpj("00.000.000/0001-00");
        dto.setRazaoSocial("Cliente Exemplo");
        
        given(pedidoDetalheService.obterDetalhesPedido(anyLong())).willReturn(dto);

        mockMvc.perform(get("/api/pedidos/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id}/itens - sucesso ao obter itens")
    public void testObterItens() throws Exception {
        given(pedidoDetalheService.obterItensPedido(anyLong())).willReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/pedidos/1/itens"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id}/observacoes - sucesso ao obter observações")
    public void testObterObservacoes() throws Exception {
        given(pedidoDetalheService.obterObservacoesPedido(anyLong())).willReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/pedidos/1/observacoes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id}/bloqueios - sucesso ao obter bloqueios")
    public void testObterBloqueios() throws Exception {
        given(pedidoDetalheService.obterBloqueiosPedido(anyLong())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pedidos/1/bloqueios"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id}/notas - sucesso ao obter notas fiscais")
    public void testObterNotasFiscais() throws Exception {
        given(pedidoDetalheService.obterNotasFiscaisPedido(anyLong())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pedidos/1/notas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id}/export/{aba} - sucesso ao exportar para excel")
    public void testExportarParaExcel() throws Exception {
        byte[] fakeExcel = "fake excel content".getBytes();
        given(pedidoDetalheService.exportarDadosParaExcel(eq(1L), eq("itens"))).willReturn(fakeExcel);

        mockMvc.perform(get("/api/pedidos/1/export/itens"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=dados_itens.xlsx"))
                .andExpect(content().bytes(fakeExcel));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id}/export/{aba} - error aba inválida")
    public void testExportarParaExcelInvalidAba() throws Exception {
        given(pedidoDetalheService.exportarDadosParaExcel(eq(1L), eq("invalida")))
                .willThrow(new RuntimeException("Aba inválida para exportação"));

        mockMvc.perform(get("/api/pedidos/1/export/invalida"))
                .andExpect(status().isBadRequest());
    }
}
