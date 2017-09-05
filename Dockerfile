FROM python
MAINTAINER u6k.apps@gmail.com

RUN pip install Flask lxml readability-lxml requests beautifulsoup4

RUN mkdir -p /opt/extract-content/
WORKDIR /opt/extract-content/
COPY main.py .

EXPOSE 5000

CMD ["python", "/opt/extract-content/main.py"]
