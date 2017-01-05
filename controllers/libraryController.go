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
	"github.com/kamu-api/utils"
)

func CreateLibrary(w http.ResponseWriter, r *http.Request) {
	var library models.Library

	err := json.NewDecoder(r.Body).Decode(&library)

	if err != nil {
		log.Printf("Erro[%s]", err)
		return
	}

	context := NewContext()
	defer context.Close()

	c := context.DbCollection("libraries")
	repo := &data.LibraryRepository{c}

	repo.Create(&library)

	if j, err := json.Marshal(library); err != nil {
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

func GetLibraries(w http.ResponseWriter, r *http.Request) {
	context := NewContext()
	defer context.Close()
	c := context.DbCollection("libraries")
	repo := &data.LibraryRepository{c}

	libraries := repo.GetAll()
	j, err := json.Marshal(LibrariesResource{Data: libraries})
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

func FindByCode(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	code := params["code"]

	context := NewContext()
	defer context.Close()

	c := context.DbCollection("libraries")
	repo := &data.LibraryRepository{c}
	library, err := repo.GetByCode(code)
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
	j, err := json.Marshal(library)
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

func AddChampions(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	code := params["code"]

	var championsResource ChampionsResource

	log.Println(r.Body)
	err := json.NewDecoder(r.Body).Decode(&championsResource)
	if err != nil {
		common.DisplayAppError(
			w,
			err,
			"Invalid champions data",
			500,
		)
		return
	}

	context := NewContext()
	defer context.Close()

	c := context.DbCollection("libraries")
	repo := &data.LibraryRepository{c}
	library, err := repo.GetByCode(code)
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
	for _, champion := range championsResource.Champions {
		if utils.Contains(library.Champions, champion) == -1 {
			library.Champions = append(library.Champions, champion)
		}
	}

	if err := repo.Update(&library); err != nil {
		common.DisplayAppError(
			w,
			err,
			"An unexpected error has occured",
			500,
		)
		return
	}
	w.WriteHeader(http.StatusNoContent)
}

func UpdateLibrary(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	code := params["code"]
	var dataResource LibraryResource

	err := json.NewDecoder(r.Body).Decode(&dataResource)
	if err != nil {
		common.DisplayAppError(
			w,
			err,
			"Invalid library data",
			500,
		)
		return
	}
	library := &dataResource.Data
	library.Code = code

	context := NewContext()
	defer context.Close()

	collection := context.DbCollection("libraries")
	repo := &data.LibraryRepository{collection}

	log.Println(library)

	if err := repo.Update(library); err != nil {
		common.DisplayAppError(
			w,
			err,
			"An unexpected error has occurred",
			500,
		)
		return
	}
	w.WriteHeader(http.StatusNoContent)
}

func DeleteLibrary(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	code := params["code"]

	context := NewContext()
	defer context.Close()

	col := context.DbCollection("libraries")
	repo := &data.LibraryRepository{C: col}

	err := repo.Delete(code)
	if err != nil {
		common.DisplayAppError(
			w, err,
			"An unexpected error has occurred", 500,
		)
		return
	}
	w.WriteHeader(http.StatusNoContent)

}

func DeleteChampions(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	code := params["code"]

	var championsResource ChampionsResource

	err := json.NewDecoder(r.Body).Decode(&championsResource)
	if err != nil {
		common.DisplayAppError(
			w,
			err,
			"Invalid champions data",
			500,
		)
		return
	}
	context := NewContext()
	defer context.Close()

	c := context.DbCollection("libraries")
	repo := &data.LibraryRepository{c}
	library, err := repo.GetByCode(code)
	if err != nil {
		if err == mgo.ErrNotFound {
			w.WriteHeader(http.StatusNoContent)
		} else {
			common.DisplayAppError(
				w,
				err,
				"An unexpected error has occurred",
				500,
			)
			return
		}
	}
	for i, newChampion := range championsResource.Champions {
		log.Printf("Pos[%d] - NewChampion %s", i, newChampion)
		pos := utils.Contains(library.Champions, newChampion)
		if pos != -1 {
			library.Champions = append(library.Champions[:pos],
				library.Champions[pos+1:]...)
		}
	}
	if err := repo.Update(&library); err != nil {
		common.DisplayAppError(
			w,
			err,
			"An unexpected error has occured",
			500,
		)
		return
	}
	w.WriteHeader(http.StatusNoContent)
}
