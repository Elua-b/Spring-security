package com.eloi.example.v1.dto.requests;

import com.eloi.example.v1.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CreateUserDTO {
    private String email;
    private String username;
    private Gender gender;
    private String national_id;
    private String password;
}
