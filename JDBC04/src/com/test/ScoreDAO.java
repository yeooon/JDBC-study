/*=================================
 	ScoreDTO.java
 	- 데이터베이스 액션 처리 전용 객체
===================================*/

package com.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.util.DBConn;

public class ScoreDAO
{
	//주요 속성 구성
	private Connection conn;
	
	// 데이터베이스 연결 담당 메소드
	public Connection connection() throws ClassNotFoundException, SQLException
	{
		conn = DBConn.getConnection();
		return conn;
		
	}
	
	
	// 데이터 입력 담당 메소드
	// public add(번호 이름 국어 수학 영어...) 
	public int add(ScoreDTO dto) throws SQLException 
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		String sql = String.format("INSERT INTO TBL_SCORE(SID, NAME, KOR, ENG, MAT) VALUES(SCORESEQ.NEXTVAL, '%s', %d, %d, %d)"
								, dto.getName(), dto.getKor(), dto.getEng(), dto.getMat());
		result = stmt.executeUpdate(sql);
		stmt.close();
		return result;				
	}
	
	// 전체 리스트 출력 담당 메소드(직원 정렬)
	public ArrayList<ScoreDTO> lists() throws SQLException
	{
		ArrayList<ScoreDTO> result = new ArrayList<ScoreDTO>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT SID, NAME, KOR, ENG,MAT" 
				+ ",(KOR+ENG+MAT) AS TOT" 
				+ ",(KOR+ENG+MAT)/3 AS AVG"
				+ ",RANK() OVER(ORDER BY (KOR+ENG+MAT) DESC) AS RANK " 
				+ "FROM TBL_SCORE " 
				+ "ORDER BY SID ASC";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
		{
			ScoreDTO dto = new ScoreDTO();
			
			dto.setSid(rs.getString("SID")); 
			dto.setName(rs.getString("NAME"));
			dto.setKor(rs.getInt("KOR"));
			dto.setEng(rs.getInt("ENG"));
			dto.setMat(rs.getInt("MAT"));
			dto.setTot(rs.getInt("TOT"));
			dto.setAvg(rs.getDouble("AVG"));
			dto.setRank(rs.getInt("RANK"));
			
			result.add(dto);
		}
		rs.close();
		stmt.close();
		
		return result;
		
	}
	
	// 이름 검색 담당 메소드
	// lists(String name) ← 이 부분에 매개변수를 담는 것이 다르다
	public ArrayList<ScoreDTO> lists(String name) throws SQLException
	{
		ArrayList<ScoreDTO> result = new ArrayList<ScoreDTO>();
		Statement stmt = conn.createStatement();
		String sql = String.format("SELECT * "
				+ "FROM"
				+ "(SELECT SID, NAME, KOR, ENG, MAT, (KOR+ENG+MAT) AS TOT"
				+ ", (KOR+ENG+MAT)/3 AS AVG, RANK() OVER(ORDER BY (KOR+ENG+MAT) DESC) AS RANK "
				+ "FROM TBL_SCORE)"
				+ "WHERE NAME = '%s'",name);
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next())
		{
			ScoreDTO dto = new ScoreDTO();
			
			dto.setSid(rs.getString("SID")); 
			dto.setName(rs.getString("NAME"));
			dto.setKor(rs.getInt("KOR"));
			dto.setEng(rs.getInt("ENG"));
			dto.setMat(rs.getInt("MAT"));
			dto.setTot(rs.getInt("TOT"));
			dto.setAvg(rs.getDouble("AVG"));
			dto.setRank(rs.getInt("RANK"));
			
			result.add(dto);			
		}
		rs.close();
		stmt.close();
		
		return result;
	}
	
	// 번호 검색 메소드
	public ArrayList<ScoreDTO> lists(int sid) throws SQLException
	{
		ArrayList<ScoreDTO> result = new ArrayList<ScoreDTO>();
		Statement stmt = conn.createStatement();
		String sql = String.format("SELECT *"
				+ "FROM "
				+ "(SELECT SID, NAME, KOR, ENG, MAT"
				+ ",(KOR+ENG+MAT) AS TOT, (KOR+ENG+MAT)/3 AS AVG"
				+ ",RANK() OVER(ORDER BY (KOR+ENG+MAT) DESC) AS RANK "
				+ "FROM TBL_SCORE)"
				+ "WHERE SID = %d", sid);
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next())
		{
			ScoreDTO dto = new ScoreDTO();
			
			dto.setSid(rs.getString("SID")); 
			dto.setName(rs.getString("NAME"));
			dto.setKor(rs.getInt("KOR"));
			dto.setEng(rs.getInt("ENG"));
			dto.setMat(rs.getInt("MAT"));
			dto.setTot(rs.getInt("TOT"));
			dto.setAvg(rs.getDouble("AVG"));
			dto.setRank(rs.getInt("RANK"));
			
			result.add(dto);			
		}
		rs.close();
		stmt.close();
		
		return result;
	}	
	
	//인원 수 확인 담당 메소드
	public int count() throws SQLException
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		String sql = "SELECT COUNT(*) AS COUNT FROM TBL_SCORE";
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next())					//if.(rs.next())
			result = rs.getInt("COUNT");	// result = rs.getInt(1);
		rs.close();
		stmt.close();
		
		return result;
	}
	
	// 데이터 수정 담당 메소드 → 매개변수의 유형 check~!!!
	// 일일히 정보를 입력해야 하는 건 ScoreDTO dto
	// 번호 검색은 ↑위에서 가져오고... 나머지 데이터는 ScoreDTO dto에서 데려옴.
	public int modify(ScoreDTO dto) throws SQLException
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		String sql = String.format("UPDATE TBL_SCORE SET NAME = '%s', KOR=%d, ENG=%d, MAT=%d WHERE SID = '%s'"
				, dto.getName(), dto.getKor(), dto.getEng(), dto.getMat(), dto.getSid());		
		result = stmt.executeUpdate(sql);
		stmt.close();
		
		return result;
		
	}
	
	// 데이터 삭제 담당 메소드
	// 일일히 정보를 입력 안해도 되는 건 그냥 int sid 하나로
	public int remove(int sid) throws SQLException
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		String sql = String.format("DELETE FROM TBL_SCORE WHERE SID = %d",sid);
		result = stmt.executeUpdate(sql);
		stmt.close();
		
		return result;
	}
	
	
	// 데이터베이스 연결 종료 메소드
	public void close() throws SQLException
	{
		DBConn.close();
	}
}
