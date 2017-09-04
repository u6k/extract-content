FROM python
MAINTAINER u6k.apps@gmail.com

RUN pip install Flask readability-lxml requests

RUN mkdir -p /opt/extract-content/
WORKDIR /opt/extract-content/
COPY main.py .

EXPOSE 5000

CMD ["python", "/opt/extract-content/main.py"]
