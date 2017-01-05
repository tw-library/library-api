package models

import (
	"gopkg.in/mgo.v2/bson"
)

type (
	Office struct {
		Copies int      `json:	"Copies"`
		Borrow []string `json:	"Borrow"`
	}

	Book struct {
		Id              bson.ObjectId `bson:	"_id, omitempty" json:"id"`
		Title           string        `json: 	"Title"`
		Author          string        `json:	"Author"`
		Subtitle        string        `json:	"Subtitle"`
		Description     string        `json: 	"Description"`
		Isbn            string        `json:	"Isbn"`
		Publisher       string        `json:	"Publisher"`
		PublicationDate string        `json:	"PublicationDate"`
		NumberOfPages   int           `json: 	"NumerOfPages"`
		ImageUrl        string        `json: 	"ImageUrl"`
		//Offices         []Office      `json:	"Offices"`
	}

	Library struct {
		Id        bson.ObjectId `bson: "_id, omitempty" json:"id"`
		Name      string        `json: "Name"`
		Code      string        `json: "Code"`
		Champions []string      `json: "Champions"`
	}

	User struct {
		Id    bson.ObjectId `bson: "_id, omitempty" json: "id"`
		Name  string        `json: "Name"`
		Email string        `json: "Email"`
	}
)
