package error;

import org.antlr.v4.runtime.*;

public class SyntaxErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        String error = "Erro de sintaxe na linha " + line + ":" + charPositionInLine + " - " + msg;
        throw new RuntimeException(error);
    }
}
