FROM golang

COPY . /go/src/github.com/kamu-api

RUN go get github.com/tools/godep

WORKDIR /go/src/github.com/kamu-api

RUN godep get

EXPOSE 8080
EXPOSE 27017

CMD go run main.go
