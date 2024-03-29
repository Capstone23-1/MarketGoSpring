package com.example.marketgospring.controller;


import com.example.marketgospring.entity.Json;
import com.example.marketgospring.repository.JsonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value="/json")
public class JsonController {
    private JsonRepository jsonRepository;

    @Autowired
    public JsonController(JsonRepository jsonRepository) { this.jsonRepository=jsonRepository;}
    @GetMapping(value = "/{fileId}")
    public Map getFile(@PathVariable("fileId") Integer fileId) throws IOException {

        String fileURL= jsonRepository.getFilePath(fileId);
        System.out.println(fileURL);
        String apiURL = "https://klb5jq7eu5.apigw.ntruss.com/custom/v1/23264/e6e687523e843edf5ef01fb5dcfaf41dc6a69d93edbb78fdfd4de68056a9d8c2/general";
        String secretKey = "c2hFamhkYWtZTWp1ekFFdVBvaWxPdkZGZ2ZxbWpUamg=";
        String imageFile = fileURL;
        StringBuffer response = new StringBuffer();
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod("POST");
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V1");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            long start = System.currentTimeMillis();
            File file = new File(imageFile);
            writeMultiPart(wr, postParams, file, boundary);
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e);
        }

        return getJson(String.valueOf(response));
    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws
            IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }
    //@GetMapping(value = "/{json}")
    public Map getJson(/*@PathVariable("json") */String jsonData) {

        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";
        String accessKey = "94a79136-75ad-4b96-bb41-10d6a8702ac6";   // 발급받은 API Key
        String analysisCode = "ner";        // 언어 분석 코드
        String text = jsonData;           // 분석할 텍스트 데이터
        Gson gson = new Gson();
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
        Map<Integer, String> resultMap = new HashMap<>();
        argument.put("analysis_code", analysisCode);
        argument.put("text", text);

        request.put("argument", argument);

        URL url;
        Integer responseCode;
        String responseBodyJson;
        Map<String, Object> responseBody;

        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", accessKey);


            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            responseBodyJson = sb.toString();

            // http 요청 오류 시 처리
            if ( responseCode != 200 ) {
                // 오류 내용 출력
                System.out.println("[error] " + responseBodyJson);
                return null;
            }

            responseBody = gson.fromJson(responseBodyJson, Map.class);
            Integer result = ((Double) responseBody.get("result")).intValue();
            Map<String, Object> returnObject;
            List<Map> sentences;

            // 분석 요청 오류 시 처리
            if ( result != 0 ) {

                // 오류 내용 출력
                System.out.println("[error] " + responseBody.get("result"));
                return null;
            }
            returnObject = (Map<String, Object>) responseBody.get("return_object");
            sentences = (List<Map>) returnObject.get("sentence");




            for( Map<String, Object> sentence : sentences ) {

                // 개체명 분석 결과 수집 및 정렬
                List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                for( Map<String, Object> nameEntityInfo : nameEntityRecognitionResult ) {
                    String name = (String) nameEntityInfo.get("text");
                    String type = (String) nameEntityInfo.get("type");
                    if (type.contains("PT")==true) {
                        resultMap.put(0, name);
                    }
                    if (type.equals("QT_COUNT")==true || type.equals("QT_WEIGHT")==true) {
                        resultMap.put(1, name);
                    }
                    if (type.equals("QT_PRICE")==true) {
                        resultMap.put(2, name);
                    }
                    if (type.equals("QT_OTHERS")) {
                        try {
                            int x=Integer.parseInt(name);
                            if (x>100) {
                                resultMap.put(1, name);
                            }
                            else {
                                resultMap.put(2, name);
                            }
                        } catch (NumberFormatException e) {
                            resultMap.put(2, name);
                            //throw new RuntimeException(e);
                        }
                    }
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    @GetMapping(value = "/image/{image}")
    public Optional<Json> getText(@PathVariable("image") Integer image) {
        return jsonRepository.findById(image);
    }

    @GetMapping(value = "/text/{text}")
    public Map getResult(@PathVariable("text") String text) {
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";
        String accessKey = "94a79136-75ad-4b96-bb41-10d6a8702ac6";   // 발급받은 API Key
        String analysisCode = "ner";        // 언어 분석 코드         // 분석할 텍스트 데이터
        Gson gson = new Gson();
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
        Map<Integer, String> resultMap = new HashMap<>();
        argument.put("analysis_code", analysisCode);
        argument.put("text", text);

        request.put("argument", argument);

        URL url;
        Integer responseCode;
        String responseBodyJson;
        Map<String, Object> responseBody;

        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", accessKey);


            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            responseBodyJson = sb.toString();

            // http 요청 오류 시 처리
            if ( responseCode != 200 ) {
                // 오류 내용 출력
                System.out.println("[error] " + responseBodyJson);
                return null;
            }

            responseBody = gson.fromJson(responseBodyJson, Map.class);
            Integer result = ((Double) responseBody.get("result")).intValue();
            Map<String, Object> returnObject;
            List<Map> sentences;

            // 분석 요청 오류 시 처리
            if ( result != 0 ) {

                // 오류 내용 출력
                System.out.println("[error] " + responseBody.get("result"));
                return null;
            }
            returnObject = (Map<String, Object>) responseBody.get("return_object");
            sentences = (List<Map>) returnObject.get("sentence");




            for( Map<String, Object> sentence : sentences ) {

                // 개체명 분석 결과 수집 및 정렬
                List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                for( Map<String, Object> nameEntityInfo : nameEntityRecognitionResult ) {
                    String name = (String) nameEntityInfo.get("text");
                    String type = (String) nameEntityInfo.get("type");
                    if (type.contains("PT")==true) {
                        resultMap.put(0, name);
                    }
                    if (type.equals("QT_COUNT")==true || type.equals("QT_WEIGHT")==true) {
                        resultMap.put(1, name);
                    }
                    if (type.equals("QT_PRICE")==true) {
                        resultMap.put(2, name);
                    }
                    if (type.equals("QT_OTHERS")) {
                        try {
                            int x=Integer.parseInt(name);
                            if (x>100) {
                                resultMap.put(1, name);
                            }
                            else {
                                resultMap.put(2, name);
                            }
                        } catch (NumberFormatException e) {
                            resultMap.put(2, name);
                            //throw new RuntimeException(e);
                        }
                    }
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
