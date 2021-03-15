parser grammar WACCParser;
import WACCRules;
options {
  tokenVocab=WACCLexer;
}

prog: BEGIN func* stat END EOF ;