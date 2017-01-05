package common

//StartUp bootstrap the application
func StartUp() {
	//Initialize AppConfig variable
	initConfig()

	createDBSession()

	//Initialize Logger objects with Log Level
	//setLogLevel(Level(AppConfig.LogLevel))
}
