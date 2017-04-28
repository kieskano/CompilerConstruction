lexer grammar Grammar1CC6;

PL1STRING   : ('"'(~'"')*'"')+;
WS          : [ \t\r\n]+ -> skip;
