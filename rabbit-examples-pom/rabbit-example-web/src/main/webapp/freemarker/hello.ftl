<html>
    <head>
        <title>ftl模板</title>
        <link rel="shortcut icon" href="<@contextPath/>/favicon.ico"/>
    </head>
    <body>
        <h1>Welcome ${user}!</h1>
        <p>items:<br/>
            <#list items as item>
                ${item}<br/>
            </#list>
    </body>
</html>