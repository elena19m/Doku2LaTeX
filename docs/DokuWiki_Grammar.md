# DokuWiki Grammar

This grammar was written for ANTLR4

This grammar has support for:
* [different types of headline](#headlines)
* [bold text](#simple-text-formatting)
* [italic text](#simple-text-formatting)
* [underline text](#simple-text-formatting)
* [monospaced](#simple-text-formatting)
* [superscript](#simple-text-formatting)
* [subscript](#simple-text-formatting)
* [deleted text](#simple-text-formatting)
* [footnote](#simple-text-formatting)
* [horizontal line](#horizontal-line)
* [code sections](#code-sections)
* [no wiki sections](#no-wiki-sections)
* [note sections for note plugin](#note)
* [latex syntax for latex and mathjax plugins](#latex-syntax)
* [links](#links)
* [lists](#lists)

## Overview
In this grammar, the input text is considered to be junks of formatted text. A formatted text (*formatted_text* rule) is a text that has at least one of the features supported or that is raw/unformatted (*raw_text* rule).

DokuWiki syntax can be found [here](https://www.dokuwiki.org/syntax).

## Headlines
DokuWiki supports multiple levels of headlines. Only rule is that there should be at least two **=** signs before and after headline's text. A headline cannot be formatted.

## Simple text formatting
Simple text formatting such as bold, italic, monospaced, superscript, subscript, deleted, or footnote are parsed using the same pattern:
```
TYPE formatted_text TYPE
```
because those options can be combined.

## Horizontal line
A horizontal line has at least four dashes on the same line.

## Code sections
A code section is considered to be everything between *CODE_START* and *CODE_END* tokens. Non-greedy syntax is used to parse only what is needed. A token is used for code syntax (*CODE_TOKEN1*) because wildcard character **.** has different meaning if is used in a token or in a rule in ANTLR4 (In a token, it means any character and, in a rule, it means any token).

## No wiki sections
As code sections, nowiki sections are considered to be everything between *NOWIKI_START* and *NOWIKI_END* tokens (*NOWIKI_TOKEN*).

Also, nowiki sections can be found between **%%** characters (*NOWIKI_TOKEN2*).

## Note
This grammar accepts note sections which are between *NOTE_START* and *NOTE_END* tokens.

[Note](https://www.dokuwiki.org/plugin:note) is a DokuWiki plugin.


## Links
Links are considered to be everything between **[[** and **]]** or an URL (*URL1* token).

Links (*LINK* token) are parsed by another grammar: [DokuWikiLinksGrammar.g4](./DokuWiki_LinksGrammar.md).

## Lists
Lists are considered to be more than one lines which starts with an even number of spaces (more that two) and a symbol for list type followed by item's text.
```
(SPACE SPACE)+ LIST_TYPE text
```
Particular cases such as code or nowiki sections in lists are considered.

## LaTeX syntax
LaTeX syntax as described in [latex](https://www.dokuwiki.org/plugin:latex) and [mathjax](https://www.dokuwiki.org/plugin:mathjax) plugin is parsed.

This Grammar is used to convert DokuWiki syntax in LaTeX syntax and it is useful to have support for this kind of plugins.
