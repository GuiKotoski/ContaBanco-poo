package br.com.banco.service;

import br.com.banco.domain.Banco;
import br.com.banco.domain.Cliente;
import br.com.banco.domain.Conta;
import br.com.banco.domain.ContaCorrente;
import br.com.banco.domain.ContaPoupanca;

// Service: regras de alto nível para gerenciar contas no banco.
// Não guarda saldo, não faz “cálculo bancário” (isso é da Conta).
// Ele cria, busca e lista contas.
public class BancoService {

    private final Banco banco;

    public BancoService(Banco banco) {
        this.banco = banco;
    }

    // Cria conta (1=Corrente, 2=Poupança) e adiciona ao banco.
    public Conta criarConta(String nomeCliente, int tipo) {
        Cliente cliente = new Cliente(nomeCliente);

        Conta conta;
        if (tipo == 1) {
            conta = new ContaCorrente(cliente);
        } else if (tipo == 2) {
            conta = new ContaPoupanca(cliente);
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido (use 1 ou 2).");
        }

        banco.adicionarConta(conta);
        return conta;
    }

    // Busca conta por número usando getter (encapsulamento).
    public Conta buscarContaPorNumero(int numero) {
        return banco.getContas().stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + numero));
    }

    public void listarContas() {

        // Se não tem contas, informa e termina
        if (banco.getContas().isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        System.out.println("=== Contas do banco: " + banco.getNome() + " ===");

        // Percorre todas as contas cadastradas
        for (Conta c : banco.getContas()) {

            // Descobre o tipo da conta pelo tipo real do objeto
            String tipo;
            if (c instanceof ContaCorrente) {
                tipo = "Corrente";
            } else if (c instanceof ContaPoupanca) {
                tipo = "Poupança";
            } else {
                // fallback (caso você crie novos tipos no futuro)
                tipo = "Conta";
            }

            // Imprime informações principais
            System.out.println(
                    "Tipo: " + tipo
                            + " | Número: " + c.getNumero()
                            + " | Titular: " + c.getCliente().getNome()
                            + " | Saldo: R$ " + String.format("%.2f", c.getSaldo())
            );
        }
    }
}