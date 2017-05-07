<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>

		<title>Project Proposal</title>
		<link rel="stylesheet" href="ProposalPage.css">
		<meta name = "viewport" content = "width = device-width initial-scale =1">
		
	</head>
	
	<body>
	<div id="header">
		<div id="logo">
			<img src="Logo.png">
		</div>
	</div>
	
	
	
	
	
	<div class="superContainer">
	<form action="${pageContext.servletContext.contextPath}/projectProposal" method="post">
	<h1 style = "text-align: center">My Proposal</h1>
		<label><a>Project Title:</a></label><br>
		<p>${project.title}</p><br><br>
		
		<label><a>Description:</a></label>
		<p>${project.description}</p><br><br>
		
		<label><a>Duration (Semesters):</a></label><br>
		<p>${project.duration}</p><br><br>
		
		<label><a>Start Date:</a></label><br>
		<p>${project.date}</p><br><br>
		
		<label><a>Deadline:</a></label><br>
		<p>${project.deadline}</p><br><br>
		
		<label><a>Funding:</a></label><br>
		<p>${project.funding}</p><br><br>
		
		<labeL><a>Number of Students:</a></labeL><br>
		<p>${project.numStudents}</p><br><br>
			
		<labeL><a>Majors Involved</a></labeL><br>
		<p>${project.majors}</p><br><br>
			
		<label><a>Student Year(s)</a></label><br>
		<p>${project.year}</p><br><br>
			
		<label><a>Cost:</a></label><br>
		<p>${project.cost}</p><br><br>
				
		</form>
		</div>
	</body>
</html>