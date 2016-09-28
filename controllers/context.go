package controllers

import (
	"gopkg.in/mgo.v2"

	"github.com/rpinheiroalmeida/kamu/common"
)

//Struct used for mainting HTTP Request Context

type Context struct {
	MongoSession *mgo.Session
}

//Close mgo.Session
func (c *Context) Close() {
	c.MongoSession.Close()
}

// Returns mgo.Collection for the given name
func (c *Context) DbCollection(name string) *mgo.Collection {
	return c.MongoSession.DB(common.AppConfig.Database).C(name)
}

// Create a new Context object for each HTTP Request
func NewContext() *Context {
	session := common.GetSession().Copy()
	context := &Context {
		MongoSession: session,
	}
	return context
}
