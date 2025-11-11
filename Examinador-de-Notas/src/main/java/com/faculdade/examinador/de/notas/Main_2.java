package com.faculdade.examinador.de.notas;

import java.util.Scanner;

public class Main_2 {
     public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("--- Gerador de Resultados ---");

        System.out.print("Informe o arquivo da disciplina: ");
        String arquivoAlunos = sc.nextLine();

        System.out.print("Informe o arquivo de gabarito: ");
        String arquivoGabarito = sc.nextLine();

        ResultadoDisciplina.gerarResultado(arquivoAlunos, arquivoGabarito);

        sc.close();
    }
}