package utils

import (
	"reflect"
)

func Contains(slice interface{}, val interface{}) int {
	sv := reflect.ValueOf(slice)

	for i := 0; i < sv.Len(); i++ {
		if sv.Index(i).Interface() == val {
			return i
		}
	}
	return -1
}
