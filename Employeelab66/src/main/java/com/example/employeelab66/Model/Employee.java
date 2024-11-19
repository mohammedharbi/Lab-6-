package com.example.employeelab66.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {

    @NotNull(message = "cannot be null")
    @Min(value = 2,message = "Must be at least 2 digits")
    private int ID;

    @NotEmpty(message = "Cannot be null")
    @Size(min = 4,message = "must be higher than 3")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "only letters")
    private String name;

    @NotEmpty(message = "Cannot be null")
    @Email
    private String email;

    @NotEmpty(message = "Cannot be null")
    @Size(min = 10, max = 10, message = "Must be 10 digits")
    @Pattern(regexp = "^(05)([0-9]{8})")
    private String phone;

    @NotNull(message = "cannot be null")
    @Min(value = 26, message = "age must be higher than 25")
    @Positive(message = "number must be a positive")

    private int age;

    @NotEmpty(message = "Cannot be null")
    @Pattern(regexp = "^(supervisor|coordinator)$")
    private String position;

    @AssertFalse
    private boolean on_leave;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate hire_date;

    @NotNull(message = "cannot be null")
    @Positive
    private int annual_leave;

}
