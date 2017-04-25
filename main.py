from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from flask import Flask, request
from readability import Document
from tempfile import mkstemp
app = Flask(__name__)

@app.route("/", methods=['POST'])
def extract_content():
    logger.info("#extract_content start.")
    if 'file' not in request.files:
        raise BadRequestException("no file part")

    tmp_file = mkstemp()
    logger.debug("tmp_file=%s", tmp_file)
    request.files['file'].save(tmp_file[1])

    tmp_file_obj = open(tmp_file[1])
    data = tmp_file_obj.read()
    logger.debug("tmp_file_obj.size=%s", len(data))
    tmp_file_obj.close()

    doc = Document(data)
    summary = doc.summary()

    return summary

class BadRequestException(Exception):
    status_code = 400

    def __init__(self, message):
        Exception.__init__(self)
        self.message = message

@app.errorhandler(BadRequestException)
def handle_bad_request(e):
    return e.message, e.status_code

if __name__ == "__main__":
    app.run()
