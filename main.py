from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from flask import Flask, request, jsonify
from readability import Document
import requests
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
