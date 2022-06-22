/*===================
 	ScoreMain.java
====================*/
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
import java.util.Scanner;

import com.util.DBConn;

public class ScoreMain
{
	public static void main(String[] args)
	{		
		Scanner sc = new Scanner(System.in);
		ScoreProcess prc = new ScoreProcess();
		
		do
		{
			System.out.println();
			System.out.println(" ===[성적 처리]===");
			System.out.println(" 1. 성적 입력");
			System.out.println(" 2. 성적 전체 출력");
			System.out.println(" 3. 이름 검색 출력");
			System.out.println(" 4. 성적 수정");
			System.out.println(" 5. 성적 삭제");
			System.out.println(" =================");
			System.out.print(">> 선택(1~5, -1종료) : ");
			String menus = sc.next();
			
			try
			{
				int menu = Integer.parseInt(menus);
				
				if (menu == -1)
				{
					System.out.println();
					System.out.println("프로그램이 종료되었습니다.");
					return;
				}
				
				switch (menu)
				{
					case 1:
						prc.ScoreInsert();
						break;
					case 2:
						prc.ScoreShow();
						break;
					case 3:
						prc.NameShow();
						break;
					case 4:
						prc.UpdateScore();
						break;						
					case 5:
						prc.remove();
						break;							
				}	
				
			} catch (Exception e)
			{
				System.out.print(e.toString());
			}
		} while (true);
		
	}
}
