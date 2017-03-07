package scanner;

import java.util.Arrays;
import java.util.List;

public class TabelaDeSimbolos {
    private final List<String> tabela = Arrays.asList(
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
    
    public int getCodigo(String token){
        if(tabela.contains(token)){
            return tabela.indexOf(token) + 1;
        }
        else return 0;
    }
}