package interpreter;

import java.util.List;

public class ReturnException extends RuntimeException {
    private final List<Value> values;

    public ReturnException(List<Value> values) {
        super("Function returned");
        this.values = values;
    }

    public List<Value> getValues() {
        return values;
    }
}
