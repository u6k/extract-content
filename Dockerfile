FROM python
MAINTAINER u6k.apps@gmail.com

RUN apt-get update && \
    apt-get install -y pandoc && \
    apt-get clean

RUN pip install Flask lxml readability-lxml requests beautifulsoup4 pypandoc

RUN mkdir -p /opt/extract-content/
WORKDIR /opt/extract-content/
COPY main.py .

EXPOSE 5000

CMD ["python", "/opt/extract-content/main.py"]
