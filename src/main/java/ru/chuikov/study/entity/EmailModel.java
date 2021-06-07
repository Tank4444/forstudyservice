package ru.chuikov.study.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailModel {
    private String email;

}
