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
        } else if(ER.ehIdentificador(lexema)){
            return 50;
        } else if(ER.ehInteiro(lexema)){
            return 51;
        } else if(ER.ehFloat(lexema)){
            return 52;
        } else if(ER.ehChar(lexema)){
            return 53;
        }
        else return -1;
    }
    
    public static String getNome(int codigo){
        if(codigo >= 50){
            switch (codigo){
                case 50: return "IDENT";
                case 51: return "INTEIRO";
                case 52: return "FLOAT";
                case 53: return "CHAR";
            }
        }
        return tabela.get(codigo);
    }
}