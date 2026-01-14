package com.marcedev.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserImportResultDTO {
    private int totalRows;
    private int created;
    private int skipped;
    @Builder.Default
    private List<String> errors = new ArrayList<>();
    @Builder.Default
    private List<GeneratedPasswordDTO> generatedPasswords = new ArrayList<>();
}
