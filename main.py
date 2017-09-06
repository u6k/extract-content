from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from flask import Flask, request, jsonify, redirect
from readability import Document
import requests, bs4, pypandoc, codecs

app = Flask(__name__)

@app.route("/")
def index():
    return redirect("/static/demo.html", code=302)

@app.route("/extract", methods=['GET'])
def extract_content():
    logger.info("#extract_content start.")
    url = request.args.get("url")
    logger.info("url=%s", url)

    r = requests.get(url)
    logger.debug("status_code=%s", r.status_code)
    logger.debug("content_type=%s", r.headers["content-type"])

    full_content = r.text
    doc = Document(full_content)
    content = doc.summary()
    title = doc.short_title()
    markdown_content = pypandoc.convert_text(content, "md", format="html", extra_args=["--normalize", "--no-wrap"])

    result = {
        "url": url,
        "title": title,
        "full-content": full_content,
        "content": content,
        "markdown-content": markdown_content
    }

    return jsonify(result)

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
