grammar MyCalc;

import MyCalcVocab;

expr : expr POWER expr # power
     | expr PLUS expr  # plus
     | expr EQUAL expr # equal
     | LBR expr RBR    # br
     | NUMBER          # number
     | BOOL            # bool
     | STR             # str
     ;
