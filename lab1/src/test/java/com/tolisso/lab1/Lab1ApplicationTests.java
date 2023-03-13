package com.tolisso.lab1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.Body;
import com.tolisso.lab1.actor.ActorLauncher;
import com.tolisso.lab1.dto.RequestDto;
import com.tolisso.lab1.exception.ActorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureWireMock(port = 13331)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class Lab1ApplicationTests {

    @Autowired
    ActorLauncher actorLauncher;

    private final List<RequestDto> response1 = List.of(
            new RequestDto("1", 15),
            new RequestDto("2", 14),
            new RequestDto("3", 13),
            new RequestDto("4", 12),
            new RequestDto("5", 30)
    );
    private final List<RequestDto> response2 = List.of(
            new RequestDto("1", 10),
            new RequestDto("2", 10),
            new RequestDto("3", 10),
            new RequestDto("4", 10),
            new RequestDto("6", 10)
    );
    private final List<RequestDto> response3 = List.of(
            new RequestDto("1", 10),
            new RequestDto("2", 10),
            new RequestDto("3", 10),
            new RequestDto("4", 10),
            new RequestDto("7", 10)
    );

    private void stub(String url, List<RequestDto> response, int timeout) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        stubFor(get(url).willReturn(aResponse()
                .withResponseBody(Body.fromOneOf(null, json, null, null))
                .withFixedDelay(timeout)
                .withHeader("Content-Type", "application/json")
        ));
    }

    @Test
    void goodResponse() {

        stub("/api1", response1, 0);
        stub("/api2", response2, 0);
        stub("/api3", response3, 0);
        List<RequestDto> result = actorLauncher.getTopRequests();

        assertEquals(15 + 10 + 10, result.get(0).getNumOfSearches());
        assertEquals(14 + 10 + 10, result.get(1).getNumOfSearches());
        assertEquals(13 + 10 + 10, result.get(2).getNumOfSearches());
        assertEquals(12 + 10 + 10, result.get(3).getNumOfSearches());
        assertEquals(10 + 10 + 10, result.get(4).getNumOfSearches());

        assertEquals("1", result.get(0).getRequest());
        assertEquals("2", result.get(1).getRequest());
        assertEquals("3", result.get(2).getRequest());
        assertEquals("4", result.get(3).getRequest());
        assertEquals("5", result.get(4).getRequest());
    }

    @Test
    void toLongTimeResponse() {
        stub("/api1", response1, 1000);
        stub("/api2", response2, 1000);
        stub("/api3", response3, 1000);

        assertThrows(ActorException.class, () -> actorLauncher.getTopRequests());
    }
}
