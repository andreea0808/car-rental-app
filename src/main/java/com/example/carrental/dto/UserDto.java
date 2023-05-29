package com.example.carrental.dto;

import com.example.carrental.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema( description = "UserDto information")
public class UserDto {

    @NotNull(message = "FIRST NAME must not be blank")
    private String firstname;

    @NotNull(message = "LAST NAME must not be blank")
    private String lastname;

    @NotNull(message = "EMAIL must not be blank")
    @Column(unique = true)
    private String email;

    @NotNull(message = "PASSWORD must not be blank")
    private String password;

    @Schema(description = "by default it is USER")
    private Role role;
}