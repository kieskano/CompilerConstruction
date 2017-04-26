package pp.block1.cc.dfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wijtse on 26-4-2017.
 */
public class MyScanner implements Scanner {

    @Override
    public List<String> scan(State dfa, String text) {
        List<String> result = new ArrayList<String>();
        
        char[] textChars = text.toCharArray();
        int textPointer = 0;

        while (textPointer < text.length()) {
            char[] currentToken = new char[text.length()];
            int currentTokenPointer = 0;
            State currentState = dfa;

            boolean foundAcceptingState = false;
            int btTextPointer = 0;
            int btCurrentTokenPointer = 0;

            while (currentState != null && textPointer < text.length()) {
                currentState = currentState.getNext(textChars[textPointer]);
                currentToken[currentTokenPointer] = textChars[textPointer];

                textPointer++;
                currentTokenPointer++;

                if (currentState != null && currentState.isAccepting()) {
                    btTextPointer = textPointer;
                    btCurrentTokenPointer = currentTokenPointer;
                    foundAcceptingState = true;
                }
            }

            if (foundAcceptingState) {
                char[] token = new char[btCurrentTokenPointer];
                for (int i = 0; i < btCurrentTokenPointer; i++) {
                    token[i] = currentToken[i];
                }
                result.add(new String(token));

                textPointer = btTextPointer;
            } else {
                break;
            }
        }

        return result;
    }
}
