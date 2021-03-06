/*
○ 성적 처리 → 데이터베이스 연동(데이터베이스 연결 및 액션 처리)
				ScoreDTO 클래스 활용(속성만 존재하는 클래스 getter / setter 구성)
				ScoreDAO 클래스 활용(데이터베이스 액션 처리)
				ScoreProcess 클래스 활용(단위 기능 구성)

※단, 모~~~~~~~든 작업 객체는 PreparedStatement 를 활용한다.

===[성적 처리]===
 1. 성적 입력
 2. 성적 전체 출력
 3. 이름 검색 출력
 4. 성적 수정
 5. 성적 삭제
 =================
 >> 선택(1~5, -1종료) : 1
 
 7번 학생 성적 입력(이름 국어 영어 수학) : 이시우 50 60 70
 성적 입력이 완료되었습니다.
 
 8번 학생 성적 입력(이름 국어 영어 수학) : 이지연 80 80 80
 성적 입력이 완료되었습니다.
 
 9번 학생 성적 입력(이름 국어 영어 수학) : .
 
 ===[성적 처리]===
 1. 성적 입력
 2. 성적 전체 출력
 3. 이름 검색 출력
 4. 성적 수정
 5. 성적 삭제
 =================
 >> 선택(1~5, -1종료) : 2
*/

package com.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.util.DBConn;

public class ScoreDAO
{
	// 주요 속성 구성
	private Connection conn;

	// 데이터베이스 연결
	public Connection connection()
	{
		conn = DBConn.getConnection();
		return conn;
	}
	
	// 성적 입력
	public int add(ScoreDTO dto)
	{
		int result = 0;
		
		//쿼리문 준비 
		String sql = "INSERT INTO TBL_SCORE(SID, NAME"
				+ ", KOR, ENG, MAT)"
				+ " VALUES(SCORESEQ.NEXTVAL, ?, ?, ?, ?)";
		
		try
		{
			//작업 객체 생성
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getName());
			pstmt.setInt(2, dto.getKor());
			pstmt.setInt(3, dto.getEng());
			pstmt.setInt(4, dto.getMat());
			
			result = pstmt.executeUpdate();
			
			// 무한루프 돌던 원인 
			//--> if (result1 > 0 ) 로 진행했음. result 값, result1 값 따로 받아서 코드 꼬인거임.
			if (result > 0 )
			{
				pstmt.close();
			}
			pstmt.close();			
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 성적 전체 출력
	public ArrayList<ScoreDTO> lists() throws SQLException
	{
		ArrayList<ScoreDTO> result = new ArrayList<ScoreDTO>();
		
		// 쿼리문 구성
		String sql = "SELECT SID, NAME, KOR, ENG,MAT" 
				+ ",(KOR+ENG+MAT) AS TOT" 
				+ ",(KOR+ENG+MAT)/3 AS AVG"
				+ ",RANK() OVER(ORDER BY (KOR+ENG+MAT) DESC) AS RANK " 
				+ "FROM TBL_SCORE " 
				+ "ORDER BY SID ASC";
		
		try
		{
			// 작업 객체 생성
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
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
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;	
		
	}
	
	// 이름 검색 담당 메소드
	
	public ArrayList<ScoreDTO> lists(String name)
	{
		ArrayList<ScoreDTO> result = new ArrayList<ScoreDTO>();
		
		//쿼리문 구성
		String sql = "SELECT *" 
				+ " FROM (SELECT SID, NAME, KOR, ENG, MAT" 
				+ ", (KOR+ENG+MAT) AS TOT"
				+ ", (KOR+ENG+MAT)/3 AS AVG" 
				+ ", RANK() OVER(ORDER BY (KOR+ENG+MAT) DESC) AS RANK" 
				+ " FROM TBL_SCORE)"
				+ " WHERE NAME=?";
		
		try
		{
			//작업객체생성
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			
			ResultSet rs = pstmt.executeQuery();
			
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
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		return result;
		
	}
	
	// 번호 검색 담당 메소드
	public ArrayList<ScoreDTO> lists(int sid)
	{
		ArrayList<ScoreDTO> result = new ArrayList<ScoreDTO>();
		
		//쿼리문 구성
		String sql = "SELECT SID, NAME, KOR, ENG, MAT"
				+ ", (KOR+ENG+MAT) AS TOT"
				+ ", (KOR+ENG+MAT)/3 AS AVG"
				+ ", RANK() OVER(ORDER BY (KOR+ENG+MAT) DESC) AS RANK"
				+ " FROM TBL_SCORE WHERE SID = ?";
		
		try
		{
			//작업객체생성
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, sid);
			
			ResultSet rs = pstmt.executeQuery();
			
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
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		return result;
		
	}
	
	// 인원 수 담당 메소드
	// 이게 생각보다 중요함...
	
	public int count()
	{
		int result = 0;
		
		try
		{
			String sql = "SELECT COUNT(*) AS COUNT FROM TBL_SCORE";
			//"SELECT COUNT(*) FROM TBL_SCORE" <-- 계속 이렇게 진행했다가 오류났다
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				result = rs.getInt("COUNT");
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
		
	}
	
	// 성적 수정
	public int modify(ScoreDTO dto)
	{
		int result = 0;
		
		//쿼리문 준비
		String sql = String.format("UPDATE TBL_SCORE"
				+ " SET NAME = ?, KOR = ?, ENG = ?, MAT = ?"
				+ " WHERE SID = ?"
				, dto.getName(), dto.getKor(), dto.getEng(), dto.getMat(), dto.getSid());
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getName());
			pstmt.setInt(2, dto.getKor());
			pstmt.setInt(3, dto.getEng());
			pstmt.setInt(4, dto.getMat());
			pstmt.setString(5, dto.getSid());	
			
			result = pstmt.executeUpdate();
			pstmt.close();
			
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		return result;
		
	}
	
	// 성적 삭제
	public int remove(int sid)
	{
		int result = 0;
		
		//쿼리문
		String sql = "DELETE FROM TBL_SCORE WHERE SID = ?";
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			
			result = pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		return result;
	}
	
	// 데이터베이스 종료
	public void close() throws SQLException
	{
		try
		{
			DBConn.close();
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
}
