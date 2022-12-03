package operation;

import dto.rawDataDto;
import kr.co.shineware.nlp.posta.en.core.EnPosta;

import java.util.ArrayList;
import java.util.List;

public class Filtering {

    // rawDataList 받아와서 필터링, String 배열로 반환
    public ArrayList<String> filter(ArrayList<rawDataDto> rawDataList) {
        System.out.println("1. Filtering rawData...");
        ArrayList<String> preprocessedDataList = new ArrayList<>();      // 전처리된 rawData 받을 List

        // 형태소 분석 라이브러리 사용 환경 구축
        EnPosta posta = new EnPosta();
        posta.load("lib/data_file");
        posta.buildFailLink();


        String read = "";  //rawData 담을 String 초기화
        String sentence = "";  // 반환 리스트에 담을 String 초기화

        try {
            for (rawDataDto rawData : rawDataList) {
                // 1차 전처리
                read = rawData.getTitle();
                read = read.replaceAll("\\[|]|\\<.*?\\>", " "); //HTML 태그 제거 '<content>' 다 제거
                read = read.replaceAll("\\.|-|\\)|\\(|[0-9]+", " "); // 일부 특수문자 제거
                read = read.toLowerCase();       // 처리된 거 전체 소문자로 변경

                // 최종 전처리 (형태소 라이브러리)
                List<String> enpostaList = posta.analyze(read); // 문장 단어 분석 결과  >>>> "~/NN", "~/VB" 같은 형식 (List에 하나씩 저장됨)
                for (String enpostaData : enpostaList) {
                    if (enpostaData.endsWith("/NN"))
                        sentence = word(sentence, enpostaData, "/NN"); // 단수명사
                    else if (enpostaData.endsWith("/NNS"))
                        sentence = word(sentence, enpostaData, "/NNS"); // 복수명사
                    else if (enpostaData.endsWith("/VB"))
                        sentence = word(sentence, enpostaData, "/VB"); // 일반동사
                    else if (enpostaData.endsWith("/VBD"))
                        sentence = word(sentence, enpostaData, "/VBD"); // 과거동사
                    else if (enpostaData.endsWith("/VBG"))
                        sentence = word(sentence, enpostaData, "/VBG"); // 진행형동사
                    else if (enpostaData.endsWith("/VBZ"))
                        sentence = word(sentence, enpostaData, "/VBZ"); // 복수형 동사
                    else if (enpostaData.endsWith("/VBN"))
                        sentence = word(sentence, enpostaData, "/VBN"); // 완료형 동사
                    else if (enpostaData.endsWith("/JJ"))
                        sentence = word(sentence, enpostaData, "/JJ"); // 부사? 형용사?
                }

                preprocessedDataList.add(sentence); // 최종 전처리된 문장 전달할 리스트에 추가
                sentence = ""; // 반환 리스트에 담을 String 초기화
            }
            System.out.println("2. Succeeded to Filtering rawData!");
        } catch (NullPointerException e) {
            System.out.println("2. Failed to Filtering rawData!");
        }

        return preprocessedDataList; // 결과값 DB에 전송하기 위한 List 반환
    }

    // 패턴 필터링 및 전달할 문장 생성
    String word (String sentence, String rawWord, String rule) {
        if (!exceptedWord(rawWord, rule))
            return sentence;
        else if (sentence.equals(""))
            sentence = rawWord.replace(rule, "");
        else
            sentence = sentence + " " + rawWord.replace(rule, "");
        return sentence;
    }

    // 예외 처리 단어 판단 (추후 예외 단어 여기)
    Boolean exceptedWord(String word, String rule) {
        String[] exceptedWords = {"was", "wasn", "were", "can", "cannot", "couldn", "don", "does", "doesn", "new"};
        if (word.replace(rule, "").length() < 3)
            return false;
        for (String exceptedWord : exceptedWords)
            if (word.replace(rule, "").equals(exceptedWord))
                return false;
        return true;
    }

}
