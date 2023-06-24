/*====================
   ReviewDAO.java
=====================*/

package com.test.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.util.DBConn;

public class ReviewDAO
{
	private Connection conn;
	
	// 데이터베이스 연결 메소드 정의 → 생성자 형태로 정의
	public ReviewDAO() throws ClassNotFoundException, SQLException
	{
		conn = DBConn.getConnection();
	}
	
	// 전체 가게 갯수 구하는 메소드
	public int getStoreCount()
	{
		int result=0;
		
		PreparedStatement pstmt = null;
		String sql = "";
		ResultSet rs = null;
		
		try
		{
			sql = "SELECT COUNT(*) AS COUNT FROM TBL_RESTAURANT";
			pstmt = conn.prepareStatement(sql);
		
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				result = rs.getInt("COUNT");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 가게 출력 메소드 (메인페이지에 보여주는 리스트)
	public ArrayList<ReviewDTO> getStoreLists()
	{
		ArrayList<ReviewDTO> result = new ArrayList<ReviewDTO>();
		
		PreparedStatement pstmt = null;
		String sql="";
		ResultSet rs = null;
		
		try
		{			
			sql = "SELECT A.NUM AS RESNUM, A.NAME AS NAME, COUNT(*) AS COUNT, ROUND(NVL(AVG(B.STAR),0),1) AS AVG"
					+ " FROM TBL_RESTAURANT A LEFT JOIN TBL_REVIEWS B ON A.NUM = B.RESNUM"
					+ " GROUP BY (A.NAME, A.NUM)";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				ReviewDTO dto = new ReviewDTO();
				
				dto.setResNum(rs.getInt("RESNUM"));
				dto.setResName(rs.getString("NAME"));
				dto.setReviewCount(rs.getInt("COUNT"));
				dto.setStarAvg(rs.getDouble("AVG"));
				
				result.add(dto);
			}
			
			rs.close();
			pstmt.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 해당 가게의 전체 리뷰 갯수 구하는 메소드
	public int getReviewCount(int resNum)
	{
		int result = 0;
		
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			// 쿼리문 준비
			sql = "SELECT COUNT(*) AS COUNT FROM TBL_REVIEWS A LEFT JOIN TBL_RESTAURANT B ON A.RESNUM = B.NUM WHERE A.RESNUM = ?";
			// statement 객체 준비
			pstmt = conn.prepareStatement(sql);
			
			// 변수 값 set
			pstmt.setInt(1, resNum);
			
			// statement DB 전달
			rs = pstmt.executeQuery();
			
			// 받아 온 값 담기
			if (rs.next()) // 단일 값이므로 if 로 처리
				result = rs.getInt("COUNT");

			// 사용 객체 반납
			rs.close();
			pstmt.close();

		} 
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 해당 가게의 리뷰 리스트를 가져오는 메소드 
	public ArrayList<ReviewDTO> getLists(int resNum, int start, int end)
	{
		ArrayList<ReviewDTO> result = new ArrayList<ReviewDTO>();
		
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			sql = "SELECT NUM, RESNUM, UNAME, UPWD, RECEIPT, CREATED, CONTENT, STAR"
					+ " FROM (SELECT ROWNUM AS RNUM, T.*"
					+ " FROM (SELECT A.NUM AS NUM , B.NUM AS RESNUM, A.UNAME AS UNAME, A.UPWD AS UPWD, A.RECEIPT AS RECEIPT"
					+ ", A.CREATED AS CREATED, A.CONTENT AS CONTENT, A.STAR AS STAR"
					+ " FROM TBL_REVIEWS A LEFT JOIN TBL_RESTAURANT B"
					+ " ON A.RESNUM = B.NUM WHERE A.RESNUM = ?"
					+ " ORDER BY NUM DESC) T) S"
					+ " WHERE RNUM>=? AND RNUM<=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, resNum);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				ReviewDTO dto = new ReviewDTO();
				dto.setNum(rs.getInt("NUM"));
				dto.setResNum(rs.getInt("RESNUM"));
				dto.setUserName(rs.getString("UNAME"));
				dto.setUserPwd(rs.getString("UPWD"));
				dto.setReceipt(rs.getLong("RECEIPT"));
				dto.setCreated(rs.getString("CREATED"));
				dto.setContent(rs.getString("CONTENT"));
				dto.setStar(rs.getInt("STAR"));
				
				result.add(dto);
			}
			
			rs.close();
			pstmt.close();
		} 
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}

	// 해당 가게의 리뷰수, 별점평균, 가게명을 가져오는 메소드
	public ReviewDTO getResNum(int resNum)
	{
		ReviewDTO dto = null;
		
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			sql = "SELECT T.RESNAME AS RESNAME, COUNT(*) AS COUNT, ROUND(NVL(AVG(STAR),0),1) AS AVG"
					 + " FROM (SELECT A.NUM AS NUM, A.NAME AS RESNAME, B.STAR AS STAR"
					 + " FROM TBL_RESTAURANT A JOIN TBL_REVIEWS B ON A.NUM = B.RESNUM )T"
			  		 + " GROUP BY (T.NUM, T.RESNAME)  HAVING T.NUM=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, resNum);
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				dto = new ReviewDTO();
				
				dto.setReviewCount(rs.getInt("COUNT"));
				dto.setStarAvg(rs.getDouble("AVG"));
				dto.setResName(rs.getString("RESNAME"));
				
			}
			
			rs.close();
		  	pstmt.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		return dto;
	}
		

	// 식당 번호로 POS 번호 가져오기
	public long posCode(int resNum)
	{
		long result = 0;
		
		String sql ="";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			sql="SELECT POS FROM TBL_RESTAURANT WHERE NUM = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, resNum);
			
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				result = rs.getLong("POS");
			}
			
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 게시물 최댓값 확인
	public int getMax()
	{
		int result = 0;
		
		String sql="";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			sql ="SELECT MAX(NUM) AS MAXNUM FROM TBL_REVIEWS";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				result = rs.getInt("MAXNUM");
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 게시물 작성 → 데이터 입력
	public int insertData(String resNumStr, int star, long receipt, String userName, String userPwd, String content)
	{
		int result=0;
		
		PreparedStatement pstmt = null;
		String sql = "";
		
		int resNum = Integer.parseInt(resNumStr);
		
		// 영수증 번호에서 앞 6자리만 추출한다 → 영수증을 발부한 식당의 포스번호
		String receptNum = String.valueOf(receipt).substring(0, 6);
		
		// 식당 페이지에서 넘거온 식당의 포스번호와 사용자가 입력한 영수증의 포스번호가 일치하지 않는다면 
		if (posCode(resNum) != Integer.parseInt(receptNum))
		{
			// 입력 메소드 강제 중지
			return 0;
		}
		
		// 리뷰 번호 얻어오기
		int num = getMax()+1;
		
		// 일치한다면 아래 쿼리문대로 실행
		try
		{
			sql = "INSERT INTO TBL_REVIEWS(NUM,RESNUM,UNAME,UPWD,STAR,RECEIPT,CREATED,CONTENT) VALUES(?, ?, ?, ?, ?, ?, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);
			// 1 리뷰번호 2 식당번호 3 사용자명 4 비밀번호 5 별점 6 영수증 7 내용
			pstmt.setInt(1,  num);
			pstmt.setInt(2,  resNum);
			pstmt.setString(3, userName); 
			pstmt.setString(4, userPwd);
			pstmt.setInt(5, star);
			pstmt.setLong(6, receipt);
			pstmt.setString(7, content);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 게시물 삭제 → 데이터 삭제
	public int removeData(int num, String pwd)
	{
		int result = 0;
		
		String sql = "";
		PreparedStatement pstmt = null;
		
		try
		{
			sql = "DELETE FROM TBL_REVIEWS WHERE NUM = ? AND UPWD = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, pwd);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
		} 
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 데이터베이스 종료 메소드 정의
	public void close() throws SQLException
	{
		DBConn.close();
	}
}
