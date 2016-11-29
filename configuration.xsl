<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:h="http://www.esei.uvigo.es/dai/hybridserver">

	<xsl:output method="html" indent="yes" encoding="utf-8"/>
	
	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE HTML&gt;</xsl:text>
			<html>
				<head>
					<title>Configuration</title>
				</head>
				<body>
					<h1>Server configuration</h1>
					<h2>Connection</h2>
					<xsl:apply-templates select="h:configuration/h:connections"/>
					<h2>Database</h2>
					<xsl:apply-templates select="h:configuration/h:database"/>
					<h2>Servers</h2>
					<xsl:apply-templates select="h:configuration/h:servers"/>
					
				</body>
			</html>
	</xsl:template>

	<xsl:template match="h:connections">
		<div>
			<xsl:template match="h:webservice">
				<span><b>Webservice: </b> <xsl:value-of select="h:webservice/text()"/></span><br></br>
			</xsl:template>
			<xsl:template match="h:numClients">
				<span><b>N. Clients: </b> <xsl:value-of select="h:numClients/text()"/></span><br></br>
			</xsl:template>
			<xsl:template match="h:http">
				<span><b>Puerto: </b> <xsl:value-of select="h:http/text()"/></span><br></br>
			</xsl:template>
		</div>
	</xsl:template>
	
	<xsl:template match="h:database">
		<div>
			<xsl:template match="h:user/text()">
				<span><b>User: </b> <xsl:value-of select="h:user/text()"/></span><br></br>
			</xsl:template>
			<xsl:template match="h:password">
				<span><b>Password: </b> <xsl:value-of select="h:password/text()"/></span><br></br>
			</xsl:template>
			<xsl:template match="h:url">
				<span><b>Url: </b> <xsl:value-of select="h:url/text()"/></span><br></br>
			</xsl:template>
		</div>
	</xsl:template>
	
	<xsl:template match="h:servers">
		<xsl:for-each select="h:server">
			<div>
				<span><b><xsl:value-of select="@name"/>: </b> <xsl:value-of select="@httpAddress"/></span><br></br>
				<span><b>Wsdl: </b><xsl:value-of select="@wsdl"/></span><br></br>
				<span><b>Namespace: </b><xsl:value-of select="@namespace"/></span><br></br>
				<span><b>Service: </b><xsl:value-of select="@service"/></span><br></br>
			</div>
			<br></br>
		</xsl:for-each>
	</xsl:template>
	
</xsl:stylesheet>