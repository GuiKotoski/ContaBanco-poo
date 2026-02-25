package br.com.banco.domain;

// ContaPoupanca também é uma CONTA real.
// Mesma base, comportamento comum, outro tipo.
public class ContaPoupanca extends Conta {

    public ContaPoupanca(Cliente cliente) {
        super(cliente);
    }

    @Override
    public void imprimirExtrato() {
        System.out.println("=== Extrato Conta Poupança ===");
        imprimirDados();
    }
}