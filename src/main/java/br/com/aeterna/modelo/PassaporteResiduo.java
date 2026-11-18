package br.com.aeterna.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PassaporteResiduo {

    private UUID id;
    private String codigoRastreio;
    private BigDecimal volume;
    private StatusConformidade statusConformidade;
    private BigDecimal taxaDesvioAterro;
    private UnidadeOperacional unidadeOrigem;
    private UnidadeOperacional unidadeDestino;
    private TipoResiduo tipoResiduo;
    private String metadadosAuditoria;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public PassaporteResiduo() {
    }

    public PassaporteResiduo(UUID id, String codigoRastreio, BigDecimal volume, StatusConformidade statusConformidade,
                             BigDecimal taxaDesvioAterro, UnidadeOperacional unidadeOrigem, UnidadeOperacional unidadeDestino,
                             TipoResiduo tipoResiduo, String metadadosAuditoria, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.codigoRastreio = codigoRastreio;
        this.volume = volume;
        this.statusConformidade = statusConformidade;
        this.taxaDesvioAterro = taxaDesvioAterro;
        this.unidadeOrigem = unidadeOrigem;
        this.unidadeDestino = unidadeDestino;
        this.tipoResiduo = tipoResiduo;
        this.metadadosAuditoria = metadadosAuditoria;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public StatusConformidade getStatusConformidade() {
        return statusConformidade;
    }

    public void setStatusConformidade(StatusConformidade statusConformidade) {
        this.statusConformidade = statusConformidade;
    }

    public BigDecimal getTaxaDesvioAterro() {
        return taxaDesvioAterro;
    }

    public void setTaxaDesvioAterro(BigDecimal taxaDesvioAterro) {
        this.taxaDesvioAterro = taxaDesvioAterro;
    }

    public UnidadeOperacional getUnidadeOrigem() {
        return unidadeOrigem;
    }

    public void setUnidadeOrigem(UnidadeOperacional unidadeOrigem) {
        this.unidadeOrigem = unidadeOrigem;
    }

    public UnidadeOperacional getUnidadeDestino() {
        return unidadeDestino;
    }

    public void setUnidadeDestino(UnidadeOperacional unidadeDestino) {
        this.unidadeDestino = unidadeDestino;
    }

    public TipoResiduo getTipoResiduo() {
        return tipoResiduo;
    }

    public void setTipoResiduo(TipoResiduo tipoResiduo) {
        this.tipoResiduo = tipoResiduo;
    }

    public String getMetadadosAuditoria() {
        return metadadosAuditoria;
    }

    public void setMetadadosAuditoria(String metadadosAuditoria) {
        this.metadadosAuditoria = metadadosAuditoria;
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
        return "PassaporteResiduo [codigoRastreio=" + codigoRastreio + ", volume=" + volume + ", status=" + statusConformidade
                + ", taxaDesvioAterro=" + taxaDesvioAterro + "%]";
    }
}