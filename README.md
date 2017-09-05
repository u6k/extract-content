# extract-content

[![CircleCI](https://img.shields.io/circleci/project/github/u6k/extract-content.svg)](https://circleci.com/gh/u6k/extract-content)
[![license](https://img.shields.io/github/license/u6k/extract-content.svg)](https://github.com/u6k/extract-content/blob/master/LICENSE)
[![GitHub release](https://img.shields.io/github/release/u6k/extract-content.svg)](https://github.com/u6k/extract-content/releases)
[![Docker Pulls](https://img.shields.io/docker/pulls/u6kapps/extract-content.svg)](https://hub.docker.com/r/u6kapps/extract-content/)

HTML文書の本文部分を抽出します。

## Description

ブログやニュースなどのHTML文書は、メニュー・広告など本文以外の様々な情報が含まれます。これら余計な情報を除去し、本文部分のHTMLだけを抽出します。本文抽出処理はWebAPIとして提供するため、どの処理系でも使用することができます。

## Requirement

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

## Usage

### 本文抽出

urlパラメータに、本文部分を抽出したいURLを指定します。

```
$ curl -v "http://localhost:5000/extract?url=http%3a%2f%2fjp%2etechcrunch%2ecom%2f2017%2f08%2f31%2f20170826this-vr-cycle-is-dead%2f"
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
	"content": "<html><body><div><div class=\"article-entry text\" ...(中略)...</body></html>",
	"full-content": "<!DOCTYPE html>\n<html xmlns=\"http://www.w3.org/1999/xhtml\" ...(中略)...expires in 77 seconds\n-->\n",
	"title": "VR\u306e\u4eca\u56de\u306e\u30cf\u30a4\u30d7\u30fb\u30b5\u30a4\u30af\u30eb\u306f\u7d42\u308f\u3063\u305f | TechCrunch Japan",
	"url": "http://jp.techcrunch.com/2017/08/31/20170826this-vr-cycle-is-dead/"
}
```

### Swaggerドキュメントを参照

Swaggerドキュメントを参照するには、swagger-uiコンテナを起動します。

```
$ docker run \
    -d \
    -e "SWAGGER_JSON=/opt/swagger.yaml" \
    -v ${PWD}/src/doc:/opt \
    swaggerapi/swagger-ui
```

TODO: 2017/9/4時点のswagger-codegenは、`openapi 3.0.0`のSwaggerドキュメントを読み込めないもよう。対応されたら、ビルド・プロセスで静的ファイルを出力するようにします。

## Installation

実行用Dockerコンテナを起動します。

```
$ docker run \
    --name extract-content \
    -p 5000:5000 \
    u6kapps/extract-content
```

## Development

### 開発環境を構築

開発用Dockerイメージをビルドします。

```
$ docker build -t extract-content-dev -f Dockerfile-dev .
```

開発用Dockerコンテナを起動します。

```
$ docker run \
    -it \
    --rm \
    --name extract-content-dev \
    -p 5000:5000 \
    -v ${PWD}:/opt/extract-content \
    extract-content-dev
```

`main.py`を実行します。

```
$ python main.py
```

### ビルド

実行用Dockerイメージをビルドします。

```
$ docker build -t u6kapps/extract-content .
```

## Author

- [u6k/extract-content](https://github.com/u6k/extract-content)
- [extract-content - u6k.Redmine](https://redmine.u6k.me/projects/extract-content)
- [u6k.Blog](https://blog.u6k.me/)

## License

[MIT License](https://github.com/u6k/extract-content/blob/master/LICENSE)
