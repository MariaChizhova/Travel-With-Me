# Travel-With-Me

Приложение под Android, реализованное на языке программирования Java.

Социальная сеть для путешественников, в которой пользователи могут делиться своими маршрутами и опытом путешествия. 

Маршрут — путь на карте, сопровождаемый текстовым описанием мест, а также их фотографиями. 

## Сервер для работы с приложением [Travel-With-Me-Server](https://github.com/MariaChizhova/Travel-With-Me).

## Запуск

Для корректной работы с картами необходимо узнать ключи у разработчиков приложения либо же вставить свои вместо "":

 * В файл AndroidManifest.xml
```
<meta-data
        android:name="com.google.android.maps.v2.API_KEY"
        android:value="" />
```

 * В файл MapActivity.java
```
public final static String KEY = "";
```

## Используемые технологии
 * Регистрация пользователей и чат через Firebase
 * Сервер написан на Spring Framework
 * СУБД: MySQL
 * Хранение фотографий в AWS S3
 * Карта и маршруты с помощью Google Map API

## Демо видео



https://user-images.githubusercontent.com/57176713/122470636-f8d33e80-cfc6-11eb-8577-0990714009e7.mp4



Приложение пишется в рамках проектной работы в НИУ ВШЭ СПб.

Авторы: Чижова Мария, Шейн Андрей, Потрясаева Анна


