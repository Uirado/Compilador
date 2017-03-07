/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

/**
 *
 * @author cais
 */
public class Erro {
    private static final String[] mensagens = 
    {
        "Nenhum arquivo lido", //0
        "Parametro inválido" //0
    };
    
    public static void show(int codigo){
        Print.show(mensagens[codigo]);
    }
    
}
