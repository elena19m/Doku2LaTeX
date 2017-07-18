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

import java.util.ArrayList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DokuWikiTablesListener extends DokuWikiTablesGrammarBaseListener {
	DokuWikiTablesGrammarParser parser;
	private String wikihost = "";
	private int rowIndex = -1;
	private int colIndex = -1;
	private int rowNo = 0;
	private int colNo = 0;

	private ArrayList<ArrayList<String>> matrix;

	public DokuWikiTablesListener(DokuWikiTablesGrammarParser parser) {
		this.parser = parser;
		matrix = new ArrayList<ArrayList<String>>();
	}

	public DokuWikiTablesListener(DokuWikiTablesGrammarParser parser, String dokuWikiHost) {
		this.parser = parser;
		this.wikihost = dokuWikiHost;
		matrix = new ArrayList<ArrayList<String>>();
	}

	private int multicolumn(int row, int position) {
		ArrayList<String> rowList = matrix.get(row);
		int multicolumnIndex = 0;
		int i = position + 1;
		while (true) {
			if (i >= colNo)
				break;
			if (! (rowList.get(i).equals("^") || rowList.get(i).equals("|")))
				break;
			i += 1;
			multicolumnIndex += 1;
		}

		return multicolumnIndex;
	}

	private char checkAlign(String s) {
		int trailing = 0, leading = 0;
		int i = 0;
		while(true) {
			if (i >= s.length())
				break;
			if (s.charAt(i) != ' ')
				break;
			leading += 1;
			i += 1;
		}

		i = s.length() - 1;
		while(true) {
			if(i <= 0)
				break;
			if (s.charAt(i) != ' ')
				break;
			trailing += 1;
			i -= 1;
		}

		if (leading > trailing && leading >= 2)
			return 'r';

		if (trailing > leading  && trailing >= 2)
			return 'l';

		if (leading <= 1 && trailing <= 1)
			return 'l';

		return 'c';
	}

	private int multirow(int startFromRow, int column) {
		int multirowIndex = 0;
		int i = startFromRow + 1;
		while(true) {
			if (i >= rowNo)
				break;
			if (!matrix.get(i).get(column).contains(":::"))
				break;
			multirowIndex += 1;
			i += 1;
		}
		return multirowIndex;
	}

	private void printLaTeXMatrix() {
		char align;
		System.out.print("\\begin{tabular}{|");
		for (int i = 0 ; i < colNo; i++)
			System.out.print("l ");
		System.out.println("|}");

		int r = -1, c = -1;
		System.out.println("\\hline");

		for (ArrayList<String> rowList : matrix) {
			r += 1;
			c = -1;
			int toIgnore = 0;
			for (String cell : rowList) {
				c += 1;
				if (toIgnore > 0) {
					toIgnore -= 1;
					continue;
				}
				// see if multicolumn
				int multicolumnIndex = multicolumn(r, c);
				// System.out.println("DEBUG: multicolumn " + multicolumnIndex);
				// see if multirow
				int multirowIndex = multirow(r, c);
				// System.out.println("DEBUG: multirow " + multirowIndex);
				if (c > 0)
					System.out.print("&");

				if (cell.equals("")) {

				}
				else if(multicolumnIndex == 0 && multirowIndex == 0) {
					// print cell
					/*
					if (cell.contains("\\begin{verbatim}") || cell.contains("\\vbox")) {
						if (cell.startsWith("|")){
							System.out.print(cell.replace("|", ""));
						} else {
							System.out.print("\\cellcolor[gray]{0.8}{" + cell.replace("^", "") + "}");
						}
					} else
					*/
					if (cell.startsWith("|")){
						// cell = cell.substring(1, cell.length());
						// align = checkAlign(cell);
						// cell = cell.trim();
						// if (align == 'l') {
						System.out.print(cell.replace("|", ""));
						// } else if (align == 'r') {
						// 	System.out.print("\\multicolumn{1}{r}{" + cell + "}");
						// } else {
						// 	System.out.print("\\multicolumn{1}{c}{" + cell + "}");
						// }
						// System.out.print(cell.replace("|", ""));
					} else {
						System.out.print("\\cellcolor[gray]{0.8}{" + cell.replace("^", "") + "}");
					}

				} else if (multicolumnIndex > 0) {
					toIgnore = multicolumnIndex;
					System.out.print("\\multicolumn{" + (1 + multicolumnIndex) + "}{l}{");
					if (cell.startsWith("|")){
						System.out.print(cell.replace("|", ""));
					}
					else {
						System.out.print("\\cellcolor[gray]{0.8}{" + cell.replace("^", "") + "}");
					}
					System.out.print("} ");
					
				} else if (multirowIndex > 0) {
					System.out.print("\\multirow{" + (1 + multirowIndex) + "}*{");
					if (cell.startsWith("|")){
						System.out.print(cell.replace("|", ""));
					}
					else {
						System.out.print("\\cellcolor[gray]{0.8}{" + cell.replace("^", "") + "}");
					}
					System.out.print("} ");

					for (int i = r + 1; i < multirowIndex + r + 1; i ++) {
						ArrayList<String> rowListAux = matrix.get(i);
						rowListAux.set(c, "");
					}
				} 

				

			}
			System.out.println("\\\\");
		}
		System.out.println("\\hline");
		System.out.println("\\end{tabular}");
	}

	@Override
	public void enterTable(DokuWikiTablesGrammarParser.TableContext ctx) {
	}
	
	@Override
	public void exitTable(DokuWikiTablesGrammarParser.TableContext ctx) {
		printLaTeXMatrix();
	}

	@Override 
	public void enterRow(DokuWikiTablesGrammarParser.RowContext ctx) {
		rowNo += 1;
		colIndex = -1;
		rowIndex += 1;
		// if (rowNo != 1) {
		// 	System.out.println(" \\\\ \\hline");
		// }
		ArrayList<String> rowList = new ArrayList<String>();
		matrix.add(rowIndex, rowList);

		int start = ctx.start.getStartIndex();
    	int end = ctx.stop.getStopIndex();
     	Interval interval = new Interval(start, end);
 		
     	String row = ctx.start.getInputStream().getText(interval).toString();

     	colNo = Utils.countCharInString(row, '|') + Utils.countCharInString(row, '^') - 1;

     	// if (rowNo == 1) {
     	// 	System.out.print("{|");
     	// 	for (int k = 0; k < colNo; k ++)
     	// 		System.out.print("c|");
     	// 	System.out.println("}");
     	// }
	}
	
	@Override
	public void exitRow(DokuWikiTablesGrammarParser.RowContext ctx) {

	}
	
	@Override public void enterCell(DokuWikiTablesGrammarParser.CellContext ctx) {
		colIndex += 1;
		ArrayList<String> rowList = matrix.get(rowIndex);

		int start = ctx.start.getStartIndex();
    	int end = ctx.stop.getStopIndex();
     	Interval interval = new Interval(start, end);
 		
     	String cell = ctx.start.getInputStream().getText(interval).toString();
     	if (cell.startsWith("^"))
			rowList.add(colIndex, "^");
		else
			rowList.add(colIndex, "|");
	}
	
	@Override
	public void exitCell(DokuWikiTablesGrammarParser.CellContext ctx) {
		// if (colIndex < colNo - 1) {
		// 	// System.out.print("&");

		// 	// ArrayList<String> rowList = matrix.get(rowIndex);
		// 	// String elem = rowList.get(colIndex);
		// 	// elem += "&";
		// 	// rowList.set(colIndex, elem);
		// }
	}
	
	
	@Override
	public void enterTable_nowiki(DokuWikiTablesGrammarParser.Table_nowikiContext ctx) {
		if (ctx.NOWIKI_TOKEN() == null)
			return;
		String s = ctx.NOWIKI_TOKEN().toString();
		s = Utils.removeFirstAndLastTag(s);
		s = SpecialChars.replaceLaTeXSpecialChars(s);

		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += s; ///* "\\vbox{"   + "\\begin{verbatim}"  + */ s /* + "\\end{verbatim}"  + "}" */;
		rowList.set(colIndex, elem);
	}

	@Override
	public void enterTable_nowiki2(DokuWikiTablesGrammarParser.Table_nowiki2Context ctx) {
		if (ctx.NOWIKI_TOKEN2() == null)
			return;
		String s = ctx.NOWIKI_TOKEN2().toString();
		s = s.substring(2, s.length() - 2);
		s = SpecialChars.replaceLaTeXSpecialChars(s);

		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += s;//"\\vbox{" /* + "\\begin{verbatim}" */ + s  /* + "\\end{verbatim}" */ + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_code(DokuWikiTablesGrammarParser.Table_codeContext ctx) {
		if (ctx.CODE_TOKEN1() == null)
			return;
		Utils.writeInLogFile("  -> Be aware of code sections inside table");
		String s = ctx.CODE_TOKEN1().toString();
		s = Utils.removeFirstAndLastTag(s);

		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + "\\begin{verbatim}" + s + "\\end{verbatim}" + "}";
		rowList.set(colIndex, elem);
	}

	@Override
	public void enterTable_file(DokuWikiTablesGrammarParser.Table_fileContext ctx) {
		if (ctx.FILE_TOKEN() == null)
			return;
		Utils.writeInLogFile("  -> Be aware of code sections inside table");
		String s = ctx.FILE_TOKEN().toString();
		s = Utils.removeFirstAndLastTag(s);

		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + "\\begin{verbatim}" + s + "\\end{verbatim}" + "}";
		rowList.set(colIndex, elem);
	}
	
//	@Override
//	public void enterTable_latexs(DokuWikiTablesGrammarParser.Table_latexsContext ctx) {
//		String s = ctx.LATEX_TOKEN_S().toString();
//		ArrayList<String> rowList = matrix.get(rowIndex);
//		String elem = rowList.get(colIndex);
//		elem += s;
//		rowList.set(colIndex, elem);
//	}
//	
//	@Override
//	public void enterTable_latexss(DokuWikiTablesGrammarParser.Table_latexssContext ctx) {
//		String s = ctx.LATEX_TOKEN_SS().toString();
//		ArrayList<String> rowList = matrix.get(rowIndex);
//		String elem = rowList.get(colIndex);
//		elem += s.substring(1, s.length() - 1);
//		rowList.set(colIndex, elem);
//	}
	
	@Override
	public void enterTable_latextag(DokuWikiTablesGrammarParser.Table_latextagContext ctx) {

		String s = ctx.LATEX_TOKEN_TAG().toString();
		s = Utils.removeFirstAndLastTag(s);
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += s.trim();
		rowList.set(colIndex, elem);
	}

	@Override
	public void enterTable_latex_displaymath(DokuWikiTablesGrammarParser.Table_latex_displaymathContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		
		String s = ctx.LATEX_TOKEN_DISPLAYMATH().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_eqnarray(DokuWikiTablesGrammarParser.Table_latex_eqnarrayContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");

		String s = ctx.LATEX_TOKEN_EQNARRAY().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_eqnarray_star(DokuWikiTablesGrammarParser.Table_latex_eqnarray_starContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");

		String s = ctx.LATEX_TOKEN_EQNARRAY_STAR().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	

	@Override
	public void enterTable_latex_equation(DokuWikiTablesGrammarParser.Table_latex_equationContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");

		String s = ctx.LATEX_TOKEN_EQUATION().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_equation_star(DokuWikiTablesGrammarParser.Table_latex_equation_starContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_EQUATION_STAR().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}

	@Override
	public void enterTable_latex_align(DokuWikiTablesGrammarParser.Table_latex_alignContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_ALIGN().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_align_star(DokuWikiTablesGrammarParser.Table_latex_align_starContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_ALIGN_STAR().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_alignat(DokuWikiTablesGrammarParser.Table_latex_alignatContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_ALIGNAT().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_alignat_star(DokuWikiTablesGrammarParser.Table_latex_alignat_starContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_ALIGNAT_STAR().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_flalign(DokuWikiTablesGrammarParser.Table_latex_flalignContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_FLALIGN().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_flalign_star(DokuWikiTablesGrammarParser.Table_latex_flalign_starContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_FLALIGN_STAR().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_gather(DokuWikiTablesGrammarParser.Table_latex_gatherContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_GATHER().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_gather_star(DokuWikiTablesGrammarParser.Table_latex_gather_starContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_GATHER_STAR().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_multiline(DokuWikiTablesGrammarParser.Table_latex_multilineContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
  		String s = ctx.LATEX_TOKEN_MULTILINE().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_multiline_star(DokuWikiTablesGrammarParser.Table_latex_multiline_starContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_MULTILINE_STAR().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_latex_math(DokuWikiTablesGrammarParser.Table_latex_mathContext ctx) {
		Utils.writeInLogFile("  -> Be aware of LaTeX sections inside table");
		String s = ctx.LATEX_TOKEN_MATH().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\vbox{" + s + "}";
		rowList.set(colIndex, elem);
	}

	@Override
	public void enterTable_link(DokuWikiTablesGrammarParser.Table_linkContext ctx) {
		String s = ctx.LINK().toString();
		if (s.length() <= 4)
			return;
		s = s.substring(2, s.length() - 2);
		s = s.trim();

		StringBuilder linkSB = new StringBuilder();

		try {
			InputStream linkIs = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

			ANTLRInputStream linksInput = new ANTLRInputStream(linkIs);

        	DokuWikiLinksGrammarLexer linksLexer = new DokuWikiLinksGrammarLexer(linksInput);
        	CommonTokenStream linksTokens = new CommonTokenStream(linksLexer);
	        DokuWikiLinksGrammarParser linksParser = new DokuWikiLinksGrammarParser(linksTokens);
	        ParseTree linksTree = linksParser.links_start();

	        ParseTreeWalker linksWalker = new ParseTreeWalker();
	        DokuWikiLinkListener linksExtractor = new DokuWikiLinkListener(linksParser, linkSB);
	        linksWalker.walk(linksExtractor, linksTree);
    	} catch (Exception e) {
    		System.err.println(e);
    	}

		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += linkSB.toString();
		rowList.set(colIndex, elem);
	}

	@Override
	public void enterTable_url1(DokuWikiTablesGrammarParser.Table_url1Context ctx) {
		String s = ctx.URL1().toString();
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\url{" + s + "}";
		rowList.set(colIndex, elem);	
	}
	
	@Override public void enterTable_mail(DokuWikiTablesGrammarParser.Table_mailContext ctx) {
		String mail = ctx.MAIL().toString();
		mail = mail.replace("<", "");
		mail = mail.replace(">", "");

		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\href{mailto:" + mail + "}{" + mail + "}";
		rowList.set(colIndex, elem);
	}

	@Override
	public void enterTable_media(DokuWikiTablesGrammarParser.Table_mediaContext ctx) {
		Utils.writeInLogFile("  -> Be aware of images inside a table."
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
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);

		if (!text.isEmpty())
			elem += "\\caption{" + text + "} ";
		elem += "\\vbox{\\includegraphics";

		if (width > -1) {
			if (height > -1) {
				elem += "[width = " + width + "cm, height = " + height + "cm, keepaspectratio]";
			} else {
				elem += "[width = " + width + "cm, keepaspectratio]";
			}
		}
		elem += "{" + imageName + "}}";

		rowList.set(colIndex, elem);
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(Utils.logImagesFileName, true));
			output.write(imageName + "\n");
			output.close();
		} catch (Exception e) {

		}

	}

	@Override
	public void enterTable_html(DokuWikiTablesGrammarParser.Table_htmlContext ctx) {
		String s = ctx.HTML_TOKEN().toString();
		Utils.writeInLogFile("  -> HTML section in table\n"
									+ "    -> HTML sections are not converted in LaTeX."
									);
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += s;
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_php(DokuWikiTablesGrammarParser.Table_phpContext ctx) {
		String s = ctx.PHP_TOKEN().toString();
		Utils.writeInLogFile("  -> PHP section as list table\n"
									+ "    -> PHP sections are not converted in LaTeX."
									);
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += s;
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_bold(DokuWikiTablesGrammarParser.Table_boldContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\textbf{";
		rowList.set(colIndex, elem);
	}

	@Override
	public void exitTable_bold(DokuWikiTablesGrammarParser.Table_boldContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "}";
		rowList.set(colIndex, elem);
	}

	@Override
	public void enterTable_italic(DokuWikiTablesGrammarParser.Table_italicContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\textit{";
		rowList.set(colIndex, elem);

	}
	
	@Override
	public void exitTable_italic(DokuWikiTablesGrammarParser.Table_italicContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "}";
		rowList.set(colIndex, elem);
	}
	
	@Override 
	public void enterTable_underline(DokuWikiTablesGrammarParser.Table_underlineContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\underline{";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void exitTable_underline(DokuWikiTablesGrammarParser.Table_underlineContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_monospaced(DokuWikiTablesGrammarParser.Table_monospacedContext ctx) { 
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\texttt{";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void exitTable_monospaced(DokuWikiTablesGrammarParser.Table_monospacedContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_superscript(DokuWikiTablesGrammarParser.Table_superscriptContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\textsuperscript{";
		rowList.set(colIndex, elem);

	}
	
	@Override
	public void exitTable_superscript(DokuWikiTablesGrammarParser.Table_superscriptContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "}";
		rowList.set(colIndex, elem);
	}
	
	@Override
	public void enterTable_subscript(DokuWikiTablesGrammarParser.Table_subscriptContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "$_{\\text{";
		rowList.set(colIndex, elem);

	}
	
	@Override
	public void exitTable_subscript(DokuWikiTablesGrammarParser.Table_subscriptContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "}}$";
		rowList.set(colIndex, elem);

	}
	
	@Override
	public void enterTable_del(DokuWikiTablesGrammarParser.Table_delContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\sout{";
		rowList.set(colIndex, elem);

	}
	
	@Override
	public void exitTable_del(DokuWikiTablesGrammarParser.Table_delContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "}";
		rowList.set(colIndex, elem);

	}
	
	@Override
	public void enterTable_footnote(DokuWikiTablesGrammarParser.Table_footnoteContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "\\footnote{";
		rowList.set(colIndex, elem);

	}
	
	@Override
	public void exitTable_footnote(DokuWikiTablesGrammarParser.Table_footnoteContext ctx) {
		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += "}";
		rowList.set(colIndex, elem);

	}

	@Override
	public void enterTable_unparsed(DokuWikiTablesGrammarParser.Table_unparsedContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTable_unparsed(DokuWikiTablesGrammarParser.Table_unparsedContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRaw_text(DokuWikiTablesGrammarParser.Raw_textContext ctx) {
		String s;
		if(ctx.UNPARSED() != null) {
			s = ctx.UNPARSED().toString();
			if (s.matches("((.|\n)*)" + SpecialChars.regex_keys +"((.|\n)*)")) {
				s = SpecialChars.replaceSpecialChars(s);
			}
			s = SpecialChars.replaceLaTeXSpecialChars(s);

			ArrayList<String> rowList = matrix.get(rowIndex);
			String elem = rowList.get(colIndex);
			elem += s;
			rowList.set(colIndex, elem);

			// System.out.print(s);
			// System.out.print(ctx.UNPARSED());
			if (ctx.ANYCHAR() == null)
				return;
		}
		
		if (ctx.SPECIAL_CHAR() != null) {
			s = ctx.SPECIAL_CHAR().toString();
			if (s.matches("((.|\n)*)" + SpecialChars.regex_keys +"((.|\n)*)")) {
				s = SpecialChars.replaceSpecialChars(s);
			}
			s = SpecialChars.replaceLaTeXSpecialChars(s);


			ArrayList<String> rowList = matrix.get(rowIndex);
			String elem = rowList.get(colIndex);
			elem += s;
			rowList.set(colIndex, elem);
			// System.out.print(s);
		}

		if (ctx.ANYCHAR() == null)
			return;
		s = ctx.ANYCHAR().toString();
		if (s.matches("((.|\n)*)" + SpecialChars.regex_keys +"((.|\n)*)")) {
			s = SpecialChars.replaceSpecialChars(s);
		}
		s = SpecialChars.replaceLaTeXSpecialChars(s);

		// System.out.print(s);

		ArrayList<String> rowList = matrix.get(rowIndex);
		String elem = rowList.get(colIndex);
		elem += s;
		rowList.set(colIndex, elem);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRaw_text(DokuWikiTablesGrammarParser.Raw_textContext ctx) {

	}

}
