grammar Tabular;

table       : BEGIN ARG content END;
content     : rowContent content    # contentRow
            |                       # contentEmpty
            ;
rowContent  : ENTRY? restRow;
restRow     : COLUMN ENTRY? restRow  # restRowEntry
            | ROW                   # restRowEnd
            ;


BEGIN : [ \t\n\r]* '\\begin{tabular}';
ARG : '{' [lcr]* '}' WS*;
ENTRY   : (~[\\{}$&#^_~%\t\n\r ])+
        | (~[\\{}$&#^_~%\t\n\r ])+ (' '* (~[\\{}$&#^_~%\t\n\r ])+)+;
COLUMN : WS* '&' WS*;
ROW : WS* '\\\\' WS*;
END : WS* '\\end{tabular}' WS*;
fragment WS : [ \t\n\r];

COMMENT : '%' (~[\n])* '\n' -> skip;


//tabular     : begin arg content end;
//content     : rowContent content
//            |
//            ;
//rowContent  : ENTRY restRow;
//restRow     : column ENTRY restRow
//            | row
//            ;
//arg     : WS* ARG WS*;
//begin   : WS* BEGIN WS*;
//end     : WS* END WS*;
//row     : WS* ROW WS*;
//column  : WS* COLUMN WS*;
//
//BEGIN : '\\begin{tabular}';
//ARG : '{' [lcr]* '}';
//ENTRY : (~[\\{}$&#^_~%\n\r])*;
//COLUMN : '&';
//ROW : '\\\\';
//END : '\\end{tabular}';
//WS : [ \t\n\r];