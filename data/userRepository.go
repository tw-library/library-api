package data

import (
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"

	"github.com/kamu-api/models"
)

type UserRepository struct {
	C *mgo.Collection
}

func (r *UserRepository) Create(user *models.User) error {
	obj_id := bson.NewObjectId()
	user.Id = obj_id
	err := r.C.Insert(&user)
	return err
}

func (r *UserRepository) GetAll() []models.User {
	var users []models.User
	iter := r.C.Find(nil).Iter()
	result := models.User{}
	for iter.Next(&result) {
		users = append(users, result)
	}
	return users
}

func (r *UserRepository) GetByEmail(email string) (user models.User, err error) {
	err = r.C.Find(bson.M{"email": email}).One(&user)
	return
}
