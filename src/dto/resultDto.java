package dto;

public class resultDto {

    private String word;
    private int count;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public resultDto(String word, int count) {
        this.word = word;
        this.count = count;
    }
}
