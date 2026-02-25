package br.com.banco.domain;

import java.util.ArrayList;
import java.util.List;

// Classe Banco: representa o "banco" e guarda as contas cadastradas
public class Banco {

    // Nome do banco (ex: "ContaBanco-poo")
    private final String nome;

    // Lista interna de contas (simula um banco de dados em memória)
    private final List<Conta> contas = new ArrayList<>();

    // Construtor: cria o banco com um nome
    public Banco(String nome) {
        this.nome = nome;
    }

    // Adiciona uma conta na lista
    public void adicionarConta(Conta conta) {
        contas.add(conta);
    }

    // Retorna uma cópia imutável da lista (boa prática: não expor a lista real)
    public List<Conta> getContas() {
        return List.copyOf(contas);
    }

    // Retorna o nome do banco
    public String getNome() {
        return nome;
    }
}