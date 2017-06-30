/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */

/*
 * This is a grammar for DokuWiki internal & external links
 * Syntax:
 * [[external_link]]
 * [[internal_pagename]]
 * [[link | Text]]
 * [[some:namespaces]]
 * [[syntax#internal | this ]]
 * and InterWiki
 * [[wiki_type > Text]]
 *
 * NOTE: This parses only the content existing between '[[' and ']]' and
 * only one link at a time
 */
grammar DokuWikiLinksGrammar ;

fragment SCHEME : 'https' | 'http' | 'ftp' | 'mailto' | 'file' ; //[a-zA-Z][A-Za-z0-9.-]* ;
fragment USER : [a-zA-Z] [a-zA-Z0-9.-]* ;
fragment PASSWORD : .*? ;
fragment HOST : [a-zA-Z] [A-Za-z0-9.-]* ('.' [a-zA-Z] [a-zA-Z0-9._/-]* )*
		| [0-9]+ '.' [0-9]+ '.' [0-9]+ '.' [0-9]+ ;

fragment PORT : [0-9]+ ;
fragment PATH : [a-zA-Z] [a-zA-Z0-9.-]* ('/' [a-zA-Z] [a-zA-Z0-9.-]*)* ;
fragment QUERY_HELP: [a-zA-Z] [a-zA-Z0-9.-]* ('=' [a-zA-Z0-9] [a-zA-Z0-9.-]*)? ;
fragment QUERY : QUERY_HELP ('&' QUERY_HELP)* ;

fragment FRAGM : [a-zA-Z] [a-zA-Z0-9._-]*;

URL1 : SCHEME '://' (USER (':' PASSWORD)? '@')? HOST (':' PORT)? ( '?' QUERY)? ('#' FRAGM)? ;

PAGENAME : [a-zA-Z0-9] [a-zA-Z0-9._/-]* ('#' FRAGM)? ;

//TEXT : [a-zA-Z0-9 \n\t\\_.!@#$%^&*()+={};':"|<>,?/-]+ ;
TEXT : . ;

fragment NAMESPACE : [a-zA-Z] [a-zA-Z0-9._-]* ;

//NAMESPACES: PAGENAME (':' PAGENAME)* ;

//INTERWIKI_TEXT : [a-zA-Z] [a-zA-Z0-9_.-]+ ;

START_FRAGM: {_input.LA(-1) < 0}? '#' [a-zA-Z]  (~('|' | '\n'))* ;

links_start : URL1  EOF									# external1
		| PAGENAME EOF								# internal1
		| URL1 ' '*? '|' .*? EOF						# external_text
		| PAGENAME ' '*? '|' .*? EOF						# internal_text
		| PAGENAME (':' PAGENAME)*? ':' PAGENAME EOF				# internal_ns
		| PAGENAME (':' PAGENAME)*? ':' PAGENAME ' '*? '|' .*? EOF		# internal_ns_text
		| PAGENAME ' '*? '>' ' '*? PAGENAME (':' PAGENAME)*? EOF		# interwiki
		| PAGENAME ' '*? '>' ' '*? PAGENAME (':' PAGENAME)*? ' '*? '|' .*? EOF	# interwiki_text
		| START_FRAGM EOF							# thispage_fragm
		| START_FRAGM '|' .*? EOF						# thispage_fragm_text
		;
