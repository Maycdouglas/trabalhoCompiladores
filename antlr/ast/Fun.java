package ast;

import java.util.List;

// Função
public class Fun implements Def {
    public final String id;
    public final List<Param> params;
    public final List<Type> retTypes; // pode ser vazio
    public final Cmd body;

    public Fun(String id, List<Param> params, List<Type> retTypes, Cmd body) {
        this.id = id;
        this.params = params;
        this.retTypes = retTypes;
        this.body = body;
    }
}