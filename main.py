from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from flask import Flask, request, jsonify
from readability import Document
import requests
from html.parser import HTMLParser

app = Flask(__name__)

class ExtractHTMLParser(HTMLParser):
    def handle_starttag(self, tag, attrs):
        logger.debug("starttag: %s", tag)
        for attr in attrs:
            logger.debug("        attr: %s", attr)

    def handle_endtag(self, tag):
        logger.debug("endtag  : %s", tag)

    def handle_data(self, data):
        logger.debug("data    : %s", data)

@app.route("/extract", methods=['GET'])
def extract_content():
    logger.info("#extract_content start.")
    url = request.args.get("url")
    logger.info("url=%s", url)

    r = requests.get(url)
    logger.debug("status_code=%s", r.status_code)
    logger.debug("content_type=%s", r.headers["content-type"])

    doc = Document(r.text)
    summary = doc.summary()
    title = doc.short_title()

    parser = ExtractHTMLParser()
    parser.feed(r.text)

    result = {
        "url": url,
        "full-content": r.text,
        "title": title,
        "content": summary
    }

    return jsonify(result)

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
