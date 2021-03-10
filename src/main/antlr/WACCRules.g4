parser grammar WACCRules;
options {
  tokenVocab=WACCLexer;
}

func: type IDENT OPEN_PAREN paramList? CLOSE_PAREN IS stat END ;

paramList: param (COMMA param)* ;
param: type IDENT;

// Statements
stat: SKIP_                               #skipStat
    | param EQUAL assignRhs               #declarationStat
    | assignLhs EQUAL assignRhs           #assignmentStat
    | IF expr THEN stat ELSE stat FI      #ifStat
    | WHILE expr DO stat DONE             #whileStat
    | BEGIN stat END                      #beginStat
    | READ    assignLhs                   #readStat
    | FREE    expr                        #freeStat
    | EXIT    expr                        #exitStat
    | RETURN  expr                        #returnStat
    | PRINT   expr                        #printStat
    | PRINTLN expr                        #printlnStat
    | stat SEMICOLON stat                 #seqCompositionStat
    ;

assignLhs: IDENT
         | arrayElem
         | pairElem;

assignRhs: expr
         | arrayLiter
         | newPair
         | pairElem
         | funcCall;

argList: expr (COMMA expr)*;

type: baseType
    | pairType
    | type OPEN_SQR_PAREN CLOSE_SQR_PAREN
    ;

baseType: INT | BOOL | CHAR | STRING;
pairType: PAIR OPEN_PAREN pairElemType COMMA pairElemType CLOSE_PAREN;
pairElemType: baseType | type OPEN_SQR_PAREN CLOSE_SQR_PAREN | PAIR;

expr:
      BOOL_LITER                                      #boolLiteral
    | CHAR_LITER                                      #charLiteral
    | STR_LITER                                       #strLiteral
    | NULL                                            #pairLiteral
    | IDENT                                           #identifier
    | OPEN_PAREN expr CLOSE_PAREN                     #bracketedExpr
    | arrayElem                                       #arrayElemExpr
    | expr op=(MUL | DIV | MOD) expr                  #binOpExpr
    | expr op=(PLUS | MINUS) expr                     #binOpExpr
    | expr op=(GT | GE | LT | LE) expr                #binOpExpr
    | expr op=(EQ | NEQ) expr                         #binOpExpr
    | expr op=AND expr                                #binOpExpr
    | expr op=OR expr                                 #binOpExpr
    | (PLUS | MINUS)? INT_LITER                       #intLiteral
    | unaryOperator expr                              #unaryOpExpr
    ;

unaryOperator: NOT | MINUS | ORD | LEN | CHR ;


funcCall : CALL IDENT OPEN_PAREN argList? CLOSE_PAREN;
newPair: NEWPAIR OPEN_PAREN expr COMMA expr CLOSE_PAREN;
pairElem: FST expr | SND expr;

arrayLiter: OPEN_SQR_PAREN (expr (COMMA expr)* )? CLOSE_SQR_PAREN;
arrayElem: IDENT (OPEN_SQR_PAREN expr CLOSE_SQR_PAREN)+;
