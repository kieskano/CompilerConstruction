grammar Arith;

expr    : term
        | expr OPP term;
term    : factor
        | term OPM factor;
factor  : power
        | <assoc=right> power POW factor;
power   : NR
        | BROP expr BRCL;

OPP : '+' | '-';
OPM : '*' | '/';
NR : [-]*[123456789] [0123456789]*;
POW : '^';
BROP : '(';
BRCL : ')';

// ignore whitespace
WS : [ \t\n\r] -> skip;