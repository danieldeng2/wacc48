parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

pair_liter: NULL ;
prog: (pair_liter)*  EOF ;