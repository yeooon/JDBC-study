/*
○ 성적 처리 → 데이터베이스 연동(데이터베이스 연결 및 액션 처리)
				ScoreDTO 클래스 활용(속성만 존재하는 클래스 getter / setter 구성)
				ScoreDAO 클래스 활용(데이터베이스 액션 처리)
				ScoreProcess 클래스 활용(단위 기능 구성)

※단, 모~~~~~~~든 작업 객체는 PreparedStatement 를 활용한다.

	private static String sid, name, kor, eng, mat, tot, avg, rank;
*/



package com.test;

public class ScoreDTO
{
	private String sid, name;
	private int kor, eng, mat, tot, rank;
	private double avg;
	
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
	public int getKor()
	{
		return kor;
	}
	public void setKor(int kor)
	{
		this.kor = kor;
	}
	public int getEng()
	{
		return eng;
	}
	public void setEng(int eng)
	{
		this.eng = eng;
	}
	public int getMat()
	{
		return mat;
	}
	public void setMat(int mat)
	{
		this.mat = mat;
	}
	public int getTot()
	{
		return tot;
	}
	public void setTot(int tot)
	{
		this.tot = tot;
	}
	public int getRank()
	{
		return rank;
	}
	public void setRank(int rank)
	{
		this.rank = rank;
	}
	public double getAvg()
	{
		return avg;
	}
	public void setAvg(double avg)
	{
		this.avg = avg;
	}
	
}
