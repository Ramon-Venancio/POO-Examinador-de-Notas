package com.faculdade.examinador.de.notas;


public class Aluno {
    
    private String nome;
    private String respostas;
    private int pontos;

    public Aluno(String nome, String respostas, int pontos){
        this.nome = nome;
        this.respostas = respostas;
        this.pontos = pontos;
    }
       
       public String getNome(){
           return nome;
       }
       
       public String getRespostas(){
        return respostas;
       }
       
       public int getPontos(){
           return pontos;
       }
       
       @Override
       public String toString(){
        return nome + " | " + respostas + " | NOTA: " + pontos;
       }
}
