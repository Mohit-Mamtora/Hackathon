<?php

define('DB_SERVER','localhost');
define('DB_USER','id5037488_root');
define('DB_PASS' ,'nanima2640');
define('DB_NAME', 'id5037488_db1');

$db=mysql_connect('mysql9.000webhost.com','id5037488_root','nanima2640');
mysql_select_db('id5037488_db1', $db);

$result = mysql_query('SELECT * FROM log ',$db);


echo "<table border='1'>
<tr>
<td>Roll</td>
</tr>";

while($row = mysql_fetch_array($result))
  {
  echo "<tr>";
  echo "<td>" . $row['0'] . "</td>";
  echo "</tr>";
  }
echo "</table>";
?>
