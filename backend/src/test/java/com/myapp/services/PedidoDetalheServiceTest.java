package com.myapp.services;

import com.myapp.models.Cliente;
import com.myapp.models.Pedido;
import com.myapp.models.dto.PedidoDetalheDto;
import com.myapp.repositories.ItemRepository;
import com.myapp.repositories.NotaFiscalRepository;
import com.myapp.repositories.ObservacaoRepository;
import com.myapp.repositories.PedidoBloqueioRepository;
import com.myapp.repositories.PedidoRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PedidoDetalheServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ObservacaoRepository observacaoRepository;

    @Mock
    private PedidoBloqueioRepository pedidoBloqueioRepository;

    @Mock
    private NotaFiscalRepository notaFiscalRepository;

    @InjectMocks
    private PedidoDetalheService pedidoDetalheService;

    private Pedido pedido;

    @BeforeEach
    public void setup() {
        pedido = new Pedido();
        pedido.setNumeroPedido("12345");
        
        Cliente cliente = new Cliente();
        cliente.setCnpj("00.000.000/0001-00");
        cliente.setRazaoSocial("Cliente Exemplo");
        
        pedido.setCliente(cliente);
    }

    @Test
    @DisplayName("obterDetalhesPedido - Pedido encontrado")
    public void testObterDetalhesPedidoFound() {
        given(pedidoRepository.findById(anyLong())).willReturn(Optional.of(pedido));

        PedidoDetalheDto dto = pedidoDetalheService.obterDetalhesPedido(1L);
        assertNotNull(dto);
        assertEquals("12345", dto.getNumeroPedido());
        assertEquals("00.000.000/0001-00", dto.getCnpj());
        assertEquals("Cliente Exemplo", dto.getRazaoSocial());
    }

    @Test
    @DisplayName("obterDetalhesPedido - Pedido não encontrado")
    public void testObterDetalhesPedidoNotFound() {
        given(pedidoRepository.findById(anyLong())).willReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoDetalheService.obterDetalhesPedido(1L);
        });
        assertEquals("Pedido não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("exportarDadosParaExcel - aba válida")
    public void testExportarDadosParaExcelValid() {
        // Para o teste, vamos simular que a lista retornada não é vazia
        given(itemRepository.findByPedidoId(anyLong())).willReturn(Collections.singletonList(new Object()));

        byte[] excelBytes = pedidoDetalheService.exportarDadosParaExcel(1L, "itens");
        assertNotNull(excelBytes);
        // Verifica se o array de bytes possui tamanho maior que zero
        assertTrue(excelBytes.length > 0);
    }

    @Test
    @DisplayName("exportarDadosParaExcel - aba inválida")
    public void testExportarDadosParaExcelInvalidAba() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoDetalheService.exportarDadosParaExcel(1L, "invalida");
        });
        assertEquals("Aba inválida para exportação", exception.getMessage());
    }
}
