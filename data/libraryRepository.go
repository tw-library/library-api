package data

import (
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"

	"github.com/kamu-api/models"
)

type LibraryRepository struct {
	C *mgo.Collection
}

func (r *LibraryRepository) Create(library *models.Library) error {
	obj_id := bson.NewObjectId()
	library.Id = obj_id
	err := r.C.Insert(&library)
	return err
}

func (r *LibraryRepository) GetAll() []models.Library {
	var libraries []models.Library
	iter := r.C.Find(nil).Iter()
	result := models.Library{}
	for iter.Next(&result) {
		libraries = append(libraries, result)
	}
	return libraries
}

func (r *LibraryRepository) GetByCode(code string) (library models.Library, err error) {
	err = r.C.Find(bson.M{"code": code}).One(&library)
	return
}

func (r *LibraryRepository) Update(library *models.Library) error {
	err := r.C.Update(bson.M{"code": library.Code},
		bson.M{"$set": bson.M{
			"Name":      library.Name,
			"Code":      library.Code,
			"champions": library.Champions,
		}})
	return err
}

func (r *LibraryRepository) Delete(code string) error {
	err := r.C.Remove(bson.M{"code": code})
	return err
}
