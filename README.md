# extract-content

![Badge Status](https://ci-as-a-service)

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
TODO
```

アップロード方法が間違えている、何らかの理由で抽出に失敗したなどの場合、以下のようにエラーが返ります。

```
TODO
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
