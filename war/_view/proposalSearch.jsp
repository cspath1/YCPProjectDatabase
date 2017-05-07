<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
	<title>Proposed Project Search</title>
	<link rel="stylesheet" href="Search.css">
	<meta name="viewport" content="width = device-width initial-scale=1">
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
	<form action="${pageContext.servletContext.contextPath}/proposalSearch" method="post">
	<h1 style = "text-align: center">Proposed Project Search</h1>
		<label><a>Select a field to search by:</a></label><br>
		<select name="attribute">
				<option value="title"selected>Title</option>
				<option value="description">Description</option>
				<option value="start">Start Date</option>
				<option value="duration">Duration</option>
				<option value="deadline">Deadline</option>
				<option value="major">Major</option>
				<option value="class">Class</option>
				<option value="numStudents">Number of Students</option>
				<option value="isFunded">Funding Availability</option>
				<option value="cost">Cost</option>
				
		<input type="text" name="keyword"><br><br>
		
		</select>
		<table>
			 <c:forEach items="${projects}" var="project">
				<tr class = "projectRow">
					<td class = "Title">${project.title}</td>
				</tr>
			</c:forEach>
		</table>
			
		<input type="Submit" name="submit" value="Search" class = "button button5">
		</form>
		</div>
	</body>
</html>