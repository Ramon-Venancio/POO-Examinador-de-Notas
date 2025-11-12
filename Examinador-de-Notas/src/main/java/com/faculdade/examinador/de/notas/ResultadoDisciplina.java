package com.faculdade.examinador.de.notas;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResultadoDisciplina {

    public static int compararRespostas(String gabarito, String respostas) {

        int acertos = 0;

        for (int i = 0; i < gabarito.length(); i++) {

            if (gabarito.charAt(i) == respostas.charAt(i)) {
                acertos++;
            }
        }

        // Evita fraude marcando tudo igual
        if (respostas.chars().allMatch(c -> c == 'V') ||
            respostas.chars().allMatch(c -> c == 'F')) {

            acertos = 0;
        }

        return acertos;
    }
    
    public static void gerarResultado(File arquivoAlunos, File arquivoGabarito, String disciplina){
        try (BufferedReader brGabarito = new BufferedReader(new FileReader(arquivoGabarito));
             BufferedReader brAlunos = new BufferedReader(new FileReader(arquivoAlunos));)
        {

            /*
             * 1. LER GABARITO
             * 
             * Lê apenas a primeira linha do arquivo de gabarito com as respostas
             */
            
            
            String gabarito = brGabarito.readLine().trim();
            
            System.out.println("\nGABARITO: " + gabarito + "\n");

            /*
             * 2. LER ARQUIVO DE ALUNOS + RESPOSTAS
             * 
             * Cada linha contém:
             *   - primeiros 10 caracteres → respostas (ex: VVFVVFVFVV)
             *   - restante da linha → nome do aluno
             */
            
            
            List<Aluno> alunos = new ArrayList<>();

            String linha;

            // Loop até acabar o arquivo
            while ((linha = brAlunos.readLine()) != null) {

                if (linha.trim().isEmpty()) {
                    continue; // ignora linhas em branco
                }

                String[] partes = linha.split("\t"); 
                String respostas = "";
                String nome = "";
                
                if (partes.length == 2) {
                    respostas = partes[0];
                    nome = partes[1];
                }

                int pontos = compararRespostas(gabarito, respostas);

                // Criamos um objeto Aluno contendo: nome | respostas | nota
                alunos.add(new Aluno(nome, respostas, pontos));
            }

            /*
             * 3. CALCULAR MÉDIA DO DESEMPENHO
             * 
             * Usamos Stream para somar notas e obter a média.
             */
            
            double media = alunos.stream()
                                 .mapToInt(Aluno::getPontos)
                                 .average()
                                 .orElse(0.0);

            /*
             * 4. CRIAR LISTAS ORDENADAS
             * 
             * - lista alfabética
             * - lista por nota (maior para menor)
             */
            
            List<Aluno> listaAlfabetica = new ArrayList<>(alunos);
            listaAlfabetica.sort(Comparator.comparing(Aluno::getNome));

            List<Aluno> listaPorNota = new ArrayList<>(alunos);
            listaPorNota.sort((a, b) -> Integer.compare(b.getPontos(), a.getPontos()));

            /*
             * 5. GRAVAR ARQUIVOS DE RESULTADOS
             * 
             * Cada arquivo mostra:
             *   GABARITO
             *   LISTA DE ALUNOS (nome + respostas + nota)
             *   MÉDIA FINAL
             */
            File diretorioResultados = new File("Resultados");
            
            if (!diretorioResultados.exists()) {
                diretorioResultados.mkdirs();
            }
            
            File diretorioDisciplina  = new File(diretorioResultados, disciplina);
            
            if (!diretorioDisciplina.exists()) {
                diretorioDisciplina.mkdirs();
            }
            
            gravar(diretorioDisciplina, "resultado_alfabetico.txt", listaAlfabetica, gabarito, media);
            gravar(diretorioDisciplina, "resultado_por_nota.txt", listaPorNota, gabarito, media);

            /*
             * 6. MOSTRAR RESULTADOS NO TERMINAL
             */
     
            System.out.println("==== RESULTADO ====");
            for (Aluno a : listaAlfabetica) {
                System.out.println(a);
            }

            System.out.printf("\nMÉDIA DA TURMA: %.2f\n", media);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /*
     * COMPARAR RESPOSTAS DO ALUNO COM O GABARITO
     * 
     * Cada posição correta soma +1 ponto.
     * Porém:
     * - se todas as respostas forem V, nota = 0
     * - se todas forem F, nota = 0
     */
    
    

    /*
     * GRAVAR RESULTADO EM ARQUIVO .TXT
     * 
     * Salva:
     *   - gabarito
     *   - nome | respostas | nota
     *   - média final
     */
    
    private static void gravar(File diretorio, String nomeArquivo, List<Aluno> alunos, String gabarito, double media) throws Exception {
        File arquivo = new File(diretorio, nomeArquivo);
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo));) {
            pw.println("GABARITO: " + gabarito + "\n");
            
            for (Aluno aluno : alunos) {
                pw.println(aluno);
            }
            if (nomeArquivo.equals("resultado_por_nota.txt")) {
                pw.printf("\nMÉDIA DA TURMA: %.2f", media);
            }
        } catch (IOException e) {
                System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }        
    }
}
