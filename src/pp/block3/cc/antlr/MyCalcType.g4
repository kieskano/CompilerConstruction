grammar MyCalcType;

import MyCalcVocab;

@members {

    private Type fpower(Type t1, Type t2) {
        if (t1 != Type.BOOL && t2 == Type.NUM) {
            return t1;
        } else {
            return Type.ERR;
        }
    }

    private Type fplus(Type t1, Type t2) {
        if (t1 == t2) {
            return t1;
        } else {
            return Type.ERR;
        }
    }

    private Type fequal(Type t1, Type t2) {
            if (t1 == t2) {
                return Type.BOOL;
            } else {
                return Type.ERR;
            }
        }
}


expr returns [ Type type ]
     : e0=expr POWER e1=expr
       { $type = fpower($e0.type, $e1.type); }

     | e0=expr PLUS e1=expr
       { $type = fplus($e0.type, $e1.type); }

     | e0=expr EQUAL e1=expr
       { $type = fequal($e0.type, $e1.type); }

     | LBR e=expr RBR
       { $type = $e.type; }

     | NUMBER
       { $type = Type.NUM; }

     | BOOL
            { $type = Type.BOOL; }

     | STR
            { $type = Type.STR; }
     ;
