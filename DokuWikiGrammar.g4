/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */


grammar DokuWikiGrammar;

wiki_start : wstart;

wstart:		formatted_text* EOF					# start
		| START_LIST_TOKEN formatted_text* EOF			# start_list
		| START_TABLES_TOKEN formatted_text* EOF		# start_table
		;
formatted_text :  HEADLINE raw_text+ HEADLINE				# headline
/*		| BOLD formatted_text BOLD				# bold
		| ITALIC formatted_text ITALIC				# italic
		| UNDERLINE formatted_text UNDERLINE			# underline
		| MONOSPACED formatted_text MONOSPACED			# monospaced
		| SUP_START formatted_text SUP_END			# superscript
		| SUB_START formatted_text SUB_END			# subscript
		| DEL_START formatted_text DEL_END			# del
		| FOOTNOTE_START formatted_text FOOTNOTE_END		# footnote
*/
		| combined						# combined_text

		| HORIZONTAL_LINE					# hline
//		| raw_text+?						# raw
		| UNPARSED						# unparsed_token
//		| NOWIKI_TOKEN						# nowiki
//		| NOWIKI_TOKEN2						# nowiki2
//		| NOTE_TOKEN						# note
		| NOTE_START formatted_text+? NOTE_END			# note
		| CODE_TOKEN1						# code
		| FILE_TOKEN						# file
		| LATEX_TOKEN_S						# latexs
		| LATEX_TOKEN_SS					# latexss
		| LATEX_TOKEN_TAG					# latextag
		| LATEX_TOKEN_DISPLAYMATH				# latex_displaymath
		| LATEX_TOKEN_EQNARRAY					# latex_eqnarray
		| LATEX_TOKEN_EQNARRAY_STAR				# latex_eqnarray_star
		| LATEX_TOKEN_EQUATION					# latex_equation
		| LATEX_TOKEN_EQUATION_STAR				# latex_equation_star
		| LATEX_TOKEN_ALIGN					# latex_align
		| LATEX_TOKEN_ALIGN_STAR				# latex_align_star
		| LATEX_TOKEN_ALIGNAT					# latex_alignat
		| LATEX_TOKEN_ALIGNAT_STAR				# latex_alignat_star
		| LATEX_TOKEN_FLALIGN					# latex_flalign
		| LATEX_TOKEN_FLALIGN_STAR				# latex_flalign_star
		| LATEX_TOKEN_GATHER					# latex_gather
		| LATEX_TOKEN_GATHER_STAR				# latex_gather_star
		| LATEX_TOKEN_MULTILINE					# latex_multiline
		| LATEX_TOKEN_MULTILINE_STAR				# latex_multiline_star
		| LATEX_TOKEN_MATH					# latex_math
		| URL1							# url1
		| LINK							# link
		| MAIL							# mail
		| LIST_TOKEN						# list
		| TABLES_TOKEN						# tables
		| MEDIA_FILES_TOKEN					# media_files
		| QUOTING_TOKEN 					# quoting
		| HTML_TOKEN						# html
		| PHP_TOKEN						# php
		;


combined : BOLD combined+? BOLD						# bold
	| ITALIC combined+? ITALIC					# italic
	| UNDERLINE combined+? UNDERLINE				# underline
	| MONOSPACED combined+? MONOSPACED				# monospaced
	| SUP_START combined+? SUP_END					# superscript
	| SUB_START combined+? SUB_END					# subscript
	| DEL_START combined+? DEL_END					# del
	| FOOTNOTE_START combined+? FOOTNOTE_END			# footnote
//	| NOTE_START combined+? NOTE_END				# note
	| NOWIKI_TOKEN						# nowiki
	| NOWIKI_TOKEN2						# nowiki2
	| raw_text+?							# raw
	;

H : '=';
HEADLINE : H H+;

BOLD : '**';

ITALIC : '//';

UNDERLINE : '__';

MONOSPACED : '\'\'';

SUP_START : '<sup>' ;
SUP_END : '</sup>';

SUB_START : '<sub>';
SUB_END : '</sub>';

DEL_START : '<del>';
DEL_END : '</del>';

NOWIKI_START: '<nowiki>' ;
NOWIKI_END: '</nowiki>' ;
NOWIKI_TOKEN : NOWIKI_START .*? NOWIKI_END ;
NOWIKI_TOKEN2 : '%%' .*? '%%' ;


NOTE_START : '<note' .*? '>' ;
NOTE_END : '</note>';
// NOTE_TOKEN : NOTE_START .*? NOTE_END ;

fragment CODE_START : ('<code' | '<CODE') (~'>')* '>' ;
fragment CODE_END : '</code>' | '</CODE>';
CODE_TOKEN1: CODE_START .*? CODE_END;

fragment FILE_START : ('<file' | '<FILE') .*? '>' ;
fragment FILE_END : '</file>' | '</FILE>';
FILE_TOKEN: FILE_START .*? FILE_END;


HTML_TOKEN :  ( '<html>' | '<HTML>')  .*?  ( '</html>' | '</HTML>')  ;
PHP_TOKEN : ('<php>' | '<PHP>')  .*? ('</php>' | '</PHP>')  ;

LINE : '-' ;
HORIZONTAL_LINE : {_input.LA(-1) == '\n' || _input.LA(-1) < 0}? LINE LINE LINE LINE+ '\n' ;

FOOTNOTE_START : '((' ;
FOOTNOTE_END : '))' ;

/*
 * LaTeX plugins support.
 * plugins: latex, mathjax
 */

fragment SPACES : ' ' | '\t' ;
fragment START_DISPLAYMATH : '\\begin{' SPACES*? 'displaymath' SPACES*? '}' ;
fragment END_DISPLAYMATH : '\\end{' SPACES*? 'displaymath' SPACES*? '}';

fragment START_EQNARRAY : '\\begin{' SPACES*? 'eqnarray' SPACES*? '}' ;
fragment END_EQNARRAY : '\\end{' SPACES*? 'eqnarray' SPACES*? '}';

fragment START_EQNARRAY_STAR : '\\begin{' SPACES*? 'eqnarray*' SPACES*? '}' ;
fragment END_EQNARRAY_STAR : '\\end{' SPACES*? 'eqnarray*' SPACES*? '}';

fragment START_EQUATION : '\\begin{' SPACES*? 'equation' SPACES*? '}' ;
fragment END_EQUATION : '\\end{' SPACES*? 'equation' SPACES*? '}';

fragment START_EQUATION_STAR : '\\begin{' SPACES*? 'equation*' SPACES*? '}' ;
fragment END_EQUATION_STAR : '\\end{' SPACES*? 'equation*' SPACES*? '}';

fragment START_ALIGN : '\\begin{' SPACES*? 'align' SPACES*? '}' ;
fragment END_ALIGN : '\\end{' SPACES*? 'align' SPACES*? '}';

fragment START_ALIGN_STAR : '\\begin{' SPACES*? 'align*' SPACES*? '}' ;
fragment END_ALIGN_STAR : '\\end{' SPACES*? 'align*' SPACES*? '}';

fragment START_ALIGNAT : '\\begin{' SPACES*? 'alignat' SPACES*? '}' ;
fragment END_ALIGNAT : '\\end{' SPACES*? 'alignat' SPACES*? '}';

fragment START_ALIGNAT_STAR : '\\begin{' SPACES*? 'alignat*' SPACES*? '}' ;
fragment END_ALIGNAT_STAR : '\\end{' SPACES*? 'alignat*' SPACES*? '}';

fragment START_FLALIGN : '\\begin{' SPACES*? 'flalign' SPACES*? '}' ;
fragment END_FLALIGN : '\\end{' SPACES*? 'flalign' SPACES*? '}';

fragment START_FLALIGN_STAR : '\\begin{' SPACES*? 'flalign*' SPACES*? '}' ;
fragment END_FLALIGN_STAR : '\\end{' SPACES*? 'flalign*' SPACES*? '}';

fragment START_GATHER : '\\begin{' SPACES*? 'gather' SPACES*? '}' ;
fragment END_GATHER : '\\end{' SPACES*? 'gather' SPACES*? '}';

fragment START_GATHER_STAR : '\\begin{' SPACES*? 'gather*' SPACES*? '}' ;
fragment END_GATHER_STAR : '\\end{' SPACES*? 'gather*' SPACES*? '}';

fragment START_MULTILINE : '\\begin{' SPACES*? 'multline' SPACES*? '}' ;
fragment END_MULTILINE : '\\end{' SPACES*? 'multline' SPACES*? '}';

fragment START_MULTILINE_STAR : '\\begin{' SPACES*? 'multline*' SPACES*? '}' ;
fragment END_MULTILINE_STAR : '\\end{' SPACES*? 'multline*' SPACES*? '}';

fragment START_MATH : '\\begin{' SPACES*? 'math' SPACES*? '}' ;
fragment END_MATH : '\\end{' SPACES*? 'math' SPACES*? '}';

LATEX_TOKEN_S			: '$' .*? '$' ;
LATEX_TOKEN_SS			: '$$' .*? '$$' ;
LATEX_TOKEN_TAG			: '<latex>' .*? '</latex>' ;
LATEX_TOKEN_DISPLAYMATH		: START_DISPLAYMATH .*? END_DISPLAYMATH ;
LATEX_TOKEN_EQNARRAY		: START_EQNARRAY .*? END_EQNARRAY ;
LATEX_TOKEN_EQNARRAY_STAR	: START_EQNARRAY_STAR .*? END_EQNARRAY_STAR ;
LATEX_TOKEN_EQUATION		: START_EQUATION .*? END_EQUATION ;
LATEX_TOKEN_EQUATION_STAR	: START_EQUATION_STAR .*? END_EQUATION_STAR ;

LATEX_TOKEN_ALIGN		: START_ALIGN .*? END_ALIGN ;
LATEX_TOKEN_ALIGN_STAR		: START_ALIGN_STAR .*? END_ALIGN_STAR ;
LATEX_TOKEN_ALIGNAT		: START_ALIGNAT .*? END_ALIGNAT ;
LATEX_TOKEN_ALIGNAT_STAR	: START_ALIGNAT_STAR .*? END_ALIGNAT_STAR ;
LATEX_TOKEN_FLALIGN		: START_FLALIGN .*? END_FLALIGN ;
LATEX_TOKEN_FLALIGN_STAR	: START_FLALIGN_STAR .*? END_FLALIGN_STAR ;
LATEX_TOKEN_GATHER		: START_GATHER .*? END_GATHER ;
LATEX_TOKEN_GATHER_STAR		: START_GATHER_STAR .*? END_GATHER_STAR ;
LATEX_TOKEN_MULTILINE		: START_MULTILINE .*? END_MULTILINE ;
LATEX_TOKEN_MULTILINE_STAR	: START_MULTILINE_STAR .*? END_MULTILINE_STAR ;
LATEX_TOKEN_MATH		: START_MATH .*? END_MATH ;

/*
 * External & Internal Links
 */

fragment SCHEME : 'https' | 'http' | 'ftp' | 'mailto' | 'file' ; //[a-zA-Z][A-Za-z0-9.-]* ;
fragment USER : [a-zA-Z] [a-zA-Z0-9.-]* ;
fragment PASSWORD : .*? ;
fragment HOST : [a-zA-Z] [A-Za-z0-9.-]* ('.' [a-zA-Z] [a-zA-Z0-9.-])*
		| [0-9]+ '.' [0-9]+ '.' [0-9]+ '.' [0-9]+ ;

fragment PORT : [0-9]+ ;
fragment PATH : [a-zA-Z] [a-zA-Z0-9.-]* ('/' [a-zA-Z] [a-zA-Z0-9.-]*)* ;
fragment QUERY_HELP: [a-zA-Z] [a-zA-Z0-9.-]* ('=' [a-zA-Z0-9] [a-zA-Z0-9.-]*)? ;
fragment QUERY : QUERY_HELP ('&' QUERY_HELP)* ;

fragment FRAGM : [a-zA-Z] [a-zA-Z0-9._-]*;

URL1 : SCHEME '://' (USER (':' PASSWORD)? '@')? HOST (':' PORT)? ( '?' QUERY)? ('#' FRAGM)? ;
//URL2 : (USER (':' PASSWORD)? '@')? HOST (':' PORT)? ( '?' QUERY)? ('#' FRAGM)? ;

LINK : '[[' .*? ']]' ;
MAIL : '<' USER '@' HOST '>' ;

/*
 * List implementation
 * There is a different implementation for lists
 */

LIST_TOKEN : ('\n' '  '+ ' '? ('-' | '*') ~('\n' | '<' | '\\')*
						(CODE_START (. | '\n')*? CODE_END
						| NOWIKI_START (. | '\n')*? NOWIKI_END
						| FILE_START (. | '\n')*? FILE_END
//						| HTML_TOKEN
//						| PHP_TOKEN
						| '<latex>' '\n'? .*? '</latex>'
						| '\\begin{' .*? '}' (. | '\n')*? '\\end{' .*? '}'
						)* (~'\n')*? {_input.LA(1) == '\n' || _input.LA(1) == EOF}?
		)+ 
		;

START_LIST_TOKEN : '  '+ ' '? ('-' | '*') ~('\n' | '<' | '\\')*
						(CODE_START (. | '\n')*? CODE_END
						| NOWIKI_START (. | '\n')*? NOWIKI_END
						| FILE_START (. | '\n')*? FILE_END
//						| HTML_TOKEN
//						| PHP_TOKEN
						| '<latex>' '\n'? .*? '</latex>'
						| '\\begin{' .*? '}' (. | '\n')*? '\\end{' .*? '}'
						)* (~'\n')*? {_input.LA(1) == '\n' || _input.LA(1) == EOF}?
		LIST_TOKEN*
		;

/*
 * Tables implementation
 * Tables will be parsed in a new grammar
 */

/*TABLES_TOKEN : ('\n'
		('^' | '|')
		~('\n')*
		)+;
*/

TABLES_TOKEN : ('\n' ('^' | '|') ~('\n' | '<' | '\\')*
						(CODE_START (. | '\n')*? CODE_END
						| NOWIKI_START (. | '\n')*? NOWIKI_END
						| FILE_START (. | '\n')*? FILE_END
//						| HTML_TOKEN
//						| PHP_TOKEN
						| '<latex>' '\n'? .*? '</latex>'
						| '\\begin{' .*? '}' (. | '\n')*? '\\end{' .*? '}'
						)* (~'\n')* {_input.LA(1) == '\n' || _input.LA(1) == EOF}?
		)+ 
		;

START_TABLES_TOKEN : {_input.LA(-1) < 0}? ('^' | '|' )  ~('\n' | '<' | '\\')*
						(CODE_START (. | '\n')*? CODE_END
						| NOWIKI_START (. | '\n')*? NOWIKI_END
						| FILE_START (. | '\n')*? FILE_END
//						| HTML_TOKEN
//						| PHP_TOKEN
						| '<latex>' '\n'? .*? '</latex>'
						| '\\begin{' .*? '}' (. | '\n')*? '\\end{' .*? '}'
						)* (~'\n')* {_input.LA(1) == '\n' || _input.LA(1) == EOF}?
		TABLES_TOKEN*
		;

/*
 * Media Files
 */
MEDIA_FILES_TOKEN : '{{' .*? '}}' ;

/*
 * Quoting
 */
//QUOTING_TOKEN : ('\n' '>'+ (~'\n')* {_input.LA(1) == '\n' || _input.LA(1) == EOF}?
//		| {_input.LA(-1) < 0}? '>'+ (~'\n')* {_input.LA(1) == '\n' || _input.LA(1) == EOF}? )+;
QUOTING_TOKEN : (('\n' '>'+ | {_input.LA(-1) < 0}? '>'+) ~('\n' | '<' | '\\')*
						(CODE_START (. | '\n')*? CODE_END
						| NOWIKI_START (. | '\n')*? NOWIKI_END
						| FILE_START (. | '\n')*? FILE_END
						| '<latex>' '\n'? .*? '</latex>'
						| '\\begin{' .*? '}' (. | '\n')*? '\\end{' .*? '}'
						)* (~'\n')* {_input.LA(1) == '\n' || _input.LA(1) == EOF}?
		)+
		;

SPECIAL_CHAR : [ăĂâÂîÎţțŢşșŞ] ;
ANYCHAR : ([a-zA-Z0-9 \t\\] | SPECIAL_CHAR)+ ;
BACKSLASH_N : '\n' ;

raw_text : UNPARSED? ANYCHAR  | LINE? ANYCHAR | SPECIAL_CHAR | H? ANYCHAR | BACKSLASH_N | UNPARSED | LINE | H;

UNPARSED : . ;

