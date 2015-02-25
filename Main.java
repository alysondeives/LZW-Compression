/**
  * ------------------------------------------------------------------Documentacao preliminar
  * Pontificia Universidade Catolica de Minas Gerais 
  * Curso de Ciencia da Computacao 
  * Algoritmos e Estruturas de Dados III
  * 
  * Trabalho Pratico 04 - Compressão LZW
  *
  * Objetivo:
  * Aplicar os conceitos da compactação LZW para comprimir arquivos de imagens do formato PGM.  
  * 
  * @author Alyson Deives Pereira  	Matricula: 416589
  * @author Paulo Amorim				Matricula: 412765
  * @version 1.0 21/10/2011            
*/
   //Lista de dependencias
   import java.io.*;
   import java.util.*;

   public class Main{
   
   /**
   * Metodo Principal
   * Realiza a compactacao e descompactacao de arquivos de imagens no formato .pgm, utilizando o algoritmo de compactacao LZW
   */
      public static void main (String [] args){
         try{
            int escolha = 0;
            String arquivo;
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.println("Pontificia Universidade Catolica de Minas Gerais"); 
            System.out.println("Curso de Ciencia da Computacao");
            System.out.println("Algoritmos e Estruturas de Dados III\n");
            System.out.println("Trabalho Pratico 04 - Compactacao de imagens .pgm utilizando o algoritmo LZW");
            System.out.println("Aluno: Alyson Deives Pereira	Matricula: 416589");
            System.out.println("Aluno: Paulo Amorim			 	Matricula: 412765\n");
            System.out.println("Programa para comprimir arquivos de imagem no formato .pgm");
         	
         	//Repete enquanto a opcao de sair nao for escolhida
            while( escolha!= 3){
            
               System.out.println("O que deseja fazer?");
               System.out.println("1 - Compactar imagem");
               System.out.println("2 - Descompactar imagem");
               System.out.println("3 - Sair\n");
            
               escolha = Integer.parseInt(teclado.readLine());
               switch (escolha){
                  case 1:
                     System.out.println("Digite o nome do arquivo a ser compactado, sem a extensão .pgm:");
                     arquivo = teclado.readLine();
                     LZW.compactar(arquivo);
                     break;
                  case 2:
                     System.out.println("Digite o nome do arquivo a ser descompactado, sem a extensão .lzw:");
                     arquivo = teclado.readLine();
                     LZW.descompactar(arquivo);
                  case 3:
                     System.out.println("Encerrando programa...");
                     break;
                  default:
                     System.out.println("Opcao errada, encerrando programa...");
               }
            }
         }
         
            catch(Exception e){
               e.printStackTrace();	//Exibe mensagens de erro
            }
      }
   
   }