/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;

import java.io.*;

public class Doku2Latex {
	private static void makeLogFile(String fileName) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(Utils.logFileName, false));
			output.write("****************** Doku2Latex ******************\n");
			output.write("Start converting DokuWiki Markup into LaTeX \n");
			if (fileName == null)
				output.write("Reading from stdin \n\n");
			else
				output.write("Reading from " + fileName + "\n\n");

			output.close();
		} catch (Exception e) {}

	}

	public static void main(String[] args) throws Exception {
        String inputFile = null;

		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(Utils.logImagesFileName, false));
			output.close();
		} catch (Exception e) {}

        if (args.length > 0) 
        	inputFile = args[0];
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }

        makeLogFile(inputFile);

        ANTLRInputStream input = new ANTLRInputStream(is);

        DokuWikiGrammarLexer lexer = new DokuWikiGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DokuWikiGrammarParser parser = new DokuWikiGrammarParser(tokens);
        ParseTree tree = parser.wiki_start();

        ParseTreeWalker walker = new ParseTreeWalker();
        Doku2LatexListener extractor = new Doku2LatexListener(parser);
        walker.walk(extractor, tree);
    }
}
