package com.faculdade.examinador.de.notas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResultadoDisciplina {

    public static void gerarResultado(String arquivoAlunos, String arquivoGabarito){
        try{
            //===== 1. Ler Gabarito =====
            // Abre o arquivo contendo o gabarito oficial da prova (ex: VFFVVFVFVV)
            BufferedReader brGabarito = new BufferedReader(new FileReader(arquivoGabarito));

            // Lê apenas a primeira linha (o gabarito)
            String gabarito = brGabarito.readLine().trim();
            brGabarito.close();
            
            //===== 2. Ler alunos =====
            // Abre o arquivo contendo os alunos + respostas
            BufferedReader brAlunos = new BufferedReader(new FileReader(arquivoAlunos));
            List<Aluno> alunos = new ArrayList<>();
            
            // Lê a primeira linha
            String linha = brAlunos.readLine();

            // Loop para ler todos os alunos até o fim do arquivo
            while (linha != null && !linha.isEmpty()) {

                // Primeiros 10 caracteres são as respostas (ex: VFFVVFVVFF)
                String respostas = linha.substring(0, 10).trim();

                // O restante da linha é o nome do aluno (pode ter espaços)
                String nome = linha.substring(10).trim();

                // Compara respostas do aluno com o gabarito e retorna pontuação
                int pontos = compararRespostas(gabarito, respostas);

                // Adiciona o aluno na lista
                alunos.add(new Aluno(nome, pontos));

                // lê a próxima linha
                linha = brAlunos.readLine();
            }
            brAlunos.close();
            
            //===== 3. Calcular Média =====
            // Usa stream para somar as notas e calcular a média automaticamente
            double media = alunos.stream()
                                 .mapToInt(a -> a.getPontos())   // transforma cada aluno em sua nota
                                 .average()                      // calcula a média
                                 .orElse(0.0);                   // caso lista esteja vazia, média = 0

            /*
            Basicamente esse programa de cima faz o seguinte:
            
            int soma = 0;
            
            for(Alunos a : alunos){
                soma += a.getPontos(); <-- mapToInt(a -> a.getPontos())
            }
            
            double media;
            
            if(alunos.size() > 1){
                media = (double) soma / alunos.size(); <------ "average()" faz o calculo automaticamente
            } else {
                media = 0.0; <--- orElse(0.0)
            }          
            */
            
            // ==== Ordenar alfabeticamente ====
            // Cria uma cópia da lista original para ordenar apenas por nome
            List<Aluno> listaAlfabetica = new ArrayList<>(alunos);
            listaAlfabetica.sort(Comparator.comparing(Aluno::getNome));
            
            // ==== Ordenar por nota ====
            // Cria outra cópia e ordena pela maior nota
            List<Aluno> listaPorNota = new ArrayList<>(alunos);
            listaPorNota.sort((a,b) -> Integer.compare(b.getPontos(), a.getPontos()));
            
            // ==== Gerar arquivos ====
            // Grava os resultados em dois arquivos diferentes
            gravar("resultado_alfabetico.txt", listaAlfabetica, media);
            gravar("resultado_por_nota.txt", listaPorNota, media);
            
            // ==== Mostrar dados na tela ====
            System.out.println("\n==== Ordenado por nome ====");
            listaAlfabetica.forEach(System.out::println);
            
            System.out.printf("\nMédia da turma: %.2f\n", media);
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    // ==== Compara as respostas de um aluno com o gabarito ====
    public static int compararRespostas(String gabarito, String respostas) {

        int acertos = 0;
        
        // Percorre cada uma das 10 questões
        for(int i = 0; i < gabarito.length(); i++) {
            if(gabarito.charAt(i) == respostas.charAt(i)){
                acertos++;
            }
        }
        
        // Caso o aluno marcar tudo igual (só V ou só F), zera a nota
        if (respostas.chars().allMatch(c -> c == 'V') || 
            respostas.chars().allMatch(c -> c == 'F')) {
            acertos = 0;
        }

        return acertos;
    }
    
    // === Escreve lista de alunos em um arquivo ===
    private static void gravar(String nomeArquivo, List<Aluno> alunos, double media) throws Exception {

        // PrintWriter escreve texto no arquivo
        PrintWriter pw = new PrintWriter(new FileWriter(nomeArquivo));

        // Escreve cada aluno usando o toString() da classe Aluno
        for (Aluno aluno : alunos) {
            pw.println(aluno);
        }

        // Escreve a média no final
        pw.printf("\nMédia da turma: %.2f", media);

        pw.close();
    }
}
