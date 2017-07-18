/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */


/*
 * This is a grammar for DokuWiki list Syntax
 *
 * Accepted syntax:
 *	'\n' (SPACE SPACE)+ '*' LIST_ITEM
 * where LIST ITEM could be:
 *	- formatted text that does not contain '\n'
 *	- code section <code>...</code>
 *	- nowiki section <nowiki>...</nowiki>
 *	- latex syntax for latex & mathjax syntax:
 *		- <latex>...</latex>
 *		- \begin{displaymath}...\end{displaymath}
 *		- \begin{eqnarray}...\end{eqnarray}
 *		- \begin{eqnarray*}...\end{eqnarray*}
 *		- \begin{equation}...\end{equation}
 *		- \begin{equation}...\end{equation*}
 *		- \begin{align}...\end{align}
 *		- \begin{align*}...\end{align*}
 *		- \begin{alignat}...\end{alignat}
 *		- \begin{alignat*}...\end{alignat*}
 *		- \begin{flalign}...\end{flalign}
 *		- \begin{flalign*}...\end{flalign*}
 *		- \begin{gather}...\end{gather}
 *		- \begin{gather*}...\end{gather*}
 *		- \begin{math}...\end{mah}
 *		- \begin{multiline}...\end{multiline}
 *		- \begin{multiline*}...\end{multiline*}
 *		- $...$
 *		- $$..$$
 */

grammar DokuWikiListGrammar;

list_start : line+ EOF;

DOUBLE_SPACE : '  ';
TAG : '-' | '*' ;

line : '\n' DOUBLE_SPACE+ TAG one_line_formatted* ;

one_line_formatted :	
/* 			BOLD one_line_formatted BOLD				# list_bold
			| ITALIC one_line_formatted ITALIC			# list_italic
			| UNDERLINE one_line_formatted UNDERLINE		# list_underline
			| MONOSPACED one_line_formatted MONOSPACED		# list_monospaced
			| SUP_START one_line_formatted SUP_END			# list_superscript
			| SUB_START one_line_formatted SUB_END			# list_subscript
			| DEL_START one_line_formatted DEL_END			# list_del
			| FOOTNOTE_START one_line_formatted FOOTNOTE_END	# list_footnote
*/
			combined						# list_combined
//			| NOWIKI_TOKEN						# list_nowiki
//			| NOWIKI_TOKEN2						# list_nowiki2
			| CODE_TOKEN1						# list_code
			| FILE_TOKEN						# list_file
//			| LATEX_TOKEN_S						# list_latexs
//			| LATEX_TOKEN_SS					# list_latexss
			| LATEX_TOKEN_TAG					# list_latextag
			| LATEX_TOKEN_DISPLAYMATH				# list_latex_displaymath
			| LATEX_TOKEN_EQNARRAY					# list_latex_eqnarray
			| LATEX_TOKEN_EQNARRAY_STAR				# list_latex_eqnarray_star
			| LATEX_TOKEN_EQUATION					# list_latex_equation
			| LATEX_TOKEN_EQUATION_STAR				# list_latex_equation_star
			| LATEX_TOKEN_ALIGN					# list_latex_align
			| LATEX_TOKEN_ALIGN_STAR				# list_latex_align_star
			| LATEX_TOKEN_ALIGNAT					# list_latex_alignat
			| LATEX_TOKEN_ALIGNAT_STAR				# list_latex_alignat_star
			| LATEX_TOKEN_FLALIGN					# list_latex_flalign
			| LATEX_TOKEN_FLALIGN_STAR				# list_latex_flalign_star
			| LATEX_TOKEN_GATHER					# list_latex_gather
			| LATEX_TOKEN_GATHER_STAR				# list_latex_gather_star
			| LATEX_TOKEN_MULTILINE					# list_latex_multiline
			| LATEX_TOKEN_MULTILINE_STAR				# list_latex_multiline_star
			| LATEX_TOKEN_MATH					# list_latex_math
			| LINK							# list_link
			| URL1							# list_url1
			| MAIL							# list_mail
			| MEDIA_FILES_TOKEN					# list_media
			| HTML_TOKEN						# list_html
			| PHP_TOKEN						# list_php
//			| raw_text+						# list_unparsed
			;

combined : BOLD combined+? BOLD							# list_bold
	| ITALIC combined+? ITALIC						# list_italic
	| UNDERLINE combined+? UNDERLINE					# list_underline
	| MONOSPACED combined+? MONOSPACED					# list_monospaced
	| SUP_START combined+? SUP_END						# list_superscript
	| SUB_START combined+? SUB_END						# list_subscript
	| DEL_START combined+? DEL_END						# list_del
	| FOOTNOTE_START combined+? FOOTNOTE_END				# list_footnote
	| NOWIKI_TOKEN								# list_nowiki
	| NOWIKI_TOKEN2								# list_nowiki2	
	| raw_text+?								# list_unparsed
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


NOTE_START : '<note' .*? '>' ;
NOTE_END : '</note>';
NOTE_TOKEN : NOTE_START .*? NOTE_END ;

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




//LATEX_TOKEN_S			: '$' .*? '$' ;
//LATEX_TOKEN_SS			: '$$' .*? '$$' ;
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
ANYCHAR : ([a-zA-Z0-9\t\\=] | SPECIAL_CHAR)+ ;
UNPARSED : . ;
raw_text : UNPARSED? ANYCHAR  | TAG? ANYCHAR | SPECIAL_CHAR | DOUBLE_SPACE | UNPARSED | TAG;


