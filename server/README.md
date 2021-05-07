Создаем докер-сеть, через которую будет взаимодействовать база и сервер:
```
sudo docker network create travel-with-me
```

Запускаем контейнер, в котором будет работь база:
```
sudo docker container run --name travel-with-me-mysql \ 
                          --network travel-with-me \
                          -e MYSQL_ROOT_PASSWORD=forever18 \
                          -e MYSQL_DATABASE=travel_with_me \
                          -d mysql
```

Чуть-чуть подождите

Создадим образ сервера: 
```
sudo docker image build -t travel-with-me-jdbc .
```

Запускаем контейнер с сервером:
```
sudo docker container run --network travel-with-me 
                          --name travel-with-me-server 
                          -p 9090:9090 
                          -d travel-with-me-jdbc
```

Можно было воспользоваться `docker-compose`, но у меня не получилось =(
