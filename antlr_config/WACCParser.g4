parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

prog: BEGIN func* stat END ;

func: type IDENT OPEN_PAREN param_list? CLOSE_PAREN IS stat END ;

param_list: param (COMMA param)* ;
param: type IDENT;

// Statements
stat: SKIP_
    | declaration_stat
    | assignment_stat
    | if_stat
    | while_stat
    | begin_stat
    | READ    expr
    | FREE    expr
    | EXIT    expr
    | RETURN  expr
    | PRINT   expr
    | PRINTLN expr
    | stat SEMICOLON stat;


declaration_stat: type IDENT EQUAL assign_rhs;
assignment_stat:  assign_lhs EQUAL assign_rhs;
if_stat:    IF expr THEN stat ELSE stat FI;
while_stat: WHILE expr DO stat DONE;
begin_stat: BEGIN stat END;

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

expr: INT_LITER
    | BOOL_LITER
    | CHAR_LITER
    | STR_LITER
    | pair_liter
    | IDENT
    | array_elem
    | unary_oper expr
    | expr binary_oper expr
    | OPEN_PAREN expr CLOSE_PAREN
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