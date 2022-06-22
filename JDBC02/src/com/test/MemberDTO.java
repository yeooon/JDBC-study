package com.test;

public class MemberDTO
{
	// getter / setter 외부에서 값을 불러올 때 무결성을 깨트리지 않기 위해 쓰는 방법
	// getter : 값을 읽어옴
	// setter : 값을 변경함
	
	// 주요 속성 구성
	// 번호, 이름, 전화번호
	// 변수 선언 → 우클릭 → Source → Generator getter setter 클릭하면
	// 알아서 게터세터 다 만들어준다. 
	private String sid, name, tel;

	public String getSid()
	{
		return sid;
	}

	public void setSid(String sid)
	{
		this.sid = sid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTel()
	{
		return tel;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}
	

	
}
