package br.com.banco.domain;

// ContaCorrente é uma CONTA real.
// Ela herda tudo da classe Conta.
public class ContaCorrente extends Conta {

    // Construtor recebe um Cliente
    // e repassa para o construtor da classe mãe (Conta)
    public ContaCorrente(Cliente cliente) {
        super(cliente);
    }

    // Implementação específica do extrato
    // Cada tipo de conta imprime o extrato do seu jeito
    @Override
    public void imprimirExtrato() {
        System.out.println("=== Extrato Conta Corrente ===");
        imprimirDados(); // método herdado da classe Conta
    }
}