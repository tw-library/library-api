package routers

import (
	"github.com/gorilla/mux"
)

func InitRoutes() *mux.Router {
	router := mux.NewRouter().StrictSlash(false)
	//routes for the Book entity
	router = AddBookRoutes(router)

	return router
}
