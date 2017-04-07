package parser;

import compilador.CodigosToken;
import java.util.List;
import scanner.Token;
import scanner.Tokenizer;
import util.Erro;
public class Parser {
    Token token;
    Tokenizer scanner;
    
    public Parser(Tokenizer scanner){
        this.scanner = scanner;
    }
    
    public void run(){
        scan();
        if(!scanner.eof()){
            programa();
        }//else parserError(First.programa);
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
    }
    
    private void decl_var() {
        //<tipo> <id> {,<id>}* ";"
        
        tipo();
        if(token.getCodigo() == CodigosToken.ID){
            scan();
            while(token.getCodigo() == CodigosToken.VIRGULA){
                scan();
                if(token.getCodigo() == CodigosToken.ID){
                    scan();
                } else parserError(CodigosToken.ID);
            }
            if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                scan();
            }else parserError(CodigosToken.PONTO_VIRGULA); 
        }else parserError(CodigosToken.ID); 
    }
    
    private void tipo(){
        if(First.tipo.contains(token.getCodigo())){
            scan();
        } else parserError(First.tipo);
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
        
        expr_arit();
        op_relacional();
        expr_arit();
    }
    
    private void op_relacional(){
        // "==" | "!=" | "<" | ">" | "<=" | ">="
        if(First.op_relacional.contains(token.getCodigo())){
            scan();
        } else parserError(First.op_relacional);
    }
    
    private void termo(){
        fator();
        while(token.getCodigo() == CodigosToken.MULTIPLICACAO || token.getCodigo() == CodigosToken.DIVISAO){
            scan();
            fator();
        }
    }
    
    private void fator(){
        //“(“ <expr_arit> “)”
        //<id>
        //<real>
        //<inteiro>
        //<char>

        if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
            scan();
            expr_arit();
            if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                scan();
            }else parserError(CodigosToken.FECHA_PARENTESES);
        } else if(token.getCodigo() == CodigosToken.ID){
             scan();
        } else if(token.getCodigo() == CodigosToken.VALOR_FLOAT){
             scan();
        } else if(token.getCodigo() == CodigosToken.VALOR_INT){
             scan();
        } else if(token.getCodigo() == CodigosToken.VALOR_CHAR){
             scan();
        }else parserError(First.fator);
    }
    
    private void expr_arit(){
        //<termo> <expr_arit2>
        termo();
        expr_arit2();
    }
    
    private void expr_arit2(){
        //"+" <termo> <expr_arit2>
        //"-" <termo> <expr_arit2>
        //λ
        
        if(token.getCodigo() == CodigosToken.SOMA){
            scan();
            termo();
            expr_arit2();
        } else if(token.getCodigo() == CodigosToken.SUBTRACAO){
            scan();
            termo();
            expr_arit2();
        } else{
            //λ
        }
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
        
        if(token.getCodigo() == CodigosToken.ID){
            scan();
            if(token.getCodigo() == CodigosToken.ATRIBUICAO){
                scan();
                expr_arit();
                if(token.getCodigo() == CodigosToken.PONTO_VIRGULA){
                    scan();
                } else parserError(CodigosToken.PONTO_VIRGULA);
            } else parserError(CodigosToken.ATRIBUICAO);
        } else parserError(CodigosToken.ID);
    }

    private boolean scan() {
        token = scanner.scan();
        if(token != null) return true;
        return false;
    }

    private void parserError(int codigoToken) {
        Erro.sintaxError(codigoToken, scanner.getUltimoTokenValido(), scanner.eof(), scanner.getCursor());
    }
    private void parserError(List<Integer> codigoToken) {
        Erro.sintaxError(codigoToken, scanner.getUltimoTokenValido(), scanner.eof(), scanner.getCursor());
    }

}

