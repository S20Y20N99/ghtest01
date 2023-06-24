/*============================
    StoreSendController.java
==============================*/

package com.test.ctrl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.logic.StoreModel;

public class StoreSendController extends HttpServlet
{
private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGetPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGetPost(request, response);
	}
	
	protected void doGetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 서블릿 관련 코딩
		request.setCharacterEncoding("UTF-8");
		
		try
		{
			// Model 객체 연결 → 업무 로직 수행, View 정보 얻어내기
			StoreModel sm = new StoreModel();
			
			// 데이터 처리 후 이동할 페이지(Store.page)의 주소를 result에 담는다 
			String result = sm.reviewList(request, response);
			
			// 주석
			//request.setAttribute("result", result);
			//request.getAttribute("lists");
			
			// StorePage.jsp 로 안내
			RequestDispatcher dispatcher = request.getRequestDispatcher(result);
			// sm.reviewList 메소드를 진행하면서 request에 담았던 pageIndexList, lists, resNum, dataCount 들을 같이 보낸다.
			dispatcher.forward(request, response);
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
	}
	
}
