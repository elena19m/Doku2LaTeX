/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Doku2LatexListener extends DokuWikiGrammarBaseListener {
	DokuWikiGrammarParser parser;

	public Doku2LatexListener(DokuWikiGrammarParser parser) {
		this.parser = parser;
	}

	@Override
	public void enterWiki_start(DokuWikiGrammarParser.Wiki_startContext ctx) {
	//	System.out.println("enter wiki start");
	//	System.out.println(ctx);
		System.out.println("\\documentclass[a4paper]{article}");
		System.out.println("\\usepackage[a4paper]{geometry}");
		System.out.println("\\usepackage[utf8x]{inputenc}");
		System.out.println("\\usepackage{amsmath}");
		System.out.println("\\usepackage{ulem}");
		System.out.println("\\usepackage{hyperref}");
		System.out.println("\\usepackage[table]{xcolor}");
		System.out.println("\\usepackage{multirow}");
		System.out.println("\\usepackage{graphicx}");
		System.out.println("\\usepackage{tcolorbox}");
		System.out.println("\\begin{document}");
	}

	@Override
	public void exitWiki_start(DokuWikiGrammarParser.Wiki_startContext ctx) {
	//	System.out.println("Exit wiki start");
	//	System.out.println(ctx);
		System.out.println("\n\\end{document}");
	}
	
	@Override
	public void enterHeadline(DokuWikiGrammarParser.HeadlineContext ctx) {
	//	System.out.println("Enter headline");
	//	System.out.println(ctx);
	//	System.out.print("<h2>");
		System.out.println();

		try {
			String headline_start = ctx.HEADLINE().get(0).toString();
			String headline_end   = ctx.HEADLINE().get(1).toString();

			int min = headline_start.length();
			if (headline_end.length() < min)
				min = headline_end.length();

			/* It seems that dokuwiki intern parser looks only at first headline symbol
			 * and renders after it's length.
			 */
			min = headline_start.length();

			switch (min) {
				case 2:
					System.out.print("{\\large ");
					break;
				case 3:
					System.out.print("{\\Large ");
					break;
				case 4:
					System.out.print("{\\LARGE ");
					break;
				case 5:
					System.out.print("{\\huge ");
					break;
				default:
					System.out.print("{\\Huge ");
					break;

			}
		} catch (Error e) {
			System.out.print("{\\large ");
		}
	}
	
	@Override
	public void exitHeadline(DokuWikiGrammarParser.HeadlineContext ctx) {
	//	System.out.println("Exit headline");
	//	System.out.println(ctx);
	//	System.out.print("<\\h2>");
		System.out.println("}\\\\");

	}
	
	@Override
	public void enterBold(DokuWikiGrammarParser.BoldContext ctx) {
	//	System.out.println("Enter bold");
	//	System.out.println(ctx);
	//	System.out.print("<b>");
		System.out.print("\\textbf{");
	}
	
	@Override
	public void exitBold(DokuWikiGrammarParser.BoldContext ctx) {
	//	System.out.println("Exit bold");
	//	System.out.println(ctx);
	//	System.out.print("<\\b>");
		System.out.print("}");
	}
	
	@Override
	public void enterItalic(DokuWikiGrammarParser.ItalicContext ctx) {
	//	System.out.println("Enter italic");
	//	System.out.println(ctx);
	//	System.out.print("<i>");
		System.out.print("\\textit{");
	}
	
	@Override
	public void exitItalic(DokuWikiGrammarParser.ItalicContext ctx) {
	//	System.out.println("Exit italic");
	//	System.out.println(ctx);
	//	System.out.print("<\\i>");
		System.out.print("}");
	}
	
	@Override
	public void enterUnderline(DokuWikiGrammarParser.UnderlineContext ctx) {
	//	System.out.println("Enter underline");
	//	System.out.println(ctx);
	//	System.out.print("<u>");
		System.out.print("\\underline{");
	}
	
	@Override
	public void exitUnderline(DokuWikiGrammarParser.UnderlineContext ctx) {
	//	System.out.println("Exit underline");
	//	System.out.println(ctx);
	//	System.out.print("<\\u>");
		System.out.print("}");
	}
	
	@Override
	public void enterMonospaced(DokuWikiGrammarParser.MonospacedContext ctx) {
		System.out.print("\\texttt{");
	}
	
	@Override
	public void exitMonospaced(DokuWikiGrammarParser.MonospacedContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterSuperscript(DokuWikiGrammarParser.SuperscriptContext ctx) {
		System.out.print("\\textsuperscript{");
	}
	
	@Override
	public void exitSuperscript(DokuWikiGrammarParser.SuperscriptContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterSubscript(DokuWikiGrammarParser.SubscriptContext ctx) {
		//System.out.print("\\textsubscript{");
		System.out.print("$_{\\text{");
	}
	
	@Override
	public void exitSubscript(DokuWikiGrammarParser.SubscriptContext ctx) {
		//System.out.print("}");
		System.out.print("}}$");
	}

	@Override
	public void enterDel(DokuWikiGrammarParser.DelContext ctx) {
		System.out.print("\\sout{");	
	}
	
	@Override
	public void exitDel(DokuWikiGrammarParser.DelContext ctx) {
		System.out.print("}");
	}
	
	@Override
	public void enterFootnote(DokuWikiGrammarParser.FootnoteContext ctx) {
		System.out.print("\\footnote{");
	}
	
	@Override
	public void exitFootnote(DokuWikiGrammarParser.FootnoteContext ctx) {
		System.out.print("}");
	}

	@Override
	public void enterHline(DokuWikiGrammarParser.HlineContext ctx) {
		// System.out.print("\n\n\\rule{\\textwidth}{1pt}\n\n");
		// System.out.println("\n\\noindent\\makebox[\\linewidth]{\\rule{\\paperwidth}{0.4pt}}\n");
		System.out.println("\\hrulefill\n");
	}

	@Override
	public void enterRaw(DokuWikiGrammarParser.RawContext ctx) {
	//	System.out.println("Enter raw");
 	//	TokenStream tokens = parser.getTokenStream();
	//	System.out.print(tokens.getText(ctx.raw_text()));
	}
	
	@Override
	public void exitRaw(DokuWikiGrammarParser.RawContext ctx) {
	//	System.out.println("Exit raw");
	}
	
	@Override
	public void enterUnparsed_token(DokuWikiGrammarParser.Unparsed_tokenContext ctx) {
	//	System.out.println("Enter unparsed");
		String s = ctx.UNPARSED().toString();
		if (s.matches("(.*)" + SpecialChars.regex_keys +"(.*)")) {
			s = SpecialChars.replaceSpecialChars(s);
		}
		System.out.print(s);
		//System.out.print(ctx.UNPARSED());
	}
	
	@Override
	public void exitUnparsed_token(DokuWikiGrammarParser.Unparsed_tokenContext ctx) {
	//	System.out.println("Exit unparsed");
	}

	@Override
	public void enterNowiki(DokuWikiGrammarParser.NowikiContext ctx) {
		if (ctx.NOWIKI_TOKEN() == null)
			return;
		String s = ctx.NOWIKI_TOKEN().toString();
		s = Utils.removeFirstAndLastTag(s);
		// System.out.println("\\begin{verbatim}");
		s = SpecialChars.replaceLaTeXSpecialChars(s);
		System.out.println(s);
	}

	@Override
	public void exitNowiki(DokuWikiGrammarParser.NowikiContext ctx) {
		// System.out.println("\\end{verbatim}");
	}

	@Override
	public void enterNowiki2(DokuWikiGrammarParser.Nowiki2Context ctx) {
		if (ctx.NOWIKI_TOKEN2() == null)
			return;
		String s = ctx.NOWIKI_TOKEN2().toString();
		if (s == null || s.length() < 4)
			return;
		s = s.substring(0, s.length() - 2);
		s = s.substring(2, s.length());
		// System.out.println("\\begin{verbatim}");
		s = SpecialChars.replaceLaTeXSpecialChars(s);
		System.out.println(s);
	}

	@Override
	public void exitNowiki2(DokuWikiGrammarParser.Nowiki2Context ctx) {
		// System.out.println("\\end{verbatim}");
	}

	@Override
	public void enterNote(DokuWikiGrammarParser.NoteContext ctx) {
		System.out.println("\\begin{tcolorbox}");
		
		// if (ctx.NOTE_TOKEN() == null)
		// 	return;
		// String s = ctx.NOTE_TOKEN().toString();
		// s = Utils.removeFirstAndLastTag(s);
		// System.out.println("\\begin{verbatim}");
		// System.out.println(s);
		
	}
	
	@Override
	public void exitNote(DokuWikiGrammarParser.NoteContext ctx) {
		// System.out.println("\\end{verbatim}");
		System.out.println("\\end{tcolorbox}");
	}

	@Override
	public void enterCode(DokuWikiGrammarParser.CodeContext ctx) {
		if (ctx.CODE_TOKEN1() == null)
			return;
		String s = ctx.CODE_TOKEN1().toString();
		s = Utils.removeFirstAndLastTag(s);
		System.out.println("\\begin{verbatim}");
		System.out.println(s);
	}

	@Override
	public void exitCode(DokuWikiGrammarParser.CodeContext ctx) {
		System.out.println("\\end{verbatim}");
	}

	@Override
	public void enterFile(DokuWikiGrammarParser.FileContext ctx) {
		if (ctx.FILE_TOKEN() == null)
			return;
		String s = ctx.FILE_TOKEN().toString();
		String tag = "";
		int tagPos = s.indexOf('>');
		// if (tagPos >= 0) {
		// 	tag = s.substring(0, tagPos);
		// 	String[] substrings = tag.split(" ");
		// 	System.out.println(substrings[substrings.length - 1]);
		// }
		s = Utils.removeFirstAndLastTag(s);
		System.out.println("\\begin{verbatim}");
		System.out.println(s);
		System.out.println("\\end{verbatim}");
	}

	@Override
	public void enterLatexs(DokuWikiGrammarParser.LatexsContext ctx) {
		/*
		 * $ eq $
		 */
		String s = ctx.LATEX_TOKEN_S().toString();
		System.out.println(s);
	}
	
	@Override 
	public void enterLatexss(DokuWikiGrammarParser.LatexssContext ctx) {
		/*
		 * $$ eq $$
		 */
		String s = ctx.LATEX_TOKEN_SS().toString();
		System.out.println(s);
	}
	
	@Override 
	public void enterLatextag(DokuWikiGrammarParser.LatextagContext ctx) {
		/*
		 * <latex> ... </latex>
		 */
		if (ctx.LATEX_TOKEN_TAG() == null)
			return;
		String s = ctx.LATEX_TOKEN_TAG().toString();
		s = Utils.removeFirstAndLastTag(s);
		System.out.println(s);
	}
	
	@Override 
	public void enterLatex_displaymath(DokuWikiGrammarParser.Latex_displaymathContext ctx) {
		/*
		 * \begin{displaymath}…\end{displaymath}
		 */
		String s = ctx.LATEX_TOKEN_DISPLAYMATH().toString();
		System.out.println(s);
	}
	
	@Override 
	public void enterLatex_eqnarray(DokuWikiGrammarParser.Latex_eqnarrayContext ctx) {
		/*
		 * \begin{eqnarray}…\end{eqnarray}
		 */
		String s = ctx.LATEX_TOKEN_EQNARRAY().toString();
		System.out.println(s);
	}
	
	@Override 
	public void enterLatex_eqnarray_star(DokuWikiGrammarParser.Latex_eqnarray_starContext ctx) {
		/*
		 * \begin{eqnarray*}…\end{eqnarray*} 
		 */
		String s = ctx.LATEX_TOKEN_EQNARRAY_STAR().toString();
		System.out.println(s);
	}
	
	@Override 
	public void enterLatex_equation(DokuWikiGrammarParser.Latex_equationContext ctx) {
		/*
		 * \begin{equation}…\end{equation}
		 */
		String s = ctx.LATEX_TOKEN_EQUATION().toString();
		System.out.println(s);
	}
	
	@Override 
	public void enterLatex_equation_star(DokuWikiGrammarParser.Latex_equation_starContext ctx) {
		/*
		 * \begin{equation*}…\end{equation*}
		 */
		String s = ctx.LATEX_TOKEN_EQUATION_STAR().toString();
		System.out.println(s);
	}

	@Override
	public void enterLatex_align(DokuWikiGrammarParser.Latex_alignContext ctx) {
		String s = ctx.LATEX_TOKEN_ALIGN().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_align_star(DokuWikiGrammarParser.Latex_align_starContext ctx) {
		String s = ctx.LATEX_TOKEN_ALIGN_STAR().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_alignat(DokuWikiGrammarParser.Latex_alignatContext ctx) {
		String s = ctx.LATEX_TOKEN_ALIGNAT().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_alignat_star(DokuWikiGrammarParser.Latex_alignat_starContext ctx) {
		String s = ctx.LATEX_TOKEN_ALIGNAT_STAR().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_flalign(DokuWikiGrammarParser.Latex_flalignContext ctx) {
		String s = ctx.LATEX_TOKEN_FLALIGN().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_flalign_star(DokuWikiGrammarParser.Latex_flalign_starContext ctx) {
		String s = ctx.LATEX_TOKEN_FLALIGN_STAR().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_gather(DokuWikiGrammarParser.Latex_gatherContext ctx) {
		String s = ctx.LATEX_TOKEN_GATHER().toString();
		System.out.println(s);
	}
	
	@Override 
	public void enterLatex_gather_star(DokuWikiGrammarParser.Latex_gather_starContext ctx) {
		String s = ctx.LATEX_TOKEN_GATHER_STAR().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_multiline(DokuWikiGrammarParser.Latex_multilineContext ctx) {
		String s = ctx.LATEX_TOKEN_MULTILINE().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_multiline_star(DokuWikiGrammarParser.Latex_multiline_starContext ctx) {
		String s = ctx.LATEX_TOKEN_MULTILINE_STAR().toString();
		System.out.println(s);
	}
	
	@Override
	public void enterLatex_math(DokuWikiGrammarParser.Latex_mathContext ctx) {
		String s = ctx.LATEX_TOKEN_MATH().toString();
		System.out.println(s);
	}

	@Override
	public void enterUrl1(DokuWikiGrammarParser.Url1Context ctx) {
		String s = ctx.URL1().toString();
		System.out.print("\\url{" + s + "}");	
	}

	@Override
	public void exitUrl1(DokuWikiGrammarParser.Url1Context ctx) {

	}

	@Override
	public void enterLink(DokuWikiGrammarParser.LinkContext ctx) {
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
	public void enterMail(DokuWikiGrammarParser.MailContext ctx) {
		String mail = ctx.MAIL().toString();
		mail = mail.replace("<", "");
		mail = mail.replace(">", "");

		System.out.print("\\href{mailto:" + mail + "}{" + mail + "}");
	}

	@Override
	public void enterList(DokuWikiGrammarParser.ListContext ctx) {
		String s = ctx.LIST_TOKEN().toString();
		Utils.writeInLogFile("\n- Processing List");

		try {
			InputStream listIs = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

			ANTLRInputStream listInput = new ANTLRInputStream(listIs);

        	DokuWikiListGrammarLexer listLexer = new DokuWikiListGrammarLexer(listInput);
        	CommonTokenStream listTokens = new CommonTokenStream(listLexer);
	        DokuWikiListGrammarParser listParser = new DokuWikiListGrammarParser(listTokens);
	        ParseTree listTree = listParser.list_start();

	        ParseTreeWalker listWalker = new ParseTreeWalker();
	        DokuWikiListListener listExtractor = new DokuWikiListListener(listParser);
	        listWalker.walk(listExtractor, listTree);
    	} catch (Exception e) {
    		System.err.println(e);
    	}
	}

	@Override
	public void enterStart_list(DokuWikiGrammarParser.Start_listContext ctx) {
		String s = ctx.START_LIST_TOKEN().toString();
		Utils.writeInLogFile("\n- Processing List");

		s = "\n" + s;

		try {
			InputStream listIs = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

			ANTLRInputStream listInput = new ANTLRInputStream(listIs);

        	DokuWikiListGrammarLexer listLexer = new DokuWikiListGrammarLexer(listInput);
        	CommonTokenStream listTokens = new CommonTokenStream(listLexer);
	        DokuWikiListGrammarParser listParser = new DokuWikiListGrammarParser(listTokens);
	        ParseTree listTree = listParser.list_start();

	        ParseTreeWalker listWalker = new ParseTreeWalker();
	        DokuWikiListListener listExtractor = new DokuWikiListListener(listParser);
	        listWalker.walk(listExtractor, listTree);
    	} catch (Exception e) {
    		System.err.println(e);
    	}
	}

	@Override
	public void enterTables(DokuWikiGrammarParser.TablesContext ctx) {
		String s = ctx.TABLES_TOKEN().toString();
		System.out.println();
		System.out.println();
		Utils.writeInLogFile("\n- Processing Table\n" + 
							"  -> Be aware of cell alignment. By default cells are left aligned");

//		System.out.println("TABLE:\n" + s);

// 		String adjustString = "\n";
// 		String[] lines = s.split("\n");

// //		using (StringReader reader = new StringReader(s)) {
//   		for (int i = 1; i < lines.length; i++) {
//   			String line = lines[i];
// 			line.trim();
//     		if (line.endsWith("|") || line.endsWith("^"))
//     			adjustString = adjustString + line + "\n";
//     		else {
//     			line = line + "|";
//     			adjustString = adjustString + line + "\n";
//     		}
// 		}

// 		System.out.println("MODIFY TABLE:\n" + adjustString);

		try {
			InputStream tableIs = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

			ANTLRInputStream tableInput = new ANTLRInputStream(tableIs);

        	DokuWikiTablesGrammarLexer tableLexer = new DokuWikiTablesGrammarLexer(tableInput);
        	CommonTokenStream tableTokens = new CommonTokenStream(tableLexer);
	        DokuWikiTablesGrammarParser tableParser = new DokuWikiTablesGrammarParser(tableTokens);
	        ParseTree tableTree = tableParser.table();

	        ParseTreeWalker tableWalker = new ParseTreeWalker();
	        DokuWikiTablesListener tableExtractor = new DokuWikiTablesListener(tableParser);
	        tableWalker.walk(tableExtractor, tableTree);
    	} catch (Exception e) {
    		System.err.println(e);
    	}
	}

	@Override
	public void enterStart_table(DokuWikiGrammarParser.Start_tableContext ctx) {
		String s = ctx.START_TABLES_TOKEN().toString();
		Utils.writeInLogFile("\n- Processing Table\n" + 
							"  -> Be aware of cell alignment. By default cells are left aligned");

		s = "\n" + s;
		System.out.println();
		try {
			InputStream tableIs = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

			ANTLRInputStream tableInput = new ANTLRInputStream(tableIs);

        	DokuWikiTablesGrammarLexer tableLexer = new DokuWikiTablesGrammarLexer(tableInput);
        	CommonTokenStream tableTokens = new CommonTokenStream(tableLexer);
	        DokuWikiTablesGrammarParser tableParser = new DokuWikiTablesGrammarParser(tableTokens);
	        ParseTree tableTree = tableParser.table();

	        ParseTreeWalker tableWalker = new ParseTreeWalker();
	        DokuWikiTablesListener tableExtractor = new DokuWikiTablesListener(tableParser);
	        tableWalker.walk(tableExtractor, tableTree);
    	} catch (Exception e) {
    		System.err.println(e);
    	}				
	}


	@Override
	public void enterMedia_files(DokuWikiGrammarParser.Media_filesContext ctx) {
		Utils.writeInLogFile("\n- Processing images."
			+ " Please review image dimensions and image position.\n"
			+ "  -> Please read " + Utils.logImagesFileName + " file.\n"
			+ "  -> Be aware of the fact that all media files are considered to be images. "
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
				try {
					width = Integer.parseInt(splitWH[0].trim());
					if (splitWH.length == 2) {
						height = Integer.parseInt(splitWH[1]);
					}
				} catch (Exception e) {

				} finally {
					width = -1;
					height = -1;
				}
			}
		}

		// System.out.println("\\begin{figure}[!htb]");
		if (!text.isEmpty())
			System.out.print("\\caption{" + text + "}");
		System.out.print("\\vbox{\\includegraphics");

		if (width > -1) {
			if (height > -1) {
				System.out.print("[width = " + width + "cm, height = " + height + "cm, keepaspectratio]");
			} else {
				System.out.print("[width = " + width + "cm, keepaspectratio]");
			}
		}
		System.out.println("{" + imageName + "}}");
		// System.out.println("\\end{figure}");

		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(Utils.logImagesFileName, true));
			output.write(imageName + "\n");
			output.close();
		} catch (Exception e) {

		}

	}

	@Override
	public void enterQuoting(DokuWikiGrammarParser.QuotingContext ctx) {
		String s = ctx.QUOTING_TOKEN().toString();
		Utils.writeInLogFile("\n- Processing Quoting\n"
							+ "  -> Be aware that quoting is considered to be something like a email conversation\n"
							+ "  -> Please modify if quoting section if it's not a conversation, but a quote"
							);
		System.out.println("\\begin{verbatim}");
		System.out.println(s);
		System.out.println("\\end{verbatim}");
	}

	@Override
	public void enterHtml(DokuWikiGrammarParser.HtmlContext ctx) {
		String s = ctx.HTML_TOKEN().toString();
		Utils.writeInLogFile("\n- HTML section\n"
									+ "  -> HTML sections are not converted in LaTeX."
									);
		System.out.println(s);
	}

	@Override
	public void enterPhp(DokuWikiGrammarParser.PhpContext ctx) {
		String s = ctx.PHP_TOKEN().toString();
		Utils.writeInLogFile("\n- PHP section\n"
									+ "  -> PHP sections are not converted in LaTeX."
									);
		System.out.println(s);
	}

	@Override
	public void enterRaw_text(DokuWikiGrammarParser.Raw_textContext ctx) {
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
		if (ctx.LINE() != null) {
			System.out.print(ctx.LINE());
			if (ctx.ANYCHAR() == null)
				return;
		}

		if (ctx.H() != null) {
			System.out.print("=");
			if (ctx.ANYCHAR() == null)
				return;
		}

		if (ctx.BACKSLASH_N() != null) {
			System.out.print("\n");
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

		if (ctx.ANYCHAR() == null)
			return;
		s = ctx.ANYCHAR().toString();
		if (s.matches("((.|\n)*)" + SpecialChars.regex_keys +"((.|\n)*)")) {
			s = SpecialChars.replaceSpecialChars(s);
		}
		s = SpecialChars.replaceLaTeXSpecialChars(s);

		System.out.print(s);
	}
	
	@Override 
	public void exitRaw_text(DokuWikiGrammarParser.Raw_textContext ctx) {

	}

}
