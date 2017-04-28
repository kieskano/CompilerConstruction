lexer grammar Grammar1CC7;

LA          : L(a)+(_)*;
LALA        : L(a)+(_)*L(a)+(_)*;
LALALALI    : L(a)+(_)*L(a)+(_)*L(a)+(_)*Li(_)*;
WS          : [ \t\r\n]+ -> skip;
