# NOTE: tags below still reference the old (inaccessible) Docker Hub repo.
# Registry step (see docs/ROADMAP.md): retag to the new Docker Hub account, e.g.
# <new-account>/greospilat-<svc>, and move to immutable git-SHA tags.
group "default" {
  targets = ["auth-service", "user-service","event-service","frontend-service","notification-service", "join-service", "comment-service"]
}

target "auth-service" {
  context    = "."
  dockerfile = "authentication/Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:auth-service"]
}

target "user-service" {
  context    = "."
  dockerfile = "users/Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:user-service"]
}

target "event-service" {
  context    = "."
  dockerfile = "events/Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:event-service"]
}

target "notification-service" {
  context    = "."
  dockerfile = "notifications/Dockerfile"
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
  context    = "."
  dockerfile = "comments/Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:comment-service"]
}
target "join-service" {
  context    = "."
  dockerfile = "join/Dockerfile"
  platforms  = ["linux/amd64", "linux/arm64"]
  tags       = ["lukaexmedicinec/prpo-ll:join-service"]
}
