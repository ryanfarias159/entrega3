package br.com.aeterna.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class TipoResiduo {

    private UUID id;
    private CategoriaResiduo categoria;
    private String nome;
    private String descricao;
    private String unidadeMedida;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public TipoResiduo() {
    }

    public TipoResiduo(UUID id, CategoriaResiduo categoria, String nome, String descricao, String unidadeMedida, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.categoria = categoria;
        this.nome = nome;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CategoriaResiduo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaResiduo categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    @Override
    public String toString() {
        return "TipoResiduo [id=" + id + ", nome=" + nome + ", unidadeMedida=" + unidadeMedida + ", categoria=" + (categoria != null ? categoria.getNome() : null) + "]";
    }
}