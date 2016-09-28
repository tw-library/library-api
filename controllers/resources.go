package controllers

import (
	"github.com/rpinheiroalmeida/kamu/models"
)

//Models for JSON resources
type (
	//For Post/Put - /books
	//For Get - /book/id
	BookResource struct {
		Book models.Book `json`
	}

	//For Get - /books
	BooksResource struct {
		Data []models.Book `json:"data"`
	}
)
