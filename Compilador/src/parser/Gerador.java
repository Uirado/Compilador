/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import compilador.CodigosToken;
import util.Print;

/**
 *
 * @author cais
 */
public class Gerador {

    static void gen(Expr expr1, Expr expr2, int op) {
        String OP = CodigosToken.getLexema(op);
        StringBuilder codigo = new StringBuilder();
        codigo.append(expr1.getLex());
        codigo.append(' ');
        codigo.append(OP);
        codigo.append(' ');
        codigo.append(expr2.getLex());
        
        Print.show(codigo.toString(), true);
    }
    
    static void gen(Expr result, Expr arg1, Expr arg2, int op) {
        String OP = CodigosToken.getLexema(op);
        StringBuilder codigo = new StringBuilder();
        codigo.append(result.getLex());
        codigo.append(" = ");        
        codigo.append(arg1.getLex());
        codigo.append(' ');
        codigo.append(OP);
        codigo.append(' ');
        codigo.append(arg2.getLex());
        
        Print.show(codigo.toString(), true);
    }
    
}
