package parser;

import compilador.CodigosToken;
import java.util.List;
import java.util.Stack;
import scanner.Token;
import scanner.Tokenizer;
import util.Erro;

public class Parser {
    private Token token;
    private Tokenizer scanner;
    private int escopo = -1;
    private Stack<SimboloID> tabelaSimbolos = new Stack();
    
    
    public Parser(Tokenizer scanner){
        this.scanner = scanner;
    }
    
    public void run(){
//        while(!scanner.eof()){
//            scan();
//        }
        scan();
        if(!scanner.eof()){
            programa();
            if(!scanner.eof()){
                parserError(CodigosToken.EOF);
            }
        }else parserError(First.programa);
    }
    
    private void programa(){
        //int main"("")" <bloco>
        
        if(token.getCodigo() == CodigosToken.INT){
            scan();
            if(token.getCodigo() == CodigosToken.MAIN){
                scan();
                if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                    scan();
                    if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                        scan();
                        bloco();
                    }else parserError(CodigosToken.FECHA_PARENTESES);
                }else parserError(CodigosToken.ABRE_PARENTESES);
            } else parserError(CodigosToken.MAIN);
        } else parserError(CodigosToken.INT);
    }

    private void bloco() {
        //“{“ {<decl_var>}* {<comando>}* “}”
        escopo++;
        if(token.getCodigo() == CodigosToken.ABRE_CHAVES){
            scan();
            while(First.decl_var.contains(token.getCodigo())){
                decl_var();
            }
            while(First.comando.contains(token.getCodigo())){
                comando();
            }
            if(token.getCodigo() == CodigosToken.FECHA_CHAVES){
                scan();
            } else parserError(CodigosToken.FECHA_CHAVES);
        } else parserError(CodigosToken.ABRE_CHAVES);
        limparSimbolos(escopo);
        escopo--;
    }
    
    private void decl_var() {
        //<tipo> <id> {,<id>}* ";"
        
        String tipo = tipo();
        if(token.getCodigo() == CodigosToken.ID){
            newID(token, escopo, tipo);
            scan();
            while(token.getCodigo() == CodigosToken.VIRGULA){
                scan();
                if(token.getCodigo() == CodigosToken.ID){
                    newID(token, escopo, tipo);
                    scan();
                } else parserError(CodigosToken.ID);
            }
            if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                scan();
            }else parserError(CodigosToken.PONTO_VIRGULA); 
        }else parserError(CodigosToken.ID); 
    }
    
    
    private String tipo(){
        String tipo = token.getLexema();
        if(First.tipo.contains(token.getCodigo())){
            scan();
            return tipo;
        } else parserError(First.tipo);
        return null;
    }    

    private void comando() {
        //    <comando_básico>
        //    <iteração>
        //    if "("<expr_relacional>")" <comando> {else <comando>}?
        
        if(First.comando_basico.contains(token.getCodigo())){ // comando_basico
            comando_basico();
        } else if(First.iteracao.contains(token.getCodigo())){ // iteracao
            iteracao();
        } else if(First._if.contains(token.getCodigo())){ // if
            _if();
        } else parserError(First.comando);
    }
    
    private void _if(){
        // if "("<expr_relacional>")" <comando> {else <comando>}?
        
        if(token.getCodigo() == CodigosToken.IF){
            scan();
            if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                scan();
                expr_relacional();
                if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                    scan();
                    comando();
                    
                    //else opcional
                    if(token.getCodigo() == CodigosToken.ELSE){
                        scan();
                        comando();
                    }
                    
                }else parserError(CodigosToken.FECHA_PARENTESES);
            }else parserError(CodigosToken.ABRE_PARENTESES);
        } else parserError(First._if);
    }
    
    private void iteracao(){
        // while "("<expr_relacional>")" <comando>
        // do <comando> while "("<expr_relacional>")"";"

        if(token.getCodigo() == CodigosToken.WHILE){
            //while
            scan();
            if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                scan();
                expr_relacional();
                if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                    scan();
                    comando();
                } else parserError(CodigosToken.FECHA_PARENTESES);
            } else parserError(CodigosToken.ABRE_PARENTESES);
            
        }else if(token.getCodigo() == CodigosToken.DO){
            //do
            scan();
            comando();
            if(token.getCodigo() == CodigosToken.WHILE){
                scan();
                if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                    scan();
                    expr_relacional();
                    if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                        scan();
                        if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                            scan();
                        } else parserError(CodigosToken.PONTO_VIRGULA);
                    } else parserError(CodigosToken.FECHA_PARENTESES);
                } else parserError(CodigosToken.ABRE_PARENTESES);
            }else parserError(CodigosToken.WHILE);
            
        } else parserError(First.iteracao);
    }
    
    private void expr_relacional(){
        //<expr_arit> <op_relacional> <expr_arit>
        String tipo1, tipo2;
        tipo1 = expr_arit();
        op_relacional();
        tipo2 = expr_arit();
        
        checarTipoExprRelacional(tipo1, tipo2);
    }
    
    private void checarTipoExprRelacional(String tipo1, String tipo2){
        if(!tipo1.equals(tipo2) && (tipo1.equals("char") || tipo2.equals("char"))){
            semanticError(tipo1, tipo2, 3);
        }
    }
    
    private void op_relacional(){
        // "==" | "!=" | "<" | ">" | "<=" | ">="
        if(First.op_relacional.contains(token.getCodigo())){
            scan();
        } else parserError(First.op_relacional);
    }
    
    private String termo(){
        String tipo1, tipo2;
        int op;
        tipo1 = fator();
        while(token.getCodigo() == CodigosToken.MULTIPLICACAO || token.getCodigo() == CodigosToken.DIVISAO){
            op = token.getCodigo();
            scan();
            tipo2 = fator();
            tipo1 = checarTipos(tipo1, tipo2, op);
        }
        return tipo1;
    }
    
    private String checarTipos(String tipo1, String tipo2, int op){
        if(tipo2 == null){
            return tipo1;
        }else if(tipo1.equals(tipo2) && tipo1.equals("char")){
            return tipo1;
        } else if(tipo1.equals("char") || tipo2.equals("char")){
            semanticError(tipo1, tipo2, 3); //erro de char operando com outros tipos
        }else if(op == CodigosToken.DIVISAO){
            return "float";
        } else if(tipo1.equals("float") || tipo2.equals("float")){
            return "float";
        }
        return "int";
    }
    
    private String fator(){
        //“(“ <expr_arit> “)”
        //<id>
        //<real>
        //<inteiro>
        //<char>
        String tipo = null;
        SimboloID tempSimbolo;
        
        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){    
            scan();
            tipo = expr_arit();
            if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                scan();
            }else parserError(CodigosToken.FECHA_PARENTESES);
        } else if(token.getCodigo() == CodigosToken.ID){
            
            tempSimbolo = buscaSimbolo(token.getLexema(), -1);
            if(tempSimbolo != null){
                tipo = tempSimbolo.getTipo();
            } else semanticError(token.getLexema(), null, 1); //variavel nao declarada
            
            scan();
        } else if(token.getCodigo() == CodigosToken.VALOR_REAL){
            tipo = "float";
            scan();
        } else if(token.getCodigo() == CodigosToken.VALOR_INTEIRO){
            tipo = "int";
            scan();
        } else if(token.getCodigo() == CodigosToken.VALOR_CHAR){
            tipo = "char";
            scan();
        }else parserError(First.fator);
        
        return tipo;
    }
    
    private String expr_arit(){
        //<termo> <expr_arit2>
        String tipo1, tipo2;
        tipo1 = termo();
        tipo2 = expr_arit2();
        
        return checarTipos(tipo1, tipo2, -1);
    }
    
    private String expr_arit2(){
        //"+" <termo> <expr_arit2>
        //"-" <termo> <expr_arit2>
        //λ
        String tipo1, tipo2;
        
        if(token.getCodigo() == CodigosToken.SOMA){
            scan();
            tipo1 = termo();
            tipo2 = expr_arit2();
        } else if(token.getCodigo() == CodigosToken.SUBTRACAO){
            scan();
            tipo1 = termo();
            tipo2 = expr_arit2();
        } else{
            //λ
            return null;
        }
        return checarTipos(tipo1, tipo2, -1);
    }
    

    private void comando_basico(){
        //    <atribuição>
        //    <bloco>       
        
        if(First.atribuicao.contains(token.getCodigo())){ // atribuicao
            atribuicao();
        }else if(First.bloco.contains(token.getCodigo())){ //bloco
            bloco();
        } else parserError(First.comando_basico);
    }
    
    private void atribuicao(){
        //<id> "=" <expr_arit> ";"
        String tipo1 = null, tipo2;
        SimboloID tempSimbolo;
        if(token.getCodigo() == CodigosToken.ID){
            
            tempSimbolo = buscaSimbolo(token.getLexema(), -1);
            if(tempSimbolo != null){
                tipo1 = tempSimbolo.getTipo();
            } else semanticError(token.getLexema(), null, 1); //variavel nao declarada
            
            scan();
            if(token.getCodigo() == CodigosToken.ATRIBUICAO){
                scan();
                tipo2 = expr_arit();
                
                checarTipoAtribuicao(tipo1, tipo2);
                
                if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                    scan();
                } else parserError(CodigosToken.PONTO_VIRGULA);
            } else parserError(CodigosToken.ATRIBUICAO);
        } else parserError(CodigosToken.ID);
    }
    
    private void checarTipoAtribuicao(String tipo1, String tipo2){
        if(!tipo1.equals(tipo2)){
            if(!(tipo1.equals("float") && tipo2.equals("int"))){
                semanticError(tipo1, tipo2, 2);
            }
        }
    }

    private void scan() {
        token = scanner.scan();
        //if(token != null) Print.printToken(token);
    }

    private void parserError(int codigoToken) {
        Erro.sintaxError(codigoToken, scanner.getUltimoTokenValido(), scanner.eof(), scanner.getCursor());
    }
    private void parserError(List<Integer> codigoToken) {
        Erro.sintaxError(codigoToken, scanner.getUltimoTokenValido(), scanner.eof(), scanner.getCursor());
    }
    private void semanticError(String lexema1, String lexema2, int codErro) {
        Erro.semanticError(lexema1, lexema2, codErro, scanner.getUltimoTokenValido(), scanner.getCursor());
    }


    private void newID(Token token, int escopo, String tipo) {
        SimboloID novo = new SimboloID(token, escopo, tipo);
        
        if(buscaSimbolo(novo.getLexema(), novo.getEscopo()) == null){
            tabelaSimbolos.push(novo);
        }else semanticError(novo.getLexema(), null, 0);
    }

    private SimboloID buscaSimbolo(String lexema, int escopo) {
        SimboloID temp;
        for(int i = tabelaSimbolos.size() - 1; i >= 0; i--){
            temp = tabelaSimbolos.get(i);
            if(temp.getLexema().equals(lexema)){
                if(escopo == -1){
                    return temp;
                } else if(temp.getEscopo() == escopo){
                    return temp;
                }
            }
        }
        return null;
    }
    
    private void limparSimbolos(int escopoAtual){
        SimboloID temp;
        if(!tabelaSimbolos.isEmpty()){
            do{
                temp = tabelaSimbolos.peek();
                if(temp.getEscopo() == escopoAtual){
                    tabelaSimbolos.pop();
                }
            }while(!tabelaSimbolos.isEmpty() && temp.getEscopo() == escopoAtual);
        }
    }

    private static class SimboloID {
        private String tipo;
        private String lexema;
        private int escopo;
        
        public SimboloID(Token token, int escopo, String tipo) {
            this.escopo = escopo;
            lexema = token.getLexema();
            this.tipo = tipo;
        }

        public int getEscopo() {
            return escopo;
        }

        public String getLexema() {
            return lexema;
        }

        public String getTipo() {
            return tipo;
        }
        
    }

}

