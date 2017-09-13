from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from flask import Flask, request, jsonify, redirect
from .extractor import *

app = Flask(__name__)

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
