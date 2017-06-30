# DokuWiki Links Grammar

## Overview
This is a grammar able to parse internal and external links from DokuWiki syntax.

NOTE: It only parse the text between **[[** and **]]** with no leading or trailing whitespaces.

## Syntax supported
Links should be in this format:
* page
* page | page_text
* namespace : page
* namespace : page | page_text
* interwiki_text > page
* interwiki_text > page | page_text

## Interwiki
DokuWiki has an implementation for some [interwikis](https://www.dokuwiki.org/Interwiki) links.
This grammar parses all text before '>' token and, in a listener class treats only : wikipedia, phpfn, google search, skype and current wiki.
