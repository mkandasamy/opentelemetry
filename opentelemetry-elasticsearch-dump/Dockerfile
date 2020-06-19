FROM node:alpine
WORKDIR /etc/temp/
RUN apk --no-cache add curl
RUN npm install elasticdump -g
COPY data data
COPY dump-script.sh ./
CMD sh dump-script.sh
