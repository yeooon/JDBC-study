/*===================================
 	MemberDAO.java
 	- 데이터베이스 액션 처리 전용 클래스
=====================================*/

// ExecuteQuery → ResultSet
// ExecuteUpdate → Int

package com.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.util.DBConn;

public class MemberDAO
{
	//주요 속성 구성
	private Connection conn;
	
	//데이터베이스 연결
	public Connection connection() throws ClassNotFoundException, SQLException
	{
		conn = DBConn.getConnection();
		return conn;
	}
	
	//데이터베이스 연결 종료
	public void close() throws SQLException
	{
		DBConn.close();
	}
	
	//직원 데이터 입력
	public int add(MemberDTO dto) throws SQLException
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		String sql = String.format("INSERT INTO TBL_EMP"
				+ "(EMP_ID, EMP_NAME, SSN, IBSADATE, CITY_ID"
				+ ", TEL, BUSEO_ID, JIKWI_ID, BASICPAY, SUDANG)"
				+ " VALUES (EMPSEQ.NEXTVAL, '%s', '%s', TO_DATE('%s', 'YYYY-MM-DD')"
				+ ", (SELECT CITY_ID FROM TBL_CITY WHERE CITY_NAME='%s')"
				+ ", '%s', (SELECT BUSEO_ID FROM TBL_BUSEO WHERE BUSEO_NAME='%s')"
				+ ", (SELECT JIKWI_ID FROM TBL_JIKWI WHERE JIKWI_NAME='%s'), %d, %d)"
				, dto.getEmpName(),dto.getSsn(), dto.getIbsaDate()
				,dto.getCityName(),dto.getTel(),dto.getBuseoName(),dto.getJikwiName()
				,dto.getBasicPay(),dto.getSudang());
		
		result = stmt.executeUpdate(sql);
		stmt.close();
		
		return result;
	}
	
	// 전체 직원 수 조회 메소드
	public int memberCount() throws SQLException
	{
		// 반환할 결과 변수 선언 및 초기화
		int result = 0;
		
		// 작업 객체 생성
		Statement stmt = conn.createStatement();
		
		// 쿼리문 준비
		String sql = "SELECT COUNT(*) AS COUNT FROM TBL_EMP";
		
		// 쿼리문 실행 select → executeQuery → ResultSet 반환
		ResultSet rs = stmt.executeQuery(sql);
		
		// ResultSet 처리 → 반복문 구성(단일값의 경우 조건문도 가능) → 결과 수신
		while (rs.next())
		{
			result = rs.getInt("COUNT");
		}
		
		// 리소스 반납
		rs.close();
		stmt.close();
		
		// 최종 결과값 반환
		return result;
		
	}//end memberCount()
	
	
	// 검색 결과 직원 수 조회
	// 메소드 오버로딩... 매개변수의 유형을 다르게
	//--WHERE EMP_ID = 1001;			→key:EMP_ID		/ value:1001
	//--WHERE EMP_NAME = '이호석'		→key:EMP_NAME		/ value:이호석
	//--WHERE BUSEO_NAME = '개발부'		→key:BUSEO_NAME	/ value:개발부
	//--WHERE BUSEO_NAME = '대리'		→key:BUSEO_NAME	/ value:대리
	
	public int memberCount(String key, String value) throws SQLException
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		String sql = "";
		
		if (key.equals("EMP_ID"))
			sql = String.format("SELECT COUNT(*) AS COUNT FROM EMPVIEW WHERE %s = %s", key, value);
		else 
			sql = String.format("SELECT COUNT(*) AS COUNT FROM EMPVIEW WHERE %s = '%s'", key, value);			
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
		{
			result = rs.getInt("COUNT");
		}
		
		rs.close();
		stmt.close();
		
		return result;
	}//end memberCount()
	
	// 직원 데이터 전체 조회 (사번/이름/부서/직위/급여내림차순)
	public ArrayList<MemberDTO> lists(String key) throws SQLException
	{
		//반환할 결과값 선언 및 초기화
		ArrayList<MemberDTO> result = new ArrayList<MemberDTO>();
				
		Statement stmt = conn.createStatement();
		
		String sql = String.format("SELECT EMP_ID, EMP_NAME, SSN, IBSADATE"
					+ ", CITY_NAME, TEL, BUSEO_NAME, JIKWI_NAME"
					+ ", BASICPAY, SUDANG, PAY "
					+ "FROM EMPVIEW "
					+ "ORDER BY %s", key);
		
		//executeQuery → ResultSet으로 결과 받아오기
		//ResultSet 
		//- 결과값을 저장할 수 있다.
		//- 저장된 값을 한 행 단위로 불러올 수 있다.
		//- 한 행에서 값을 가져올 때는 타입을 지정해 불러올 수 있다.		
		
		// 쿼리문 실행
		ResultSet rs = stmt.executeQuery(sql);
				
		// getter : 호출(불러오기)
		// setter : 입력(저장하기)
		while (rs.next())
		{
			MemberDTO member = new MemberDTO();
			
			// 생성된 MemberDTO에 값 채워넣기 → 값이 채워진 dto
			member.setEmpId(rs.getInt("EMP_ID"));
			// EmpId를 저장할거다 → resultset 의 getInt 메소드를 사용해서
			member.setEmpName(rs.getString("EMP_NAME"));
			member.setSsn(rs.getString("SSN"));
			member.setIbsaDate(rs.getString("IBSADATE"));
			member.setCityName(rs.getString("CITY_NAME"));
			member.setTel(rs.getString("TEL"));
			member.setBuseoName(rs.getString("BUSEO_NAME"));
			member.setJikwiName(rs.getString("JIKWI_NAME"));
			member.setBasicPay(rs.getInt("BASICPAY"));
			member.setSudang(rs.getInt("SUDANG"));
			member.setPay(rs.getInt("PAY"));
			
			//ArrayList 에 요소로 추가
			result.add(member);
		}
		
		rs.close();
		stmt.close();
		
		return result;
		
	}// end lists()
	
	
	// 직원 데이터 검색 조회
	public ArrayList<MemberDTO> searchLists(String key, String value) throws SQLException
	{
		ArrayList<MemberDTO> result = new ArrayList<MemberDTO>();
		Statement stmt = conn.createStatement();
		String sql = "";
		
		if (key.equals("EMP_ID"))
		{
			sql = String.format("SELECT EMP_ID, EMP_NAME, SSN, IBSADATE"
					+ ", CITY_NAME, NVL(TEL,'번호없음') AS TEL"
					+ ", BUSEO_NAME, JIKWI_NAME, BASICPAY, SUDANG, PAY"
					+ " FROM EMPVIEW"
					+ " WHERE %s = %s", key, value);
		}
		else 
		{
			sql = String.format("SELECT EMP_ID, EMP_NAME, SSN, IBSADATE"
					+ ", CITY_NAME, NVL(TEL,'번호없음') AS TEL"
					+ ", BUSEO_NAME, JIKWI_NAME, BASICPAY, SUDANG, PAY"
					+ "  FROM EMPVIEW"
					+ " WHERE %s = '%s'", key, value);			
		}
		
		
		// 세트같은 느낌
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
		{
			MemberDTO member = new MemberDTO();
			
			// 생성된 MemberDTO에 값 채워넣기 → 값이 채워진 dto
			member.setEmpId(rs.getInt("EMP_ID"));
			member.setEmpName(rs.getString("EMP_NAME"));
			member.setSsn(rs.getString("SSN"));
			member.setIbsaDate(rs.getString("IBSADATE"));
			member.setCityName(rs.getString("CITY_NAME"));
			member.setTel(rs.getString("TEL"));
			member.setBuseoName(rs.getString("BUSEO_NAME"));
			member.setJikwiName(rs.getString("JIKWI_NAME"));
			member.setBasicPay(rs.getInt("BASICPAY"));
			member.setSudang(rs.getInt("SUDANG"));
			member.setPay(rs.getInt("PAY"));
			
			//ArrayList 에 요소로 추가
			result.add(member);			
			
		}
		
		rs.close();
		stmt.close();
		
		return result;
	}// end searchLists()
	
	
	// 지역 리스트 조회 → 그냥 조회는 매개변수가 없다
	public ArrayList<String> searchCity() throws SQLException
	{
		ArrayList<String> result = new ArrayList<String>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT CITY_NAME FROM TBL_CITY";
		
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
			result.add(rs.getString("CITY_NAME"));
		
		rs.close();
		stmt.close();
		
		return result;
	}
	
	// 부서 리스트 조회
	public ArrayList<String> searchBuseo() throws SQLException
	{
		ArrayList<String> result = new ArrayList<String>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT BUSEO_NAME FROM TBL_BUSEO";
		
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
			result.add(rs.getString("BUSEO_NAME"));
		
		rs.close();
		stmt.close();
		
		return result;
	}
	
	// 직위 리스트 조회
	public ArrayList<String> searchJikwi() throws SQLException
	{
		ArrayList<String> result = new ArrayList<String>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT JIKWI_NAME FROM TBL_JIKWI";
		
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
			result.add(rs.getString("JIKWI_NAME"));
		
		rs.close();
		stmt.close();
		
		return result;
	}
	
	// 직위에 따른 최소 기본급 검색 → 조건이 있으면 매개변수가 있다
	public int searchBasicPay(String jikwi) throws SQLException
	{
		int result = 0;
		
		Statement stmt = conn.createStatement();
		String sql = String.format("SELECT MIN_BASICPAY FROM TBL_JIKWI WHERE JIKWI_NAME = '%s'", jikwi);
		
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
			result = rs.getInt("MIN_BASICPAY");
			
		rs.close();
		stmt.close();
		
		return result;
	
	}
	
	// 직원 데이터 수정 → 조건을 입력해야하므로 매개변수가 있다.
	public int modify(MemberDTO dto) throws SQLException
	{
		int result = 0;
		
		Statement stmt = conn.createStatement();
		String sql = String.format("UPDATE TBL_EMP"
				+ " SET EMP_NAME = '%s', SSN = '%s', IBSADATE = TO_DATE('%s','YYYY-MM-DD')"
				+ ", CITY_ID = (SELECT CITY_ID FROM TBL_CITY WHERE CITY_NAME = '%s')"
				+ ", TEL = '%s'"
				+ ", BUSEO_ID = (SELECT BUSEO_ID FROM TBL_BUSEO WHERE BUSEO_NAME = '%s')"
				+ ", JIKWI_ID = (SELECT JIKWI_ID FROM TBL_JIKWI WHERE JIKWI_NAME = '%s')"
				+ ", BASICPAY = %d, SUDANG = %d "
				+ "WHERE EMP_ID = %d"
				, dto.getEmpName(), dto.getSsn(), dto.getIbsaDate()
				, dto.getCityName(), dto.getTel(), dto.getBuseoName()
				, dto.getJikwiName(), dto.getBasicPay(), dto.getSudang()
				, dto.getEmpId());
		
		result = stmt.executeUpdate(sql);
		
		stmt.close();
		return result;
		
	}
	
	// 직원 데이터 삭제
	public int remove(int empId) throws SQLException
	{
		int result = 0;
		
		Statement stmt = conn.createStatement();
		String sql = String.format("DELETE FROM TBL_EMP WHERE EMP_ID = %d", empId);
		result = stmt.executeUpdate(sql);
		
		stmt.close();
		
		return result;
	}//end remove()
	
}
