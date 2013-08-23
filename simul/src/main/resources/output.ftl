<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Title of document</title>
</head>

<body>

<table border="1">
  <tr>
    <th>Month</th>
    <th>Savings</th>
  </tr>
  
    
    <#list stats.currentPlayerList as playerName>
    <tr>
    <td>${playerName}</td>
    <#assign pStat=stats.playerSessionStats[playerName]/>
    <td>${pStat.totalHands}</td>
    <td>${pStat.stats.vpip}</td>
    </tr>
</#list>
  
</table>

</body>

</html>
