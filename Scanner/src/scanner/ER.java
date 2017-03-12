
package scanner;

public class ER {
    
    static String L = "[a-zA-Z]";
    static String D = "[0-9]";
    static String ID = "(_|[a-zA-Z])(_|[a-zA-Z]|[0-9])*";
    static String INTEIRO = "[0-9]+";
    static String FLOAT = "[0-9]*.[0-9]+";
    
    static boolean ehLetra(String ch) {
        if(ch.matches(L)){
            //Print.show("É letra");
            return true;
        } else{
            //Print.show("Não é letra");
            return false;
        }
    }
    
    static boolean ehDigito(String ch) {
        if(ch.matches(D)){
            //Print.show("É número"); 
            return true;
        } else{
            //Print.show("Não é número");
            return false;
        }
        
    }

    static boolean ehIdentificador(String lexema) {
        if(lexema.matches(ID)){
            return true;
        }else{
            return false;                
        }
    }

    static boolean ehInteiro(String lexema) {
        if(lexema.matches(INTEIRO)){
            return true;
        }else{
            return false;                
        }
    }

    static boolean ehFloat(String lexema) {
        if(lexema.matches(FLOAT)){
            return true;
        }else{
            return false;                
        }
    }
    
    
}
