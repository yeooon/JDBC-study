/*=========================
	MemberDAO.java
===========================*/

// 데이터베이스에 액세스 하는 기능
// → DBConn 활용(전담 계층)

// 데이터를 입력하는 기능 → insert

// 인원 수 확인하는 기능
// 즉, 대상 테이블(TBL_MEMBER)의 레코드 카운팅 기능 → select

// 전체 리스트를 조회하는 기능
// 즉, 대상 테이블(TBL_MEMBER)의 데이터를 조회하는 기능 → select


package com.test;

import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.DBConn;

public class MemberDAO
{
	//주요 속성 구성 → DB 연결 객체 
	//내부에서만 쓸 거라면 getter / setter 쓸 필요 없음
	private Connection conn;
	
	//실제 연결 GO!
	//public : 외부에서 가져다 쓸 수 있음
	//생성자 정의() 나중에 인스턴스 호출하면 자동 DBConn 연결
	public MemberDAO() throws ClassNotFoundException, SQLException
	{
		conn = DBConn.getConnection();
	}
	
	// 메소드 정의 → 데이터를 입력하는 기능 → insert
	// add(매개변수)
	public int add(MemberDTO dto) throws SQLException
	{
		// 반환할 결과값을 담아낼 변수(적용된 행의 갯수)
		int result = 0;
		
		// 작업 객체 생성(리소스 사용)
		Statement stmt = conn.createStatement();
		
		// 쿼리문 준비 
		String sql = String.format("INSERT INTO TBL_MEMBER(SID, NAME, TEL)"
								+ " VALUES (MEMBERSEQ.NEXTVAL,'%s','%s')",dto.getName(),dto.getTel());
		
		// 작업 객체를 활용하여 쿼리문 실행(전달)	
		result = stmt.executeUpdate(sql);
		
		// 사용한 리소스 반납
		stmt.close();
		
		//최종 결과값 반환 = 적용된 행의 갯수(int)
		return result;
	}// end add()

	
	
	// 메소드 정의 → 전체 인원수 확인 → select
	public int count() throws SQLException
	{
		// 결과값으로 반환하게 될 변수 선언 및 초기화
		int result = 0;
		
		// 작업 객체 생성
		Statement stmt = conn.createStatement();
		
		//쿼리문 준비
		String sql = "SELECT COUNT(*) AS COUNT FROM TBL_MEMBER";
		
		//생성된 작업 객체를 활용하여 쿼리문 실행 → select 쿼리문 → executeQuery() → ResultSet 반환 → 일반적 반복 처리
		ResultSet rs = stmt.executeQuery(sql);
		
		//ResultSet 처리 → 반복문 구성 → 결과값 수신
		//ResultSet을 쓸 땐 while 반복문이 나온다고 보면 된다!
		while (rs.next())				//if(rs.next)
		{
			result = rs.getInt("COUNT");	//rs.getInt(1); //※ 컬럼 인덱스는 1 부터...
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
	public ArrayList<MemberDTO> list() throws SQLException
	{
		// 결과값으로 반환할 변수 선언 및 초기화
		ArrayList<MemberDTO> result = new ArrayList<MemberDTO>();
		
		// 작업 객체 구성
		Statement stmt = conn.createStatement();
		
		// 쿼리문 준비 → select 
		String sql = "SELECT SID, NAME, TEL FROM TBL_MEMBER ORDER BY SID";
		
		// 생성된 작업 객체를 활용하여 쿼리문 실행 → select → executeQuery() → ResultSet 반환 → 일반적으로 반복 처리
		ResultSet rs = stmt.executeQuery(sql);
		
		// ResultSet 처리 → 반복문 구성 → MemberDTO 인스턴스 생성 → ArrayList에 적재
		while (rs.next())
		{
			MemberDTO dto = new MemberDTO();
			
			dto.setSid(rs.getString("SID"));
			dto.setName(rs.getString("NAME"));
			dto.setTel(rs.getString("TEL"));
			
			result.add(dto);
		}
		// 리소스 반납
		rs.close();
		stmt.close();
		
		// 최종 결과값 반환
		return result;
	}//end lists()
	
	// 데이터베이스 연결 종료 메소드
	public void close() throws SQLException 
	{
		DBConn.close();
	}
	
}
