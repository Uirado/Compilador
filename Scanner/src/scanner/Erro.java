package scanner;

public class Erro {
    private static final String[] mensagens = 
    {
        "Nenhum arquivo lido", //0
        "Parametro inválido" //0
    };
    
    public static void show(int codigo){
        Print.show(mensagens[codigo], true);
    }
    
}
