package br.com.sistemagestao.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Usuario {

    private UUID uuid;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime dataCadastro;

    public Usuario() {
    }

    public Usuario(UUID uuid, String nome, String email, String senha, LocalDateTime dataCadastro) {
        this.uuid = uuid;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
    }

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}