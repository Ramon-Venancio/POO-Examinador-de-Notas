package com.faculdade.examinador.de.notas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main_2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("--- Gerador Automático de Resultados ---");

        /*
         * 1. LISTAR AS DISCIPLINAS AUTOMATICAMENTE
         * 
         * Procura na pasta /arquivos todos os arquivos .txt que NÃO
         * sejam gabaritos
         */
        List<String> disciplinas = listarDisciplinas("arquivos/");

        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina encontrada na pasta.");
            sc.close();
            return;
        }

         /*
         * 2. EXIBIR AS DISCIPLINAS PARA O USUÁRIO ESCOLHER
         */
        System.out.println("\nDisciplinas encontradas:");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + " - " + disciplinas.get(i));
        }

        System.out.print("\nEscolha a disciplina: ");
        int opcao = sc.nextInt();
        sc.nextLine(); // limpa buffer

        // Validação: impede opções inválidas
        if (opcao < 1 || opcao > disciplinas.size()) {
            System.out.println("Opção inválida!");
            sc.close();
            return;
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
        
        String arquivoAlunos = "arquivos/" + disciplina + ".txt";
        String arquivoGabarito = "arquivos/gabarito_" + disciplina + ".txt";

        /*
         * 4. VERIFICAR SE O GABARITO EXISTE
         * 
         * Se o arquivo gabarito_historia.txt não existir, exibimos mensagem
         * de erro e encerramos.
         */
        
        File gabaritoFile = new File(arquivoGabarito);

        if (!gabaritoFile.exists()) {
            System.out.println("\nERRO: Gabarito não encontrado!");
            System.out.println("Esperado em: " + arquivoGabarito);
            sc.close();
            return;
        }

        /*
         * 5. PROCESSAR OS ARQUIVOS E GERAR O RESULTADO
         */
        
        ResultadoDisciplina.gerarResultado(arquivoAlunos, arquivoGabarito);

        sc.close();
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
