<html>
	<head>
		<title>ftl模板</title>
	</head>
	<body>
		<h1>Welcome ${user}!</h1>
		<p>items:<br/>
			<#list items as item>
			${item}<br/>
			</#list>
	</body>
</html>