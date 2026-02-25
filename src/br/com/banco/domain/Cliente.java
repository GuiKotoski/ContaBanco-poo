package br.com.banco.domain;

// Classe de domínio: representa um cliente do banco
public class Cliente {

    private final String nome;

    // Construtor com validação de regra de negócio
    public Cliente(String nome) {

        // trim() remove espaços antes e depois
        // isBlank() verifica se está vazio ou só com espaços
        if (nome == null || nome.trim().isBlank()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio.");
        }

        // Salva o nome já tratado
        this.nome = nome.trim();
    }

    public String getNome() {
        return nome;
    }
}