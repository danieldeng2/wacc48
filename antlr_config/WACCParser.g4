parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

pair_liter: NULL ;
prog: (pair_liter)*  EOF ;

// Statements
stat:
     SKIP;

declaration: type IDENT

type: INT | BOOL | CHAR | STRING;