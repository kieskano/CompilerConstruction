lexer grammar Grammar1CC7;

LA          : 'L'('a')+('_')*;
LALA        : LA LA;
LALALALI    : 'L'('a')+('_')*'L'('a')+('_')*'L'('a')+('_')*'Li'('_')*;
WS          : [ \t\r\n]+ -> skip;
