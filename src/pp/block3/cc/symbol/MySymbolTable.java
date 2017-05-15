package pp.block3.cc.symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wijtse on 12-5-2017.
 */
public class MySymbolTable implements SymbolTable {

    private List<List<String>> scopes = new ArrayList<>();

    public MySymbolTable() {
        scopes.add(new ArrayList<>());
    }

    @Override
    public void openScope() {
        scopes.add(new ArrayList<>());
    }

    @Override
    public void closeScope() {
        if (scopes.size() > 1) {
            scopes.remove(scopes.size()-1);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean add(String id) {
        if (scopes.get(scopes.size()-1).contains(id)) {
            return false;
        } else {
            scopes.get(scopes.size()-1).add(id);
            return true;
        }
    }

    @Override
    public boolean contains(String id) {
        for (List<String> scope : scopes) {
            if (scope.contains(id)) {
                return true;
            }
        }
        return false;
    }
}
