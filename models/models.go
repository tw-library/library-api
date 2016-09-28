package models

import (
	"gopkg.in/mgo.v2/bson"
)

type (
	Book struct {
		Id						bson.ObjectId `bson:	"_id, omitempty" json:"id"`
		Title					string			`json: 	"Title"`
		Author				string		`json:	"Author"`
		Subtitle			string		`json:	"Subtitle"`
		Description		string	`json: 	"Description"`
		Isbn					string			`json:"Isbn"`
		Publisher			string		`json:	"Publisher"`
		NumberOfPages	int		`json: 	"NumerOfPages"`
		ImageUrl			string		`json: 	"ImageUrl"`
	}
)
