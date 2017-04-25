from logging import getLogger, StreamHandler, DEBUG
logger = getLogger(__name__)
handler = StreamHandler()
handler.setLevel(DEBUG)
logger.setLevel(DEBUG)
logger.addHandler(handler)

from flask import Flask, request
app = Flask(__name__)

@app.route("/", methods=['POST'])
def extract_content():
    logger.info("#extract_content start.")
    if 'file' not in request.files:
        raise BadRequestException("no file part")

    file = request.files['file']
    logger.info("file=%s", file)
    return "ok"

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
