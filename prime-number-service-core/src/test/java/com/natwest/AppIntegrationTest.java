/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.natwest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.natwest.App;
import org.natwest.model.ErrorInfo;
import org.natwest.model.PrimeNumberResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class AppIntegrationTest {

    private static HttpHeaders headers;
    private static ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturn200WhenSendingRequestToManagementEndpoint() throws Exception {

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/actuator/health");

        HttpEntity<String> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                headersEntity,
                Map.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnPrimesWhenUsingDefaultAlgorithm() throws Exception {

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/primes/4");

        HttpEntity<String> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<PrimeNumberResult> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                headersEntity,
                PrimeNumberResult.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().getInitial()).isEqualTo(4);
    }

    @Test
    public void shouldReturnPrimesWhenUsingBruteForceAlgorithm() throws Exception {

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/primes/6")
                .queryParam("algorithm", "bruteForce");

        HttpEntity<String> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<PrimeNumberResult> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                headersEntity,
                PrimeNumberResult.class);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody().getInitial()).isEqualTo(6);
        then(response.getBody().getPrimes()).isEqualTo(Arrays.asList(1, 2, 3, 5));
    }

    @Test
    public void shouldReturnPrimesWhenUsingHeuristicAlgorithm() throws Exception {

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/primes/7")
                .queryParam("algorithm", "heuristic");

        HttpEntity<String> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<PrimeNumberResult> entity = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                headersEntity,
                PrimeNumberResult.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(entity.getBody().getPrimes()).isEqualTo(Arrays.asList(1, 2, 3, 5, 7));
        then(entity.getBody().getInitial()).isEqualTo(7);
    }

    @Test
    public void shouldReturnErrorWhenUsingUnknownAlgorithm() throws Exception {

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/primes/7")
                .queryParam("algorithm", "unknown");

        HttpEntity<String> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<ErrorInfo> entity = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                headersEntity,
                ErrorInfo.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(entity.getBody().getMessage()).isEqualTo("Prime number generator 'unknown' not found");
        then(entity.getBody().getUrl()).isNotNull();
    }

    @Test
    public void shouldReturnErrorWhenSendingNegativeNumber() throws Exception {

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/primes/-7");

        HttpEntity<String> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<ErrorInfo> entity = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                headersEntity,
                ErrorInfo.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(entity.getBody().getMessage()).isEqualTo("Limit \'-7\' must be greater than 0");
    }
}
