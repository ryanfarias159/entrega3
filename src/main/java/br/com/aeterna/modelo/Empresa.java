package br.com.aeterna.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Empresa {

    private UUID id;
    private String nome;
    private String cnpjCpf;
    private String email;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Empresa() {
    }

    public Empresa(UUID id, String nome, String cnpjCpf, String email, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.cnpjCpf = cnpjCpf;
        this.email = email;
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

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "Empresa [id=" + id + ", nome=" + nome + ", cnpjCpf=" + cnpjCpf + ", email=" + email + "]";
    }
}