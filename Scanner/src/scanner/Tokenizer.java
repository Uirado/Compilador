package scanner;

import java.util.Scanner;

public class Tokenizer {
    
    private String linha;
    private char[] caracteres;
    private final Scanner scanner;
    
    public Tokenizer(Scanner scan){
        scanner = scan;
    }
    
    public void run(){
        int linhaAtual = 0;
        int colunaAtual;
        
        while(scanner.hasNextLine()){
            colunaAtual = 0;
            linhaAtual++;
            linha = scanner.nextLine();
            caracteres = linha.toCharArray();
            
            for(char charTemp: caracteres){
                colunaAtual++;
                Print.show(charTemp, false);
                
            }
        }
        
    }
    
}
