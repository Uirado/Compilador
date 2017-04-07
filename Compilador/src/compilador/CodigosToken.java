/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author uira
 */
public class CodigosToken {
    public static final int EOF = -1;
    public static final int MAIN = 0;
    public static final int IF = 1;
    public static final int ELSE = 2;
    public static final int WHILE = 3;
    public static final int DO = 4;
    public static final int FOR = 5;
    public static final int INT = 6;
    public static final int FLOAT = 7;
    public static final int CHAR = 8;
    public static final int MENOR = 9;
    public static final int MAIOR = 10;
    public static final int MENOR_IGUAL = 11;
    public static final int MAIOR_IGUAL = 12;
    public static final int IGUAL = 13;
    public static final int DIFERENTE = 14;
    public static final int SOMA = 15;
    public static final int SUBTRACAO = 16;
    public static final int MULTIPLICACAO = 17;
    public static final int DIVISAO = 18;
    public static final int ATRIBUICAO = 19;
    public static final int FECHA_PARENTESES = 20;
    public static final int ABRE_PARENTESES = 21;
    public static final int ABRE_CHAVES = 22;
    public static final int FECHA_CHAVES = 23;
    public static final int VIRGULA = 24;
    public static final int PONTO_VIRGULA = 25;
    public static final int ID = 50;
    public static final int VALOR_INT = 51;
    public static final int VALOR_FLOAT = 52;
    public static final int VALOR_CHAR = 53;
}