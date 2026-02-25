package br.com.banco.domain;

import br.com.banco.service.IOperacoesConta;

import br.com.banco.service.IOperacoesConta;

// Classe abstrata: serve de base para ContaCorrente e ContaPoupanca.
// Ela implementa o contrato IOperacoesConta (depositar, sacar, transferir, extrato).
public abstract class Conta implements IOperacoesConta {

    // Sequencial de contas: cada conta nova recebe um número diferente.
    private static int SEQUENCIAL = 1;

    // protected: filhas conseguem usar, mas o ideal é acessar por getters fora do domínio.
    protected int agencia;
    protected int numero;
    protected double saldo;
    protected Cliente cliente;

    // Construtor: cria a conta com agência 1, número sequencial e saldo 0.
    protected Conta(Cliente cliente) {
        this.agencia = 1;
        this.numero = SEQUENCIAL++;
        this.cliente = cliente;
        this.saldo = 0.0;
    }

    // Getter do número: é isso que o service deve usar.
    public int getNumero() {
        return numero;
    }

    // Getter do saldo: o service pode mostrar saldo sem “mexer” direto.
    public double getSaldo() {
        return saldo;
    }

    // Getter do cliente: o service pega o nome do titular por aqui.
    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor inválido");
        }
        saldo += valor;
    }

    @Override
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor inválido");
        }
        if (valor > saldo) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        saldo -= valor;
    }

    @Override
    public void transferir(double valor, IOperacoesConta destino) {
        this.sacar(valor);
        destino.depositar(valor);
    }

    // Método comum para extrato (usado pelas filhas).
    protected void imprimirDados() {
        System.out.println("Titular: " + cliente.getNome());
        System.out.println("Agência: " + agencia);
        System.out.println("Número: " + numero);
        System.out.printf("Saldo: R$ %.2f%n", saldo);
    }

    // Cada conta concreta define o título do extrato.
    @Override
    public abstract void imprimirExtrato();
}