package error;

import org.antlr.v4.runtime.*;

public class SyntaxErrorListener extends BaseErrorListener {
    
    private boolean hasError = false;
    
    public boolean hasErrors() {
        return hasError;
    }
    
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e) {
        hasError = true;
        System.err.printf("Erro de sintaxe na linha %d:%d - %s%n", line, charPositionInLine, msg);
    }
}
