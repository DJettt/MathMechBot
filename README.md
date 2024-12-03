# Шаги по деплою

На хостинге [Timeweb](https://timeweb.cloud/) был снят сервер на Ubuntu 24.04. Хостинг выдал IP адрес
сервера и пароль root пользователя. Далее были созданы SSH ключи с помощью команды:

```dtd
ssh-keygen -t ed25519
```

На сервер через админ-панель был добавлен публичный ключ. После авторизовались через SSH:

```dtd
ssh root@{ip_адрес_сервера}
```

Далее был склонирован репозиторий с кодом:

```dtd
git clone https://github.com/DJettt/MathMechBot.git
```

После переносились переменные окружения. Внутри клона проекта запустил vim:

```dtd
vi .env
```

И вставил туда содержимое из подготовленного файла с переменными окружения.

После был запущен скрипт, настраивающий сервер (на текущий момент просто устанавливающий
Docker):

```dtd
./server_setup.sh
```

Далее производилась авторизация в Docker аккаунт для повышения лимита разрешённых pull'ов
из Docker Hub:

```dtd
docker login -u {имя_пользователя_на_dockerhub}
```

После чего были подняты Docker контейнеры:

```dtd
docker compose up
```

На этом всё: приложение поднято на удалённом сервере.