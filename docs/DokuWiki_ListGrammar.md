# DokuWiki List Grammar

## Overview
This grammar parses only DokuWiki ordered and unordered lists.

A list item is represented on a line. There are some exceptions:
* code sections
* nowiki sections
* LaTeX blocks from latex and mathjax DokuWiki plugins
```
<latex> ... </latex>
```
and
```
\begin{<x>} ... \end{<x>}
```

A list item can not contain note sections.

This grammar is similar with [Doku Wiki Main Grammar](DokuWiki_Grammar.md).
