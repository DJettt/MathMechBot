.PHONY: run
run:
	sudo docker compose build && sudo docker compose up

.PHONY: clear-db
clear-db:
	sudo docker volume rm mathmechbot_pgdata
