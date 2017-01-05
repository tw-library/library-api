package main

import (
	"net/http"
	"encoding/json"
	"github.com/auth0/go-jwt-middleware"
	"github.com/dgrijalva/jwt-go"

	"github.com/go-martini/martini"
	"github.com/kamu-api/controllers"
)

func main() {
	jwtMiddleware := jwtmiddleware.New(jwtmiddleware.Options{
		ValidationKeyGetter: func(token *jwt.Token) (interface{}, error) {
			return []byte("secret"), nil
		},
		Debug: true,
		SigningMethod: jwt.SigningMethodHS256,
	})

	m := martini.Classic()

	m.Get("/", jwtMiddleware.CheckJWT, PingHandler)

	m.Group("/kamu/v1", func(r martini.Router) {
		r.Get("/books", controllers.GetBooks)
		r.Post("/books", controllers.CreateBook)
		r.Get("/books/:isbn", controllers.FindByIsbn)

		r.Get("/libraries", controllers.GetLibraries)
		r.Post("/libraries", controllers.CreateLibrary)
		r.Get("/libraries/:code", controllers.FindByCode)
		r.Put("/libraries/:code", controllers.UpdateLibrary)
		r.Delete("/libraries/:code", controllers.DeleteLibrary)
		r.Put("/libraries/:code/champions", controllers.AddChampions)
		r.Delete("/libraries/:code/champions", controllers.DeleteChampions)

		r.Get("/users", controllers.GetUsers)
		r.Post("/users", controllers.CreateUser)
		r.Post("/users/:email", controllers.FindByEmail)
	})

	m.Run()
}

type Response struct {
	Text string `json:"text"`
}

func respondJson(text string, w http.ResponseWriter) {
	response := Response{text}

	jsonResponse, err := json.Marshal(response)
	if err != nil {
		w.Header().Set("Content-Type", "application/json")
		http.Error(w, err.Error(), http.StatusInternalServerError)

		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.Write(jsonResponse)
}

func PingHandler(w http.ResponseWriter, r *http.Request) {
	respondJson("All good. You don't need to be authenticated to call this", w)
}
