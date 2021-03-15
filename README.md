## Diplom

Проект автоматизации тестирования веб-приложения, взаимодействующего с СУБД и API Банка. 

Описание задания можно найти здесь - https://github.com/netology-code/qa-diploma

Описание тестовых сценариев, запланированных для автоматизации, можно посмотреть здесь - [Plan](https://github.com/NikolayT35/Diplom2/blob/main/Documents/Plan.md)

Перед запуском автотестов необходимо убедиться, что на компьютере, где они будут запускаться, установлены Docker Desktop, IntelliJ IDEA и Java 11.
Как запустить автотесты
1.	Открыть в IntelliJ IDEA терминал(Terminal) и запустить контейнеры командой docker-compose up
2.	После того как контейнеры запустятся, в соседней вкладке терминала запустить приложение командой:

**Вариант 1** (с подключением к MySQL)java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -Dspring.datasource.username=app -Dspring.datasource.password=pass -jar ./artifacts/aqa-shop.jar

**Вариант 2** (с подключением к PostgreSQL)java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/postgres -Dspring.datasource.username=postgres -Dspring.datasource.password=mysecretpassword -jar ./artifacts/aqa-shop.jar

3.	После того как приложение запустится, в следующей соседней вкладке терминала запустить тесты командой:

**Вариант 1** (с подключением к MySQL) gradlew test -Ddatabase.url=jdbc:mysql://localhost:3306/app -Ddatabase.name=app -Ddatabase.password=pass

**Вариант 2** (с подключением к PostgreSQL) gradlew test -Ddatabase.url=jdbc:postgresql://localhost:5432/postgres -Ddatabase.name=postgres -Ddatabase.password=mysecretpassword

Отчеты о проделанной работе можно посмотреть здесь - [Report](https://github.com/NikolayT35/Diplom2/blob/main/Documents/Report.md), [Summary](https://github.com/NikolayT35/Diplom2/blob/main/Documents/Summary.md)

