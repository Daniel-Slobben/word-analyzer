package slobben.wordanalyzer.dto;

public record WordFrequencyDto(String word, int frequency) implements WordFrequency {
    @Override
    public String getWord() {
        return word;
    }

    @Override
    public int getFrequency() {
        return frequency;
    }
}
