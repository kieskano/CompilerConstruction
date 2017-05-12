lexer grammar MyCalcVocab;

POWER  : '^';
PLUS   : '+';
EQUAL  : '==';
LBR    : '(';
RBR    : ')';

NUMBER : [0-9]+;
BOOL   : 'true' | 'false';
STR    : '"' (~('"'))* '"';

// ignore whitespace
WS : [ \t\n\r] -> skip;
