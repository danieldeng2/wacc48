lexer grammar WACCLexer;

//skip white spaces and comments
WS: [ \n\t\r]+ -> skip ;
COMMENT: '#' ~[\r\n]* '\r'? '\n' -> skip ;

//reserved keywords
BEGIN: 'begin';
END: 'end';
IS: 'is';
SKIP_: 'skip';
READ: 'read';
FREE: 'free';
RETURN: 'return';
EXIT: 'exit';
PRINT: 'print';
PRINTLN: 'println';
IF: 'if';
THEN: 'then';
ELSE: 'else';
FI: 'fi';
WHILE: 'while';
DO: 'do';
DONE: 'done';
CALL: 'call';

// Pairs
NEWPAIR: 'newpair';
FST: 'fst';
SND: 'snd';
PAIR: 'pair';

//Base types
INT: 'int';
BOOL: 'bool';
CHAR: 'char';
STRING: 'string';

//unary operators
NOT: '!' ;
LENGTH: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

//binary operators
PLUS: '+' ;
MINUS: '-' ;
MULT: '*' ;
DIV: '/' ;
MOD: '%' ;

//comparison operators
GREATER_THAN: '>' ;
GREATER_THAN_EQUAL: '>=' ;
LESS_THAN: '<' ;
LESS_THAN_EQUAL: '<=' ;
EQUALS: '==' ;
NOT_EQUALS: '!=' ;
AND: '&&' ;
OR: '||' ;

IDENT: ('_' | [a-z] | [A-Z]) ('_' | [a-z] | [A-Z] | [0-9])* ;

// Fragments
fragment DIGIT: '0'..'9' ;
fragment CHARACTER: ~('"' | '\'' | '\\') | ('\\' ESCAPED_CHAR) ;
fragment ESCAPED_CHAR: '0' | 'b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\' ;

// Type literals
INT_LITER: (PLUS | MINUS)? DIGIT+ ;
BOOL_LITER: 'true' | 'false' ;
CHAR_LITER: '\'' CHARACTER '\'' ;
STR_LITER: '"' CHARACTER* '"' ;

//array liter is defined in the parser
//to be used in pair_liter in the parser
NULL: 'null' ;

//brackets
OPEN_PAREN: '(' ;
CLOSE_PAREN: ')' ;
OPEN_SQR_PAREN: '[' ;
CLOSE_SQR_PAREN: ']' ;

COMMA: ',';
SEMICOLON: ';';

EQUAL: '=';