package dao;

import connecter.DBConnection;
import dto.rawDataDto;

import java.sql.*;
import java.util.ArrayList;

public class rawDataDao {
    private Connection conn = null;
    private Statement state = null;

    // 배열로 기존 DB 내역 가져오기
    public ArrayList<rawDataDto> importRawData() {

        ArrayList<rawDataDto> rawDataList = new ArrayList<>(); // 받은 RawData를 전송할 List

        try {
            DBConnection dc = new DBConnection();
            conn = dc.getConnection("127.0.0.1","3306"); //db 내의 데이터를 저장
            state = conn.createStatement(); //sql 문을 실행하기 위해 conn 연결 정보를 state로 생성

            String sql;
            // rawData 필터링
            sql = "select * from rawData";
            ResultSet rs = state.executeQuery(sql); // sql 실행결과를 rs에 저장
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                rawDataList.add(new rawDataDto(id, title)); //전처리한 DB 결과를 List 저장
            }
            rs.close();
            state.close();
            conn.close();
            System.out.println("2. Succeeded to import rawData!");
        } catch (SQLException e) {
            System.out.println("2. Failed to import rawData!");
        } finally {
            try{
                if(state != null)
                    state.close();
                if(conn != null)
                    conn.close();
                System.out.println("3. Succeeded to disconnect distribute_database!");
            } catch(SQLException e){
                System.out.println("3. Failed to disconnect distribute_database!");
            }
        }
        System.out.println("----------------------------------------------");
        return rawDataList; //RawData List 반환
    }

}