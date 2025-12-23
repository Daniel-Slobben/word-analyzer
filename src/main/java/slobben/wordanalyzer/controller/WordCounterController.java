package slobben.wordanalyzer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slobben.wordanalyzer.dto.InputRequest;
import slobben.wordanalyzer.service.WordFrequencyAnalyzer;

@RestController
@RequestMapping("/text-processing")
@RequiredArgsConstructor
public class WordCounterController {
    private final WordFrequencyAnalyzer wordFrequencyAnalyzer;

    @PostMapping("/highest-frequency")
    public ResponseEntity<Integer> getWordCount(@RequestBody InputRequest request) {
        return ResponseEntity.ok(wordFrequencyAnalyzer.calculateHighestFrequency(request.input()));
    }
}
