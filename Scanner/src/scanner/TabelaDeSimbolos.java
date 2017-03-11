package scanner;

import java.util.Arrays;
import java.util.List;

public class TabelaDeSimbolos {
    private static final List<String> tabela = Arrays.asList(
        "main",
        "if",
        "else",
        "while",
        "do",
        "for",
        "int",
        "float",
        "char",
        "<",
        ">",
        "<=",
        ">=",
        "==",
        "!=",
        "+",
        "-",
        "*",
        "/",
        "=",
        ")",
        "(",
        "{",
        "}",
        ",",
        ";"
    );
    
    public static int lookUp(String lexema){
        if(tabela.contains(lexema)){
            return tabela.indexOf(lexema);
        }
        else return 0;
    }
    
    public static String getNome(int codigo){
        return tabela.get(codigo);
    }
}