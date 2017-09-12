from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from flask import Flask, request, jsonify, redirect
from readability import Document
import requests
import pypandoc
import codecs
from auto_abstracts.auto_abstractor import AutoAbstractor, AbstractableTopNRank

app = Flask(__name__)
app.config.from_object("settings")

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

@app.route("/")
def index():
    return redirect("/static/demo.html", code=302)

@app.route("/info", methods=['GET'])
def info():
    logger.info("#info start.")

    result = {
        "version": app.config["VERSION"]
    }

    resp = jsonify(result)
    resp.headers["X-Api-Version"] = app.config["VERSION"]
    return resp

@app.route("/extract", methods=['GET'])
def extract_content():
    logger.info("#extract_content start.")
    url = request.args.get("url")
    logger.info("url=%s", url)

    extractor = HtmlContentExtractor(url)

    resp = jsonify(extractor.toDictionary())
    resp.headers["X-Api-Version"] = app.config["VERSION"]
    return resp

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
