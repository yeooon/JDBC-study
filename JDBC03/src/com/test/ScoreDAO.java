package com.test;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.util.DBConn;

public class ScoreDAO
{
	//주요 속성 구성 → DB 연결 객체 
	//내부에서만 쓸 거라면 getter / setter 쓸 필요 없음
	private Connection conn;
		
	//실제 연결 GO!
	//public : 외부에서 가져다 쓸 수 있음
	//생성자 정의() 나중에 인스턴스 호출하면 자동 DBConn 연결
	public ScoreDAO() throws ClassNotFoundException, SQLException
	{
		conn = DBConn.getConnection();
	}
	
	// 메소드 정의 → 데이터를 입력하는 기능 → insert
	// add(매개변수)
	public int add(ScoreDTO dto) throws SQLException
	{
		// 반환할 결과값을 담아낼 변수(적용된 행의 갯수)
		int result = 0;
		
		// 작업 객체 생성(리소스 사용)
		Statement stmt = conn.createStatement();

		
		// 쿼리문 준비 
		String sql = String.format("INSERT INTO TBL_SCORE(SID, NAME, KOR, ENG, MAT) VALUES(SCORESEQ.NEXTVAL,'%s','%d','%d','%d')"
				, dto.getName(),dto.getKor(),dto.getEng(),dto.getMat());		
		
		// 작업 객체를 활용하여 쿼리문 실행(전달)	
		result = stmt.executeUpdate(sql);

		
		// 사용한 리소스 반납
		stmt.close();

		//최종 결과값 반환 = 적용된 행의 갯수(int)
		return result;		
	}	// end add()
	
	// 전체 인원수 확인 x
	// 총점이랑 평균을 확인하는 메소드? → 필요없네...
	// 메소드 정의 → 전체 인원수 확인 → select
	public int count() throws SQLException
	{
		// 결과값으로 반환하게 될 변수 선언 및 초기화
		int result = 0;
		
		// 작업 객체 생성
		Statement stmt = conn.createStatement();

		
		//쿼리문 준비
		String sql = String.format("SELECT COUNT(*) AS COUNT FROM TBL_SCORE");

		
		//생성된 작업 객체를 활용하여 쿼리문 실행 → select 쿼리문 → executeQuery() → ResultSet 반환 → 일반적 반복 처리
		ResultSet rs = stmt.executeQuery(sql);
		
		//ResultSet 처리 → 반복문 구성 → 결과값 수신
		//ResultSet을 쓸 땐 while 반복문이 나온다고 보면 된다!
		while (rs.next())
		{
			result = rs.getInt("COUNT");
			// COUNT 컬럼의 값을 정수형태로 받겠다.
		}
		
		//ResultSet와 Statement의 사용한 리소스 반납
		rs.close();
		stmt.close();
		
		//최종 결과값 반환
		return result;		
	}//end count
	
	
	// void는 반환할 값이 없는 유형 → 반환을 받지 않겠따
	// 메소드 정의 → 전체 리스트를 조회하는 기능 → select
	// MemberDTO들이 구성요소인 ArrayList를 반환
	public ArrayList<ScoreDTO> lists() throws SQLException
	{
		// 결과값으로 반환할 변수 선언 및 초기화
		ArrayList<ScoreDTO> result = new ArrayList<ScoreDTO>();
		
		// 작업 객체 구성
		Statement stmt = conn.createStatement();
		
		// 쿼리문 준비 → select 
		String sql = String.format("SELECT SID, NAME, KOR, ENG, MAT, "
				+ "KOR + ENG + MAT AS TOT, (KOR + ENG + MAT)/3.0 AS AVG FROM TBL_SCORE ODER BY TOT");
		
		// 생성된 작업 객체를 활용하여 쿼리문 실행 → select → executeQuery() → ResultSet 반환 → 일반적으로 반복 처리
		ResultSet rs = stmt.executeQuery(sql);
		
		// ResultSet 처리 → 반복문 구성 → MemberDTO 인스턴스 생성 → ArrayList에 적재
		while (rs.next())
		{
			ScoreDTO dto = new ScoreDTO();
			
			dto.setSid(rs.getInt("SID"));
			dto.setName(rs.getString("NAME"));
			dto.setKor(rs.getInt("KOR"));
			dto.setEng(rs.getInt("ENG"));
			dto.setMat(rs.getInt("MAT"));
			dto.setTot(rs.getInt("KOR"), rs.getInt("ENG"), rs.getInt("MAT"));
			dto.setAvg(rs.getInt("KOR"), rs.getInt("ENG"), rs.getInt("MAT"));
			
			result.add(dto);
		}
		// 리소스 반납
		rs.close();
		stmt.close();

		// 최종 결과값 반환	
		return result;
	}//end lists()	
	
	
}
