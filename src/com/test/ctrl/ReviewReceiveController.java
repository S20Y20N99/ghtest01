/*=================================
   ReviewReceiveController.java
==================================*/

package com.test.ctrl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.test.logic.StoreModel;

public class ReviewReceiveController extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		process(request, response);
	}
	
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 서블릿 관련 코딩
		request.setCharacterEncoding("UTF-8");
		
		try
		{
			
			// Model 객체 연결 → 업무 로직 수행 , View 정보 얻어내기
			StoreModel model = new StoreModel();
			
			// 입력 처리가 제대로 진행되었는지 확인할 반환 값을 action에 저장한다.
			int action = model.actionLogic(request, response);
			
			String resNum = request.getParameter("resNum");
		
			// 입력이 제대로 진행되지 않았다면 (영수증 번호가 존재하지 않는 번호라면)
			if (action==0)
			{
				// (예외 처리로 한글을 출력하기 위한 printWriter 생성과 인코딩 설정)
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter writer = response.getWriter();
				writer.println("<script>alert('제대로 된 영수증 번호를 입력하세요.'); location.href='storepage?resNum="+resNum+"';</script>"); 
				writer.close();
				// 입력 과정을 강제 중지시킨다.
				return;
			}
			
			// 확인
			//PrintWriter out = response.getWriter();
			//out.print(resNum);
			
			// View 객체 연결
			// 입력 처리 후 해당 식당 페이지로 귀환시킨다.
			response.sendRedirect("storepage?resNum=" + resNum);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
}
