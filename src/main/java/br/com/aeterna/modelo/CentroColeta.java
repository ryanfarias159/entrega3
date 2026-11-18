package br.com.aeterna.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class CentroColeta {

    private UUID id;
    private String nome;
    private String endereco;
    private String localizacao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public CentroColeta() {
    }

    public CentroColeta(UUID id, String nome, String endereco, String localizacao, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.localizacao = localizacao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
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
        return "CentroColeta [id=" + id + ", nome=" + nome + ", endereco=" + endereco + ", localizacao=" + localizacao + "]";
    }
}