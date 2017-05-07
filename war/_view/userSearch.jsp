<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
	<title>User Search</title>
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
	<form action="${pageContext.servletContext.contextPath}/userSearch" method="post">
	<h1 style = "text-align: center">User Search</h1>
		<label><a>Select a field to search by:</a></label><br>
		<select name="attribute">
				<option value="username"selected>Username</option>
				<option value="email">Email</option>
				<option value="majortype">Major Type</option>
				<option value="usertype">User Type</option>
				<option value="firstname">First Name</option>
				<option value="lastname">Last Name</option>
				<option value="name">Business Name</option>
				<option value="address">Business Address</option>
				<option value="number">Business Number</option>
				
		<input type="text" name="keyword">
			
		</select>
		<table>
			 <c:forEach items="${users}" var="user">
				<tr class = "userRow">
					<td class = "username">${user.username}</td>
				</tr>
			</c:forEach>
		</table>
		<input type="Submit" name="submit" value="Search" class = "button button5">
	</form>
	</div>
	</body>
</html>