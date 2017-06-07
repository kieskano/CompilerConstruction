package pp.block1.cc.dfa;

/**
 * Created by Wijtse on 25-4-2017.
 */
public class MyChecker implements Checker{

    @Override
    public boolean accepts(State start, String word) {

        State currentState = start;
        for (char ch : word.toCharArray()) {
            State nextState = currentState.getNext(ch);
            if (nextState == null) {
                return false;
            }
            currentState = nextState;
        }

        return currentState.isAccepting();
    }
}
