package compilador;

import scanner.ER;
import java.util.Arrays;
import java.util.List;

public class TabelaDeSimbolos {
    CodigosToken nomesTokens;
    
    private static final List<String> TABELA = Arrays.asList(
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
        if(TABELA.contains(lexema)){
            return TABELA.indexOf(lexema);
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
                case 50: return "ID";
                case 51: return "VALOR_INT";
                case 52: return "VALOR_FLOAT";
                case 53: return "VALOR_CHAR";
            }
        } else if(codigo >= 0){
            switch(TABELA.get(codigo)){
                case "main":    return "MAIN";
                case "if":      return "IF";
                case "else":    return "ELSE";
                case "while":   return "WHILE";
                case "do":      return "DO";
                case "for":     return "FOR";
                case "int":     return "INT";
                case "float":   return "FLOAT";
                case "char":    return "CHAR";
                case "<":       return "MENOR";
                case ">":       return "MAIOR";
                case "<=":      return "MENOR_IGUAL";
                case ">=":      return "MAIOR_IGUAL";
                case "==":      return "IGUAL";        
                case "!=":      return "DIFERENTE";    
                case "+":       return "SOMA";
                case "-":       return "SUBTRACAO";
                case "*":       return "MULTIPLICACAO";
                case "/":       return "DIVISAO";
                case "=":       return "ATRIBUICAO";
                case ")":       return "FECHA_PARENTESES";
                case "(":       return "ABRE_PARENTESES";
                case "{":       return "ABRE_CHAVES";
                case "}":       return "FECHA_CHAVES";
                case ",":       return "VIRGULA";
                case ";":       return "PONTO_VIRGULA";
                }
        }
        return "EOF";
    }
    
    
    
}