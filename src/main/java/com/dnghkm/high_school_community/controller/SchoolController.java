package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.dto.SchoolDto;
import com.dnghkm.high_school_community.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    @GetMapping("/{schoolName}")
    public ResponseEntity<List<SchoolDto>> getSchools(@PathVariable String schoolName) {
        List<SchoolDto> schoolListFromApi = schoolService.getSchoolListFromApi(schoolName);
        return ResponseEntity.ok(schoolListFromApi);
    }
}
