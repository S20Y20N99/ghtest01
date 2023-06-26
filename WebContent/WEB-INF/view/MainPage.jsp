<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>쌍용리뷰 : 메인페이지</title>
<link rel="stylesheet" type="text/CSS" href="<%=cp %>/css/style.css">

<script type="text/javascript">

	/* 식당 리스트들을 출력할 때 식당코드를 넘겨주어 web.xml → StoreSendController(resNum)과 함께 → StorePage?resNum=? 꼴로 
		값을 넘겨 특정 가게의 리뷰 출력 페이지를 가지고 오게 한다.  */
	function storePage(i)
	{
		var f = document.myForm;
		/* 전달을 위해 form에서 매개변수로 전달 받은 식당 코드를 담는다.  */
		f.resNum.value = i;
		
		/* 담은 식당 코드를 담아 storePage(StorePage:각 식당 페이지를 불러온다.)  */
		f.action = "storepage";
		f.submit();
		
		//변경사항
		alert("확인");
	}

</script>
	
</head>
<body>

<div id="outerBox">
	
	<div id="titleBox" style="height:42px; margin-top: 40px; margin-bottom : 55px;">
		 <h1 id="resName" style="text-align:center;">SSANGYONG REVIEW</h1>	
	</div>

	<!-- 가게 리스트 출력을 위한 form  -->
	<form action="" name="myForm">	
		<table id="resListTbl" style="width: 800px; border: 1px; border-radius: 10px; background-color: white; margin: auto; text-align: center; font-size: 12pt; margin: auto; margin-top: 30px; margin-bottom: 30px;">
			<tr id="firstRow" style="height: 50px;">
				<th class="img"></th>
				<th class="store">상호명</th>
				<th class="reviewCount">리뷰 수</th>
				<th class="starCount">별점</th>
			</tr>
			
			<!-- 반복문을 통해 Arraylists 타입은 lists의 element 수 만큼 반복 진행시키는 반복문 -->
			<!-- 반복문이 진행되는 동안 출력대상이 되는 식당 정보가 담긴 lists의 요소들을 index란 변수에 담는다.  -->
			<c:forEach var="index" items="${lists }">
			<tr  style="height: 50px;">
				<c:choose>
					<%-- 가게마다 식당코드를 다르게 부여하였기 때문에 각 코드와 이미지를 따로 부여하여서 식당마다
						 각 식당에 맞는 이미지를 출력 시켰다.--%>
					<%-- 가게 정보가 담겨있는 index의 변수들을 불러와 출력 → 가게 정보들(가게 이미지, 가제 이름, 가게 리뷰수, 평점) 들을 출력한다.  --%>
					<c:when test="${index.resNum==10001 }">
						<td style="height:100pt; width: 110pt"><img src="images/gogi.jpg" style="height:80pt; width: 100pt"></td>
					</c:when>
					<c:when test="${index.resNum==10002 }">
						<td style="height:100pt; width: 110pt"><img src="images/chicken_f.jpg"style="height:80pt; width: 100pt"></td>
					</c:when>
					<c:when test="${index.resNum==10003 }">
						<td style="height:100pt; width: 110pt"><img src="images/burger.jpg" style="height:80pt; width: 100pt"></td>
					</c:when>
					<c:when test="${index.resNum==10004 }">
						<td style="height:100pt; width: 110pt"><img src="images/tuna.jpg" style="height:80pt; width: 100pt"></td>
					</c:when>
				</c:choose>
				<td class="store" >
					<%-- 클릭 시 해당 가게의 리뷰 사이트로 이동시키는 기능을 자바스크립의 함수 실행 + form action으로 구현  --%>
					<a href="javascript:storePage(${index.resNum })" >${index.resName }</a>
				</td>
				<td class="reviewCount">${index.reviewCount }
				</td>
				<td class="starCount">${index.starAvg }
				</td>
			</tr>
			</c:forEach>
		</table>
		<input type="hidden" name="resNum">
	</form>	
	
	<%-- 페이징 처리 ex) 1 2 3 4 5 ...  --%>
	<div id="footer" style="text-align: center;">
		<p>
		<c:choose>
		<%-- dao.getStoreCount()로 계산한 dataCount의 값이 있다면 즉, 등록된 가게 데이터가 0일 아닐 경우  가게 수 페이징 처리
			 현재 등록된 가게 수가 많지 않기 때문에 현재 주석처리 하였다.--%>
		<c:when test="${!empty dataCount }">
			<%-- ${pageIndexList } --%>
		</c:when>
		<c:otherwise>
			등록된 가게가 존재하지 않습니다.
		</c:otherwise>
		</c:choose>
		</p>		
	</div><%-- #footer --%>
	
</div><!-- #outerBox -->
			


</body>
</html>