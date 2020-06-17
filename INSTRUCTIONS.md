# Создание клиентов банка в тестовом режиме
Запуск сервиса создания клиентов интернет - банка в тестовом режиме
## Начало работы
1. Установите необходимое ПО на компьютер.
1. Загрузите приложение [app-ibank.jar](https://github.com/netology-code/aqa-homeworks/blob/aqa4/patterns/app-ibank.jar)
1. Запустите приложение в тестовом режиме через терминал командой `java -jar app-ibank.jar -P:profile=test`

### Prerequisites
У Вас на ПК должены быть установлены:
* Браузер Google Chrome
* IntelliJ IDEA

Создайте новый проект в IntelliJ IDEA: язык - Java 11, система сборки - Gradle.
Подключите необходимые зависимости в buildgradle:
```
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.6.1'
    testImplementation 'com.codeborne:selenide:5.11.0'
    testCompile group: 'org.slf4j', name: 'slf4j-simple', version: '1.7.23'
    testImplementation'com.github.javafaker:javafaker:1.0.1'
    compileOnly 'org.projectlombok:lombok:1.18.12'
    annotationProcessor 'org.projectlombok:lombok:1.18.12'

    testCompileOnly 'org.projectlombok:lombok:1.18.12'
    testAnnotationProcessor 'org.projectlombok:lombok:1.18.12'
    implementation 'org.jetbrains:annotations:15.0'

    testImplementation 'io.rest-assured:rest-assured:4.3.0'
    testImplementation 'com.google.code.gson:gson:2.8.6'
}
```
В качестве тестового фреймфорка используйте JUnit, также подключите фреймфорк Selenide для поиска необходимых элементов:
```
test {
    useJUnitPlatform()
    systemProperty 'selenide.headless', System.getProperty('selenide.headless')
}
```

### Запуск системы в тестовом режиме
1. Создайте класс RegistrationDTO с полями login, password, status. Добавьте конструкторы.
```
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
 private String login;
 private String password;
 private String status;
}
```
2. Создайте класс для генерации пользователей (GenerateUsers): валидных по всем параметрам и невалидных по одному из параметров.
* Класс должен содержать информацию по отправке запроса:
```
private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
```
* А также метод, отправляющий пользователя на регистрацию:

```
 static void makeRegistration(RegistrationDto registrationDto) {
        given()
                .spec(requestSpec)
                .body(registrationDto)
                        .when()
                        .post("/api/system/users")
                        .then()
                        .statusCode(200);
    }
}
```
3. Создайте класс с тестовыми методами на разные сценарии:
* наличие пользователя
* статус пользователя "blocked"
* невалидный логин
* невалидный пароль

В тестовом методе вы должны получить пользователя посредством вызова метода генерации пользователя из класса GenerateUsers. 
В методы класса GenerateUsers зашита регистрация валидного пользователя и генерация пользователя с нужными параметрами. Пример:
 ```
 public static RegistrationDto generateUserInvalidLogin() {
        Faker faker = new Faker(new Locale("en"));
        String password = faker.internet().password();
        String status = "active";
        makeRegistration(new RegistrationDto("vasya",password,status));
        return new RegistrationDto("petya",password,status);
    }
```
Данные полученного пользователя вносятся в форму (элементы которой ищем с помощью Selenide), необходимо сравнить результат заполнения формы с ожидаемым поведением системы.
Пример теста: 
 ```
 @Test
    void shouldNotSubmitRequestLoginInvalid() {
        RegistrationDto user = GenerateUsers.generateUserInvalidLogin();
        open("http://localhost:9999");
        SelenideElement form = $("[action]");
        form.$(cssSelector("[data-test-id=login] input")).sendKeys(user.getLogin());
        form.$(cssSelector("[data-test-id=password] input")).sendKeys(user.getPassword());
        form.$(cssSelector("[data-test-id=action-login] ")).click();
        $(byText("Ошибка")).waitUntil(Condition.visible, 15000);
    }
```
Если тест падает - должна быть проанализирована ошибка и оформлен bugreport.

## Лицензия
Copyright [Альфа-Банк] 
Лицензия Альфа-Банка на разработку информационных систем:
https://alfabank.ru/f/3/about/licence_and_certificate/lic.pdf

