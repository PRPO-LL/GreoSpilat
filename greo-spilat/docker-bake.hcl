group "default" {
  targets = ["auth-service", "user-service","event-service"]
}

target "auth-service" {
  context    = "./authentication"
  dockerfile = "Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:auth-service"]
}

target "user-service" {
  context    = "./users"
  dockerfile = "Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:user-service"]
}

target "event-service" {
  context    = "./events"
  dockerfile = "Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:event-service"]
}

