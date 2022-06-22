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

import java.util.ArrayList;
import java.util.Scanner;

public class ScoreProcess
{
	// 주요 속성 구성 → 데이터베이스 액션 처리 전담 객체 → ScoreDAO
	private ScoreDAO dao;

	// 생성자 정의
	public ScoreProcess()
	{
		dao = new ScoreDAO();

	}
		
	// 성적 입력 기능
	public void sungjukInsert()
	{
		try
		{
			// 데이터베이스 연결
			dao.connection();
			
			// 레코드 수 확인
			int count = dao.count();		
			
			Scanner sc = new Scanner(System.in);
			
			do
			{
				// 데이터 입력 받음
				System.out.println();
				System.out.printf("%d번 학생 성적 입력(이름 국어 영어 수학) : ", (++count));
				String name = sc.next();
				
				// 반복문의 조건을 깨뜨리는 코드 구성
				if (name.equals("."))
					break;
				
				int kor = sc.nextInt();
				int eng = sc.nextInt();
				int mat = sc.nextInt();
				
				// ScoreDTO 객체 구성
				ScoreDTO dto = new ScoreDTO();
				dto.setName(name);
				dto.setKor(kor);
				dto.setEng(eng);
				dto.setMat(mat);
				
				// dao 의 add() 메소드 호출
				int result = dao.add(dto);
				if (result>0)
				{
					System.out.println("성적 입력이 완료되었습니다.");
				}
			} while (true);
			
			// 데이터베이스 연결 종료
			dao.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	
	}//end sungjukInsert()
	
	// 성적 전체 출력 기능
	public void sungjukSelctAll()
	{
		try
		{
			// dao 의 connection() 메소드 호출 → 데이터베이스 연결
			dao.connection();
			
			// dao 의 count() 메소드 호출 → 인원 수 확인
			int count = dao.count();
			
			System.out.println();
			System.out.printf("전체 인원: %d명\n", count);
			System.out.println("번호 이름 국어 영어 수학 총점 평균 석차");
			
			for (ScoreDTO dto : dao.lists())
			{
				System.out.printf("%3s %4s %4d %5d %5d %5d %5.1f %5d\n"
						, dto.getSid(), dto.getName()
						, dto.getKor(), dto.getEng(), dto.getMat()
						, dto.getTot(), dto.getAvg(), dto.getRank());
			}
			
			// dao 의 close() 메소드 호출 → 데이터베이스 연결 종료
			dao.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}//end sungjukSelctAll()
	
	
	// 이름 검색 출력 기능
	public void sungjukSearchName()
	{
		try
		{
			// 검색할 이름 입력받기
			Scanner sc = new Scanner(System.in);
			System.out.print("검색할 이름 입력 : ");
			String name = sc.next();
			
			//-- 필요한 경우 이 과정에서 프로그래밍적으로 검증(검사) 수행
			
			// 데이터베이스 연결
			dao.connection();
			
			// dao 의 lists() 메소드 호출 → 매개변수로 검색할 이름 넣어주기
			ArrayList<ScoreDTO> arrayList = dao.lists(name);
			
			if (arrayList.size() > 0)
			{
				// 수신된 내용 출력
				System.out.println("번호 이름 국어 영어 수학 총점 평균 석차");
				
				for (ScoreDTO dto : arrayList)
				{
					System.out.printf("%3s %4s %4d %5d %5d %5d %5.1f %5d\n"
							, dto.getSid(), dto.getName()
							, dto.getKor(), dto.getEng(), dto.getMat()
							, dto.getTot(), dto.getAvg(), dto.getRank());
				}
				
			} else
			{
				System.out.println("검색 결과가 존재하지 않습니다.");
			}
			
			// 데이터베이스 연결 종료
			dao.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}//end sungjukSearchName()
	
	
	// 성적 수정 기능
	public void sungjukUpdate()
	{
		try
		{
			// 수정할 번호 입력받기
			Scanner sc = new Scanner(System.in);
			System.out.print("수정할 번호를 입력하세요 : ");
			int sid = sc.nextInt();
			
			//-- 입력받은 번호로 체크해야 할 로직 적용 삽입 가능
			
			// 데이터베이스 연결
			dao.connection();
			
			// 수정할 대상 수신 → 데이터 수정을 위해 대상 검색
			ArrayList<ScoreDTO> arrayList = dao.lists(sid);
			
		    if (arrayList.size() > 0)
			{
				// 수신된 내용 출력
		    	System.out.println("번호 이름 국어 영어 수학 총점 평균 석차");
				
				for (ScoreDTO dto : arrayList)
				{
					System.out.printf("%3s %4s %4d %5d %5d %5d %5.1f %5d\n"
							, dto.getSid(), dto.getName()
							, dto.getKor(), dto.getEng(), dto.getMat()
							, dto.getTot(), dto.getAvg(), dto.getRank());
				}
				
				System.out.println();
				System.out.print("수정 데이터 입력(이름 국어 영어 수학) : ");
				String name = sc.next();
				int kor = sc.nextInt();
				int eng = sc.nextInt();
				int mat = sc.nextInt();
				
				// dto 구성
				ScoreDTO dto = new ScoreDTO();
				dto.setName(name);
				dto.setKor(kor);
				dto.setEng(eng);
				dto.setMat(mat);
				dto.setSid(String.valueOf(sid));
								
				int result = dao.modify(dto);
				if(result > 0)
					System.out.println("수정이 완료되었습니다.");
		    	
			} else
			{
				System.out.println("수정 대상이 존재하지 않습니다.");
			}
			
		    // 데이터베이스 연결 종료 
		    dao.close();
		    		    
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}//end sungjukUpdate()
	
	// 성적 삭제 기능
	public void sungjukDelete()
	{
		try
		{
			// 삭제할 번호 입력받기
			Scanner sc = new Scanner(System.in);
			System.out.print("삭제할 번호를 입력하세요 : ");
			int sid = sc.nextInt();
			
			// 데이터베이스 연결
			dao.connection();
			
			// dao 의 list() 메소드 호출
			ArrayList<ScoreDTO> arrayList = dao.lists(sid);
			
			if (arrayList.size() > 0)
			{
				// 수신된 내용 출력
				System.out.println("번호 이름 국어 영어 수학 총점 평균 석차");
				
				for (ScoreDTO dto : arrayList)
				{
					System.out.printf("%3s %4s %4d %5d %5d %5d %5.1f %5d\n"
							, dto.getSid(), dto.getName()
							, dto.getKor(), dto.getEng(), dto.getMat()
							, dto.getTot(), dto.getAvg(), dto.getRank());
				}
				
				System.out.print(">> 정말 삭제하시겠습니까?(Y/N) : ");
				String response = sc.next();
				if (response.equals("Y") || response.equals("y"))
				{
					int result = dao.remove(sid);
					if(result>0)
						System.out.println("삭제가 완료되었습니다.");
				}
				else
				{
					System.out.println("취소되었습니다.");
				}
				
				
			} else
			{
				System.out.println("삭제할 대상이 존재하지 않습니다.");
			}
			
			// 데이터베이스 연결 종료
			dao.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
}
