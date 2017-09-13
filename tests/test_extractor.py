from pytest import fail
from unittest import TestCase
from app.extractor import HtmlContentExtractor

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

    def test_not_found(self):
        fail() # FIXME

    def test_not_html(self):
        fail() # FIXME

    def test_zero_length(self):
        fail() # FIXME

    def test_short_content(self):
        fail() # FIXME

    def test_timeout(self):
        fail() # FIXME
