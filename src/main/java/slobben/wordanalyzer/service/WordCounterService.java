package slobben.wordanalyzer.service;

import org.springframework.stereotype.Service;
import slobben.wordanalyzer.dto.WordFrequency;
import slobben.wordanalyzer.dto.WordFrequencyDto;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WordCounterService implements WordFrequencyAnalyzer {

    @Override
    public int calculateHighestFrequency(String text) {
        return getWordFrequencies(text)
                .mapToInt(WordFrequency::getFrequency)
                .max()
                .orElse(0);
    }

    @Override
    public int calculateFrequencyForWord(String text, final String word) {
        return getWordFrequencies(text)
                .filter(wordFrequency -> wordFrequency.getWord().equals(word))
                .mapToInt(WordFrequency::getFrequency)
                .findFirst()
                .orElse(0);
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        return getWordFrequencies(text)
                .sorted(Comparator.comparing(WordFrequency::getFrequency).reversed()
                        .thenComparing(WordFrequency::getWord))
                .limit(n)
                .toList();
    }

    private static Stream<WordFrequency> getWordFrequencies(String text) {
        if (text == null || text.isEmpty()) {
            return Stream.empty();
        }
        return Stream.of(text.split("[^a-zA-Z]+"))
                .parallel()
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new WordFrequencyDto(entry.getKey(), entry.getValue().intValue()));
    }
}
