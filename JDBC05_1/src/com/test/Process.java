package com.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Process
{
	private MemberDAO dao;
	
	//생성자
	public Process()
	{
		dao = new MemberDAO();
	}
	
	
	//멤버 입력
	public void MemberInsert()
	{
		try
		{
			//데이터베이스 연결
			dao.connection(); 
			
			//레코드 수 확인
			int count  = dao.count();
			
			Scanner sc = new Scanner(System.in);
			
			do
			{
				 System.out.println("직원 정보 입력 ------------------------------------");
				 System.out.print("이름 : ");
				 String name = sc.next();
				 
				 System.out.print("주민등록번호(yymmdd-nnnnnnn) : "); 
				 String ssn = sc.next();
				
				 System.out.print("입사일 (YYYY-MM-DD) : "); 
				 String ibsadate = sc.next();
				 
				 System.out.printf("지역(강원/경기/경남/경북/부산/서울/인천/전남/전북/제주/충남/충북/) : "); 
				 String city = sc.next();
				
				 System.out.print("전화번호 : "); 
				 String tel = sc.next();
				
				 System.out.print("부서(개발부/기획부/영업부/인사부/자재부/총무부/홍보부/) : "); 
				 String buseo = sc.next();
				
				 System.out.print("직위(사장/전무/상무/이사/부장/차장/과장/대리/사원/) : "); 
				 String jikwi = sc.next();
				 
				 System.out.print("기본급(최소 1800000 이상) : "); 
				 int basicpay = sc.nextInt();
				
				 System.out.print("수당 : "); 
				 int sudang = sc.nextInt();
				
				// 입력받은 항목들을 토대로 ScoreDTO 객체 구성
				MemberDTO dto = new MemberDTO();
				dto.setEMP_NAME(name);
				dto.setSSN(ssn);
				dto.setIBSADATE(ibsadate);
				dto.setCITY_NAME(city);
				dto.setTEL(tel);
				dto.setBUSEO_NAME(buseo);
				dto.setJIKWI_NAME(jikwi);
				dto.setBASICPAY(basicpay);
				dto.setSUDANG(sudang);
			
				// dao의 add 메소드 호출
				int result = dao.add(dto);
				
				if (result > 0)
				{
					System.out.println("성적 입력이 완료되었습니다.");
				}
								
			} while (true);
			
		} catch (Exception e)
		{
			System.out.print(e.toString());
		}
		finally //무조건적으로 수행해 주는 것
		{
			try
			{
				dao.close();
			} catch (Exception e2)
			{
				System.out.print(e2.toString());
			}
		}	
	}
	
	//멤버 출력
	public void MemberSelectAll() throws ClassNotFoundException, SQLException
	{
		Scanner sc = new Scanner(System.in);

		// 서브 메뉴 출력
		System.out.println("1. 사번 정렬");
		System.out.println("2. 이름 정렬");
		System.out.println("3. 부서 정렬");
		System.out.println("4. 직위 정렬");
		System.out.println("5. 급여 내림차순 정렬");
		System.out.print(">> 선택(1~5 -1 종료) : ");		
		
		String menuStr = sc.next();
		
		try
		{
			int menu = Integer.parseInt(menuStr);
			if (menu == -1)
			{
				return;
			}
			
			String key = "";
			
			switch (menu)
			{
			case 1:
				key = "EMP_ID";
				break;
			case 2:
				key = "EMP_NAME";
				break;	
			case 3:
				key = "BUSEO_NAME";
				break;			
			case 4:
				key = "JIKWI_NAME";
				break;	
			case 5:
				key = "BASICPAY DESC";
				break;		
			default:
				break;
			}
			
			//데이터베이스 연결
			dao.connection();
			
			//직원 리스트 출력
			System.out.println();
			System.out.printf("전체 인원 %d명\n", dao.count());
			System.out.println("사번	이름	주민번호	입사일	지역	전화번호	부서	직위	기본급	수당");
			ArrayList<MemberDTO> memlist = dao.arrayList(key);
			
			//memlist라는 배열을 읽어 dto에 집어넣음
			for (MemberDTO dto : memlist)
			{
				System.out.printf("%d %s %s %s %s %s %s %s %d %d"
						, dto.getEMP_ID(),dto.getEMP_NAME(),dto.getSSN(),dto.getIBSADATE()
						, dto.getCITY_NAME(),dto.getTEL(),dto.getJIKWI_NAME(),dto.getBASICPAY(),dto.getSUDANG());
			}			
			
		} 
		catch (Exception e)
		{
			System.out.print(e.toString());
		}		
	}
	
	//멤버 검색 출력
	public void SearchSelect() throws SQLException, ClassNotFoundException
	{
		Scanner sc = new Scanner(System.in);
	
		System.out.println();
		System.out.println("1. 사번 검색");
		System.out.println("2. 이름 검색");
		System.out.println("3. 부서 검색");
		System.out.println("4. 직위 검색");
		System.out.print(">> 선택(1~4 -1 종료) : ");
		
		String menuStr = sc.next();
		
		try
		{
			int menu = Integer.parseInt(menuStr);
			if (menu == -1)
			{
				return;
			}
			
			String key = "";
			String value = "";
			
			switch (menu)
			{
			case 1:
				key = "EMP_ID";
				System.out.print("검색할 사원번호 입력 : ");
				value = sc.next();
				break;
			case 2:
				key = "EMP_NAME";
				System.out.print("검색할 사원이름 입력 : ");
				value = sc.next();
				break;	
			case 3:
				key = "BUSEO_NAME";
				System.out.print("검색할 부서이름 입력 : ");
				value = sc.next();
				break;			
			case 4:
				key = "JIKWI_NAME";
				System.out.print("검색할 직위이름 입력 : ");
				value = sc.next();
				break;	
			default:
				break;
			}
			
			//데이터베이스 연결
			dao.connection();
			
			//직원 리스트 출력
			System.out.println();
			System.out.println("사번	이름	주민번호	입사일	지역	전화번호	부서	직위	기본급	수당");
			ArrayList<MemberDTO> memList = dao.lists(key, value);
			
			
			for (MemberDTO dto1 : memList)
			{
				System.out.printf("%3d %4s %4s %5s %5s %5s %5s %5s %5d %5d"
						, dto1.getEMP_ID(),dto1.getEMP_NAME(),dto1.getSSN(),dto1.getIBSADATE()
						, dto1.getCITY_NAME(),dto1.getTEL(),dto1.getJIKWI_NAME(),dto1.getBASICPAY(),dto1.getSUDANG());
			}			
			
		} 
		catch (Exception e)
		{
			System.out.print(e.toString());
		}	
	}
	
	
	// 멤버 수정
	public void MemberUpdate()
	{
		Scanner sc = new Scanner(System.in);
		try
		{
			//수정할 대상의 번호 입력받기
			System.out.print("수정할 번호를 입력하세요 : ");
			String value = sc.next();
			
			//데이터베이스 연결
			dao.connection();
			
			//수정할 대상 수신 → 데이터 수정을 위한 대상 탐색
			ArrayList<MemberDTO> member = dao.lists("EMP_ID", value);
			
			if (member.size() > 0)
			{				
				// 도시 리스트 구성
				ArrayList<String> citys = dao.searchCity();
				StringBuilder cityStr = new StringBuilder();
				for (String city : citys)
				{
					cityStr.append(city + '/');
				}
				
				
				// 부서 리스트 구성
				ArrayList<String> buseos = dao.searchBuseo();
				StringBuilder buseoStr = new StringBuilder();
				for (String buseo : buseos)
				{
					buseoStr.append(buseo + '/');
				}
				
				// 직위 리스트 구성
				ArrayList<String> jikwis = dao.searchJikwi();
				StringBuilder jikwiStr = new StringBuilder();
				for (String jikwi : jikwis)
				{
					jikwiStr.append(jikwi + '/');
				}
				
				MemberDTO dto = member.get(0);
				int epmid = dto.getEMP_ID();
				String name = dto.getEMP_NAME();
				String ssn = dto.getSSN();
				String ibsadate = dto.getIBSADATE();
				String cityname = dto.getCITY_NAME();
				String tel = dto.getTEL();
				String buseoname = dto.getBUSEO_NAME();
				String jikwiname = dto.getJIKWI_NAME();
				int basicpay = dto.getBASICPAY();
				int sudang = dto.getSUDANG();
				
				System.out.println();
				System.out.println("직원 정보 수정 -------------------------------");
				System.out.printf("기존 이름 : %s%n",name);
				System.out.println("수정 이름 : ");
				String nname = sc.next();
				if (nname.equals("-"))
					nname = name;
				System.out.printf("기존 주민번호 : %s%n",ssn);
				System.out.println("수정 주민번호 : ");
				String nssn = sc.next();
				if (nssn.equals("-"))
					nssn = ssn;
				System.out.printf("기존 입사일(YYYY-MM-DD) : %s%n",ibsadate);
				System.out.println("수정 입사일(YYYY-MM-DD) : ");
				String nibsadate = sc.next();
				if (nibsadate.equals("-"))
					nibsadate = ibsadate;

				System.out.printf("기존 지역 : %s%n",cityname);
				System.out.println("수정 지역 : ");
				String ncityname = sc.next();
				if (ncityname.equals("-"))
					ncityname = cityname;					
				System.out.printf("기존 전화번호 : %s%n",tel);
				System.out.println("수정 전화번호 : ");
				String ntel = sc.next();
				if (ntel.equals("-"))
					ntel = tel;		
				System.out.printf("기존 부서 : %s%n",buseoname);
				System.out.println("수정 부서 : ");
				String nbuseoname = sc.next();
				if (nbuseoname.equals("-"))
					nbuseoname = buseoname;
				System.out.printf("기존 직위 : %s%n",jikwiname);
				System.out.println("수정 직위 : ");
				String njikwiname = sc.next();
				if (njikwiname.equals("-"))
					njikwiname = jikwiname;	
				
				System.out.printf("기존 기본급 : %s%n",basicpay);
				System.out.println("수정 기본급(최소 1800000 이상) : ");
				String nbasicpay = sc.next();
				int Basicpay = 0;
				if (nbasicpay.equals("-"))
					Basicpay = basicpay;
				Integer.parseInt(nbasicpay);
				
				System.out.printf("기존 수당 : %s%n",sudang);
				System.out.println("수정 수당 : ");
				String nsudang = sc.next();
				int Sudang = 0;
				if (nsudang.equals("-"))
					Sudang = sudang;
				else
					Integer.parseInt(nsudang);
				
				MemberDTO member1 = new MemberDTO();
				
				member1.setEMP_ID(epmid);
				member1.setEMP_NAME(nname);
				member1.setSSN(nssn);
				member1.setIBSADATE(nibsadate);
				member1.setCITY_NAME(ncityname);
				member1.setTEL(ntel);
				member1.setBUSEO_NAME(nbuseoname);
				member1.setJIKWI_NAME(njikwiname);
				member1.setBASICPAY(Basicpay);
				member1.setSUDANG(Sudang);
				
				System.out.println(ssn);
				
				int result = dao.modify(dto);
				if (result > 0)
					System.out.println("직원 정보 수정 완료~!");
				else 
				{
					System.out.println("수정 대상을 검색하지 못했습니다.");					
				}
			}
		}

				
				/*
				//수신된 내용 출력
				System.out.println("사번	이름	주민번호	입사일	지역	전화번호	부서	직위	기본급	수당");
				
				
				for (MemberDTO dto : dao.arrayList(sid))
				{
					System.out.printf("%3d %4s %4s %5s %5s %5s %5s %5s %5d %5d"
							, dto.getEMP_ID(),dto.getEMP_NAME(),dto.getSSN(),dto.getIBSADATE()
							, dto.getCITY_ID(),dto.getTEL(),dto.getJIKWI_ID(),dto.getBASICPAY(),dto.getSUDANG());
				}
				
				System.out.println();
				System.out.print("수정 데이터 입력(이름 주민번호 입사일 지역 전화번호 부서 직위	기본급 수당) : ");
				 String name = sc.next(); 
				 String ssn = sc.next();
				 String ibsadate = sc.next();
				 String city = sc.next();
				 String tel = sc.next();
				 String buseo = sc.next();
				 String jikwi = sc.next();
				 int basicpay = sc.nextInt();
				 int sudang = sc.nextInt();
				 
				// 입력받은 항목들을 토대로 ScoreDTO 객체 구성
				MemberDTO dto = new MemberDTO();
				dto.setEMP_NAME(name);
				dto.setSSN(ssn);
				dto.setIBSADATE(ibsadate);
				dto.setCITY_NAME(city);
				dto.setTEL(tel);
				dto.setBUSEO_NAME(buseo);
				dto.setJIKWI_NAME(jikwi);
				dto.setBASICPAY(basicpay);
				dto.setSUDANG(sudang);
				
				int result = dao.modify(dto);
				
				if (result > 0)
				{
					System.out.println("수정이 완료되었습니다.");
				}
				else 
				{
					System.out.println("수정 데이터가 존재하지 않습니다.");					
				}
				
				dao.close();
			}	

		} 
			*/
		catch (Exception e)
		{
			System.out.print(e.toString());
		}
		
		finally //무조건적으로 수행해 주는 것
		{
			try
			{
				dao.close();
			} catch (Exception e2)
			{
				System.out.print(e2.toString());
			}
		}		
	}

	
	//멤버 삭제
	public void MemberDelete()
	{
		Scanner sc = new Scanner(System.in);
		
		try
		{
			//삭제할 번호 입력하기
			System.out.print("삭제할 직원의 번호를 입력하세요 : ");
			String value = sc.next();
			
			//데이터베이스 연결
			dao.connection();
			
			//dao의 lists() 메소드 호출 → 삭제할 대상 검색
			ArrayList<MemberDTO> members = dao.lists("EMP_ID", value);
			
			if (members.size() > 0)
			{
				//수신된 내용 출력
				System.out.println("사번	이름	주민번호	입사일	지역	전화번호	부서	직위	기본급	수당");
				
				
				for (MemberDTO dto : members)
				{
					System.out.printf("%3d %4s %4s %5s %5s %5s %5s %5s %5d %5d"
							, dto.getEMP_ID(),dto.getEMP_NAME(),dto.getSSN(),dto.getIBSADATE()
							, dto.getCITY_NAME(),dto.getTEL(),dto.getJIKWI_NAME(),dto.getBASICPAY(),dto.getSUDANG());
				}	
				
				System.out.println(">> 정말 삭제하시겠습니까?(Y/N) : ");
				String response = sc.next();
				
				if (response.equals("Y")||response.equals("N"))
				{
					int result = dao.remove(Integer.parseInt(value));
					if (result > 0)
					{
						System.out.println("삭제가 완료되었습니다.");
					}
				}
					else 
					{
						System.out.println("삭제할 대상이 존재하지 않습니다.");
					}
				}

		} catch (Exception e)
		{
			System.out.print(e.toString());
		}	
		
		finally //무조건적으로 수행해 주는 것
		{
			try
			{
				dao.close();
			} catch (Exception e2)
			{
				System.out.print(e2.toString());
			}
		}	
	}		
}
