package scanner;

class Token {
    private int codigo;
    private String lexema = null;

    public Token(int codigo, String lexema) {
        this.codigo = codigo;
        this.lexema = lexema;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getLexema() {
        return lexema;
    }
    
    public Token(int codigo) {
        this.codigo = codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    void print() {
        String strCodigo;
        if(codigo < 10){
            strCodigo = "0" + codigo;
        } else{
            strCodigo = Integer.toString(codigo);
        }
        
        if(lexema == null || codigo < 50){
            Print.show(strCodigo + "   " + TabelaDeSimbolos.getNome(codigo) +"");
        } else{
            Print.show(strCodigo + "   [" + TabelaDeSimbolos.getNome(codigo) + "] \"" + lexema + "\"");
        }
    }
}
