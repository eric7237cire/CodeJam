<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Hands</title>


</head>

<body>

<#list hands as handInfo>

<p>

<pre>
${handInfo.getHandLog()}
</pre>
<a name = "hand_${1+handInfo_index}" />

</p>

</#list>

</body>

</html>