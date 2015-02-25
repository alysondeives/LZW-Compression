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
   import java.util.*;
   import java.io.*;
   
   public class LZW{
   
      public static final int TAM_MAX_DICIONARIO = 65536;					//tamanho máximo do dicionario
      public static StringBuilder conteudo = new StringBuilder();			//Armazena o conteudo do arquivo
   
   /**
   * Metodo de Compactacao
   * Analisa o conteudo da string e baseado no algoritmo LZW realiza a compactação do arquivo de imagem em 
   * um arquivo de dados
   * @param arquivo 			Nome do arquivo de imagem, sem a extensão .pgm
   */
   
      public static void compactar(String arquivo) throws FileNotFoundException, IOException, NullPointerException{
         String linha;																				//Armazena uma linha do arquivo de imagem
         String prefixo = "";																		//Armazena o prefixo das strings ao se realizar a compactação
         String sufixo = "";																		//Armazena o caracter que é o sufixo da string lida ao se realizar a compactação
         short codigo = 0;																			//Codigo que indica a posicao das strings do  dicionario
         StringBuilder prefix = new StringBuilder("");									//Armazena os prefixos lidos durante a compactacao
         HashMap <String,Short> dicionarioBase = new HashMap <String,Short>();	//Tabela Hash que armazena o dicionario base (caracteres possíveis que geram o arquivo inteiro
         HashMap <String,Short> dicionario = new HashMap <String,Short>();   		//Tabela Hash que armazena o dicionario (tanto o base quanto o gerado durante a compactacao)
              
         //Stream de dados do arquivo compactado
         DataOutputStream out = new DataOutputStream(new FileOutputStream(arquivo+".lzw"));
         
         //Stream para leitura do arquivo de imagem
         BufferedReader leitor = new BufferedReader(new FileReader(arquivo+".pgm"));
      
         //Verifica se o arquivo é valido e armazena o conteudo do arquivo         
         linha = leitor.readLine();
         if(!linha.equals("P2")){
            System.out.println("Arquivo de imagem inválido!");
         }
         else{
         	System.out.println("\nIniciando compactacao...\n");
            while(leitor.ready()){
             //Verifica se a linha não indica comentario
               if(linha.charAt(0) != '#'){
                  conteudo.append(linha + "\n");
               }
               linha = leitor.readLine();   
            }
         
         //Fecha stream de leitura 			
            leitor.close();
         
         //Armazena todos os possíveis caracteres existentes no dicionario base
            for(int i = 0;i<255;i++){
               dicionarioBase.put(Character.toString((char)i),(short)i);
            }
         
         //insere o dicionario base no dicionario
            dicionario.putAll(dicionarioBase);
         
         //armazena o tamanho do dicionario na variavel codigo
            codigo = (short)dicionario.size();
         
         //realiza a compactacao
         
            for(int i = 0;i<conteudo.length();i++){   
               sufixo = ""+conteudo.charAt(i);
               String palavra = prefixo+sufixo;
            
            //Reseta o dicionario quando o tamanho máximo é atingido e insere o dicionario base nele
               if (dicionario.size() >= TAM_MAX_DICIONARIO) {
                  dicionario.clear();
                  dicionario.putAll(dicionarioBase);
                  codigo = (short)dicionarioBase.size();
               }
            
            //Se a palavra formada já existir no dicionario, adicionada o sufixo na stringbuilder prefix
               if(dicionario.containsKey(palavra)){
                  prefix.append(sufixo);
               }
               
               //senao, dados sao gravados no arquivo compactado
               else{
                  out.writeShort(dicionario.get(prefixo)); //Escreve o codigo do prefixo no arquivo de dados
                  out.writeByte((byte)sufixo.charAt(0));	  //Escreve o caracter do sufixo no arquivo de dados (como o caracter do java consome dois bytes, foi salvo como byte)
                  dicionario.put(palavra,codigo);			  //Insere no dicionario a nova palavra encontrada (prefixo+sufixo)
                  codigo++;										  //Incrementa a variavel codigo
                  prefix = new StringBuilder(sufixo);		  //A stringbuilder prefix recebe o sufixo
               }
               prefixo = prefix.toString();					  //O prefixo recebe a string armazena em prefix							
            }
            out.writeShort(dicionario.get(prefixo));			//Escreve o codigo do prefixo no arquivo de dados
            out.writeByte((byte)sufixo.charAt(0));          //Escreve o caracter do sufixo no arquivo de dados
            out.close();                                    //Fecha o stream de dados
            
            System.out.println("Arquivo "+arquivo+".pgm compactado com sucesso como "+arquivo+".lzw!\n");
         }
      }
      
   /**
   * Metodo de Descompactacao
   * Analisa o arquivo de dados compactado e baseado no algoritmo LZW, realiza a descompactacao do arquivo e escreve 
   * o contedo do arquivo de imagem conforme o original (com excessão dos comentarios)
   * @param arquivo 			Nome do arquivo de imagem, sem a extensão .pgm
   */
         
      public static void descompactar(String arquivo) throws IOException, ClassNotFoundException{																				
         short codigo = 0;																								//Armazena a posicao do dicionario
         short posicaoPrefixo = 0;																					//Armazena a posicao do prefixo lida do arquico compactado
         String simbolo = "";																							//Armazena o caracter de sufixo lido do arquivo compactado
         DataInputStream in = new DataInputStream(new FileInputStream(arquivo+".lzw"));						//Stream para leitura do arquivo compactado
         BufferedWriter escritor = new BufferedWriter(new FileWriter("desc_"+arquivo+".pgm"));	//Stream para escrita do arquivo descompactado
         HashMap<Short,String> dicionarioBase = new HashMap<Short, String>();							//Tabela Hash que armazena o dicionario base (caracteres possíveis que geram o arquivo inteiro
         HashMap<Short, String> dicionario = new HashMap<Short, String>();								//Tabela Hash que armazena o dicionario (tanto o base quanto o gerado durante a compactacao)
         
      	System.out.println("\nIniciando descompactacao...\n");
      	
         //Armazena todos os possíveis caracteres existentes no dicionario base                        
         for(short i = 0;i<255;i++){
            dicionarioBase.put(i,Character.toString((char)i));
         }  
      	
      	//insere o dicionario base no dicionario
         dicionario.putAll(dicionarioBase);
      
      	//armazena o tamanho do dicionario na variavel codigo
         codigo = (short) dicionario.size();
         
      	//Inicia a descompactacao
      		
         posicaoPrefixo = readShort(in);		//Le a posicao no dicionario em que se encontra o prefixo
         simbolo = readChar(in);					//Le o caracter que sucede o prefixo
      	
      	//Enquanto existir dados no arquivo   
         while(posicaoPrefixo != -1 && simbolo != null){
            
         //Reseta o dicionario quando o tamanho máximo é atingido e insere o dicionario base nele
            if (dicionario.size() >= TAM_MAX_DICIONARIO) {
               dicionario.clear();
               dicionario.putAll(dicionarioBase);
               codigo = (short)dicionario.size();
            }  
         	
            String descompactada = dicionario.get(posicaoPrefixo);	//Adiciona na string descompactada a string referente a posicao do prefixo lida   
            escritor.write(descompactada);									//Escreve os caracteres no arquivo descomprimido    
            dicionario.put(codigo, descompactada + simbolo);			//Adiciona a nova entrada lida ao dicionário
            codigo++;																//Incrementa a variavel codigo
            posicaoPrefixo = readShort(in);									//Le a posicao no dicionario em que se encontra o prefixo
            simbolo =  readChar(in);											//Le o caracter que sucede o prefixo 
         }  
         
      	System.out.println("Arquivo "+arquivo+".lzw descompactado com sucesso como desc_"+arquivo+".pgm!\n");            
      }
   	
   /**
   * Le um dado do tipo short do stream de dados
   * Caso o stream tenha chegado ao fim, retorna -1 
   * @param in 			Stream de dados
   * @return lido			Valor lido do stream
   */
   
      public static Short readShort (DataInputStream  in) throws IOException, ClassNotFoundException 
      {  
         short lido = -1;
         try{
            lido = in.readShort();
         }
            catch(EOFException e){
               lido = -1;
               return lido;   
            }   
         return lido;
      }	
   
   /**
   * Le um dado do tipo char do stream de dados e o armazena em uma string
   * Caso o stream tenha chegado ao fim, retorna null 
   * @param in 			Stream de dados
   * @return lido			Valor lido do stream
   */
   
      public static String readChar (DataInputStream  in) throws IOException, ClassNotFoundException 
      {  
         String lido = null;
         try{
            lido = Character.toString((char) in.readByte());
         }
            catch(EOFException e){
               lido = null;
               return lido;   
            }   
         return lido;
      }
   }
