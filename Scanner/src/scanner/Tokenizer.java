package scanner;

import java.util.Scanner;

public class Tokenizer {
    
    private final Scanner leitor;
    private String ch = null;
    private int linhaAtual;
    private int colunaAtual;
    private Token ultimoTokenValido = null;
    
    public Tokenizer(Scanner scan){
        leitor = scan;
        leitor.useDelimiter("");
        linhaAtual = 1;
        colunaAtual = 0;
    }
    
    public void run(){
        Token tempToken;
        
        getNextChar();
            
        while(ch != null){
            
            tempToken = scan();
            
            if(tempToken != null){
                tempToken.print();
                ultimoTokenValido = tempToken;
            }
            
            if(ch.charAt(0) == '\n') {
                colunaAtual = 0;
                linhaAtual++;
            }
            
            while(ch.charAt(0) == '\n' || ch.charAt(0) == ' ' || ch.charAt(0) == '\t'){
                getNextChar();
                if(ch == null) break;
            }
        }
        
    }
    
    private Token scan(){
        Token token = null;
        int codigo;
        String lexema = "";
           
        //Automato
        switch(ch.charAt(0)){
            case '<':
                lexema += ch;
                getNextChar();
                if(ch != null){
                    switch(ch.charAt(0)){
                        case '=': //token '<='
                            lexema += ch;
                            getNextChar();
                            break;
                        default: //token '<'
                            break;
                    }
                    codigo = TabelaDeSimbolos.lookUp(lexema);
                    token = new Token(codigo);
                }
                break;
            case '>':
                lexema += ch;
                getNextChar();
                if(ch != null){
                    switch(ch.charAt(0)){
                        case '=': //token '>='
                            lexema += ch;
                            getNextChar();
                            break;
                        default: //token '>'
                            break;
                    }
                    codigo = TabelaDeSimbolos.lookUp(lexema);
                    token = new Token(codigo);
                }
                break;
            case '=':
                lexema += ch;
                getNextChar();
                if(ch != null){
                    switch(ch.charAt(0)){
                        case '=': //token '=='
                            lexema += ch;
                            getNextChar();
                            break;
                        default: //token '='
                            break;
                    }
                    codigo = TabelaDeSimbolos.lookUp(lexema);
                    token = new Token(codigo);
                }
                break;
            default:
                //erro de token invalido
                Erro.tokenError(linhaAtual, colunaAtual, ultimoTokenValido, "Caractere inválido: \"" + ch + "\"");
                getNextChar();
                token = null;
                break;
        }
        
        return token;
    }

    private void getNextChar() {
        if(leitor.hasNext()){
            ch =  leitor.next();
            colunaAtual++;
        } else{
            ch = null;
        }
    }
}
