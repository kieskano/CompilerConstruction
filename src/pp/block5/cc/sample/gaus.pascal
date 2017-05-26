Program gaus;

Var i, n, r: Integer;

Begin
    In("n? ", n);
    i := 1;
    While i <= n Do
    Begin
        r := r+i;
        i := i+1;
    End;
    Out("Result is: ", r);
End.