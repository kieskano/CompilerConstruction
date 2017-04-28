lexer grammar Grammar1CC5;

fragment LETTER : 'a'..'z' | 'A'..'Z';
fragment NUMBER : '0'..'9';

TOKEN : LETTER (LETTER | NUMBER) (LETTER | NUMBER) (LETTER | NUMBER) (LETTER | NUMBER) (LETTER | NUMBER);
