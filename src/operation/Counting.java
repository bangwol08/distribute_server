package operation;

import dto.resultDto;

import java.util.ArrayList;
import java.util.HashMap;

public class Counting {
    // Dao에서 받은 전처리 DB로 작업
    public ArrayList<resultDto> wordCounter(ArrayList<String> preprocessedDataList) {
        System.out.println("3. Counting words...");

        HashMap<String, Integer> resultHashMap = new HashMap<>(); // Counting 결과 담을 Map생성
        ArrayList<resultDto> resultDataList = new ArrayList<>(); // 결과값 DB에 전송하기 위한 List 생성

        try {
            for (String sentence : preprocessedDataList) {
                String[] split = sentence.split("\\s"); // 띄어쓰기 기준으로 단어 word 단위로 배열 생성

                for (int i = 0; i < split.length; i++) {
                    if (resultHashMap.get(split[i]) != null)
                        resultHashMap.replace(split[i], resultHashMap.get(split[i]) + 1); // 키(word) 기준 검사후 중복시 +1
                    else
                        resultHashMap.put(split[i], 1);
                }
            }

            // 결과값 DB에 전송하기 위한 List에 결과값 저장
            for (String key : resultHashMap.keySet()) {
                resultDataList.add(new resultDto(key, resultHashMap.get(key)));
            }
            System.out.println("4. Succeeded to Counting words!");
        } catch (NullPointerException e) {
            System.out.println("4. Failed to Counting words!");
        }
        // 결과값 DB에 전송하기 위한 List 반환
        return resultDataList;
    }
}