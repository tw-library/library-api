package routers

import (
	"github.com/gorilla/mux"
	"github.com/rpinheiroalmeida/kamu/controllers"
)

func AddBookRoutes(router *mux.Router) *mux.Router {
	router.HandleFunc("/books", controllers.GetBooks).Methods("GET")
	router.HandleFunc("/books", controllers.CreateBook).Methods("POST")
	router.HandleFunc("/books/isbn/{id}", controllers.FindByIsbn).Methods("GET")
	return router
}
