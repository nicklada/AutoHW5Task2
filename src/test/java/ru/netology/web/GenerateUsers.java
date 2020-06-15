package ru.netology.web;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import lombok.Data;


import static io.restassured.RestAssured.given;

@Data
//@AllArgsConstructor
public class GenerateUsers {
    private static RequestSpecification requestSpec = (RequestSpecification) new RequestSpecBuilder();

    public static RegistrationDto generateValidActiveUser() {
        Faker faker = new Faker();
        return new RegistrationDto(
                faker.name().firstName().toLowerCase(),
                faker.internet().password(8, 8),
                "active");
    }

    public static RegistrationDto generateValidBlockedUser() {
        Faker faker = new Faker();
        return new RegistrationDto(
                faker.name().firstName().toLowerCase(),
                faker.internet().password(8, 8),
                "blocked");
    }

    public static RegistrationDto generateUserInvalidLogin() {
        Faker faker = new Faker();
        return new RegistrationDto(
                faker.internet().emailAddress(),
                faker.internet().password(8, 8),
                "blocked");
    }

    public static RegistrationDto generateUserInvalidPassword() {
        Faker faker = new Faker();
        return new RegistrationDto(
                faker.name().firstName().toLowerCase(),
                faker.internet().emailAddress(),
                "blocked");
    }

    static void makeRegistration(RegistrationDto registrationDto) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                //.body(new RegistrationDto("vasya", "password", "active"))
                .body(registrationDto(registrationDto.getLogin(), registrationDto.getPassword(), registrationDto.getStatus()) // передаём в теле объект, который будет преобразован в JSON
                        .when() // "когда"
                        .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                        .then() // "тогда ожидаем"
                        .statusCode(200); // код 200 OK
    }

}
