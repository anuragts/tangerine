FROM openjdk:11-jdk

WORKDIR /app

COPY . .

RUN chmod +x tangerinekv-server.sh

CMD ["./tangerinekv-server.sh"]

EXPOSE 1111