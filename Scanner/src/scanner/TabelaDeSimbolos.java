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
                case 50: return "<IDENTIFICADOR>";
                case 51: return "<valor_INTEIRO>";
                case 52: return "<valor_FLOAT>";
                case 53: return "<valor_CHAR>";
            }
        } else{
            switch(tabela.get(codigo)){
                case "main":    return "<palreservada_MAIN>";
                case "if":      return "<palreservada_IF>";
                case "else":    return "<palreservada_ELSE>";
                case "while":   return "<palreservada_WHILE>";
                case "do":      return "<palreservada_DO>";
                case "for":     return "<palreservada_FOR>";
                case "int":     return "<palreservada_INT>";
                case "float":   return "<palreservada_FLOAT>";
                case "char":    return "<palreservada_CHAR>";
                case "<":       return "<oprelacional_MENOR>";
                case ">":       return "<oprelacional_MAIOR>";
                case "<=":      return "<oprelacional_MENOR_IGUAL>";
                case ">=":      return "<oprelacional_MAIOR_IGUAL>";
                case "==":      return "<oprelacional_IGUAL>";        
                case "!=":      return "<oprelacional_DIFERENTE>";    
                case "+":       return "<oparitmetico_SOMA>";
                case "-":       return "<oparitmetico_SUBTRACAO>";
                case "*":       return "<oparitmetico_MULTIPLICACAO>";
                case "/":       return "<oparitmetico_DIVISAO>";
                case "=":       return "<oparitmetico_ATRIBUICAO>";
                case ")":       return "<especial_FECHA_PARENTESES>";
                case "(":       return "<especial_ABRE_PARENTESES>";
                case "{":       return "<especial_ABRE_CHAVES>";
                case "}":       return "<especial_FECHA_CHAVES>";
                case ",":       return "<especial_VIRGULA>";
                case ";":       return "<especial_PONTO_VIRGULA>";
                default:        return "";
                }
        
        }
        return "";
    }
}