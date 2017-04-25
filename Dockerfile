FROM python
MAINTAINER u6k.apps@gmail.com

RUN pip install Flask readability-lxml

RUN mkdir -p /opt/extract-content/
WORKDIR /opt/extract-content/
COPY main.py .

CMD ["python", "/opt/extract-content/main.py"]
