package com.udacity.timezones.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.udacity.timezones.service.TimeZoneService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import static org.junit.jupiter.api.Assertions.*;

import static com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.requestfilter.FieldTransformer;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestWrapper;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilter;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;





@ExtendWith(MockitoExtension.class)
class TimeZoneServiceTest {

    static WireMockServer wireMock = new WireMockServer(wireMockConfig().port(8089));

    private TimeZoneService timeZoneService;

    @BeforeAll
    static void setup() {
        wireMock.start();
    }
    @AfterAll
    static void cleanup() {
        wireMock.stop();
    }

    @BeforeEach
    void init() {
        wireMock.resetAll();
        timeZoneService = new TimeZoneService("http://localhost:8089");
    }

    @Test
    void getAvailableTimezoneText_timeApiReturnsStringList_returnsCountriesAsString() {
        wireMock.stubFor(
                get(urlEqualTo("/api/timezone/Europe"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("[\"Amsterdam\", \"Andorra\", \"Astrakhan\", \"Athens\"]")
                        )
        );

        String availableTimezoneText = timeZoneService.getAvailableTimezoneText("Europe");

        assertTrue(availableTimezoneText.contains("Available timezones in Europe are Amsterdam, Andorra"));
    }
}