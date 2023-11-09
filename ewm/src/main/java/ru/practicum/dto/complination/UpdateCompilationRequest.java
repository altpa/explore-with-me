package ru.practicum.dto.complination;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UpdateCompilationRequest {
    private final Set<Long> events;

    @NotNull
    private final boolean pinned;
    @Nullable
    @Size(min = 1, max = 50)
    private final String title;
}
