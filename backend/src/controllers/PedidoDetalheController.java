package com.myapp.controllers;

import com.myapp.models.dto.PedidoDetalheDto;
import com.myapp.services.PedidoDetalheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoDetalheController {

    @Autowired
    private PedidoDetalheService pedidoDetalheService;

    // Endpoint para carregar os detalhes completos do Pedido
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetalheDto> carregarDetalhesPedido(@PathVariable Long id) {
        PedidoDetalheDto dto = pedidoDetalheService.obterDetalhesPedido(id);
        return ResponseEntity.ok(dto);
    }

    // Endpoint para obter os Itens do Pedido
    @GetMapping("/{id}/itens")
    public ResponseEntity<?> obterItens(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoDetalheService.obterItensPedido(id));
    }

    // Endpoint para obter Observações do Pedido
    @GetMapping("/{id}/observacoes")
    public ResponseEntity<?> obterObservacoes(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoDetalheService.obterObservacoesPedido(id));
    }

    // Endpoint para obter Bloqueios do Pedido
    @GetMapping("/{id}/bloqueios")
    public ResponseEntity<?> obterBloqueios(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoDetalheService.obterBloqueiosPedido(id));
    }

    // Endpoint para obter Notas Fiscais do Pedido
    @GetMapping("/{id}/notas")
    public ResponseEntity<?> obterNotasFiscais(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoDetalheService.obterNotasFiscaisPedido(id));
    }

    // Endpoint para exportar dados para Excel, de acordo com a aba solicitada
    @GetMapping("/{id}/export/{aba}")
    public ResponseEntity<byte[]> exportarParaExcel(@PathVariable Long id, @PathVariable String aba) {
        byte[] excelBytes = pedidoDetalheService.exportarDadosParaExcel(id, aba);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "dados_" + aba + ".xlsx");
        
        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }
}
