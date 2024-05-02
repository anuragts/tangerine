FROM java:8-jre

WORKDIR /app

COPY . .

RUN chmod +x tangerinekv-server.sh

RUN tangerinekv-server.sh

EXPOSE 1111
