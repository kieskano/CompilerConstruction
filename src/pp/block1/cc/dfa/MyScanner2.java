package pp.block1.cc.dfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by han on 26-4-17.
 */
public class MyScanner2 implements Scanner {
    @Override
    public List<String> scan(State dfa, String text) {


        char[] textArray = text.toCharArray();
        int textPointer = 0;
        List<String> result = new ArrayList<>();

        while (textPointer < textArray.length) {
            State curState = dfa;
            char[] lexeme = new char[textArray.length];
            int tokenStartPointer = 0;
            int bestToken = 0;

            while (curState != null && textPointer < textArray.length) {
                char c = textArray[textPointer];
                State nextState = curState.getNext(c);
                if (nextState != null) {
                    lexeme[textPointer - tokenStartPointer] = c;
                    if (nextState.isAccepting()) {
                        bestToken = textPointer;
                    }
                } else {
                    if (bestToken == 0) {
                        textPointer = textArray.length;
                    } else {
                        for (int i = bestToken + 1; i <= textPointer - tokenStartPointer; i++) {
                            lexeme[i] = 0;
                        }
                        textPointer = bestToken;
                    }
                }
                textPointer++;
                curState = nextState;
            }

//            char[] tussen = new char[textPointer - tokenStartPointer];
//            for (int i = 0; i < textPointer - tokenStartPointer; i++) {
//                tussen[i] = lexeme [i];
//
//            }

            String lexStr = new String(lexeme);
            result.add(lexStr.trim());
        }


        System.out.println(result);


        return result;
    }
}
