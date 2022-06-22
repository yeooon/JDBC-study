/*==================
	ScoreMain.java
-===================*/
/*
○ 성적 처리 프로그램 구현 → 데이터베이스 연동 → ScoreDAO, ScoreDTO 클래스 활용
	여러 명의 이름, 국어점수, 영어점수, 수학점수를 입력받아
	총점, 평균을 연산하여 내용을 출력하는 프로그램을 구현한다.
	출력 시 번호(이름, 총점 등) 오름차순 정렬하여 출력한다.
	
실행 예)

1번 학생 성적 입력(이름 국어 영어 수학) : 신시은 80 75 60
2번 학생 성적 입력(이름 국어 영어 수학) : 이호석 100 90 80
3번 학생 성적 입력(이름 국어 영어 수학) : 이연주 80 85 80
4번 학생 성적 입력(이름 국어 영어 수학) : .

-----------------------------------------------------------
번호	이름	국어	영어	수학	총점	평균
-----------------------------------------------------------
1		신시은	80		75		60		215		71.xxx
2		이호석	100		90		80		270		90
						 :
						 :
------------------------------------------------------------						 
*/


package com.test;

import java.util.Scanner;

import com.util.DBConn;

public class ScoreMain
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		
		try
		{
			ScoreDAO dao = new ScoreDAO();
			int count = dao.count();
			
			do
			{
				System.out.printf("%d번 학생 성적 입력(이름 국어 영어 수학) : ", ++count);
				String name = sc.next();
				
				if(name.equals("."))
					break;
				
				int kor = sc.nextInt();
				int eng = sc.nextInt();
				int mat = sc.nextInt();
				
				// MemberDTO 객체 구성
				ScoreDTO dto = new ScoreDTO();
				
				// 속성값 구성
				dto.setName(name);
				dto.setKor(kor);
				dto.setEng(eng);
				dto.setMat(mat);
				dto.setTot(kor, eng, mat);
				dto.setAvg(kor, eng, mat);

				// 데이터베이스에 데이터를 입력하는 메소드 호출 → add		
				dao.add(dto);
				
				
			} while (true);
			
			System.out.println();
			System.out.println("------------------------------------------------------");
			System.out.println("번호	이름	국어	영어	수학	총점	평균");
			System.out.println("------------------------------------------------------");	
			
			// 리스트 가져와 출력
			for (ScoreDTO obj : dao.lists())
			{
				System.out.printf("%3d\t %3s\t %3d\t %3d\t %3d\t %3d\t %3.1f\t\n"
						, obj.getSid(), obj.getName(), obj.getKor(), obj.getEng(), obj.getMat(), obj.getTot(), obj.getAvg());
			}
			System.out.println("------------------------------------------------------");
			
		} 
		catch (Exception e)
		{
			System.out.print(e.toString());
		}
		finally 
		{
			try
			{
				DBConn.close();
				System.out.println("데이터베이스 닫힘~!");
				System.out.println("프로그램 종료됨~!");
				
			} catch (Exception e)
			{
				System.out.print(e.toString());
			}
		}
		sc.close();
	}
}
