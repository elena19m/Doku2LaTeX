/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */

grammar DokuWikiTablesGrammar;

table: row+ EOF ;

/*
row : '\n' ( ('|' |'^') combined* )+ ('|' | '^')?
		{_input.LA(1) == '\n' | _input.LA(1) == EOF}?
	;
*/

row : '\n' cell+ ('|' | '^') ;//{_input.LA(1) == EOF || _input.LA(1) == '\n'}? ;

cell : ('|' | '^') cell_formatted_text*?
//	{_input.LA(1) == '|' || _input.LA(1) == '^'}?
	;

cell_formatted_text : combined				# table_combined
			| NOWIKI_TOKEN			# table_nowiki
			| NOWIKI_TOKEN2			# table_nowiki2
			| CODE_TOKEN1			# table_code
			| FILE_TOKEN			# table_file
			| LATEX_TOKEN_S			# table_latexs
			| LATEX_TOKEN_SS		# table_latexss
			| LATEX_TOKEN_TAG		# table_latextag
			| LATEX_TOKEN_DISPLAYMATH	# table_latex_displaymath
			| LATEX_TOKEN_EQNARRAY		# table_latex_eqnarray
			| LATEX_TOKEN_EQNARRAY_STAR	# table_latex_eqnarray_star
			| LATEX_TOKEN_EQUATION		# table_latex_equation
			| LATEX_TOKEN_EQUATION_STAR	# table_latex_equation_star
			| LATEX_TOKEN_ALIGN		# table_latex_align
			| LATEX_TOKEN_ALIGN_STAR	# table_latex_align_star
			| LATEX_TOKEN_ALIGNAT		# table_latex_alignat
			| LATEX_TOKEN_ALIGNAT_STAR	# table_latex_alignat_star
			| LATEX_TOKEN_FLALIGN		# table_latex_flalign
			| LATEX_TOKEN_FLALIGN_STAR	# table_latex_flalign_star
			| LATEX_TOKEN_GATHER		# table_latex_gather
			| LATEX_TOKEN_GATHER_STAR	# table_latex_gather_star
			| LATEX_TOKEN_MULTILINE		# table_latex_multiline
			| LATEX_TOKEN_MULTILINE_STAR	# table_latex_multiline_star
			| LATEX_TOKEN_MATH		# table_latex_math
			| LINK				# table_link
			| URL1				# table_url1
			| MAIL				# table_mail
			| MEDIA_FILES_TOKEN		# table_media
			| HTML_TOKEN			# table_html
			| PHP_TOKEN			# table_php
			;

combined : BOLD combined+? BOLD				# table_bold
	| ITALIC combined+? ITALIC			# table_italic
	| UNDERLINE combined+? UNDERLINE		# table_underline
	| MONOSPACED combined+? MONOSPACED		# table_monospaced
	| SUP_START combined+? SUP_END			# table_superscript
	| SUB_START combined+? SUB_END			# table_subscript
	| DEL_START combined+? DEL_END			# table_del
	| FOOTNOTE_START combined+? FOOTNOTE_END	# table_footnote
	| raw_text+?					# table_unparsed
;



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

fragment CODE_START : ('<code' | '<CODE') (~'>')* '>' ;
fragment CODE_END : '</code>' | '</CODE>';
CODE_TOKEN1: CODE_START .*? CODE_END;

fragment FILE_START : ('<file' | '<FILE') .*? '>' ;
fragment FILE_END : '</file>' | '</FILE>';
FILE_TOKEN: FILE_START .*? FILE_END;

HTML_TOKEN :  ( '<html>' | '<HTML>')  .*?  ( '</html>' | '</HTML>')  ;
PHP_TOKEN : ('<php>' | '<PHP>')  .*? ('</php>' | '</PHP>')  ;

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
MEDIA_FILES_TOKEN : '{{' .*? '}}' ;

SPECIAL_CHAR : [ăĂâÂîÎţțŢşșŞ] ;
ANYCHAR : ([a-zA-Z0-9 \t] | SPECIAL_CHAR)+ ;
UNPARSED : . ;
raw_text : UNPARSED? ANYCHAR  | SPECIAL_CHAR | UNPARSED;


