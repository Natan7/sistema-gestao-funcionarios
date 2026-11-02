package br.com.sistemagestao.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Funcionario {

    private UUID uuid;
    private String nome;
    private LocalDate dataAdmissao;
    private BigDecimal salario;
    private boolean status;

    public Funcionario() {
    }

    public Funcionario(UUID uuid, String nome, LocalDate dataAdmissao, BigDecimal salario, boolean status) {
        this.uuid = uuid;
        this.nome = nome;
        this.dataAdmissao = dataAdmissao;
        this.salario = salario;
        this.status = status;
    }

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}