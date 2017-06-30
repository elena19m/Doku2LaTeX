/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.Stack;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DokuWikiListListener extends DokuWikiListGrammarBaseListener {
	DokuWikiListGrammarParser parser;
	private String wikihost = "";
	private int level = 0;
	private String type = "";
	private Stack<String> stack = new Stack<String>();

	public DokuWikiListListener(DokuWikiListGrammarParser parser) {
		this.parser = parser;
	}

	public DokuWikiListListener(DokuWikiListGrammarParser parser, String dokuWikiHost) {
		this.parser = parser;
		this.wikihost = dokuWikiHost;
	}

	@Override
	public void exitList_start(DokuWikiListGrammarParser.List_startContext ctx) {
		if (type.equals("*")) {
					System.out.println("\n\\end{itemize}");
		} else if (type.equals("-")) {
					System.out.println("\n\\end{enumerate}");
		}
	}

	@Override
	public void enterLine(DokuWikiListGrammarParser.LineContext ctx) {
		String crtType = ctx.TAG().toString();
		int crtLevel = ctx.DOUBLE_SPACE().size();

		System.out.println();
		if (crtLevel > level) {
			if (crtType.equals("-")) {
				System.out.println("\\begin{enumerate}");
				stack.push(type);
			} else if (crtType.equals("*")) {
				System.out.println("\\begin{itemize}");
				stack.push(type);
			}
			level = crtLevel;
			type = crtType;
		} else if (crtLevel == level) {
			if (!type.equals(crtType)) {
				if (type.equals("*")) {
					System.out.println("\\end{itemize}");
					stack.pop();
					System.out.println("\\begin{enumerate}");
					stack.push(type);
				} else if (type.equals("-")) {
					System.out.println("\\end{enumerate}");
					stack.pop();
					System.out.println("\\begin{itemize}");
					stack.push(type);
				}
				type = crtType;
			}
		} else {
			if (type.equals("*")) {
				System.out.println("\\end{itemize}");
				stack.pop();
			} else if (type.equals("-")) {
				System.out.println("\\end{enumerate}");
				stack.pop();
			}
			level = crtLevel;
			type = crtType;
			if (!stack.peek().equals(type)) {
				if (stack.peek().equals("*")) {
					System.out.println("\\end{enumerate}");
					stack.pop();
					System.out.println("\\begin{itemize}");
					stack.push(type);
				} else if (type.equals("-")) {
					System.out.println("\\end{itemize}");
					stack.pop();
					System.out.println("\\begin{enumerate}");
					stack.push(type);
				}	
			}
		}

		System.out.print("\t\\item ");
	}

	@Override
	public void enterList_bold(DokuWikiListGrammarParser.List_boldContext ctx) {
		System.out.print("\\textbf{");
	}
	
	@Override
	public void exitList_bold(DokuWikiListGrammarParser.List_boldContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterList_italic(DokuWikiListGrammarParser.List_italicContext ctx) {
		System.out.print("\\textit{");
	}
	
	@Override
	public void exitList_italic(DokuWikiListGrammarParser.List_italicContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterList_underline(DokuWikiListGrammarParser.List_underlineContext ctx) {
		System.out.print("\\underline{");
	}
	
	@Override
	public void exitList_underline(DokuWikiListGrammarParser.List_underlineContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterList_monospaced(DokuWikiListGrammarParser.List_monospacedContext ctx) {
		System.out.print("\\texttt{");
	}
	
	@Override
	public void exitList_monospaced(DokuWikiListGrammarParser.List_monospacedContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterList_superscript(DokuWikiListGrammarParser.List_superscriptContext ctx) {
		System.out.print("\\textsuperscript{");
	}
	
	@Override
	public void exitList_superscript(DokuWikiListGrammarParser.List_superscriptContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterList_subscript(DokuWikiListGrammarParser.List_subscriptContext ctx) {
		System.out.print("$_{\\text{");
	}
	
	@Override
	public void exitList_subscript(DokuWikiListGrammarParser.List_subscriptContext ctx) {
		System.out.print("}}$");
	}
	
	@Override
	public void enterList_del(DokuWikiListGrammarParser.List_delContext ctx) {
		System.out.print("\\sout{");
	}
	
	@Override
	public void exitList_del(DokuWikiListGrammarParser.List_delContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterList_footnote(DokuWikiListGrammarParser.List_footnoteContext ctx) {
		System.out.print("\\footnote{");
	}
	
	@Override
	public void exitList_footnote(DokuWikiListGrammarParser.List_footnoteContext ctx) { 
		System.out.print("}");
	}

	@Override
	public void enterList_nowiki(DokuWikiListGrammarParser.List_nowikiContext ctx) {
		if (ctx.NOWIKI_TOKEN() == null)
			return;
		String s = ctx.NOWIKI_TOKEN().toString();
		s = Utils.removeFirstAndLastTag(s);
		s = SpecialChars.replaceLaTeXSpecialChars(s);
		// System.out.println("\\begin{verbatim}");
		System.out.println(s);
	}

	@Override
	public void exitList_nowiki2(DokuWikiListGrammarParser.List_nowiki2Context ctx) {
		if (ctx.NOWIKI_TOKEN2() == null)
			return;
		String s = ctx.NOWIKI_TOKEN2().toString();
		s = s.substring(2, s.length() - 2);
		s = SpecialChars.replaceLaTeXSpecialChars(s);
		// System.out.println("\\begin{verbatim}");
		System.out.println(s);
		// System.out.println("\\end{verbatim}");
	}
	
	@Override
	public void exitList_nowiki(DokuWikiListGrammarParser.List_nowikiContext ctx) {
		// System.out.println("\\end{verbatim}");
	}
	
	@Override
	public void enterList_code(DokuWikiListGrammarParser.List_codeContext ctx) {
		if (ctx.CODE_TOKEN1() == null)
			return;
		String s = ctx.CODE_TOKEN1().toString();
		s = Utils.removeFirstAndLastTag(s);
		System.out.println("\\begin{verbatim}");
		System.out.println(s);
	}

	@Override
	public void enterList_file(DokuWikiListGrammarParser.List_fileContext ctx) {
		if (ctx.FILE_TOKEN() == null)
			return;
		String s = ctx.FILE_TOKEN().toString();
		s = Utils.removeFirstAndLastTag(s);
		System.out.println("\\begin{verbatim}");
		System.out.println(s);
		System.out.println("\\end{verbatim}");
	}
	
	@Override
	public void exitList_code(DokuWikiListGrammarParser.List_codeContext ctx) {
		System.out.println("\\end{verbatim}");
	}
	
	@Override
	public void enterList_latexs(DokuWikiListGrammarParser.List_latexsContext ctx) {
		String s = ctx.LATEX_TOKEN_S().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latexss(DokuWikiListGrammarParser.List_latexssContext ctx) {
		/*
		 * $$ eq $$
		 */
		String s = ctx.LATEX_TOKEN_SS().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latextag(DokuWikiListGrammarParser.List_latextagContext ctx) {
				/*
		 * <latex> ... </latex>
		 */
		if (ctx.LATEX_TOKEN_TAG() == null)
			return;
		String s = ctx.LATEX_TOKEN_TAG().toString();
		s = Utils.removeFirstAndLastTag(s);
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_displaymath(DokuWikiListGrammarParser.List_latex_displaymathContext ctx) {
		String s = ctx.LATEX_TOKEN_DISPLAYMATH().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_eqnarray(DokuWikiListGrammarParser.List_latex_eqnarrayContext ctx) {
		/*
		 * \begin{eqnarray}…\end{eqnarray}
		 */
		String s = ctx.LATEX_TOKEN_EQNARRAY().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_eqnarray_star(DokuWikiListGrammarParser.List_latex_eqnarray_starContext ctx) {
		/*
		 * \begin{eqnarray*}…\end{eqnarray*} 
		 */
		String s = ctx.LATEX_TOKEN_EQNARRAY_STAR().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_equation(DokuWikiListGrammarParser.List_latex_equationContext ctx) {
		/*
		 * \begin{equation}…\end{equation}
		 */
		String s = ctx.LATEX_TOKEN_EQUATION().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_equation_star(DokuWikiListGrammarParser.List_latex_equation_starContext ctx) {
			/*
		 * \begin{equation*}…\end{equation*}
		 */
		String s = ctx.LATEX_TOKEN_EQUATION_STAR().toString();
		System.out.print(s);
	}

	@Override
	public void enterList_latex_align(DokuWikiListGrammarParser.List_latex_alignContext ctx) {
		String s = ctx.LATEX_TOKEN_ALIGN().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_align_star(DokuWikiListGrammarParser.List_latex_align_starContext ctx) {
		String s = ctx.LATEX_TOKEN_ALIGN_STAR().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_alignat(DokuWikiListGrammarParser.List_latex_alignatContext ctx) {
		String s = ctx.LATEX_TOKEN_ALIGNAT().toString();
		System.out.print(s);
	}
	
	@Override 
	public void enterList_latex_alignat_star(DokuWikiListGrammarParser.List_latex_alignat_starContext ctx) {
		String s = ctx.LATEX_TOKEN_ALIGNAT_STAR().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_flalign(DokuWikiListGrammarParser.List_latex_flalignContext ctx) {
		String s = ctx.LATEX_TOKEN_FLALIGN().toString();
		System.out.print(s);
	}
	
	@Override 
	public void enterList_latex_flalign_star(DokuWikiListGrammarParser.List_latex_flalign_starContext ctx) {
		String s = ctx.LATEX_TOKEN_FLALIGN_STAR().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_gather(DokuWikiListGrammarParser.List_latex_gatherContext ctx) {
		String s = ctx.LATEX_TOKEN_GATHER().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_gather_star(DokuWikiListGrammarParser.List_latex_gather_starContext ctx) {
		String s = ctx.LATEX_TOKEN_GATHER_STAR().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_multiline(DokuWikiListGrammarParser.List_latex_multilineContext ctx) {
		String s = ctx.LATEX_TOKEN_MULTILINE().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_multiline_star(DokuWikiListGrammarParser.List_latex_multiline_starContext ctx) {
		String s = ctx.LATEX_TOKEN_MULTILINE_STAR().toString();
		System.out.print(s);
	}
	
	@Override
	public void enterList_latex_math(DokuWikiListGrammarParser.List_latex_mathContext ctx) {
		String s = ctx.LATEX_TOKEN_MATH().toString();
		System.out.print(s);
	}

	@Override
	public void enterList_link(DokuWikiListGrammarParser.List_linkContext ctx) {
		String s = ctx.LINK().toString();
			if (s.length() <= 4)
				return;
			s = s.substring(2, s.length());
			s = s.substring(0, s.length() - 2);
			s = s.trim();

			try {
				InputStream linkIs = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

				ANTLRInputStream linksInput = new ANTLRInputStream(linkIs);

	        	DokuWikiLinksGrammarLexer linksLexer = new DokuWikiLinksGrammarLexer(linksInput);
	        	CommonTokenStream linksTokens = new CommonTokenStream(linksLexer);
		        DokuWikiLinksGrammarParser linksParser = new DokuWikiLinksGrammarParser(linksTokens);
		        ParseTree linksTree = linksParser.links_start();

		        ParseTreeWalker linksWalker = new ParseTreeWalker();
		        DokuWikiLinkListener linksExtractor = new DokuWikiLinkListener(linksParser);
		        linksWalker.walk(linksExtractor, linksTree);
	    	} catch (Exception e) {
	    		System.err.println(e);
	    	}
	}
	
	@Override
	public void enterList_url1(DokuWikiListGrammarParser.List_url1Context ctx) {
		String s = ctx.URL1().toString();
		System.out.print("\\url{" + s + "}");	
	}

	@Override
	public void enterList_mail(DokuWikiListGrammarParser.List_mailContext ctx) {
		String mail = ctx.MAIL().toString();
		mail = mail.replace("<", "");
		mail = mail.replace(">", "");

		System.out.print("\\href{mailto:" + mail + "}{" + mail + "}");
	}
	@Override
	public void enterList_media(DokuWikiListGrammarParser.List_mediaContext ctx) {
		Utils.writeInLogFile("  -> Be aware of images as a list item."
			+ " Please review image dimensions and image position.\n"
			+ "    -> Please read " + Utils.logImagesFileName + " file.\n"
			+ "    -> Be aware of the fact that all media files are considered to be images. "
			+ "For other media files formats, please change manually the LaTeX code");
		String s = ctx.MEDIA_FILES_TOKEN().toString();
		s = s.substring(2, s.length() - 2);
		String text = "";
		String[] splitText = s.split("[|]");
		int width = -1, height = -1;
		if (splitText.length >= 2)
			text = splitText[1];

		String[] splitDimensions = splitText[0].split("[?]");
		String imageName = splitDimensions[0].trim();

		if (splitDimensions.length >= 2) {
			if (splitDimensions[1].contains("linkonly")) {
			} else {
				String[] splitWH = splitDimensions[1].split("x");
				width = Integer.parseInt(splitWH[0]);
				if (splitWH.length == 2) {
					height = Integer.parseInt(splitWH[1]);
				}
			}
		}

		if (!text.isEmpty())
			System.out.print("\\caption{" + text + "} ");
		System.out.print("\\vbox{\\includegraphics");

		if (width > -1) {
			if (height > -1) {
				System.out.print("[width = " + width + "cm, height = " + height + "cm, keepaspectratio]");
			} else {
				System.out.print("[width = " + width + "cm, keepaspectratio]");
			}
		}
		System.out.println("{" + imageName + "}}");

		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(Utils.logImagesFileName, true));
			output.write(imageName + "\n");
			output.close();
		} catch (Exception e) {}

	}

	@Override
	public void enterList_html(DokuWikiListGrammarParser.List_htmlContext ctx) {
		String s = ctx.HTML_TOKEN().toString();
		Utils.writeInLogFile("  -> HTML section as list item\n"
									+ "    -> HTML sections are not converted in LaTeX."
									);
		System.out.println(s);
	}
	
	@Override
	public void enterList_php(DokuWikiListGrammarParser.List_phpContext ctx) {
		String s = ctx.PHP_TOKEN().toString();
		Utils.writeInLogFile("  -> PHP section as list item\n"
									+ "    -> PHP sections are not converted in LaTeX."
									);
		System.out.println(s);
	}

	@Override
	public void enterRaw_text(DokuWikiListGrammarParser.Raw_textContext ctx) {
		String s;
		if(ctx.UNPARSED() != null) {
			s = ctx.UNPARSED().toString();
			if (s.matches("((.|\n)*)" + SpecialChars.regex_keys +"((.|\n)*)")) {
				s = SpecialChars.replaceSpecialChars(s);
			}
			s = SpecialChars.replaceLaTeXSpecialChars(s);
			System.out.print(s);
			// System.out.print(ctx.UNPARSED());
			if (ctx.ANYCHAR() == null)
				return;
		}
		if (ctx.TAG() != null) {
			System.out.print(ctx.TAG());
			if (ctx.ANYCHAR() == null)
				return;
		}

		if (ctx.SPECIAL_CHAR() != null) {
			s = ctx.SPECIAL_CHAR().toString();
			if (s.matches("((.|\n)*)" + SpecialChars.regex_keys +"((.|\n)*)")) {
				s = SpecialChars.replaceSpecialChars(s);
			}
			s = SpecialChars.replaceLaTeXSpecialChars(s);

			System.out.print(s);
		}

		if (ctx.DOUBLE_SPACE() != null) {
			s = "";
			// for (int i = 0; i < ctx.DOUBLE_SPACE().size(); i++)
			// 	s = s + "  ";
			s = ctx.DOUBLE_SPACE().toString();
			System.out.print(s);
		}

		if (ctx.ANYCHAR() == null)
			return;
		s = ctx.ANYCHAR().toString();
		if (s.matches("((.|\n)*)" + SpecialChars.regex_keys +"((.|\n)*)")) {
			s = SpecialChars.replaceSpecialChars(s);
		}
		s = SpecialChars.replaceLaTeXSpecialChars(s);

		System.out.print(s);
	}
	

}
