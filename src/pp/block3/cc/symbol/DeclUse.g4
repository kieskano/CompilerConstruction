grammar DeclUse;

program : '(' series ')' ;
series  : unit* ;
unit    : decl              # unitDecl
        | use               # unitUse
        | '(' series ')'    # unitSeries;
decl    : 'D:' ID ;
use     : 'U:' ID ;

ID : [a-zA-Z]+;
WS : [ \t\n\r]+ -> skip;
