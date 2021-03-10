parser grammar WACCShellParser;
import WACCRules;
options {
  tokenVocab=WACCLexer;
}

command: func EOF
       | stat EOF
       | expr EOF
       ;