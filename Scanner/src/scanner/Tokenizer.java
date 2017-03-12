package scanner;

import java.util.Scanner;

public class Tokenizer {
    
    private final Scanner leitor;
    private String ch = null;
    private int linhaAtual;
    private int colunaAtual;
    private Token ultimoTokenValido = null;
    private Token token = null;
    private int codigo;
    private String lexema;
    
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
            } else{
                if(!leitor.hasNext()) break;
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
        lexema = "";
        int codErro = 0;
           
        //Automato
        switch(ch.charAt(0)){
            case '<':
                lerProximoChar();
                if(ch != null){
                    switch(ch.charAt(0)){
                        case '=': //token '<='
                            defineTokenFound(true);
                            break;
                        default: //token '<'
                            defineTokenFound(false);
                            break;
                    }
                }
                break;
            case '>':
                lerProximoChar();
                if(ch != null){
                    switch(ch.charAt(0)){
                        case '=': //token '>='
                            defineTokenFound(true);
                            break;
                        default: //token '>'
                            defineTokenFound(false);
                            break;
                    }
                }
                break;
            case '=':
                lerProximoChar();
                if(ch != null){
                    switch(ch.charAt(0)){
                        case '=': //token '=='
                            defineTokenFound(true);
                            break;
                        default: //token '='
                            defineTokenFound(false);
                            break;
                    }
                }
                break;
            case '!':
                lerProximoChar();
                if(ch != null){
                    switch(ch.charAt(0)){
                        case '=': //token '!='
                            defineTokenFound(true);
                            break;
                        default: //ERRO ! SOZINHO
                            codErro = 2;
                            token = null;
                            break;
                    }
                }
                break;
            case '(':
                defineTokenFound(true);
                break;
            case ')':
                defineTokenFound(true);
                break;
            case '{':
                defineTokenFound(true);
                break;
            case '}':
                defineTokenFound(true);
                break;
            case ',':
                defineTokenFound(true);
                break;
            case ';':
                defineTokenFound(true);
                break;
            case '+':
                defineTokenFound(true);
                break;
            case '-':
                defineTokenFound(true);
                break;
            case '*':
                defineTokenFound(true);
                break;
            case '/':
                lerProximoChar();
                if(ch != null){
                    switch(ch.charAt(0)){
                        case '/': //comentario de linha unica "//"
                            consumirComentario(0);
                            break;
                        case '*': /* comentario multilinhas */
                            codErro = consumirComentario(1);
                            break;
                        default: //token '/'
                            defineTokenFound(false);
                            break;
                    }
                }
                break;
                
            default:
                //aqui são tratados as entradas que obedecem a expressões regulares
                if(ER.ehLetra(ch) || ch.charAt(0) == '_'){ //pode ser identificador ou palavra reservada
                    while(ER.ehLetra(ch) || ch.charAt(0) == '_' || ER.ehDigito(ch)){
                        lerProximoChar();
                    }
                    defineTokenFound(false, lexema);
                    
                }else if(ER.ehDigito(ch) || ch.charAt(0) == '.'){ //pode ser INTEIRO OU FLOAT
                    while(ER.ehDigito(ch)){
                        lerProximoChar();
                    }
                    if(ch.charAt(0) != '.'){ //é inteiro
                        defineTokenFound(false, lexema);
                    } else{ //vai ser float ou dar erro de má formacao
                        lerProximoChar();
                        if(ER.ehDigito(ch)){
                            while(ER.ehDigito(ch)){
                                lerProximoChar();
                            }
                            defineTokenFound(false, lexema);
                        } else{
                            codErro = 4;
                        }
                    }
                    
                } else{
                    //erro de caractere invalido
                    codErro = 1;
                    break;
                }
        }
        
        if(codErro > 0){
            switch (codErro){
                case 1:
                    Erro.tokenError(linhaAtual, colunaAtual, ultimoTokenValido, "Caractere inválido: \"" + ch + "\"");
                    getNextChar();
                    token = null;
                    break;
                case 2:
                    Erro.tokenError(linhaAtual, colunaAtual, ultimoTokenValido, "Má formação do operador relacional DIFERENTE. Caractere \"!\" sozinho");
                    break;
                case 3:
                    Erro.tokenError(linhaAtual, colunaAtual, ultimoTokenValido, "EOF antes de fechamento de comentário: faltando '*/'");
                    break;
                case 4:
                    Erro.tokenError(linhaAtual, colunaAtual, ultimoTokenValido, "Má formação de FLOAT \""+lexema+"\"");
                    break;
            }
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

    private void defineTokenFound(boolean lerProximo) {
        if(lerProximo){
            lerProximoChar();
        }
        codigo = TabelaDeSimbolos.lookUp(lexema);
        token = new Token(codigo);
    }
    
    private void defineTokenFound(boolean lerProximo, String lexema) {
        if(lerProximo){
            lerProximoChar();
        }
        codigo = TabelaDeSimbolos.lookUp(lexema);
        token = new Token(codigo, lexema);
    }
    
    private void lerProximoChar(){
        lexema += ch;
        getNextChar();
    }

    private int consumirComentario(int tipoComentario) {
        int erro = 0;
        
        if(tipoComentario == 0){ //comentario de uma linha
            
            while(ch.charAt(0) != '\n'){
                ch = leitor.next();
            }
        } else{ /* Comentario multilinha */
            
            while(true){
                getNextChar();
                if(ch != null){
                    if(ch.charAt(0) == '*'){
                        getNextChar();
                        if(ch != null){
                            if(ch.charAt(0) == '/'){
                                getNextChar();
                                break;
                            }
                        }
                    }
                }
                if(ch == null){
                    //fim de arquivo sem fechar o comentario '*/'
                    erro = 3;
                    break;
                }
            }   
        }
        
        token = null;
        return erro;
    }

    
}
