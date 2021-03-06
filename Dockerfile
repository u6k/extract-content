FROM python
MAINTAINER u6k.apps@gmail.com

RUN apt-get update && \
    apt-get install -y --force-yes pandoc mecab libmecab-dev mecab-naist-jdic && \
    apt-get clean

RUN pip install Flask requests lxml readability-lxml pypandoc git+https://github.com/u6k/python3-auto-abstracts.git

COPY . /opt/extract-content/

EXPOSE 5000

CMD ["python", "/opt/extract-content/run.py"]
