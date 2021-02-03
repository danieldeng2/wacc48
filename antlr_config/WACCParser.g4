parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

prog: BEGIN func* stat END ;

func: type IDENT OPEN_PAREN param_list? CLOSE_PAREN IS stat END ;

param_list: param (COMMA param)* ;
param: type IDENT;

// Statements
stat: SKIP_                               #skipStat
    | type IDENT EQUAL assign_rhs         #declarationStat
    | assign_lhs EQUAL assign_rhs         #assignmentStat
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

assign_lhs: IDENT
          | array_elem
          | pair_elem;

assign_rhs: expr
          | array_liter
          | NEWPAIR OPEN_PAREN expr COMMA expr CLOSE_PAREN
          | pair_elem
          | CALL IDENT OPEN_PAREN arg_list CLOSE_PAREN;

arg_list: expr (COMMA expr)*;

type: base_type | pair_type | type OPEN_SQR_PAREN CLOSE_SQR_PAREN;
base_type: INT | BOOL | CHAR | STRING;
pair_type: PAIR OPEN_PAREN pair_elem_type COMMA pair_elem_type CLOSE_PAREN;
pair_elem_type: base_type | type OPEN_SQR_PAREN CLOSE_SQR_PAREN | PAIR;

expr: INT_LITER                             #intLiteral
    | BOOL_LITER                            #boolLiteral
    | CHAR_LITER                            #charLiteral
    | STR_LITER                             #strLiteral
    | pair_liter                            #pairLiteral
    | IDENT                                 #identifier
    | array_elem                            #arrayElem
    | unary_oper expr                       #unaryOpExpr
    | expr binary_oper expr                 #binOpExpr
    | OPEN_PAREN expr CLOSE_PAREN           #bracketedExpr
    ;

unary_oper: NOT
          | MINUS
          | LENGTH
          | ORD
          | CHR ;

binary_oper: MULT
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

array_elem: IDENT (OPEN_SQR_PAREN expr CLOSE_SQR_PAREN)+ ;
pair_elem: FST expr | SND expr;

array_liter: OPEN_SQR_PAREN (expr (COMMA expr)* )? CLOSE_SQR_PAREN ;
pair_liter: NULL ;