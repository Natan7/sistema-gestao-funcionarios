package br.com.sistemagestao.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario {

    private int id;
    private String nome;
    private LocalDate dataAdmissao;
    private BigDecimal salario;
    private boolean status;

    public Funcionario() {
    }

    public Funcionario(int id, String nome, LocalDate dataAdmissao, BigDecimal salario, boolean status) {
        this.id = id;
        this.nome = nome;
        this.dataAdmissao = dataAdmissao;
        this.salario = salario;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}