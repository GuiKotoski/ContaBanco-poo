package br.com.banco.app;

import br.com.banco.domain.Banco;
import br.com.banco.domain.Conta;
import br.com.banco.service.BancoService;

import java.util.Scanner;

// Main: camada de interface (UI) no console.
// Aqui só: lê dados do usuário e chama o BancoService/Conta.
public class Main {

    public static void main(String[] args) {

        // Scanner para ler o que o usuário digitar no console
        Scanner sc = new Scanner(System.in);

        // Cria o banco (guarda contas em memória)
        Banco banco = new Banco("ContaBanco-poo");

        // Service que gerencia contas dentro do banco
        BancoService service = new BancoService(banco);

        int opcao;

        // Loop principal do sistema
        do {
            System.out.println("\n=== ContaBanco-poo ===");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Listar contas");
            System.out.println("3 - Depositar");
            System.out.println("4 - Sacar");
            System.out.println("5 - Transferir");
            System.out.println("6 - Extrato");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            // Lê a opção do usuário
            opcao = lerInt(sc);

            // Try/catch evita que o programa “morra” por erro de regra (saldo insuficiente, etc)
            try {
                switch (opcao) {

                    case 1 -> criarConta(sc, service);

                    case 2 -> service.listarContas();

                    case 3 -> depositar(sc, service);

                    case 4 -> sacar(sc, service);

                    case 5 -> transferir(sc, service);

                    case 6 -> extrato(sc, service);

                    case 0 -> System.out.println("Encerrando...");

                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                // Mostra a mensagem do erro (ex: "Saldo insuficiente")
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);

        // Fecha o Scanner (boa prática)
        sc.close();
    }

    // ======== FUNÇÕES DO MENU (para não lotar o main) ========

    private static void criarConta(Scanner sc, BancoService service) {

        // Garante que não vamos “pular” a leitura do nome por causa de leitura anterior
        // (se você estiver usando lerInt/lerDouble, isso normalmente já está ok)
        // sc.nextLine(); // deixe comentado; use só se você notar pulo de leitura

        String nome;

        while (true) {
            System.out.print("Nome do cliente: ");
            nome = sc.nextLine();

            // Validação simples no fluxo (melhora UX):
            // evita chamar o service só para descobrir que é vazio
            if (nome == null || nome.trim().isBlank()) {
                System.out.println("Erro: nome não pode ser vazio.");
                continue;
            }

            // Normaliza o nome (remove espaços extras nas bordas)
            nome = nome.trim();
            break;
        }

        int tipo;

        // Loop: pede o tipo até o usuário digitar 1 ou 2
        while (true) {
            System.out.println("Tipo de conta:");
            System.out.println("1 - Corrente");
            System.out.println("2 - Poupança");
            System.out.print("Escolha: ");

            tipo = lerInt(sc);

            if (tipo == 1 || tipo == 2) {
                break;
            }
            System.out.println("Erro: escolha 1 ou 2.");
        }

        // Agora sim: tenta criar conta.
        // Se o nome for inválido, o Cliente vai lançar IllegalArgumentException.
        try {
            Conta conta = service.criarConta(nome, tipo);
            System.out.println("Conta criada com sucesso!");
            System.out.println("Número da conta: " + conta.getNumero());
        } catch (Exception e) {
            // Se der erro (nome vazio, tipo inválido, etc), informa e não derruba o programa
            System.out.println("Erro ao criar conta: " + e.getMessage());
        }
    }

    private static void depositar(Scanner sc, BancoService service) {

        System.out.print("Número da conta: ");
        int numero = lerInt(sc);

        // Busca conta existente (se não existir, lança erro e o menu trata)
        Conta conta = service.buscarContaPorNumero(numero);

        double valor;

        // Loop: pede valor até ser > 0
        while (true) {
            System.out.print("Valor do depósito: ");
            valor = lerDouble(sc);

            if (valor <= 0) {
                System.out.println("Erro: valor deve ser maior que zero.");
                continue;
            }
            break;
        }

        // Regra final continua na Conta (domínio)
        conta.depositar(valor);

        System.out.println("Depósito realizado.");
    }

    private static void sacar(Scanner sc, BancoService service) {

        System.out.print("Número da conta: ");
        int numero = lerInt(sc);

        Conta conta = service.buscarContaPorNumero(numero);

        double valor;

        // Loop: pede valor até ser > 0
        while (true) {
            System.out.print("Valor do saque: ");
            valor = lerDouble(sc);

            if (valor <= 0) {
                System.out.println("Erro: valor deve ser maior que zero.");
                continue;
            }
            break;
        }

        // Pode lançar "Saldo insuficiente" — e o try/catch do menu vai mostrar a mensagem
        conta.sacar(valor);

        System.out.println("Saque realizado.");
    }

    private static void transferir(Scanner sc, BancoService service) {

        System.out.print("Conta ORIGEM (número): ");
        int origemNum = lerInt(sc);

        System.out.print("Conta DESTINO (número): ");
        int destinoNum = lerInt(sc);

        // Regra de fluxo: não faz sentido transferir para a mesma conta
        if (origemNum == destinoNum) {
            System.out.println("Erro: conta origem e destino não podem ser a mesma.");
            return;
        }

        // Busca as contas existentes no banco
        // Se não existir, o service lança erro e o try/catch do menu mostra a mensagem
        Conta origem = service.buscarContaPorNumero(origemNum);
        Conta destino = service.buscarContaPorNumero(destinoNum);

        double valor;

        // Loop: pede valor até ser > 0
        while (true) {
            System.out.print("Valor da transferência: ");
            valor = lerDouble(sc);

            if (valor <= 0) {
                System.out.println("Erro: valor deve ser maior que zero.");
                continue;
            }
            break;
        }

        // Regra da transferência está na Conta:
        // saca da origem e deposita no destino
        // Pode lançar "Saldo insuficiente" e o menu vai tratar
        origem.transferir(valor, destino);

        System.out.println("Transferência realizada.");
    }

    private static void extrato(Scanner sc, BancoService service) {

        System.out.print("Número da conta: ");
        int numero = lerInt(sc);

        Conta conta = service.buscarContaPorNumero(numero);

        // Polimorfismo: imprime extrato conforme o tipo real (corrente/poupança)
        conta.imprimirExtrato();
    }

    // ======== LEITORES “SEGUROS” (evitam crash por digitação errada) ========

    private static int lerInt(Scanner sc) {
        while (true) {
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }

    private static double lerDouble(Scanner sc) {
        while (true) {
            String entrada = sc.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido (ex: 10 ou 10,5): ");
            }
        }
    }
}