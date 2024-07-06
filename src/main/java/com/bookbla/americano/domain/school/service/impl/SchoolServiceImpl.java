package com.bookbla.americano.domain.school.service.impl;

import com.bookbla.americano.domain.school.controller.dto.response.SchoolReadResponse;
import com.bookbla.americano.domain.school.repository.SchoolRepository;
import com.bookbla.americano.domain.school.repository.entity.School;
import com.bookbla.americano.domain.school.service.SchoolService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
    public SchoolReadResponse readSchool() {

        List<School> schools = schoolRepository.findAll();
        return SchoolReadResponse.from(schools);
    }
}
