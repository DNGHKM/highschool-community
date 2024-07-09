package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.MealDto;
import com.dnghkm.high_school_community.entity.School;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 사용자가 급식 정보를 조회했을 때, 당월 급식 식단 정보를 API를 호출해 반환함
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MealService {
    private final UserRepository userRepository;

    @Value("${open.nice.key}")
    private String apikey;

    public List<MealDto> getMealListFromApi(String username) {
        School findSchool = findUser(username).getSchool();
        String adminCode = findSchool.getAdminCode();
        String sidoCode = findSchool.getSidoCode();
        List<MealDto> mealDtos = new ArrayList<>();

        //nice api 에서 급식정보(json String) 가져오기
        String mealData = getMealString(adminCode, sidoCode);

        //받아온 급식정보 json 파싱하기
        List<Map<String, Object>> parsedMealData = parseMeal(mealData);

        for (Map<String, Object> parsedMeal : parsedMealData) {
            String dateString = parsedMeal.get("mealDate").toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate date = LocalDate.parse(dateString, formatter);
            MealDto mealDto = MealDto.builder()
                    .mealDate(date)
                    .mealType(parsedMeal.get("type").toString())
                    .menu(parsedMeal.get("menu").toString())
                    .build();
            mealDtos.add(mealDto);
        }
        return mealDtos;
    }

    //api 호출해서 해당 월 급식정보 JSON 데이터를 String으로 받아옴
    private String getMealString(String schoolCode, String sidoCode) {
        String apiUrl;
        String startDate = YearMonth.now().atDay(1).toString().replace("-", "");
        String endDate = YearMonth.now().atEndOfMonth().toString().replace("-", "");
        apiUrl = "https://open.neis.go.kr/hub/mealServiceDietInfo?Type=json&ATPT_OFCDC_SC_CODE=" + sidoCode + "&SD_SCHUL_CODE=" + schoolCode + "&MLSV_FROM_YMD=" + startDate + "&MLSV_TO_YMD=" + endDate + "&KEY=" + apikey;
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
            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    //받아온 json String 파싱
    private List<Map<String, Object>> parseMeal(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<Map<String, Object>> resultMaps = new ArrayList<>();
        JsonNode rows = rootNode.path("mealServiceDietInfo").get(1).path("row");
        for (JsonNode row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("mealDate", row.path("MLSV_YMD").asText());
            map.put("type", row.path("MMEAL_SC_NM").asText());
            map.put("menu", row.path("DDISH_NM").asText());
            resultMaps.add(map);
        }
        return resultMaps;
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("유저를 찾을 수 없습니다. username = " + username)
        );
    }
}
