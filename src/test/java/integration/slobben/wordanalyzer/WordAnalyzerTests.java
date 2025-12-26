package integration.slobben.wordanalyzer;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import slobben.wordanalyzer.WordAnalyzerApplication;
import slobben.wordanalyzer.dto.InputRequest;
import slobben.wordanalyzer.dto.WordFrequency;
import slobben.wordanalyzer.dto.WordFrequencyDto;
import tools.jackson.core.JacksonException;
import utils.TestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WordAnalyzerApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SuppressWarnings("java:S3008")
class WordAnalyzerTests {
    private static String HIGHEST_FREQUENCY_URL;
    private static String FREQUENCY_FOR_WORD_URL;
    private static String FREQUENCY_LIST_URL;

    private final RestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @PostConstruct
    void init() {
        String url = "http://localhost:" + port + "/text-processing";
        HIGHEST_FREQUENCY_URL = url + "/highest-frequency";
        FREQUENCY_FOR_WORD_URL = url + "/frequency-for-word/{word}";
        FREQUENCY_LIST_URL = url +  "/highest-frequency-list/{size}";
    }

    public WordAnalyzerTests(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void getHighestFrequency() throws JacksonException {
        // prepare
        InputRequest payload = new InputRequest("test test_test_1");
        HttpEntity<InputRequest> request = new HttpEntity<>(payload);

        // execute
        ResponseEntity<Integer> response = restTemplate.exchange(HIGHEST_FREQUENCY_URL,
                HttpMethod.POST,
                request,
                Integer.class);

        // verify
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(3);
    }

    @Test
    void getFrequencyForWord() throws JacksonException {
        // prepare
        InputRequest payload = new InputRequest("test test_test_1");
        HttpEntity<InputRequest> request = new HttpEntity<>(payload);

        // execute
        ResponseEntity<Integer> response = restTemplate.exchange(FREQUENCY_FOR_WORD_URL,
                HttpMethod.POST,
                request,
                Integer.class,
                "test");

        // verify
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(3);
    }

    @Test
    void getFrequencyList() throws JacksonException {
        // prepare
        InputRequest payload = new InputRequest("test test_best_1)_abc");
        HttpEntity<InputRequest> request = new HttpEntity<>(payload);
        List<WordFrequencyDto> expectedList = List.of(new WordFrequencyDto("test", 2), new WordFrequencyDto("abc", 1));

        // execute
        ResponseEntity<List<WordFrequencyDto>> response = restTemplate.exchange(FREQUENCY_LIST_URL,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>(){},
                "2");

        // verify
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(expectedList);
    }

    @Test
    void tooLargeInput() throws JacksonException {
        // prepare
        var largeString = TestUtils.getStringOfLength(10_000_001);
        InputRequest payload = new InputRequest(largeString);
        HttpEntity<InputRequest> request = new HttpEntity<>(payload);
        var responseType = new ParameterizedTypeReference<List<WordFrequency>>() {};

        // execute and verify
        assertThatExceptionOfType(HttpClientErrorException.class).isThrownBy(() ->
                restTemplate.exchange(FREQUENCY_LIST_URL,
                        HttpMethod.POST,
                        request,
                        responseType,
                        "2")
        );
    }
}
