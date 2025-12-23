package unit.slobben.wordanalyzer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import slobben.wordanalyzer.dto.WordFrequency;
import slobben.wordanalyzer.dto.WordFrequencyDto;
import slobben.wordanalyzer.service.WordCounterService;
import utils.TestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
class WordFrequencyTests {

    private final WordCounterService wordCounterService = new WordCounterService();

    @ParameterizedTest
    @CsvSource({"a b c abc abc abc abc abd, 4", "abc abc cba cba, 2", "a, 1", "a1a, 2", "1a1, 1"})
    void calculateHighestFrequencyForWords(String sequence, int expectedFrequency) {
        // execute
        int result = wordCounterService.calculateHighestFrequency(sequence);

        // verify
        assertThat(result).isEqualTo(expectedFrequency);
    }

    @ParameterizedTest
    @CsvSource({"a b c abc abc abc abc abd, abc, 4", "ab1ac2ab2, ab, 2", "12, abc, 0", "abc_abc_cba_cba, cba, 2"})
    void calculateFrequencyForWord(String sequence, String wordToCheck, int expectedFrequency) {
        // Execute
        int result = wordCounterService.calculateFrequencyForWord(sequence, wordToCheck);

        // Verify
        assertThat(result).isEqualTo(expectedFrequency);
    }

    @ParameterizedTest
    @CsvSource({"a b c abc abc abc abc abd, 5", "ab1ac2ab2, 2", "12, 0", "122123123123123123123123123, 0", "abc_abc_cba_cba, 2"})
    void wordDefinitions(String input, int amountOfUniqueWords) {
        // execute
        List<WordFrequency> result = wordCounterService.calculateMostFrequentNWords(input, 100);

        // verify
        assertThat(result).hasSize(amountOfUniqueWords);
    }

    @Test
    void calculateMostFrequentWords() {
        // prepare
        final String testString = "The sun shines over the lake";

        // execute
        var result = wordCounterService.calculateMostFrequentNWords(testString, 3);

        // verify
        List<WordFrequency> expectedList = List.of(new WordFrequencyDto("the", 2), new WordFrequencyDto("lake", 1), new WordFrequencyDto("over", 1));
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedList);
    }

    @Test
    void calculateLargeString() {
        // prepare
        var largeString = TestUtils.getStringOfLength(10_000_000);

        // execute
        var result = wordCounterService.calculateMostFrequentNWords(largeString, 10);
        result.forEach((wf -> log.info("Word: {} Frequency: {}", wf.getWord(), wf.getFrequency())));

        assertThat(result).isNotEmpty();
    }


}
