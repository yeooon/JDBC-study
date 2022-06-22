
/*====================================
 MemberMain.java
=====================================*/
/*
○ TBL_MEMBER 테이블을 활용하여
이름과 전화번호를 여러 건 입력받고, 전체 출력하는 프로그램을 구현한다.
단, 데이터베이스 연동이 이루어져야 하고,
MemberDAO, MemberDTO 클래스를 활용해야 한다.

실행 예)
이름 전화번호 입력(2) : 오이삭 010-2222-2222
>> 회원 정보 입력 완료~!!!
이름 전화번호 입력(3) : 임소민 010-3333-3333
>> 회원 정보 입력 완료~!!!
이름 전화번호 입력(4) : .

--------------------------------------------
전체 회원 수 : 3명
--------------------------------------------
번호 	이름 	전화번호
1		이호석  010-1111-1111
2 		오이삭	010-2222-2222
3		임소민  010-3333-3333
---------------------------------------------
*/

// 1. DTP getter / setter 구성
// 2. DAO 구성 
// 		1) 데이터를 입력받는 메소드
//		2) 전체 인원수 확인 메소드
//		3) 전체 리스트 조회 메소드
// 3. MemberMain 구성

package com.test;

import java.util.Scanner;

import com.util.DBConn;

public class MemberMain
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		
		try
		{
			MemberDAO dao = new MemberDAO();
			
			int count = dao.count();
			
			do
			{
				System.out.printf("이름 전화번호 입력(%d) : ", (count++));
				String name = sc.next();
				
				// 반복문 나가는 조건
				if (name.equals("."))
					break;
				
				String tel = sc.next();
				// ※ 여기까지의 과정을 통해 이름과 전화번호를 사용자로부터 입력받은 이유는
				// 	  입력받은 데이터를 데이터베이스에 입력하기 위함
				//    데이터 입력을 위해서는 dao 의 add() 메소드 호출
				//    add() 메소드 호출하기 위해서는 MemberDTO 값을 넘겨주는 과정이 필요
				//    MemberDTO 값을 넘겨주기 위해서는 객체의 속성값 구성 필요
			
			// MemberDTO 객체 구성
			MemberDTO dto = new MemberDTO();
			
			// 속성값 구성
			dto.setName(name);
			dto.setTel(tel);
			
			// 데이터베이스에 데이터를 입력하는 메소드 호출 → add
			int result = dao.add(dto);
			if (result>0)
				System.out.println(">> 회원 정보 입력 완료~!!!");
			
			} while (true);
			
			System.out.println();
			System.out.println("--------------------------------------");
			System.out.printf("전체 회원 수 %s명\n",dao.count());
			System.out.println("--------------------------------------");	
			System.out.println("번호	이름	전화번호");
			
			//리스트 가져와 출력
			//MemberDTO 타입으로 하나의 오브젝트를 꺼냄
			for (MemberDTO obj : dao.list())
			{
				System.out.printf("%3s	%7s	%12s\n",obj.getSid(),obj.getName(),obj.getTel());							
			}
			
			System.out.println("--------------------------------------");
			
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
				System.out.println("데이터베이스 연결 닫힘~!!!");
				System.out.println("프로그램 종료됨~!!");
			} catch (Exception e)
			{
				System.out.println(e.toString());
			}
		}
		sc.close();
	}
}


















