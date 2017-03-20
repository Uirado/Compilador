package scanner;

public class Erro {
    private static final String[] mensagens = 
    {
        "Nenhum arquivo lido", //0
        "Parametro inválido" //1
    };
    
    public static void show(int codigo){
        Print.show(mensagens[codigo]);
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
        
        
    }
}
