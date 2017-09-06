# extract-content

[![CircleCI](https://img.shields.io/circleci/project/github/u6k/extract-content.svg)](https://circleci.com/gh/u6k/extract-content)
[![license](https://img.shields.io/github/license/u6k/extract-content.svg)](https://github.com/u6k/extract-content/blob/master/LICENSE)
[![GitHub release](https://img.shields.io/github/release/u6k/extract-content.svg)](https://github.com/u6k/extract-content/releases)
[![Docker Pulls](https://img.shields.io/docker/pulls/u6kapps/extract-content.svg)](https://hub.docker.com/r/u6kapps/extract-content/)

HTML文書の本文部分を抽出します。

## Description

ブログやニュースなどのHTML文書は、メニュー・広告など本文以外の様々な情報が含まれます。これら余計な情報を除去し、本文部分のHTMLだけを抽出します。本文抽出処理はWebAPIとして提供するため、どの処理系でも使用することができます。

## Requirement

Dockerを使用する場合、Docker以外の必要なソフトウェアはビルド時にインストールされます。

- Docker

```
$ docker version
Client:
 Version:      17.03.1-ce
 API version:  1.27
 Go version:   go1.7.5
 Git commit:   c6d412e
 Built:        Tue Mar 28 00:40:02 2017
 OS/Arch:      windows/amd64

Server:
 Version:      17.04.0-ce
 API version:  1.28 (minimum version 1.12)
 Go version:   go1.7.5
 Git commit:   4845c56
 Built:        Wed Apr  5 18:45:47 2017
 OS/Arch:      linux/amd64
 Experimental: false
```

- Python 3.x

```
$ python --version
Python 3.6.2
```

- Pandoc

```
# pandoc --version
pandoc 1.12.4.2
Compiled with texmath 0.6.6.1, highlighting-kate 0.5.8.5.
Syntax highlighting is supported for the following languages:
    actionscript, ada, apache, asn1, asp, awk, bash, bibtex, boo, c, changelog,
    clojure, cmake, coffee, coldfusion, commonlisp, cpp, cs, css, curry, d,
    diff, djangotemplate, doxygen, doxygenlua, dtd, eiffel, email, erlang,
    fortran, fsharp, gcc, gnuassembler, go, haskell, haxe, html, ini, isocpp,
    java, javadoc, javascript, json, jsp, julia, latex, lex, literatecurry,
    literatehaskell, lua, makefile, mandoc, markdown, matlab, maxima, metafont,
    mips, modelines, modula2, modula3, monobasic, nasm, noweb, objectivec,
    objectivecpp, ocaml, octave, pascal, perl, php, pike, postscript, prolog,
    pure, python, r, relaxngcompact, restructuredtext, rhtml, roff, ruby, rust,
    scala, scheme, sci, sed, sgml, sql, sqlmysql, sqlpostgresql, tcl, texinfo,
    verilog, vhdl, xml, xorg, xslt, xul, yacc, yaml
Default user data directory: /root/.pandoc
Copyright (C) 2006-2014 John MacFarlane
Web:  http://johnmacfarlane.net/pandoc
This is free software; see the source for copying conditions.  There is no
warranty, not even for merchantability or fitness for a particular purpose.
```

## Usage

### 本文抽出

urlパラメータに、本文部分を抽出したいURLを指定します。

```
$ curl -v "http://localhost:5000/extract?url=https%3A%2F%2Ftechcrunch.com%2F2017%2F09%2F02%2Fthe-product-design-challenges-of-ar-on-smartphones%2F"
```

JSONが返ります。

```
< HTTP/1.0 200 OK
< Content-Type: application/json
< Content-Length: 280396
< Server: Werkzeug/0.12.2 Python/3.6.2
< Date: Tue, 05 Sep 2017 07:19:56 GMT
<
{
  "content": "<html><body><div><div class=\"article-entry text l-featured-container\">...(中略)...</body></html>",
  "full-content": "<!DOCTYPE html>\n<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:og=\"http://opengraphprotocol.org/schema/\" xmlns:fb=\"http://www.facebook.com/2008/fbml\" lang=\"en\">...(中略)...</body>\n</html>\n<!--\n\tgenerated 268 seconds ago\n\tgenerated in 0.216 seconds\n\tserved from batcache in 0.002 seconds\n\texpires in 32 seconds\n-->\n",
  "markdown-content": "With the launch of ARKit, we are going to see augmented reality apps become available for about 500 million iPhones in the next 12 months, and...(中略)...Featured Image: Hero Images/Getty Images\n\n\n",
  "title": "The product design challenges of AR on smartphones",
  "url": "https://techcrunch.com/2017/09/02/the-product-design-challenges-of-ar-on-smartphones/"
}
```

## Installation

### for Docker

実行用Dockerコンテナを起動します。

```
$ docker run \
    -d \
    --name extract-content \
    -p 5000:5000 \
    u6kapps/extract-content
```

### for Python

`main.py`を実行します。

```
$ python main.py
```

## Development

### 開発環境を構築

#### for Docker

開発用Dockerイメージをビルドします。

```
$ docker build -t extract-content-dev -f Dockerfile-dev .
```

開発用Dockerコンテナを起動します。

```
$ docker run \
    --rm \
    --name extract-content-dev \
    -p 5000:5000 \
    -v ${PWD}:/opt/extract-content \
    extract-content-dev
```

#### for Python

Pandocをインストールします。

```
$ apt-get install -y pandoc
```

Pythonライブラリをインストールします。

```
$ pip install Flask lxml readability-lxml requests beautifulsoup4 pypandoc
```

### Swaggerドキュメントを参照

Swaggerドキュメントを参照するには、swagger-uiコンテナを起動します。

```
$ docker run \
    --rm \
    -p 8080:8080 \
    -e "SWAGGER_JSON=/opt/swagger.yaml" \
    -v ${PWD}:/opt \
    swaggerapi/swagger-ui
```

TODO: 2017/9/4時点のswagger-codegenは、`openapi 3.0.0`のSwaggerドキュメントを読み込めないもよう。対応されたら、ビルド・プロセスで静的ファイルを出力するようにします。

### ビルド

#### for Docker

実行用Dockerイメージをビルドします。

```
$ docker build -t u6kapps/extract-content .
```

#### for Python

Python環境の場合、ビルドは必要ありません。

## Author

- [u6k/extract-content](https://github.com/u6k/extract-content)
- [extract-content - u6k.Redmine](https://redmine.u6k.me/projects/extract-content)
- [u6k.Blog](https://blog.u6k.me/)

## License

[MIT License](https://github.com/u6k/extract-content/blob/master/LICENSE)
