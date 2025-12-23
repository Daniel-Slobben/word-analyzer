package integration.slobben.wordanalyzer;

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
import org.springframework.web.client.RestTemplate;
import slobben.wordanalyzer.WordAnalyzerApplication;
import slobben.wordanalyzer.dto.InputRequest;
import slobben.wordanalyzer.dto.WordFrequencyDto;
import tools.jackson.core.JacksonException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WordAnalyzerApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)   // optional, lets us use constructor injection
class WordAnalyzerTests {

    private static final String BASE_URL = "/text-processing";
    private static final String HIGHEST_FREQUENCY = "/highest-frequency";
    private static final String FREQUENCY_FOR_WORD = "/frequency-for-word/{word}";
    private static final String FREQUENCY_LIST = "/highest-frequency-list/{size}";
    private final RestTemplate restTemplate;
    @LocalServerPort
    private int port;

    public WordAnalyzerTests(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void getHighestFrequency() throws JacksonException {
        // prepare
        InputRequest payload = new InputRequest("test test_test_1");
        HttpEntity<InputRequest> request = new HttpEntity<>(payload);

        // execute
        ResponseEntity<Integer> response = restTemplate.exchange("http://localhost:" + port + BASE_URL + HIGHEST_FREQUENCY, HttpMethod.POST, request, Integer.class);

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
        ResponseEntity<Integer> response = restTemplate.exchange("http://localhost:" + port + BASE_URL + FREQUENCY_FOR_WORD, HttpMethod.POST, request, Integer.class, "test");

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
        ResponseEntity<List<WordFrequencyDto>> response = restTemplate.exchange("http://localhost:" + port + BASE_URL + FREQUENCY_LIST, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
        }, "2");

        // verify
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(expectedList);
    }
}
