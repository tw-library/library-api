package controllers

import (
	"github.com/kamu-api/models"
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

	LibrariesResource struct {
		Data []models.Library `json:"data"`
	}
	LibraryResource struct {
		Data models.Library `json:"data"`
	}
	ChampionsResource struct {
		Champions []string `json`
	}

	UserResource struct {
		Data []models.User `json:"data"`
	}
)
