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
 
 전체 인원 : 8명
 번호 이름 국어 영어 수학 총점 평균 석차
 1
 2
 3
 4				...
 5
 6
 7
 8
*/

package com.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreProcess
{
	// 주요 속성 구성 → 데이터베이스 액션 처리 전담 객체 → ScoreDAO
	private ScoreDAO dao;
	
	// 생성자 정의(사용자 정의 생성자)
	public ScoreProcess()
	{
		dao = new ScoreDAO();
	}
	
	// 성적 입력
	public void ScoreInsert()
	{
		Scanner sc = new Scanner(System.in);
		
		try
		{
			//데이터베이스 연결
			Connection conn = dao.connection();
			
			if(conn == null)
				System.out.println("데이터베이스 연결 실패");
			
			//레코드 수 확인
			int count = dao.count();
			
			//System.out.println(count);
			
			do
			{
				System.out.println();
				System.out.printf("%d번 학생 성적 입력(이름 국어 영어 수학) : ", (++count));
				String name = sc.next();
				
				if (name.equals("."))
					break;
				
				int kor = sc.nextInt();
				int eng = sc.nextInt();
				int mat = sc.nextInt();
				
				// 입력받은 항목들을 토대로 ScoreDTO 객체 구성
				ScoreDTO dto = new ScoreDTO();
				dto.setName(name);
				dto.setKor(kor);
				dto.setEng(eng);
				dto.setMat(mat);
				
				//dao 의 add() 메소드 호출
				int result = dao.add(dto);
						
				if (result > 0)
				{
					System.out.println("입력되었습니다~! ^-^");
				}				
			} while (true);
			
			dao.close();
			
		} catch (Exception e)
		{
			System.out.print(e.toString());
		}
		finally 
		{
			try
			{
				dao.close();
				
			} catch (Exception e)
			{
				System.out.println(e.toString());
			}
		}
	}
	
	// 성적 전체 출력
	public void ScoreShow()
	{
		try
		{
			dao.connection();
			
			int count = dao.count();
			
			System.out.println();
			System.out.printf("전체 인원 %d명\r\n", count);
			System.out.println("번호 	이름 	국어 	영어 	수학 	총점 	평균	석차");
			
			for (ScoreDTO dto : dao.lists())
			{
				System.out.printf("%3s %4s %5d %5d %5d %5d %5.1f %5d\n"
						, dto.getSid(),dto.getName(),dto.getKor(),dto.getEng()
						, dto.getMat(), dto.getTot(), dto.getAvg(), dto.getRank());
			}
			
			dao.close();
			
		} catch (Exception e)
		{
			System.out.print(e.toString());
		}
	}
	
	
	// 이름 검색 출력
	public void NameShow()
	{
		try
		{	
			Scanner sc = new Scanner(System.in);
			System.out.print("검색할 이름 입력 : ");
			String name = sc.next();
			
			dao.connection();
			int count = dao.count();
			
			ArrayList<ScoreDTO> arrayList = dao.lists(name);
			// 입력한 name 값만 나오지 않고 전체 출력되던 이유 --> lists() 로 진행함. 매개변수를 안 줬음.
			
			
			System.out.println();
			System.out.printf("전체 인원 %d명\r\n", count);

			
			if (arrayList.size() > 0)
			{
				System.out.println("번호 	이름 	국어 	영어 	수학 	총점 	평균	석차");
				
				for (ScoreDTO dto : arrayList)
				{
					System.out.printf("%3s %4s %5d %5d %5d %5d %5.1f %5d\n"
							, dto.getSid(),dto.getName(),dto.getKor(),dto.getEng()
							, dto.getMat(), dto.getTot(), dto.getAvg(), dto.getRank());
				}
			}
			else 
			{
				System.out.println("검색된 이름이 없습니다.");
			}
			dao.close();
			
		} catch (Exception e)
		{
			System.out.print(e.toString());
		}
	}
	
	// 성적 수정
	public void UpdateScore()
	{
		try
		{
			Scanner sc = new Scanner(System.in);
			System.out.print("수정할 번호를 입력하세요 : ");
			int sid = sc.nextInt();
			
			dao.connection();
			int count = dao.count();
			
			ArrayList<ScoreDTO> arrayList = dao.lists(sid);
			
			if (arrayList.size() > 0)
			{
				System.out.println("번호 	이름 	국어 	영어 	수학 	총점 	평균	석차");
				
				for (ScoreDTO dto : arrayList)
				{
					System.out.printf("%3s %4s %5d %5d %5d %5d %5.1f %5d\n"
							, dto.getSid(),dto.getName(),dto.getKor(),dto.getEng()
							, dto.getMat(), dto.getTot(), dto.getAvg(), dto.getRank());
				}
				
				System.out.println();
				System.out.print("수정 데이터 입력(이름 국어 영어 수학) : ");
				String name = sc.next();
				int kor = sc.nextInt();
				int eng = sc.nextInt();
				int mat = sc.nextInt();
				
				//dto 구성
				ScoreDTO dto = new ScoreDTO();
				dto.setName(name);
				dto.setKor(kor);
				dto.setEng(eng);
				dto.setMat(mat);
				// dto.setSid 이게 있어줘야 번호를 따라가 찾아 수정할 수 있음
				// --> 없으면 수정 대상이 존재하지 않는다고 뜸
				dto.setSid(String.valueOf(sid));
				
				int result = dao.modify(dto);
				
				if (result > 0)
				{
					System.out.println("수정이 완료되었습니다.");
				}
				else 
				{
					// 수신된 내용이 존재하지 않는 상황 전달(안내)
					System.out.println("수정 대상이 존재하지 않습니다.");
				}
				dao.close();
			}
			
		} catch (Exception e)
		{
			System.out.print(e.toString());
		}
	}
	
	// 성적 삭제
	public void remove()
	{
		try
		{
			Scanner sc = new Scanner(System.in);
			System.out.print("삭제할 번호를 입력하세요 : ");
			int sid = sc.nextInt();	
			
			dao.connection();
			
			ArrayList<ScoreDTO> arrayList = dao.lists(sid);
			
			if (arrayList.size() > 0)
			{
				// 수신된 내용 출력
				System.out.println("번호 	이름 	국어 	영어 	수학 	총점 	평균	석차");

				for (ScoreDTO dto : arrayList)
				{
					System.out.printf("%3s %4s %4d %5d %5d %5d %5.1f %5d"
							, dto.getSid(), dto.getName(), dto.getKor(), dto.getEng()
							, dto.getMat(), dto.getTot(), dto.getAvg(), dto.getRank());
				}
				
				System.out.print("\n\r>> 정말 삭제하시겠습니까?(Y/N) : ");
				String response = sc.next();
				
				if (response.equals("Y")||response.equals("N"))
				{
					int result = dao.remove(sid);
					if (result > 0)
					{
						System.out.println("삭제가 완료되었습니다.");
					}
					else {
						System.out.println("삭제가 취소되었습니다.");						
					}
					
					
				}
				else {
					System.out.println("삭제할 대상이 존재하지 않습니다.");
				}
			}
		} 
		catch (Exception e)
		{
			System.out.print(e.toString());
		}

		
		
	}
	
	
}
