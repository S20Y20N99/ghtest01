/*====================
   ReviewDTO.java
=====================*/

package com.test.logic;

public class ReviewDTO
{
	private int num, resNum, star,  reviewCount;
	private long receipt;
	private String resName, userName, userPwd, content, created;
	private double starAvg;
	
	public String getResName()
	{
		return resName;
	}
	public void setResName(String resName)
	{
		this.resName = resName;
	}
	public int getReviewCount()
	{
		return reviewCount;
	}
	public void setReviewCount(int reviewCount)
	{
		this.reviewCount = reviewCount;
	}
	public double getStarAvg()
	{
		return starAvg;
	}
	public void setStarAvg(double starAvg)
	{
		this.starAvg = starAvg;
	}
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	public int getResNum()
	{
		return resNum;
	}
	public void setResNum(int resNum)
	{
		this.resNum = resNum;
	}
	public int getStar()
	{
		return star;
	}
	public void setStar(int star)
	{
		this.star = star;
	}
	public long getReceipt()
	{
		return receipt;
	}
	public void setReceipt(long receipt)
	{
		this.receipt = receipt;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getUserPwd()
	{
		return userPwd;
	}
	public void setUserPwd(String userPwd)
	{
		this.userPwd = userPwd;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getCreated()
	{
		return created;
	}
	public void setCreated(String created)
	{
		this.created = created;
	}
}
