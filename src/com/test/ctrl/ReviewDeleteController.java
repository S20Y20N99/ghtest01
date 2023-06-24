/*==============================
   ReviewDeleteController.java
===============================*/

package com.test.ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.test.logic.StoreModel;

public class ReviewDeleteController extends HttpServlet
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
			String resNum = request.getParameter("resNum");
			
			// Model 객체 연결 → 업무 로직 수행 , View 정보 얻어내기
			StoreModel model = new StoreModel();
			
			// 테이터를 삭제 처리하는 메소드를 request 값들(form에서 submit을 통해 받은 값들)을 보내서 실행시킨다.
			model.actionDelete(request, response);
			
			// 데이터 삭제 처리 후 해당 식당 페이지로 되돌아간다.
			response.sendRedirect("storepage?resNum=" + resNum);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}

	}
	
}
