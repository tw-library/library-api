package controllers

import (
	"encoding/json"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"gopkg.in/mgo.v2"

	"github.com/kamu-api/common"
	"github.com/kamu-api/data"
	"github.com/kamu-api/models"
)

// var repo *data.BookRepository

func getBookRepository() *data.BookRepository {
	context := NewContext()
	// defer context.Close()
	c := context.DbCollection("books")
	repo := &data.BookRepository{c}
	return repo
}

func CreateBook(w http.ResponseWriter, r *http.Request) {
	//var dataResource BookResource
	var book models.Book

	err := json.NewDecoder(r.Body).Decode(&book)

	if err != nil {
		log.Printf("Erro [%s].", err)
		return
	}

	getBookRepository().Create(&book)
	//if j, err := json.Marshal(BookResource{Data: *book}); err != nil {
	if j, err := json.Marshal(book); err != nil {
		common.DisplayAppError(w, err, "An unexpected error has occurred", 500)
		return
	} else {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusCreated)
		w.Write(j)
	}
}

func GetBooks(w http.ResponseWriter, r *http.Request) {
	context := NewContext()
	defer context.Close()
	c := context.DbCollection("books")
	repo := &data.BookRepository{c}

	books := repo.GetAll()
	j, err := json.Marshal(BooksResource{Data: books})
	if err != nil {
		common.DisplayAppError(
			w,
			err,
			"An unexpected error has occured",
			500,
		)
		return
	}
	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "application/json")
	w.Write(j)
}

func FindByIsbn(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	isbn := vars["id"]

	book, err := getBookRepository().GetByIsbn(isbn)
	if err != nil {
		if err == mgo.ErrNotFound {
			w.WriteHeader(http.StatusNoContent)
		} else {
			common.DisplayAppError(w, err, "An expected error has occured", 500)
		}
		return
	}
	j, err := json.Marshal(book)
	if err != nil {
		common.DisplayAppError(w, err, "An unexpected error has occured", 500)
		return
	}
	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "application/json")
	w.Write(j)
}
