package com.myapp.services;

import com.myapp.models.Pedido;
import com.myapp.models.Item;
import com.myapp.models.Observacao;
import com.myapp.models.PedidoBloqueio;
import com.myapp.models.NotaFiscal;
import com.myapp.models.dto.PedidoDetalheDto;
import com.myapp.repositories.PedidoRepository;
import com.myapp.repositories.ItemRepository;
import com.myapp.repositories.ObservacaoRepository;
import com.myapp.repositories.PedidoBloqueioRepository;
import com.myapp.repositories.NotaFiscalRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoDetalheService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObservacaoRepository observacaoRepository;

    @Autowired
    private PedidoBloqueioRepository pedidoBloqueioRepository;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    // Retorna os detalhes básicos de um Pedido, incluindo dados do Cliente
    public PedidoDetalheDto obterDetalhesPedido(Long pedidoId) {
        Optional<Pedido> optional = pedidoRepository.findById(pedidoId);
        if (optional.isEmpty()) {
            throw new RuntimeException("Pedido não encontrado");
        }
        Pedido pedido = optional.get();
        PedidoDetalheDto dto = new PedidoDetalheDto();
        dto.setNumeroPedido(pedido.getNumeroPedido());
        if (pedido.getCliente() != null) {
            dto.setCnpj(pedido.getCliente().getCnpj());
            dto.setRazaoSocial(pedido.getCliente().getRazaoSocial());
        }
        return dto;
    }

    public List<Item> obterItensPedido(Long pedidoId) {
        return itemRepository.findByPedidoId(pedidoId);
    }

    public List<Observacao> obterObservacoesPedido(Long pedidoId) {
        return observacaoRepository.findByPedidoId(pedidoId);
    }

    public List<PedidoBloqueio> obterBloqueiosPedido(Long pedidoId) {
        return pedidoBloqueioRepository.findByPedidoId(pedidoId);
    }

    public List<NotaFiscal> obterNotasFiscaisPedido(Long pedidoId) {
        return notaFiscalRepository.findByPedidoId(pedidoId);
    }

    // Método que gera um arquivo Excel a partir dos dados de uma aba específica
    public byte[] exportarDadosParaExcel(Long pedidoId, String aba) {
        List<?> dados;
        switch (aba.toLowerCase()) {
            case "itens":
                dados = obterItensPedido(pedidoId);
                break;
            case "observacao":
                dados = obterObservacoesPedido(pedidoId);
                break;
            case "bloqueios":
                dados = obterBloqueiosPedido(pedidoId);
                break;
            case "notafiscal":
                dados = obterNotasFiscaisPedido(pedidoId);
                break;
            default:
                throw new RuntimeException("Aba inválida para exportação");
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Dados");
            // Criação de cabeçalhos genéricos (pode ser customizado conforme cada modelo)
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Coluna1");
            headerRow.createCell(1).setCellValue("Coluna2");
            headerRow.createCell(2).setCellValue("Coluna3");

            int rowIndex = 1;
            for (Object obj : dados) {
                Row row = sheet.createRow(rowIndex++);
                // Para efeito de exemplo, converte o objeto para string; em uma implementação real, os campos específicos seriam extraídos
                row.createCell(0).setCellValue(obj.toString());
                row.createCell(1).setCellValue("");
                row.createCell(2).setCellValue("");
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar arquivo Excel: " + e.getMessage());
        }
    }
}
