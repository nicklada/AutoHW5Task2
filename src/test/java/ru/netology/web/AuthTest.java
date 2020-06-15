package ru.netology.web;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

public class AuthTest {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    void shouldSubmitRequestIfValidUser() {
        RegistrationDto user = GenerateUsers.generateValidActiveUser();
        GenerateUsers.makeRegistration(user);
    }

    @Test
    void shouldNotSubmitRequestNoUser() {
        RegistrationDto user = new RegistrationDto();
        GenerateUsers.makeRegistration(user);
    }

    @Test
    void shouldNotSubmitRequestStatusIsBlocked() {
        RegistrationDto user = GenerateUsers.generateValidBlockedUser();
        GenerateUsers.makeRegistration(user);
    }

    @Test
    void shouldNotSubmitRequestLoginInvalid() {
        RegistrationDto user = GenerateUsers.generateUserInvalidLogin();
        GenerateUsers.makeRegistration(user);
    }

    @Test
    void shouldNotSubmitRequestPasswordInvalid() {
        RegistrationDto user = GenerateUsers.generateUserInvalidPassword();
        GenerateUsers.makeRegistration(user);
    }
}
