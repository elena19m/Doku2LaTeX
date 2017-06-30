/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;
import java.util.ArrayList;


public class DokuWikiLinkListener extends DokuWikiLinksGrammarBaseListener {
	DokuWikiLinksGrammarParser parser;
	private String wikihost = "";
	private StringBuilder out = null;

	public DokuWikiLinkListener(DokuWikiLinksGrammarParser parser) {
		this.parser = parser;
	}

	public DokuWikiLinkListener(DokuWikiLinksGrammarParser parser, String dokuWikiHost) {
		this.parser = parser;
		this.wikihost = dokuWikiHost;
	}

	public DokuWikiLinkListener(DokuWikiLinksGrammarParser parser, String dokuWikiHost,
								StringBuilder output){
		this.parser = parser;
		this.wikihost = dokuWikiHost;
		this.out = output;
	}

	public DokuWikiLinkListener(DokuWikiLinksGrammarParser parser, StringBuilder output){
		this.parser = parser;
		this.out = output;
	}
	
	@Override
	public void enterExternal1(DokuWikiLinksGrammarParser.External1Context ctx) {
		String s = ctx.URL1().toString();
		if (out != null) {
			out.append("\\url{" + s + "}");
		} else {
			System.out.print("\\url{" + s + "}");
		}	
	}

	@Override 
	public void enterInternal1(DokuWikiLinksGrammarParser.Internal1Context ctx) {
		String s = ctx.PAGENAME().toString();
		if (out != null) {
			out.append("\\url{" + wikihost + s + "}");
		} else {
			System.out.print("\\url{" + wikihost + s + "}");
		}
	}


	@Override
	public void enterExternal_text(DokuWikiLinksGrammarParser.External_textContext ctx) {
		String s = ctx.URL1().toString();
		String text;
 		
 		int start = ctx.start.getStartIndex();
    	int end = ctx.stop.getStopIndex();
     	Interval interval = new Interval(start, end);
 		
     	text = ctx.start.getInputStream().getText(interval).toString();
 		text = text.replace(s, "");
 		text = text.trim();
 		text = text.replace("|", "");
 		if (text.startsWith("{{") && text.endsWith("}}")) {
			text = text.substring(2, text.length() - 2);
 			if (out != null) {
 				out.append("\\href{" + s + "}" + "{" + "\\includegraphics{" + text + "}" + "}");
 			} else {
				System.out.print("\\href{" + s + "}" + "{" + "\\includegraphics{" + text + "}" + "}");
			}
  		} else {
	 		if (out != null) {
	 			out.append("\\href{" + s + "}" + "{" + text + "}");
	 		} else {
				System.out.print("\\href{" + s + "}" + "{" + text + "}");
			}
		}
	}

	@Override
	public void enterInternal_text(DokuWikiLinksGrammarParser.Internal_textContext ctx) {
		String s = ctx.PAGENAME().toString();
		String text;
 		
 		int start = ctx.start.getStartIndex();
    	int end = ctx.stop.getStopIndex();
     	Interval interval = new Interval(start, end);
 		
     	text = ctx.start.getInputStream().getText(interval).toString();
 		text = text.replace(s, "");
 		text = text.trim();
 		text = text.replace("|", "");

		if (text.startsWith("{{") && text.endsWith("}}")) {
			text = text.substring(2, text.length() - 2);
 			if (out != null) {
 				out.append("\\href{" + wikihost +  s + "}" + "{" + "\\includegraphics{" + text + "}" + "}");
 			} else {
				System.out.print("\\href{" + wikihost +  s + "}" + "{" + "\\includegraphics{" + text + "}" + "}");
			}
  		} else {
	 		if (out != null) {
	 			out.append("\\href{" + wikihost +  s + "}" + "{" + text + "}");
	 		} else {
				System.out.print("\\href{" + wikihost +  s + "}" + "{" + text + "}");
	 		}
	 	}
	}


	@Override
	public void enterInternal_ns(DokuWikiLinksGrammarParser.Internal_nsContext ctx) {
		String page = "";
		page = ctx.PAGENAME().get(0).toString();
		for (int i = 1; i < ctx.PAGENAME().size(); i++)
			page = page + ":" + ctx.PAGENAME().get(i).toString();
/*		String namespaces = ctx.NAMESPACES().toString();
		String pagename = ctx.PAGENAME().toString();
		System.out.print("\\url{" + wikihost + namespaces + ":" + pagename + "}");
*/
		if (out != null){
			out.append("\\url{" + wikihost + page + "}");
		} else{
			System.out.print("\\url{" + wikihost + page + "}");
		}
	}

	@Override
	public void enterInternal_ns_text(DokuWikiLinksGrammarParser.Internal_ns_textContext ctx) {
		String page = "";
		String text = "";	
 		int start = ctx.start.getStartIndex();
    	int end = ctx.stop.getStopIndex();
     	Interval interval = new Interval(start, end);
 		
     	text = ctx.start.getInputStream().getText(interval).toString();
     	String[] parts = text.split("\\|");
     	page = parts[0];
     	text = parts[1];

		if (text.startsWith("{{") && text.endsWith("}}")) {
			text = text.substring(2, text.length() - 2);
 			if (out != null) {
 				out.append("\\href{" + wikihost + page + "}" + "{" + "\\includegraphics{" + text + "}" + "}");
 			} else {
				System.out.print("\\href{" + wikihost + page + "}" + "{" + "\\includegraphics{" + text + "}" + "}");
			}
  		} else {
	     	if (out != null) {
	     		out.append("\\href{" + wikihost + page + "}{" + text + "}");
	     	} else {
				System.out.print("\\href{" + wikihost + page + "}{" + text + "}");
	     	}
	     }
	}

	@Override
	public void enterInterwiki(DokuWikiLinksGrammarParser.InterwikiContext ctx) {
		String s1 = ctx.PAGENAME().get(0).toString();
		String s2 = ctx.PAGENAME().get(1).toString();
		String url = s1;

		switch(s1) {
			case "wp" :
				url = "https://en.wikipedia.org/wiki/";
				break;
			case "phpfn" :
				url = "https://secure.php.net/";
				break;
			case "google" :
				url = "https://www.google.com/search?q=";
				break;
			case "doku" :
				url = "https://www.dokuwiki.org/";
				break;
			case "skype" :
				url = "skype:";
				break;
			case "this" :
				url = wikihost;
		}
		if (out != null) {
			out.append("\\url{" + url + s2 + "}");
		} else {
			System.out.print("\\url{" + url + s2 + "}");
		}


	}

	@Override
	public void enterInterwiki_text(DokuWikiLinksGrammarParser.Interwiki_textContext ctx) {
		String s1 = ctx.PAGENAME().get(0).toString();
		String s2 = ctx.PAGENAME().get(1).toString();
		String url = s1;
		String text;
 		
 		int start = ctx.start.getStartIndex();
    	int end = ctx.stop.getStopIndex();
     	Interval interval = new Interval(start, end);
 		
     	text = ctx.start.getInputStream().getText(interval).toString();
 		text = text.replace(s1, "");
 		text = text.replace(s2, "");
 		text = text.trim();
 		text = text.replace(">", "");
 		text = text.replace("|", "");
 		text = text.trim();

		switch(s1) {
			case "wp" :
				url = "https://en.wikipedia.org/wiki/";
				break;
			case "phpfn" :
				url = "https://secure.php.net/";
				break;
			case "google" :
				url = "https://www.google.com/search?q=";
				break;
			case "doku" :
				url = "https://www.dokuwiki.org/";
				break;
			case "skype" :
				url = "skype:";
				break;
			case "this" :
				url = wikihost;
		}
		if (text.startsWith("{{") && text.endsWith("}}")) {
			text = text.substring(2, text.length() - 2);
 			if (out != null) {
 				out.append("\\href{" + url + s2 + "}" + "{" + "\\includegraphics{" + text + "}" + "}");
 			} else {
				System.out.print("\\href{" + url + s2 + "}" + "{" + "\\includegraphics{" + text + "}" + "}");
			}
  		} else {
			if (out != null) {
				out.append("\\href{" + url + s2 + "}{" + text + "}");
			} else {
				System.out.print("\\href{" + url + s2 + "}{" + text + "}");
			}
		}
	}
}
