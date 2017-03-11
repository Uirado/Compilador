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
        if(lexema != null){
            Print.show(codigo + "   [ " + TabelaDeSimbolos.getNome(codigo) + " ]    \"" + lexema + "\"");
        } else{
            Print.show(codigo + "   [ " + TabelaDeSimbolos.getNome(codigo) +" ]");
        }
    }
}
