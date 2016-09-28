package data

import (
	"log"

	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"

	"github.com/rpinheiroalmeida/kamu/models"
)

type BookRepository struct {
	C *mgo.Collection
}

func (r *BookRepository) Create(book *models.Book) error {
	obj_id	:= bson.NewObjectId()
	book.Id = obj_id
	err := r.C.Insert(&book)
	return err
}

func (r *BookRepository) GetAll() []models.Book {
	var books []models.Book
	iter := r.C.Find(nil).Iter()
	result := models.Book{}
	for iter.Next(&result) {
		books = append(books, result)
	}
	return books
}

func (r *BookRepository) GetByIsbn(isbn string)  (book models.Book, err error) {
	log.Printf("Ibn = %s", isbn)
	err = r.C.Find(bson.M{ "isbn" : isbn }).One(&book)

	return
}
