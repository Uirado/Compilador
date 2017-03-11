package scanner;

public class Erro {
    private static final String[] mensagens = 
    {
        "Nenhum arquivo lido", //0
        "Parametro inválido" //0
    };
    
    public static void show(int codigo){
        Print.show(mensagens[codigo]);
    }
    
    public static void tokenError(int linha, int coluna, Token tok, String detalhes){
        String lex;
        
        if(tok == null){
            lex = "";
        } else{
            lex = TabelaDeSimbolos.getNome(tok.getCodigo());
        }
        
        Print.show("ERRO na linha " + linha + ", coluna " + coluna + ", ultimo token lido \""+ lex +"\": "+ detalhes);
    }
}
