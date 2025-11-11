package com.faculdade.examinador.de.notas;

import java.util.Scanner;
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
                        
                        break;
                    default:
                        throw new AssertionError();
                }
            } catch (Exception e) {
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
            String respostas = scanner.nextLine().toUpperCase().substring(0, 10);

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
            }
        }
    }
}