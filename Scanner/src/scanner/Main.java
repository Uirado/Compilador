package scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    
    

    private static String arquivo = null;
    
    public static void main(String[] args){
        if(args.length == 1){
            arquivo = args[0];
            Print.show("Arquivo: \"" + arquivo + "\"");
            
            lerArquivo(arquivo);
            
        } else if(args.length > 1){
            Erro.show(1);
        } else{
            Erro.show(0);
        }
        
    }

    private static void lerArquivo(String arquivo){
        Path path;
        Scanner scanner;
        path = Paths.get(arquivo);
    
        try {
            scanner = new Scanner(path.toFile());
            
            Print.show("## Inicio de Arquivo >>>");
            while(scanner.hasNextLine()){
                Print.show(scanner.nextLine());
            }
            Print.show("<<< Fim de Arquivo");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
