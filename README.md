# doku2latex
Dokuwiki to Latex converter


# How to install ANTLR4
From "The Definitive ANTLR 4 Reference" by Terence Parr:

Step 1.
	Download antlr4 jar from www.antlr.org
	or

	curl -0 http://www.antlr.org/download/antlr-4.6-complete.jar

Step 2.
	Move antlr-4.6-complete.jar to /usr/local/lib

Step 3.
	Set CLASSPATH enviroment variable. It could be useful to add this in .bashrc

	export CLASSPATH=".:/usr/local/lib/antlr-4.6-complete.jar:$CLASSPATH"


# How to use ANTLR4 and parse grammar
You can use the Makefile already created or:
	# to obtain java files associated with grammar run
	java -jar /usr/local/lib/antlr-4.6-complete.jar DokuWikiGrammar.g4

	# to compile Java files
	javac *.java

	# to parse a file and generate graphical tree
	java org.antlr.v4.runtime.misc.TestRig DokuWikiGrammar wiki_start -gui -input-filename <file_name>

	# to parse a fle and generate tokens
	java org.antlr.v4.runtime.misc.TestRig DokuWikiGrammar wiki_start -tokens -input-filename <file_name>

	# for more options run
	java org.antlr.v4.runtime.misc.TestRig

# How to compile Doku2Latex Parser
	make build

	# or
	# obtain java files associated with grammar
	java -jar /usr/local/lib/antlr-4.6-complete.jar DokuWikiGrammar.g4

	# compile java files with Doku2Latex.java and Doku2LatexListener.java
	java Doku2Latex*.java DokuWikiGrammar*.java

# How to run Doku2Latex Parser
	java Doku2Latex <file_name>
