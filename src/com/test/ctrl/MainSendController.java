/*============================
    MainSendController.java
=============================*/

package com.test.ctrl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.logic.StoreModel;

public class MainSendController extends HttpServlet
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
			// Model 객체 연결 → 업무 로직 수행 , View 정보 얻어내기
			StoreModel sm = new StoreModel();
			// 전체 가게 수(dataCount)와 전체 가게 리스트(lists)를 불러오는(request.setAttribute 시키는) 메소드 실행시키고
			// 다음으로 이동할 주소를 result 변수에 저장한다.
			String result = sm.list(request, response);
			
			// 주석
			//request.setAttribute("result", result);
			
			// MainPage.jsp 로 안내
			RequestDispatcher dispatcher = request.getRequestDispatcher(result);
			// request에 저장된 값들도 같이 보낸다.
			dispatcher.forward(request, response);
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
}
