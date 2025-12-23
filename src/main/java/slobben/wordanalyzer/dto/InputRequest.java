package slobben.wordanalyzer.dto;

import jakarta.validation.constraints.Size;

public record InputRequest(@Size(max = 10_000_000) String input) {
}
