package slobben.wordanalyzer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import slobben.wordanalyzer.dto.InputRequest;
import slobben.wordanalyzer.dto.WordFrequency;
import slobben.wordanalyzer.service.WordFrequencyAnalyzer;

import java.util.List;

@RestController
@RequestMapping("/text-processing")
@RequiredArgsConstructor
public class WordCounterController {
    private final WordFrequencyAnalyzer wordFrequencyAnalyzer;

    @PostMapping("/highest-frequency")
    public ResponseEntity<Integer> getWordCount(@RequestBody InputRequest request) {
        return ResponseEntity.ok(wordFrequencyAnalyzer.calculateHighestFrequency(request.input()));
    }

    @PostMapping("/frequency-for-word/{word}")
    public ResponseEntity<Integer> getWordCount(@PathVariable("word") String word, @RequestBody InputRequest request) {
        return ResponseEntity.ok(wordFrequencyAnalyzer.calculateFrequencyForWord(request.input(), word));
    }

    @PostMapping("/highest-frequency-list/{size}")
    public ResponseEntity<List<WordFrequency>> getWordCount(@PathVariable("size") int size, @RequestBody InputRequest request) {
        return ResponseEntity.ok(wordFrequencyAnalyzer.calculateMostFrequentNWords(request.input(), size));
    }
}
