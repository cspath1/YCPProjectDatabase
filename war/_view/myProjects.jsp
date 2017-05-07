<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>

<title>My Projects</title>
<link rel="stylesheet" href="Style.css">
</head>

<body>
	<div id="header">
		<div id="logo">
			<img src="Logo.png">
		</div>
	</div>
	<c:if test="${! empty errorMessage}">
		<div class="error">${errorMessage}</div>
	</c:if>

	<div class="superContainer">
		<form action="${pageContext.servletContext.contextPath}/userSearch" method="post">
		<h1 style = "text-align: center">My Projects</h1>
					
			<table>
				 <c:forEach items="${projects}" var="project">
					<tr class = "projectRow">
						<td class = "title">${project.title}</td>
					</tr>
				</c:forEach>
			</table>
		</form>
	</div>
</body>
</html>