/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.faculdade.examinador.de.notas;

import java.util.Scanner;

/**
 *
 * @author vinan
 */
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
    
    public static void adicionarAluno() {
        System.out.println("A qual disciplina você quer adicionar um aluno? (Se você adicionar um aluno a uma disciplina que não exista, ela será criada)");
        String disciplina = scanner.nextLine();
        scanner.nextLine();
        
        System.out.println("Qual é o nome do aluno?");
        String nomeAluno = scanner.nextLine();
        scanner.nextLine();
        
        System.out.println("Agora escreva o conjunto de notas do aluno " + nomeAluno);
    }
}
