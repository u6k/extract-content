from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from readability import Document
import requests
import pypandoc
import codecs
from auto_abstracts.auto_abstractor import AutoAbstractor, AbstractableTopNRank

class HtmlContentExtractor():
    def __init__(self, url):
        logger.info("HtmlContentExtractor.__init__: url=%s", url)

        # Initialize instance variable
        self.url = url
        self.title = ""
        self.full_content = ""
        self.content = ""
        self.simplified_content = ""
        self.summary_list = ""

        # Get html document
        r = requests.get(url)
        logger.debug("status_code=%s", r.status_code)
        logger.debug("content_type=%s", r.headers["content-type"])

        # Analyze html document

        ## Get full content
        self.full_content = r.text

        ## Get extracted content
        doc = Document(self.full_content)
        self.content = doc.summary()

        ## Get title
        self.title = doc.short_title()

        ## Get simplified content
        markdown_content = pypandoc.convert_text(self.content, "markdown_github", format="html", extra_args=["--normalize", "--no-wrap"])
        self.simplified_content = pypandoc.convert_text(markdown_content, "html", format="markdown_github")

        # Get summary
        auto_abstractor = AutoAbstractor()
        abstractable_doc = AbstractableTopNRank()
        abstractable_doc.set_top_n(3)
        summary_list = auto_abstractor.summarize(self.simplified_content, abstractable_doc)["summarize_result"]
        self.summary_list = [pypandoc.convert_text(summary.strip(), "plain", format="html").strip() for summary in summary_list]

    def toDictionary(self):
        result = {
            "url": self.url,
            "title": self.title,
            "full-content": self.full_content,
            "content": self.content,
            "simplified-content": self.simplified_content,
            "summary-list": self.summary_list
        }

        return result
