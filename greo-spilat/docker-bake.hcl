group "default" {
  targets = ["auth-service", "user-service","event-service","frontend-service","notification-service", "join-service", "comment-service"]
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

target "notification-service" {
  context    = "./notifications"
  dockerfile = "Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:notification-service"]
}

target "frontend-service" {
  context    = "../greo-spilat-web/"
  dockerfile = "Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:frontend-service"]
}
target "comment-service" {
  context    = "./comments"
  dockerfile = "Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:comment-service"]
}
target "join-service" {
  context    = "./join"
  dockerfile = "Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:join-service"]
}