package com.faculdade.examinador.de.notas;

import java.util.*;
import java.io.*;

public class ExaminadorDeNotas {
    public static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        menu();
    }
    
    public static void menu() {
        while(true) {
            System.out.println("O que você quer fazer?");
            System.out.println("1 - Criar/Adicionar alunos a uma disciplina");
            System.out.println("2 - Gerar resultados de uma disciplina");
            System.out.println("3 - Sair");
            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();
                switch (opcao) {
                    case 1:
                        adicionarAluno();
                        break;
                    case 2:
                        processarResultados();
                        break;
                    case 3:
                        System.out.println("Saindo do programa...");
                        scanner.close(); // Aqui sim, pode fechar o scanner
                        return;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Erro: Por favor, digite apenas um número.");
                scanner.nextLine();
            }
        }
    }
    
    public static void adicionarAluno() throws IOException {
        boolean decicaoDisciplina = true;
        String nomeDisciplina = "";
        while (true) {
            if (decicaoDisciplina) {
                System.out.println("A qual disciplina você quer adicionar um aluno(a)? (Se você adicionar um aluno a uma disciplina que não exista, ela será criada)");
                nomeDisciplina = scanner.nextLine().toLowerCase();
            }

            System.out.println("Qual é o nome do(a) aluno(a)?");
            String nomeAluno = scanner.nextLine().toUpperCase();

            System.out.println("Agora escreva o conjunto de respostas do(a) aluno(a) " + nomeAluno + " (Exemplo: VFVVFFVFFV)");
            String respostas;
            
            while (true) {                
                respostas = scanner.nextLine().toUpperCase();

                if (respostas.length() != 10) {
                    System.out.println("Digiete uma resposta com 10 caracteres.");
                } else {
                    break;
                }
            }

            File diretorio = new File("Respostas");

            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            File arquivo = new File(diretorio, nomeDisciplina + ".txt");
            
            try (FileWriter fw = new FileWriter(arquivo, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw))
            {
                out.println(respostas + "\t" + nomeAluno);
            } catch (IOException e) {
                System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
            }
            
            System.out.println("Você deseja adicionar outra resposta? (s - sim ou n - não)");
            String opcao = scanner.nextLine().toLowerCase();
            
            if (opcao.equals("n")) {
                break;
            }
            
            System.out.println("Você deseja continuar na mesma disciplina? (s - sim ou n - não)");
            opcao = scanner.nextLine().toLowerCase();
            
            if (opcao.equals("s")) {
                decicaoDisciplina = false;
            } else {
                decicaoDisciplina = true;
            }
        }
    }
    
    public static void processarResultados() {
        System.out.println("--- Gerador Automático de Resultados ---");

        /*
         * 1. LISTAR AS DISCIPLINAS AUTOMATICAMENTE
         * 
         * Procura na pasta /arquivos todos os arquivos .txt que NÃO
         * sejam gabaritos
         */
        List<String> disciplinas = listarDisciplinas("Respostas");

        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina encontrada na pasta.");
            return;
        }

         /*
         * 2. EXIBIR AS DISCIPLINAS PARA O USUÁRIO ESCOLHER
         */
        System.out.println("\nDisciplinas encontradas:");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + " - " + disciplinas.get(i));
        }
        int opcao;
        
        while (true) {
            System.out.print("\nEscolha a disciplina: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpa buffer

            // Validação: impede opções inválidas
            if (opcao < 1 || opcao > disciplinas.size()) {
                System.out.println("Opção inválida!");
            } else {
                break;
            }
        }

        // Recupera o nome da disciplina escolhida
        String disciplina = disciplinas.get(opcao - 1);

        /*
         * 3. MONTAR AUTOMATICAMENTE OS CAMINHOS DOS ARQUIVOS
         * 
         * Se o usuário escolher "historia", então:
         * 
         * arquivoAlunos   → arquivos/historia.txt
         * arquivoGabarito → arquivos/gabarito_historia.txt
         * 
         * O programa monta esses caminhos automaticamente.
         */
        File diretorioRespostas = new File("Respostas");
        
        if (!diretorioRespostas.exists()) {
            System.out.println("Diretorio 'Respostas' não criado! Crie um arquivo de repostas na opção 1 do menu.");
            return;
        }
        
        File arquivoAlunos = new File(diretorioRespostas, disciplina + ".txt");
        
        if (!arquivoAlunos.exists()) {
            System.out.println("arquivo " + disciplina + ".txt não encontrado! Crie um arquivo de repostas na opção 1 do menu.");

        }
        
        if (!diretorioRespostas.exists()) {
            System.out.println("Diretorio 'Respostas' não criado! Crie um arquivo de repostas na opção 1 do menu.");
            return;
        }
        
        File diretorioGabaritos = new File("Gabaritos");
        
        if (!diretorioGabaritos.exists()) {
            System.out.println("Diretorio 'Gabaritos' não criado! Crie um arquivo de repostas na opção 1 do menu.");
            return;
        }
        
        File arquivoGabarito = new File(diretorioGabaritos, disciplina + ".txt");

        if (!arquivoGabarito.exists()) {
            System.out.println("\nERRO: Gabarito não encontrado!");
            System.out.println("Esperado em: " + arquivoGabarito);
            return;
        }

        /*
         * 5. PROCESSAR OS ARQUIVOS E GERAR O RESULTADO
         */
        
        ResultadoDisciplina.gerarResultado(arquivoAlunos, arquivoGabarito, disciplina);

    }
    
    public static List<String> listarDisciplinas(String caminhoPasta) {

        File pasta = new File(caminhoPasta);
        File[] arquivos = pasta.listFiles();

        List<String> disciplinas = new ArrayList<>();

        if (arquivos == null) return disciplinas;

        for (File arq : arquivos) {

            String nome = arq.getName();

            // Verifica se o arquivo é uma disciplina comum
            if (nome.endsWith(".txt") && !nome.startsWith("gabarito_")) {
                disciplinas.add(nome.replace(".txt", "")); // remove extensão
            }
        }

        return disciplinas;
    }
}