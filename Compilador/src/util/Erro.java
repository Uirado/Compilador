package util;

import compilador.CodigosToken;
import scanner.Cursor;
import compilador.TabelaDeSimbolos;
import java.util.List;
import scanner.Token;

public class Erro {
    private static final String[] MENSAGENS = 
    {
        "Nenhum arquivo lido", //0
        "Parametro inválido" //1
    };
    
    public static void show(int codigo){
        Print.show(MENSAGENS[codigo]);
    }
    
    public static void tokenError(Cursor cursor, Token tok, String detalhes){
        String lex;
        
        if(tok == null){
            Print.show("ERRO na linha " + cursor.getLinha() + ", coluna " + cursor.getColuna() + ", ultimo token lido (nenhum): "+ detalhes);
            
        } else{
            lex = TabelaDeSimbolos.getNome(tok.getCodigo());
            
            if(tok.getCodigo() >= 50){ 
                Print.show("ERRO na linha " + cursor.getLinha() + ", coluna " + cursor.getColuna() + ", ultimo token lido "+ lex +" \""+tok.getLexema()+"\": "+ detalhes);
            } else{
                Print.show("ERRO na linha " + cursor.getLinha() + ", coluna " + cursor.getColuna() + ", ultimo token lido "+ lex +": "+ detalhes);
            }
        }
        System.exit(0);
        
    }

    public static void sintaxError(List<Integer> cod, Token token, boolean eof, Cursor cursor) {
        String msg1 = "";
        String msg2 = "";
        if(eof){
            msg1 = "Fim de arquivo. ";
        }
        
        msg2 = "Token esperado: " + TabelaDeSimbolos.getNome(cod.get(0));;
        for(int i = 1; i < cod.size(); i++){
            msg2 += " ou " + TabelaDeSimbolos.getNome(cod.get(i));
        }
        
        tokenError(cursor, token, msg1 + msg2);
    }
    
    public static void sintaxError(int cod, Token token, boolean eof, Cursor cursor) {
        String msg1 = "";
        String msg2 = "";
        if(eof){
            msg1 = "Fim de arquivo. ";
        }
        msg2 = "Token esperado: " + TabelaDeSimbolos.getNome(cod);

        tokenError(cursor, token, msg1 + msg2);
    }
}
