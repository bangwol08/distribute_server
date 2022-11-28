package dao;


import connecter.DBConnection;
import dto.resultDto;

import java.sql.*;
import java.util.ArrayList;

public class resultDao {
    private Connection conn = null;

    public void insertResultList(ArrayList<resultDto> resultDataList) {
        DBConnection dc = new DBConnection();
        conn = dc.getConnection("192.168.127.134","3306"); //db 내의 데이터를 저장
        PreparedStatement pstmt = null;

        try {
            String sql;
            //최종 DB에 결과값이 있으면 현재 찾은 거 더하기
            sql = "insert into resultData (word, count) values (?, ?) ON DUPLICATE KEY UPDATE count=count + ?";
            pstmt = conn.prepareStatement(sql);
            for(resultDto result : resultDataList) {
                //PreparedStatement 객체의 참조값 얻어오기
                pstmt = conn.prepareStatement(sql);
                //? 에 필요한값 바인딩하기
                pstmt.setString(1, result.getWord());
                pstmt.setInt(2, result.getCount());
                pstmt.setInt(3, result.getCount());
                //sql 문 실행하기 (INSERT, UPDATE, DELETE)
                pstmt.executeUpdate();
            }
            pstmt.close();
            conn.close();
            System.out.println("2. Succeeded to export resultData!");
        } catch (SQLException e) {
            System.out.println("2. Failed to export resultData!");
        } finally {
            try{
                if(pstmt != null)
                    pstmt.close();
                if(conn != null)
                    conn.close();
                System.out.println("3. Succeeded to disconnect distribute_database!");
            } catch(SQLException e){
                System.out.println("3. Failed to disconnect distribute_database!");
            }
        }
        System.out.println("----------------------------------------------");
    }

}
