package com.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import com.util.DBConn;

public class MemberDAO
{
	// 주요 속성 구성
	private Connection conn;
	
	// 데이터 연결 메소드
	public Connection connection() throws ClassNotFoundException, SQLException
	{
		conn = DBConn.getConnection();
		return conn;
	}
	
	// 데이터 입력 메소드
	public int add(MemberDTO dto) throws SQLException
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		String sql = String.format("INSERT INTO TBL_EMP(EMP_ID, EMP_NAME, SSN, IBSADATE, CITY_ID, TEL"
				+ ", BUSEO_ID, JIKWI_ID, BASICPAY, SUDANG)"
				+ "VALUES (EMPSEQ.NEXTVAL, '%s', '%s', '%s'"
				+ ", (SELECT CITY_ID FROM TBL_CITY WHERE CITY_NAME='%s')"
				+ ",'%s',(SELECT BUSEO_ID FROM TBL_BUSEO WHERE BUSEO_NAME = '%s')"
				+ ",(SELECT JIKWI_ID FROM TBL_JIKWI WHERE JIKWI_NAME = '%s'),%d,%d)"
				,dto.getEMP_NAME(),dto.getSSN(),dto.getIBSADATE()
				,dto.getCITY_NAME(),dto.getTEL(),dto.getBUSEO_NAME(),dto.getJIKWI_NAME()
				,dto.getBASICPAY(),dto.getSUDANG());
		result = stmt.executeUpdate(sql);
		stmt.close();
		return result;
	}
	
	
	// 데이터 출력 메소드
	public ArrayList<MemberDTO> arrayList(String key) throws SQLException
	{
		// 반환할 결과값 result 선언 및 초기화
		ArrayList<MemberDTO> result = new ArrayList<MemberDTO>();
		
		// 작업 객체 생성
		Statement stmt = conn.createStatement();
		
		// 쿼리문 준비
		// 뷰 만들어야함 직위 이름이 없음
		String sql = String.format("SELECT EMP_ID, EMP_NAME, SSN"
				+ ", IBSADATE, CITY_NAME, TEL, BUSEO_NAME, JIKWI_NAME"
				+ ", BASICPAY, SUDANG"
				+ " FROM EMPVIEW"
				+ " ORDER BY %s", key);	
		
		// 쿼리문 실행
		ResultSet rs = stmt.executeQuery(sql);

		// ResultSet 처리 → while 반복문 구성
		while (rs.next())
		{
			MemberDTO dto = new MemberDTO();
			dto.setEMP_ID(rs.getInt("EMP_ID"));
			dto.setEMP_NAME(rs.getString("EMP_NAME"));
			dto.setSSN(rs.getString("SSN"));
			dto.setIBSADATE(rs.getString("IBSADATE"));
			dto.setCITY_NAME(rs.getString("CITY_NAME"));
			dto.setTEL(rs.getString("TEL"));
			dto.setBUSEO_NAME(rs.getString("BUSEO_NAME"));
			dto.setJIKWI_NAME(rs.getString("JIKWI_NAME"));
			dto.setBASICPAY(rs.getInt("BASICPAY"));
			dto.setSUDANG(rs.getInt("SUDANG"));
			
			result.add(dto);			
		}
		
		rs.close();
		stmt.close();
		
		return result;				
	}
	
	// 직원 검색(사번검색, 이름, 부서, 직위)
	public ArrayList<MemberDTO> lists(String key, String value) throws SQLException
	{
		// 반환할 결과값 result 선언 및 초기화
		ArrayList<MemberDTO> result = new ArrayList<MemberDTO>();
		
		// 작업 객체 생성
		Statement stmt = conn.createStatement();
		
		// 쿼리문 준비
		String sql = String.format("SELECT EMP_ID, EMP_NAME, SSN, IBSADATE"
				+ ", CITY_NAME, TEL, BUSEO_NAME, JIKWI_NAME"
				+ ", BASICPAY, SUDANG FROM TBL_EMP"
				+ "WHERE %s = '%s'", key, value);
		
		// 쿼리문 실행
		ResultSet rs = stmt.executeQuery(sql);
		
		//ResultSet rs 처리 → 반복문 구성
		while (rs.next())
		{
			MemberDTO dto = new MemberDTO();
			dto.setEMP_ID(rs.getInt("EMP_ID"));
			dto.setEMP_NAME(rs.getString("EMP_NAME"));
			dto.setSSN(rs.getString("SSN"));
			dto.setIBSADATE(rs.getString("setIBSADATE"));
			dto.setCITY_NAME(rs.getString("CITY_NAME"));
			dto.setTEL(rs.getString("TEL"));
			dto.setBUSEO_NAME(rs.getString("BUSEO_NAME"));
			dto.setJIKWI_NAME(rs.getString("JIKWI_NAME"));
			dto.setBASICPAY(rs.getInt("BASICPAY"));
			dto.setSUDANG(rs.getInt("SUDANG"));
			
			result.add(dto);	
		}
		
		rs.close();
		stmt.close();
		
		return result;
	}
	
	
	// 인원 수 확인 메소드
	public int count() throws SQLException
	{
		int result = 0;
		
		Statement stmt = conn.createStatement();
		
		String sql = "SELECT COUNT(*) AS COUNT FROM TBL_EMP";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next())
		{
			result = rs.getInt("COUNT");
		}
		rs.close();
		stmt.close();
		
		return result;
	}
	
	// 지역 리스트 조회 메소드
	public ArrayList<String> searchCity() throws SQLException
	{
		ArrayList<String> result = new ArrayList<String>();
		
		Statement stmt = conn.createStatement();
		
		String sql = "SELECT CITY_NAME FROM TBL_CITY";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next())
		{
			result.add(rs.getString("CITY_NAME"));
		}
		rs.close();
		stmt.close();
		
		return result;
	}

	// 부서 조회 메소드
	public ArrayList<String> searchBuseo() throws SQLException
	{
		ArrayList<String> result = new ArrayList<String>();
		
		Statement stmt = conn.createStatement();
		
		String sql = "SELECT BUSEO_NAME FROM TBL_BUSEO";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next())
		{
			result.add(rs.getString("BUSEO_NAME"));
		}
		rs.close();
		stmt.close();
		
		return result;
	}
	
	// 직위 조회 메소드
	public ArrayList<String> searchJikwi() throws SQLException
	{
		ArrayList<String> result = new ArrayList<String>();
		
		Statement stmt = conn.createStatement();
		
		String sql = "SELECT JIKWI_NAME FROM TBL_JIKWI";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next())
		{
			result.add(rs.getString("JIKWI_NAME"));
		}
		rs.close();
		stmt.close();
		
		return result;
	}	
		
	// 데이터 수정 메소드
	public int modify(MemberDTO dto) throws SQLException
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		
		String sql = String.format("UPDATE TBL_MEMBER "
				+ "SET EMP_NAME = '%s', SSN = '%s', IBSADATE = '%s'"
				+ ", CITY_NAME = '%s', TEL = '%s', BUSEO_NAME = '%s', JIKWI_NAME = '%s'"
				+ ", BASICPAY = %d, SUDANG = %d"
				+ "WHERE EMP_ID = %d"
				, dto.getEMP_NAME(), dto.getSSN(), dto.getIBSADATE()
				, dto.getCITY_NAME(), dto.getTEL(), dto.getBUSEO_NAME(), dto.getJIKWI_NAME()
				, dto.getBASICPAY(), dto.getSUDANG(), dto.getEMP_ID());
		
		ResultSet rs = stmt.executeQuery(sql);
		
		rs.close();
		stmt.close();
		return result;	
	}
	
	// 데이터 삭제 메소드
	public int remove(int sid) throws SQLException
	{
		int result = 0;
		Statement stmt = conn.createStatement();
		String sql = String.format("DELETE FROM TBL_MEMBER WHERE EMP_ID = %d;", sid);
		
		ResultSet rs = stmt.executeQuery(sql);
		
		rs.close();
		stmt.close();
		return result;	
	}
	
	// 데이터베이스 연결 종료 메소드
	public void close() throws SQLException
	{
		DBConn.close();
	}
	
	
	
}
