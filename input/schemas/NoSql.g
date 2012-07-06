/**
* Grammer file for NoSql Query Language
*
*    @author HuaiJiang
*
**/


grammar NoSql;


options { 
	output = AST; 
	language = Java;
}

tokens {
	ATTR_NAME;
	PARAMETER_NAME;
	TABLE_NAME;
	VALUE_LIST;
	RESULT;
	INT_VAL;
	DEC_VAL;
	STR_VAL;
	SELECT_CLAUSE;
	FROM_CLAUSE;
	WHERE_CLAUSE;
	ALIAS;

	AS	=	'as';
	DOT	=	'.';
	EQ	=	'=';
	NE	=	'!=';
	GT	=	'>';
	GE	=	'>=';
	LT	=	'<';
	LE	=	'<=';
	OPEN 	=	'(';
	CLOSE   =	')';
	STAR	=	'*';
	COMMA	=	',';
	COLON	=	':';
	AND	=	'and';
	OR	=	'or';
	NOT	=	'not';
	IN	=	'in';
	
}

@header {
package com.alvazan.orm.parser.antlr;
}

@lexer::header {
package com.alvazan.orm.parser.antlr;
}


@rulecatch {
    catch (RecognitionException e)
    {
        throw e;
    }
}

statement: (  selectStatement  );

selectStatement: selectClause fromClause (whereClause)?;

selectClause: SELECT resultList -> ^(SELECT_CLAUSE resultList);

fromClause: FROM tableList -> ^(FROM_CLAUSE tableList);

whereClause: WHERE expression -> ^(WHERE_CLAUSE expression);
	
tableList: table (COMMA! table)*;
	
resultList:	(STAR | attributeList |) -> ^(RESULT attributeList? STAR?);
	
attributeList
	:	simpleAttribute (COMMA! simpleAttribute)*
	|	aliasdAttribute (COMMA! aliasdAttribute)*
	;
table: tableWithNoAlias | tableName alias -> ^(TABLE_NAME[$tableName.text] ALIAS[$alias.text]);
tableWithNoAlias: tableName -> TABLE_NAME[$tableName.text]; 

attribute: simpleAttribute | aliasdAttribute;

//This collapses the child node and renames the token ATTR_NAME while keeping the text of the token
simpleAttribute: ID -> ATTR_NAME[$ID.text];
	
aliasdAttribute: (alias)(DOT)(attrName) -> ALIAS[$alias.text] ATTR_NAME[$attrName.text];

expression: orExpr;

orExpr: andExpr (OR^ andExpr)*;

andExpr: primaryExpr (AND^ primaryExpr)*;

primaryExpr: compExpr | inExpr | parameterExpr;
	
//An attribute now is either a simpleAttribute OR a complex attribute
parameterExpr:	attribute (EQ | NE | GT | LT | GE | LE)^ parameter;
compExpr: attribute (EQ | NE | GT | LT | GE | LE)^ value;

tableName: ID;
parameterName: ID;
attrName: ID;
alias: ID;

inExpr	:	simpleAttribute IN^ valueList
	;

//This collapses the child node and renames the token PARAMETER_NAME while keeping the parameter text
parameter
	: 	COLON parameterName -> PARAMETER_NAME[$parameterName.text]
	;

valueList
	:	 value (COMMA value)*  -> ^(VALUE_LIST value (value)*)
	;

value: intVal | doubleVal | strVal;
	
intVal	:	INTEGER -> INT_VAL[$INTEGER.text];

doubleVal   :   DECIMAL -> DEC_VAL[$DECIMAL.text];
    
strVal	:	stringA | stringB;
	
stringA : STRINGA -> STR_VAL[$STRINGA.text];
stringB : STRINGB -> STR_VAL[$STRINGB.text];
	
SELECT	:	('S'|'s')('E'|'e')('L'|'l')('E'|'e')('C'|'c')('T'|'t');
FROM	:	('F'|'f')('R'|'r')('O'|'o')('M'|'m');
WHERE	:	('W'|'w')('H'|'h')('E'|'e')('R'|'r')('E'|'e');
// Lexer Rules
ID	:	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
	;

INTEGER :  ('0'..'9')+
    ;
    
DECIMAL	:	('.' ('0'..'9')+)  | (('0'..'9')+ '.' '0'..'9'*)
	;

STRINGA	:	'"' (options {greedy=false;}: ESC | .)* '"';
STRINGB	:	'\'' (options {greedy=false;}: ESC | .)* '\'';

WS	:
 	(   ' '
        |   '\t'
        |   '\r'
        |   '\n'
        )+
        { $channel=HIDDEN; }
        ;  
    
ESC	:
	'\\' ('"'|'\''|'\\')
	;
