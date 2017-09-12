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

    r = requests.get(url)
    logger.debug("status_code=%s", r.status_code)
    logger.debug("content_type=%s", r.headers["content-type"])

    full_content = r.text
    doc = Document(full_content)
    content = doc.summary()
    title = doc.short_title()
    markdown_content = pypandoc.convert_text(content, "markdown_github", format="html", extra_args=["--normalize", "--no-wrap"])
    simplified_content = pypandoc.convert_text(markdown_content, "html", format="markdown_github")

    auto_abstractor = AutoAbstractor()
    abstractable_doc = AbstractableTopNRank()
    abstractable_doc.set_top_n(3)
    summary_list = auto_abstractor.summarize(simplified_content, abstractable_doc)["summarize_result"]
    summary_list = [pypandoc.convert_text(summary.strip(), "plain", format="html").strip() for summary in summary_list]

    result = {
        "url": url,
        "title": title,
        "full-content": full_content,
        "content": content,
        "simplified-content": simplified_content,
        "summary-list": summary_list
    }

    resp = jsonify(result)
    resp.headers["X-Api-Version"] = app.config["VERSION"]
    return resp

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
