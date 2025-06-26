package com.myapp.models;

import javax.persistence.*;

@Entity
@Table(name = "notas_fiscais")
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cod_nota_fiscal")
    private String codNotaFiscal;

    @Column(name = "serie")
    private String serie;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getCodNotaFiscal() {
        return codNotaFiscal;
    }

    public void setCodNotaFiscal(String codNotaFiscal) {
        this.codNotaFiscal = codNotaFiscal;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
