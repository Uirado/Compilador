package scanner;

public class Print {
    public static void show(String texto){
        System.out.println(texto);
    }
    
    public static void show(char caractere, boolean quebraLinha){
        if(!quebraLinha) System.out.println(caractere);
        else  System.out.print(caractere);
    }
}