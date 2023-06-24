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
<title>쌍용리뷰 : ${dto.resName }</title>
<link rel="stylesheet" type="text/CSS" href="<%=cp %>/css/style.css">

<style type="text/css">

 body {text-align: center;}
 .reviewInsert {text-align: center;}
 #reviewList {margin: auto;}

</style>

<script type="text/javascript">
	
	/* 페이징을 처리하는 form의 action을 위해 만든 function   */
	function storePage(i)
	{
		f = document.footerForm;
		
		/* footerForm 안에 페이지넘버의 value를 넘겨받은 후  */
		f.pageNum.value=i;
		
		f.action = "pagenum";
		
		f.submit();
	}
	/*  입력되어있는 리뷰를 삭제하기 위한 메소드 『삭제』 버튼을 눌렀을 때 실행된다.』*/
	function reviewDelete(i)
	{
		f = document.listForm;

		//alert(f.elements['pwBox'+i].value);
		
		// 기존의 비밀번호가 담겨있는 pwBox의 값과 새로 입력한 uPwd의 값이 불일치한 경우 return 으로 삭제 과정 강제 중지  
		if (f.elements['uPwd'+i].value != f.elements['pwBox'+i].value || f.elements['pwBox'+i]==null)
		{
			alert("\n올바른 비밀번호를 입력하세요");
			f.elements['pwBox'+i].focus();
			return;
		}
		
		// 비밀번호가 일치한 경우 삭제를 진행할 것인지 확인
		var res = confirm("정말 삭제하시겠습니까?");
		
		
		// 아닌 경우 삭제 과정 강제 중지
		if (res==false)
		{
			return;
		}

		// 삭제에 동의한 경우 삭제 진행을 위한 reviewDelete(ReviewDeleteContoller → StoreModel.actionDelete 메소드 진행)
		f.action = "reviewDelete";
		// action=""와 submit을 통해 삭제 후 기존 페이지로 다시 돌아온다
		f.submit();
	}

</script>

</head>
<body>

<div id="outerBox"> 

	<div>
		<h1 style="color:white;">[ ${dto.resName } ]</h1>
	</div>

	<div id="titleBox" style="height: 60px;">
	   <!-- 해당 가게의 평점을 페이지 상단에 보여줄수 있도록   -->
	   <h2 id="star">평점 : 
	   <c:choose>
            <c:when test="${dto.starAvg==5}">
               ★★★★★
            </c:when>
            <c:when test="${dto.starAvg<5 && dto.starAvg>4}">
               ★★★★☆   
            </c:when>
            <c:when test="${dto.starAvg==4}">
               ★★★★
            </c:when>
            <c:when test="${dto.starAvg<4 && dto.starAvg>3}">
               ★★★☆   
            </c:when>
            <c:when test="${dto.starAvg==3}">
               ★★★
            </c:when>
            <c:when test="${dto.starAvg<3 && dto.starAvg>2}">
               ★★☆   
            </c:when>
            <c:when test="${dto.starAvg==2}">
               ★★
            </c:when>
            <c:when test="${dto.starAvg<2 && dto.starAvg>1}">
               ★☆   
            </c:when>
            <c:when test="${dto.starAvg==1}">
               ★
            </c:when>
            <c:when test="${dto.starAvg<1 && dto.starAvg>0}">
               ☆   
            </c:when>
            <c:otherwise>
               ☆☆☆☆☆
            </c:otherwise>
      </c:choose>
	   (${dto.starAvg}) </h2>
	   <br>
	   <h3 id="reviewCnt">총 리뷰 수 : ${dto.reviewCount }</h3>
	</div>
	
	<br><br>

	<div id="reviewInsert">
		<h2 id="titleInsert" style="color:white;"> 리뷰 등록 </h2>
			<br>
			<!-- 데이터(리뷰) 입력을 위한 form  입력 버튼을 누르면 web.xml → ReviewReceiveController 를 통해 dao의 isertData로 값을 입력한다 -->
			<form action="reviewreceive" name="reviewForm" method="post">
				<select name="star">			
					<option value="0" >☆☆☆☆☆</option>
					<option value="1" >★☆☆☆☆</option>
					<option value="2" >★★☆☆☆</option>
					<option value="3" >★★★☆☆</option>
					<option value="4" >★★★★☆</option>
					<option value="5" >★★★★★</option>
				</select>
				<input type="text" name="receipt" id="recNum" placeholder= "영수증 번호" style="width: 100px;">
				<input type="text" name="userName" id="name" placeholder= "닉네임(3글자)">
				<input type="text" name="userPwd" id="pw" placeholder= "비밀번호">
				<input type="text" name="content" id="review" placeholder="리뷰를 작성해주세요.">
				<button type="submit" name="comInsertBtn" class="btn">등록하기</button>
				
				<!-- resNum을 form action시 같이 넘기기 위해 hidden 처리하여 담아둔다. -->
				<input type="hidden" name="resNum" value="${resNum }">
			</form>
	</div><!-- #reviewInsertBox -->

	<br><br>

	<div id="reviewListBox"	>
		<h2 id="titleList"> 리뷰 목록 </h2>
		<br>
		<form action="" name="listForm" method="post">
			<table id="reviewList">
				<tr id="firstRow" style="height: 40px; font-size: 10pt;">
					<th class="starCount">별점</th>
					<th class="uName">사용자이름</th>
					<th class="reviewCon">리뷰내용</th>
					<th class="created">작성일</th>
					<th class="remove"></th>
				</tr>
				<c:set var="i" value="1"></c:set>	<%-- name 속성 값을 변경할 수 있는 변수 설정 --%>
				<c:forEach var="review" items="${lists }">
				<!-- 초기값이 1인 변수 i를 lists의 요소만큼 반복문 설정  -->
				<tr style="height: 35px;">
					<!-- 식당별 데이터들을 가지고 있는 list 하나 하나를 review에 저장한 다음
						 각 리뷰에 있는 데이터들을 출력한다.(반복문동안)  -->
					<td class="starCount">${review.star }</td>	
					<td class="uName">${review.userName }</td>	
					<td class="reviewCon">${review.content }</td>	
					<td class="created">${review.created }</td>
					<td class="remove">
					
						<!-- i는 각 식당 페이지에서 각 리뷰사이의 식별자 역할을 한다.  -->
						<input type="text" class= "pwBox" name="pwBox${i }" placeholder="비밀번호 입력" style="width: 100px;"> <!-- 추후에 text가 아닌 password 박스로 -->
						
						<!-- 삭제 버튼 클릭시 리뷰들 사이의 식별자를 매개변수로 담은 reVieDelete(i)를 실행시킨다.-->
						<button type="submit" class="btn" onclick="reviewDelete(${i})">삭제</button>
						
						<!-- 삭제를 입력한 비밀번호가 기존 리뷰 생성시 비밀번호와 같은지 확인하기 위해 
							 기존 비밀번호 값을 hidden 처리하여 request에 담는다.-->
						<input type="hidden" name="uPwd${i }" value="${review.userPwd}" />
						<input type="hidden" name="num${i }" value="${review.num}" />
						<input type="hidden" name="resNum" value="${resNum}" />
						
						<%-- ${i } 1 부터 10까지 숫자 확인 --%>
						<c:set var="i" value="${i +1}"></c:set>	<%-- 값을 증가 시키는 구문 --%>
					</td>
				</tr>
				</c:forEach>				
			</table>
		</form>		
	</div><!-- #reviewListBox -->
	<br>
	<div id="footer" style="text-align: center;">
	<form action="" name="footerForm" method="post">
		<c:choose>
			<c:when test="${!empty dataCount }">
				<!-- StoreModel.java 에서 가지고온 String 변수 pageIndexList를 출력(페이징 현재 페이지와 이동할 페이지 출력) ex) 1 2 3 4 5... -->
				<p id="pageNums">${pageIndexList }</p>
			</c:when>
			<c:otherwise>
				<p>리뷰가 존재하지 않습니다.</p>
			</c:otherwise>
		</c:choose>
		<input type="hidden" name="pageNum">
		<input type="hidden" name="resNum" value="${resNum }">
	</form>
		<br><br>
		<button class="btn" id="backToMain" onclick="location='store'" style="color:white;">메인 화면으로</button>
	</div><!-- #footer -->

</div><!-- #outerBox -->

</body>
</html>