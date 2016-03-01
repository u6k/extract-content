# Webページ本文抽出アプリ

## 概要

特定サイトのページをキレイに取得して、ローカルに保存しておきたい。しかし、いちいち手動でトリミングを行うのは非常に面倒臭い。そこで、本文部分を抽出するアプリを作成する。

## 機能

* 特定サイトにおける本文部分の開始・終了を登録する。
* URLを投げると、本文部分を返す。

## 備考

* 機能は全て、WebAPIとして提供する。それを操作するWebページ、スマホアプリなどは後付で作成するかも。
* とりあえず、リクエストはクエリ文字列、レスポンスはテキスト形式で返す。さっさと作りたいので。
* ある程度カタチになったら、JAX-RS(REST)で作り直す。
* 稼働環境はGoogleAppEngine(Java)。OpenShiftも考えたけど、他で稼働させるつもりは無い。
* 文書、タスク、ソースコードはGitHubで管理する。
  * あーでも、UML文書はどうしようかな…

## 備考

* [Google App Engine 上で JAX-RS](http://d.hatena.ne.jp/winplus/20100215/1266236038)

## ePubについて参考

* [webページは「dotEPUB」使ってePub保管する時代になる！？](http://ebookpro.jp/blog/epub/webdotepubepub.html)
  * dotEPUBはWebAPIも提供している。
* [電子書籍ファイルePubについて -ePubを自分で作成する-](http://naoki.sato.name/lab/archives/45)
* [日本語Epubブックサンプル](http://www.kobu.com/docs/epub/index.htm)
* [うわっ…あんたのEPUB、ダメすぎ…？ 検証とその対処](http://densho.hatenablog.com/entry/epubcheck)

## 著作権法について参考

* [SmartNewsがダメならなぜPocketもダメなのか](http://anond.hatelabo.jp/201212260100359)
* [著作物の私的使用の場合](http://cozylaw.com/copy/tyosakuken/sitekisiyou.htm)
* [著作物が自由に使える場合は？](http://www.cric.or.jp/qa/hajime/hajime7.html)

## 本文抽出について

* [CRFを使ったWeb本文抽出](http://www.slideshare.net/shuyo/crf-web)
* [Web本文抽出 using CRF](http://www.slideshare.net/shuyo/web-using-crf)
* [Webページの本文抽出](http://labs.cybozu.co.jp/blog/nakatani/2007/09/web_1.html)
