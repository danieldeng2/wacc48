parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

prog: BEGIN func* stat END ;

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
    | READ    expr                        #readStat
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

expr: INT_LITER                                       #intLiteral
    | BOOL_LITER                                      #boolLiteral
    | CHAR_LITER                                      #charLiteral
    | STR_LITER                                       #strLiteral
    | NULL                                            #pairLiteral
    | IDENT                                           #identifier
    | OPEN_PAREN expr CLOSE_PAREN                     #bracketedExpr
    | arrayElem                                       #arrayElemExpr
    | unaryOper expr                                  #unaryOpExpr
    | expr binaryOper expr                            #binOpExpr
    ;

unaryOper: NOT
          | MINUS
          | LENGTH
          | ORD
          | CHR
          ;

binaryOper: MULT
           | DIV
           | MOD
           | PLUS
           | MINUS
           | GREATER_THAN
           | GREATER_THAN_EQUAL
           | LESS_THAN
           | LESS_THAN_EQUAL
           | EQUALS
           | NOT_EQUALS
           | AND
           | OR
           ;

funcCall : CALL IDENT OPEN_PAREN argList CLOSE_PAREN;
newPair: NEWPAIR OPEN_PAREN expr COMMA expr CLOSE_PAREN;
pairElem: FST expr | SND expr;

arrayLiter: OPEN_SQR_PAREN (expr (COMMA expr)* )? CLOSE_SQR_PAREN;
arrayElem: IDENT (OPEN_SQR_PAREN expr CLOSE_SQR_PAREN)+;