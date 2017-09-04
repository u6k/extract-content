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

本文部分を抽出するには、以下のようにファイルをアップロードします。

```
$ curl -v \
    -X POST \
    -F file=@/path/to/file \
    http://localhost:5000/
```

本文部分が返ります。

```
HTTP/1.0 200 OK
Content-Type: text/html; charset=utf-8
Content-Length: 18651
Server: Werkzeug/0.12.1 Python/3.6.1
Date: Tue, 25 Apr 2017 10:08:05 GMT

<html><body><div><div class="text">
                        <p>あらかじめ用意した次のようなテキストファイル（sample.txt）をPythonのプログラムから読み込む方法に ついて説明します。</p>
(中略)
* Closing connection 0
                </div></body></html>
```

アップロード方法が間違えている、何らかの理由で抽出に失敗したなどの場合、以下のようにエラーが返ります。

```
HTTP/1.0 400 BAD REQUEST
Content-Type: text/html; charset=utf-8
Content-Length: 12
Server: Werkzeug/0.12.1 Python/3.6.1
Date: Tue, 25 Apr 2017 08:27:30 GMT

no file part
```

## Installation

Dockerイメージをビルドします。

```
$ docker build -t u6kapps/extract-content .
```

Dockerコンテナを起動します。

```
$ docker run \
    -d \
    --name extract-content \
    -p 5000:5000 \
    u6kapps/extract-content
```

## Author

- [u6k/extract-content](https://github.com/u6k/extract-content)
- [extract-content - u6k.Redmine](https://redmine.u6k.me/projects/extract-content)
- [u6k.Blog](https://blog.u6k.me/)

## License

[MIT License](https://github.com/u6k/extract-content/blob/master/LICENSE)
