package main

import (
	"log"
	"net/http"
	
	"github.com/codegangsta/negroni"
	"github.com/rpinheiroalmeida/kamu/common"
	"github.com/rpinheiroalmeida/kamu/routers"
)

func main() {
	
	//Calls startup login
	common.StartUp()

	//Get the mux router object
	router := routers.InitRoutes()

	//Create negroni instance
	n := negroni.Classic()
	n.UseHandler(router)

	server := &http.Server {
		Addr:		common.AppConfig.Server,
		Handler:	n,
	}
	log.Println("Listening...")
	server.ListenAndServe()
} 
	
