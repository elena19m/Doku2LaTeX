##
# Author: Mihailscu Maria-Elena
# Faculty of Automatic Control and Computer Science,
# University POLITHENICA of Bucharest
#

build: grammar link_grammar list_grammar tables_grammar compile

grammar:
	java -jar /usr/local/lib/antlr-4.6-complete.jar DokuWikiGrammar.g4

link_grammar:
	java -jar /usr/local/lib/antlr-4.6-complete.jar DokuWikiLinksGrammar.g4

list_grammar:
	java -jar /usr/local/lib/antlr-4.6-complete.jar DokuWikiListGrammar.g4

tables_grammar:
	java -jar /usr/local/lib/antlr-4.6-complete.jar DokuWikiTablesGrammar.g4

compile:
	javac Doku2Latex*.java DokuWikiGrammar*.java SpecialChars.java Utils.java DokuWikiLink*.java DokuWikiList*.java DokuWikiTables*.java

compile_grammar:
	javac DokuWikiGrammarBaseListener.java DokuWikiGrammarLexer.java DokuWikiGrammarListener.java DokuWikiGrammarParser.java

clean_class:
	rm *.class

clean_java:
	rm DokuWikiGrammarBaseListener.java DokuWikiGrammarLexer.java DokuWikiGrammarListener.java DokuWikiGrammarParser.java

clean_tokens:
	rm *.tokens

clean: clean_class
