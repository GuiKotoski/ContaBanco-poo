package br.com.banco.service;

// Interface: define um CONTRATO (o que uma conta deve saber fazer).
// Interface não guarda estado (não tem saldo, não tem cliente).
public interface IOperacoesConta {

    // Depósito: aumenta saldo
    void depositar(double valor);

    // Saque: diminui saldo (se tiver saldo suficiente)
    void sacar(double valor);

    // Transferência: tira de uma conta e coloca em outra
    // Repare: o destino é do tipo IOperacoesConta (contrato), não uma classe concreta
    void transferir(double valor, IOperacoesConta destino);

    // Extrato: mostra informações da conta
    void imprimirExtrato();
}