package com.bookbla.americano.domain.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.bookbla.americano.domain.test.controller.dto.request.TestCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TestControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 테스트를_조회할_수_있다() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TestCreateRequest("test_contents"))
                .when().post("/api/tests");

        // expected (when + then)
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tests?contents=test_contents")
                .then().log().all()
                .body("isSuccess", Matchers.equalTo(true))
                .body("code", Matchers.equalTo("2000"));
    }

    @Test
    void 테스트를_생성한다() {
        // expected
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TestCreateRequest("나는 내용이에요"))
                .when().post("/api/tests")
                .then().log().all()
                .body("isSuccess", Matchers.equalTo(true))
                .body("code", Matchers.equalTo("2000"))
                .body("result.id", Matchers.greaterThan(0));
    }

    @Test
    void 테스트_예외가_발생한다() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tests/error")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }
}
