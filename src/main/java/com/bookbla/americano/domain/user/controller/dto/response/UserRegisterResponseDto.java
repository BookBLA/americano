package com.bookbla.americano.domain.user.controller.dto.response;

import com.bookbla.americano.domain.auth.controller.dto.response.AuthorityResponseDto;
import com.bookbla.americano.domain.user.repository.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponseDto {

    private String email;

    private String nickname;

    private String sex;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthday;

    private String profileImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;

    private Set<AuthorityResponseDto> authorityResponseDtoSet;

    public static UserRegisterResponseDto from(User user) {
        if (user == null) return null;

        return UserRegisterResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .sex(user.getSex())
                .birthday(user.getBirthday())
                .authorityResponseDtoSet(user.getUserAuthoritySet().stream()
                        .map(authority ->
                                AuthorityResponseDto.builder()
                                        .authorityName(authority != null ? authority.getAuthority().getAuthorityName() : null)
                                        .build())
                        .collect(Collectors.toSet()))
                .createAt(user.getCreatedAt())
                .build();
    }
}

