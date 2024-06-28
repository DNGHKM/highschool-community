package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.SchoolDto;
import com.dnghkm.high_school_community.entity.School;
import com.dnghkm.high_school_community.repository.SchoolRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 사용자가 학교이름으로 검색했을 때, API를 통해서 학교 List 생성해서 보여주고,
 * 가입 신청 시 json 내에 포함된 학교행정코드를 DB에 조회하고,
 * 해당 학교가 있는 경우 저장하지 않고,
 * 해당 학교가 없는 경우 DB에 저장한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SchoolService {
    private final SchoolRepository schoolRepository;

    @Value("${open.nice.key}")
    private String apikey;

    public List<SchoolDto> getSchoolListFromApi(String schoolName) {
        List<SchoolDto> schools = new ArrayList<>();

        //nice api 에서 학교정보(json String) 가져오기
        String schoolData = getSchoolString(schoolName);

        //받아온 학교정보 json 파싱하기
        List<Map<String, Object>> parsedSchoolList = parseSchool(schoolData);
        for (Map<String, Object> parsedSchool : parsedSchoolList) {
            SchoolDto schoolDto = SchoolDto.builder().name(parsedSchool.get("name").toString())
                    .adminCode(parsedSchool.get("adminCode").toString())
                    .sidoCode(parsedSchool.get("sidoCode").toString())
                    .schoolType(parsedSchool.get("schoolType").toString())
                    .address(parsedSchool.get("address").toString())
                    .addressDetail(parsedSchool.get("addressDetail").toString())
                    .build();
            schools.add(schoolDto);
            log.info("schoolName = {}", schoolDto.getName());
        }
        saveSchools(schools);
        return schools;
    }

    private void saveSchools(List<SchoolDto> schools) {
        for (SchoolDto school : schools) {
            if(!schoolRepository.existsByAdminCode(school.getAdminCode())){
                schoolRepository.save(School.builder()
                        .adminCode(school.getAdminCode())
                        .sidoCode(school.getSidoCode())
                        .name(school.getName())
                        .schoolType("고등학교")
                        .address(school.getAddress())
                        .addressDetail(school.getAddressDetail()).build());
            }
        }
    }

    //api 호출해서 JSON 데이터를 String으로 받아옴
    private String getSchoolString(String schoolName) {
        String apiUrl;
        String encodedSchoolName = URLEncoder.encode(schoolName, StandardCharsets.UTF_8);
        String highSchool = URLEncoder.encode("고등학교", StandardCharsets.UTF_8);
        apiUrl = "https://open.neis.go.kr/hub/schoolInfo?Type=json&pIndex=1&pSize=100&&SCHUL_KND_SC_NM="+highSchool+"&SCHUL_NM=" + encodedSchoolName + "&KEY=" + apikey;
        try {
            URL url = new URI(apiUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader br;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            log.info("result = {}",response);
            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    //받아온 json String 파싱
    private List<Map<String, Object>> parseSchool(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<Map<String, Object>> resultMaps = new ArrayList<>();
        JsonNode rows = rootNode.path("schoolInfo").get(1).path("row");
        log.info("rows = {}", rows.toString());
        for (JsonNode row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", row.path("SCHUL_NM").asText());
            log.info("학교이름 = {}",row.path("SCHUL_NM").asText());
            map.put("adminCode", row.path("SD_SCHUL_CODE").asText());
            map.put("sidoCode", row.path("ATPT_OFCDC_SC_CODE").asText());
            map.put("schoolType", row.path("SCHUL_KND_SC_NM").asText());
            map.put("address", row.path("ORG_RDNMA").asText());
            map.put("addressDetail", row.path("ORG_RDNDA").asText());
            resultMaps.add(map);
        }
        return resultMaps;
    }
}
