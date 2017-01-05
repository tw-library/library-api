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

func CreateUser(w http.ResponseWriter, r *http.Request) {
	var user models.User

	err := json.NewDecoder(r.Body).Decode(&user)
	if err != nil {
		log.Printf("Erro [%s]", err)
		return
	}

	context := NewContext()
	defer context.Close()
	c := context.DbCollection("users")
	repo := &data.UserRepository{c}

	repo.Create(&user)
	if j, err := json.Marshal(user); err != nil {
		common.DisplayAppError(
			w,
			err,
			"An unexpected error has occurred",
			500,
		)
		return
	} else {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusCreated)
		w.Write(j)
	}
}

func GetUsers(w http.ResponseWriter, r *http.Request) {
	context := NewContext()
	defer context.Close()
	c := context.DbCollection("users")
	repo := &data.UserRepository{c}
	users := repo.GetAll()
	j, err := json.Marshal(users)
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

func FindByEmail(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	email := params["email"]

	context := NewContext()
	defer context.Close()

	c := context.DbCollection("users")
	repo := &data.UserRepository{c}

	user, err := repo.GetByEmail(email)
	if err != nil {
		if err == mgo.ErrNotFound {
			w.WriteHeader(http.StatusNoContent)
		} else {
			common.DisplayAppError(
				w,
				err,
				"An unexpected error has occured",
				500,
			)
		}
		return
	}
	j, err := json.Marshal(user)
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
