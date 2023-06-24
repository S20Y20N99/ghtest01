package com.test.logic;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.MyUtil;

public class StoreModel
{	
	// 가게 리스트 담아서 주소값과 함께 넘겨주는 것에 사용하는 메소드
	public String list(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException
	{
		// 메소드 실행을 위한 dao 인스턴스 생성
		ReviewDAO dao = new ReviewDAO();

		String result = "";
		
		// dataCount 라는 변수에 전체 가게의 수 값을 반환해주는 메소드 실행
		int dataCount = dao.getStoreCount();
		
		// 가게 리스트 가져와서 DTO 데이터를 다루는 ArrayList 타입에 가게 리뷰를 조회하는 메소드의 반환값들(리뷰 리스트들)을 받아 list에 넣는다.
		ArrayList<ReviewDTO> lists = dao.getStoreLists();
		
		// StoreModel 을 통해 데이터를 처맇나 후 이동한 페이지 반환
		result = "/WEB-INF/view/MainPage.jsp";
		
		// 이 페이지 내의 html 영역에서 사용할 수 있도록 변수를 setAttribute 처리한다.
		request.setAttribute("lists", lists);
		request.setAttribute("dataCount", dataCount);
		
		dao.close();
		
		return result;
	}
	
	
	// StorePage.jsp 에서 각 가게에 해당하는 리뷰들을 보여주기 위한 메소드
	public String reviewList(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException
	{		
		// 이전 페이지로부터 넘어온 해당 가게 번호
		int resNum = Integer.parseInt(request.getParameter("resNum"));
		
		// 이전 페이지(?)로부터 넘어온 페이지 번호 수신
		String pageNum = request.getParameter("pageNum");
		int currentPage = 1;
		
		// 이전 페이지로부터 넘어온 페이지 값이 없다면 → 최초 실행이라면 
		if(pageNum != null)
			currentPage = Integer.parseInt(pageNum);
		
		ReviewDAO dao = new ReviewDAO();
		MyUtil myUtil = new MyUtil();
		
		// 전체 데이터 갯수 구하기 (식당 고유 번호로 해당 식당의 리뷰 수를 dataCount에 저장
		int dataCount = dao.getReviewCount(resNum);
		
		// 한 페이지당 게시글 수(10)와 해당 식당의 리뷰수(dataCount)로 총 페이지 수 계산
		int numPerPage = 10;
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);
		
		// 확인
		//System.out.println("totalPage : " + totalPage);
		//System.out.println("currentPage : " + currentPage);
		
		// 전체 페이지 수 보다 표시할 페이지가 큰 경우
		// 표시할 페이지를 전페 페이지로 처리
		// → 데이터를 삭제해서 페이지가 줄어들었을 경우...
		if (currentPage > totalPage)
			currentPage = totalPage;
		
		// 데이터베이스에서 가져올 시작과 끝 위치
		int start = (currentPage-1) * numPerPage + 1;
		int end = currentPage * numPerPage;

		// 확인
		//System.out.println("start: " + start);
		//System.out.println("end : " + end);
		
		// 실제 리스트 가져오기
		// 식당 코드를 기준으로 10개씩 리뷰를 끊어서 출력하기 위한 reviewLists 변수에 값 저장
		ArrayList<ReviewDTO> reviewLists = dao.getLists(resNum, start,  end);
		
		// 페이징 처리
		String listUrl = "/WEB-INF/view/StorePage.jsp?resNum=" + resNum + "&pageNum=" + pageNum;
		String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);
		
		// 데이터를 전송할 컨트롤러에 pageIndexList, lists, resNum, dataCount 를 request.setAttribute 에 실어서 보낸다.
		request.setAttribute("pageIndexList", pageIndexList);
		request.setAttribute("lists", reviewLists);
		request.setAttribute("resNum", resNum);
		request.setAttribute("dataCount", dataCount);
		
		// 데이터를 전송할 컨트롤러에  식당 코드로 알아낸 리뷰 리스트들을 담은 ArrayLists 타입인 dto 를 request.setAttribute 에 실어서 보낸다. 
		ReviewDTO dto = dao.getResNum(resNum);
		
		request.setAttribute("dto", dto);
		
		dao.close();

		return listUrl;
	}
	
	// 해당 가게의 리뷰를 등록하기 위한 메소드
	public int actionLogic(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ClassNotFoundException, SQLException
	{
		request.setCharacterEncoding("UTF-8");
		
		int star = Integer.parseInt(request.getParameter("star"));
		long receipt = Long.parseLong(request.getParameter("receipt"));
		
		String userName = request.getParameter("userName");
		String userPwd = request.getParameter("userPwd");
		String content = request.getParameter("content");
	
		String resNum = request.getParameter("resNum");
	
		//메소드 사용을 위한 DAO 인스턴스 생성
		ReviewDAO dao = new ReviewDAO();
		
		// 데이터 입력 수행
		int result = dao.insertData(resNum, star, receipt, userName, userPwd, content);
		
		dao.close();
		
		return result;
	}

	
	// 해당 가게의 리뷰를 삭제하기위한 메소드
	public void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException
	{
		String pwBoxStr = "";
		String numStr = "";
		
		// 값이 들어있는 pwBox 찾기
		for (int i = 1; i <= 10; i++)
		{
			String checkBoxNum = "pwBox" + i;
			
			// 입력한 비밀번호가 존재하는 즉, 삭제하기 위한 비밀번호가 입력된 게시물을 찾는다
			if (!request.getParameter(checkBoxNum).equals(""))
			{
				//System.out.println(checkBoxNum);
				pwBoxStr = request.getParameter(checkBoxNum);
				
				// 리뷰 고유 번호로 삭제를 진행시키기 위해 StorePage.jsp 삭제 버튼에서 받아왔던 리뷰 고유 번호를 가지고온다.
				String checkNum = "num" + i;
				numStr = request.getParameter(checkNum);
				break;
			}
		}
		
		String pwBox = pwBoxStr;
		
		int  num= 0;
		
		try
		{	
			// dao 에서 removeData(num, pwBox)의 num이 정수타입의 매개 변수이기 때문에 정수 타입으로 형변환을 진행시켜준다.
			num = Integer.parseInt(numStr);
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		// 메소드 사용을 위한 DAO 인스턴스 생성
		ReviewDAO dao = new ReviewDAO();
		
		//확인
		//System.out.println("pwBox : " + pwBox);
		//System.out.println("num : "+num);
		
		// 쿼리문이 담겨있는 dao.removeData로 데이터 삭제 수행
		dao.removeData(num, pwBox);
		
		dao.close();
		
	}
	
}
