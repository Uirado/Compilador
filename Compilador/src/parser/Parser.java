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
        
        int tipo = tipo();
        if(token.getCodigo() == CodigosToken.ID){
            novoSimbolo(token, escopo, tipo);
            scan();
            while(token.getCodigo() == CodigosToken.VIRGULA){
                scan();
                if(token.getCodigo() == CodigosToken.ID){
                    novoSimbolo(token, escopo, tipo);
                    scan();
                } else parserError(CodigosToken.ID);
            }
            if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                scan();
            }else parserError(CodigosToken.PONTO_VIRGULA); 
        }else parserError(CodigosToken.ID); 
    }
    
    
    private int tipo(){
        int tipo = token.getCodigo();
        if(First.tipo.contains(token.getCodigo())){
            scan();
            return tipo;
        } else parserError(First.tipo);
        return -10;
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
        int tipo1, tipo2;
        tipo1 = expr_arit();
        op_relacional();
        tipo2 = expr_arit();
        
        checarTipoExprRelacional(tipo1, tipo2);
    }
    
    private void checarTipoExprRelacional(int tipo1, int tipo2){
        if(tipo1 != tipo2 && (tipo1 == CodigosToken.CHAR || tipo2 == CodigosToken.CHAR)){
            semanticError(tipo1, tipo2, 3);
        }
    }
    
    private void op_relacional(){
        // "==" | "!=" | "<" | ">" | "<=" | ">="
        if(First.op_relacional.contains(token.getCodigo())){
            scan();
        } else parserError(First.op_relacional);
    }
    
    private int termo(){
        int tipo1 = -10, tipo2 = -10;
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
    
    private int checarTipos(int tipo1, int tipo2, int op){
        if(tipo2 == -10){
            return tipo1;
        }else if(tipo1 == tipo2 && tipo1 == CodigosToken.CHAR){
            return tipo1;
        } else if(tipo1 == CodigosToken.CHAR || tipo2 == CodigosToken.CHAR){
            semanticError(tipo1, tipo2, 3); //erro de char operando com outros tipos
        }else if(op == CodigosToken.DIVISAO){
            return CodigosToken.FLOAT;
        } else if(tipo1 == CodigosToken.FLOAT || tipo2 == CodigosToken.FLOAT){
            return CodigosToken.FLOAT;
        }
        return CodigosToken.INT;
    }
    
    private int fator(){
        //“(“ <expr_arit> “)”
        //<id>
        //<real>
        //<inteiro>
        //<char>
        int tipo = -10;
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
            } else semanticError(token.getCodigo(), -10, 1); //variavel nao declarada
            scan();
            
        } else if(token.getCodigo() == CodigosToken.VALOR_REAL){
            tipo = CodigosToken.FLOAT;
            scan();
        }else if(token.getCodigo() == CodigosToken.VALOR_INTEIRO){
            tipo = CodigosToken.INT;
            scan();
        }else if(token.getCodigo() == CodigosToken.VALOR_CHAR){
            tipo = CodigosToken.CHAR;
            scan();
        }else parserError(First.fator);
        
        return tipo;
    }
    
    private int expr_arit(){
        //<termo> <expr_arit2>
        int tipo1, tipo2;
        tipo1 = termo();
        tipo2 = expr_arit2();
        
        return checarTipos(tipo1, tipo2, -1);
    }
    
    private int expr_arit2(){
        //"+" <termo> <expr_arit2>
        //"-" <termo> <expr_arit2>
        //λ
        int tipo1 = -10, tipo2 = -10;
        
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
            return -10;
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
        int tipo1 = -10, tipo2;
        SimboloID tempSimbolo;
        if(token.getCodigo() == CodigosToken.ID){
            
            tempSimbolo = buscaSimbolo(token.getLexema(), -1);
            if(tempSimbolo != null){
                tipo1 = tempSimbolo.getTipo();
            } else semanticError(token.getCodigo(), -10, 1); //variavel nao declarada
            
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
    
    private void checarTipoAtribuicao(int tipo1, int tipo2){
        if(tipo1 != tipo2){
            if(!(tipo1 == CodigosToken.FLOAT && tipo2 == CodigosToken.INT)){
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
    private void semanticError(int tipo1, int tipo2, int codErro) {
        Erro.semanticError(tipo1, tipo2, codErro, scanner.getUltimoTokenValido(), scanner.getCursor());
    }

    private void novoSimbolo(Token token, int escopo, int tipo) {
        SimboloID novo = new SimboloID(token, escopo, tipo);
        
        if(buscaSimbolo(novo.getLexema(), novo.getEscopo()) == null){
            tabelaSimbolos.push(novo);
        }else semanticError(novo.getTipo(), -10, 0);
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
        //tipos possiveis: int:6 / float:7 / char:8
        
        private int tipo;
        private String lexema;
        private int escopo;
        
        public SimboloID(Token token, int escopo, int tipo) {
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

        public int getTipo() {
            return tipo;
        }
        
    }

}

