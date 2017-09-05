from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from flask import Flask, request, jsonify
from readability import Document
import requests, bs4

app = Flask(__name__)

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

    soup = bs4.BeautifulSoup(summary, "lxml")
    parse_element(soup.find("body"), 0)

    result = {
        "url": url,
        "title": title,
        "full-content": r.text,
        "content": summary,
        "simplified-content": soup.prettify()
    }

    return jsonify(result)

def parse_element(element, indent):
    if isinstance(element, bs4.element.Tag):
        logger.debug("  " * indent + "%s %s", type(element), element.name)
        for content in element.contents:
            parse_element(content, indent + 1)
    else:
        logger.debug("  " * indent + "%s %s", type(element), repr(element))

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
