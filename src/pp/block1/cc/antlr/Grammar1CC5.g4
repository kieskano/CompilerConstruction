lexer grammar Grammar1CC5;

fragment LETTER : 'a'..'z' | 'A'..'Z';
fragment NUMBER : '0'..'9';

IDENTIFIER  : LETTER (LETTER | NUMBER) (LETTER | NUMBER) (LETTER | NUMBER) (LETTER | NUMBER) (LETTER | NUMBER);
WS          : [ \t\r\n]+ -> skip;
