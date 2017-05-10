grammar MySentence;

/** Full sentence: the start symbol of the grammar. */
sentence: subject VERB object ENDMARK;
/** Grammatical subject in a sentence. */
subject : modifier subject  # modSubject
        | NOUN              # simpleSubject
        ;
/** Grammatical object in a sentence. */
object: modifier object | NOUN;
/** Modifier in an object or subject. */
modifier: modifier ',' modifier //<assoc=right> modifier ',' modifier
        | ADJECTIVE
        ;

NOUN : 'students' | 'compilers' ;
VERB : 'love' ;
ADJECTIVE : 'all' | 'smart' | 'undergraduate' ;
ENDMARK : '.' ;

// ignore whitespace
WS : [ \t\n\r] -> skip;

// everything else is a typo
TYPO : [a-zA-Z]+;

// all, smart, undergratuate students love compilers.
//(sentence (subject (modifier (modifier all) , (modifier (modifier smart) , (modifier undergraduate))) (subject students)) love (object compilers) .) == right associative
//(sentence (subject (modifier (modifier (modifier all) , (modifier smart)) , (modifier undergraduate)) (subject students)) love (object compilers) .)