from pytest import fail, raises
from unittest import TestCase
from app.extractor import *

class TestHtmlContentExtractor(TestCase):
    def test_ok_japanese(self):
        extractor = HtmlContentExtractor("http://jp.techcrunch.com/2017/09/08/20170907google-publishes-its-documentation-style-guide-for-developers/")

        assert extractor.url == "http://jp.techcrunch.com/2017/09/08/20170907google-publishes-its-documentation-style-guide-for-developers/"
        assert len(extractor.title) > 0
        assert len(extractor.full_content) > 0
        assert len(extractor.content) > 0
        assert len(extractor.simplified_content) > 0
        assert len(extractor.summary_list) == 3

    def test_ok_english(self):
        extractor = HtmlContentExtractor("https://techcrunch.com/2017/08/10/microsoft-wants-to-make-blockchain-networks-enterprise-ready-with-its-new-coco-framework/")

        assert extractor.url == "https://techcrunch.com/2017/08/10/microsoft-wants-to-make-blockchain-networks-enterprise-ready-with-its-new-coco-framework/"
        assert len(extractor.title) > 0
        assert len(extractor.full_content) > 0
        assert len(extractor.content) > 0
        assert len(extractor.simplified_content) > 0
        assert len(extractor.summary_list) == 3

    def test_ok_with_file(self):
        with open("tests/data/ok.html") as f:
            full_content = f.read()

        extractor = HtmlContentExtractor("http://aaa.com", full_content)

        assert extractor.url == "http://aaa.com"
        assert len(extractor.title) > 0
        assert len(extractor.full_content) > 0
        assert len(extractor.content) > 0
        assert len(extractor.simplified_content) > 0
        assert len(extractor.summary_list) == 3

    def test_url_is_none(self):
        with raises(RuntimeError):
            HtmlContentExtractor(None)

    def test_not_found(self):
        with raises(ContentNotFoundException):
            HtmlContentExtractor("http://example.com/aaa/bbb/ccc.html")

    def test_not_html(self):
        with open("tests/data/not_html.json") as f:
            full_content = f.read()

        extractor = HtmlContentExtractor("http://example.com/", full_content)

        assert extractor.url == "http://example.com/"
        assert len(extractor.title) > 0
        assert len(extractor.full_content) > 0
        assert len(extractor.content) > 0
        assert len(extractor.simplified_content) > 0
        assert len(extractor.summary_list) == 3

    def test_zero_length(self):
        with raises(ContentNoDataException):
            HtmlContentExtractor("http://example.com/", "")

    def test_short_content(self):
        with open("tests/data/short_content.html") as f:
            full_content = f.read()

        extractor = HtmlContentExtractor("http://aaa.com", full_content)

        assert extractor.url == "http://aaa.com"
        assert len(extractor.title) == 0
        assert len(extractor.full_content) > 0
        assert len(extractor.content) > 0
        assert len(extractor.simplified_content) > 0
        assert len(extractor.summary_list) == 3

    def test_timeout(self):
        with raises(ContentRequestFailException):
            HtmlContentExtractor("http://jp.techcrunch.com/2017/09/08/20170907google-publishes-its-documentation-style-guide-for-developers/", timeout=0.001)
