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
        logger.debug("requests.get: start. url=%s", url)
        r = requests.get(url)
        logger.debug("requests.get: end. status_code=%s, content_type=%s", r.status_code, r.headers["content-type"])

        # Analyze html document

        ## Get full content
        logger.debug("get full_content: start.")
        self.full_content = r.text
        logger.debug("get full_content: end. len(full_content)=%s", len(self.full_content))

        ## Get extracted content
        logger.debug("extract content: start.")
        doc = Document(self.full_content)
        self.content = doc.summary()
        logger.debug("extract content: end. len(content)=%s", len(self.content))

        ## Get title
        logger.debug("get title: start.")
        self.title = doc.short_title()
        logger.debug("get title: end. title=%s", self.title)

        ## Get simplified content
        logger.debug("content simplify: start.")
        markdown_content = pypandoc.convert_text(self.content, "markdown_github", format="html", extra_args=["--normalize", "--no-wrap"])
        self.simplified_content = pypandoc.convert_text(markdown_content, "html", format="markdown_github")
        logger.debug("content simplify: end. len(simplified_content)=%s", len(self.simplified_content))

        # Get summary
        logger.debug("summarize: start.")
        auto_abstractor = AutoAbstractor()
        abstractable_doc = AbstractableTopNRank()
        abstractable_doc.set_top_n(3)
        summary_list = auto_abstractor.summarize(self.simplified_content, abstractable_doc)["summarize_result"]
        self.summary_list = [pypandoc.convert_text(summary.strip(), "plain", format="html").strip() for summary in summary_list]
        logger.debug("summarize: end. len(summary_list)=%s", len(self.summary_list))

    def toDictionary(self):
        logger.debug("to dictionary: start.")
        result = {
            "url": self.url,
            "title": self.title,
            "full-content": self.full_content,
            "content": self.content,
            "simplified-content": self.simplified_content,
            "summary-list": self.summary_list
        }
        logger.debug("to dictionary: end.")

        return result
