package pp.block3.cc.antlr;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import pp.block3.cc.antlr.MyCalcParser.*;

public class MyCalculator extends MyCalcBaseListener {
    /** Map storing the val attribute for all parse tree nodes. */
    private ParseTreeProperty<Type> types;

    /** Initialises the calculator before using it to walk a tree. */
    public void init() {
        this.types = new ParseTreeProperty<Type>();
    }


    @Override
    public void exitPower(PowerContext ctx) {
        set(ctx, fpower(type(ctx.expr(0)), type(ctx.expr(1))));
    }

    @Override
    public void exitPlus(PlusContext ctx) {
        set(ctx, fplus(type(ctx.expr(0)), type(ctx.expr(1))));
    }

    @Override
    public void exitEqual(EqualContext ctx) {
        set(ctx, fequal(type(ctx.expr(0)), type(ctx.expr(1))));
    }

    @Override
    public void exitBr(BrContext ctx) {
        set(ctx, type(ctx.expr()));
    }

    @Override
    public void exitNumber(NumberContext ctx) {
        set(ctx, Type.NUM);
    }

    @Override
    public void exitBool(BoolContext ctx) {
        set(ctx, Type.BOOL);
    }

    @Override
    public void exitStr(StrContext ctx) {
        set(ctx, Type.STR);
    }

    /** Sets the val attribute of a given node. */
    private void set(ParseTree node, Type type) {
        this.types.put(node, type);
    }

    /** Retrieves the val attribute of a given node. */
    public Type type(ParseTree node) {
        return this.types.get(node);
    }

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
