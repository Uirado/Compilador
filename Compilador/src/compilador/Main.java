package compilador;

import util.Erro;
import util.Print;
import scanner.Tokenizer;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import scanner.Token;

public class Main {
    
    private static String enderecoArquivo = null;
    
    public static void main(String[] args){
        Tokenizer scanner;
        Token tempToken;
        
        if(args.length == 1){
            enderecoArquivo = args[0];
            scanner = abrirArquivo(enderecoArquivo);
            
            if(scanner != null){
                while(!scanner.eof()){
                    tempToken = scanner.scan();

                    if(tempToken != null){
                        Print.printToken(tempToken);
                    } 
                    //se o token for NULL e não for fim de arquivo: ERRO de token
                    //se o token NULL e for fim de arquivo: Poder ter erro de comentário "/* EOF" ou apenas fim de arquivo
                }
            } else{
                Erro.show(0);
            }
            
        } else if(args.length > 1){
            Erro.show(1);
        } else{
            Erro.show(0);
        }

    }

    private static Tokenizer abrirArquivo(String arquivo){
        Path path;
        Scanner scan;
        path = Paths.get(arquivo);
    
        try {
            scan = new Scanner(path.toFile());
            return new Tokenizer(scan);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
}
