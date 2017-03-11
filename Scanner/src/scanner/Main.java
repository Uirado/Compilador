package scanner;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.parser.Token;

public class Main {
    private static final boolean debug = false;
    
    private static String arquivo = null;
    
    public static void main(String[] args){
        if(args.length == 1){
            arquivo = args[0];
            
            if(debug) Print.show("Arquivo: \"" + arquivo + "\"");
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
        Tokenizer tokenizer;
    
        try {
            scanner = new Scanner(path.toFile());
            tokenizer = new Tokenizer(scanner);
            tokenizer.run();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
