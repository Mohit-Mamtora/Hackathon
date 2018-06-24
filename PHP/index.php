<?php
	include "connection.php";
	
	  $data_array = array();
	$result=mysqli_query($conn,"select * from log order by latitude desc ");
	//echo"<center><br><table border=1>";
	if($result)
	{
/*		echo"<tr><th>Symbol Id</th><th>Longtitude</th><th>Lattitude</th><th>Side</th><th>City</th><th>Aptitude</th><th></th></tr>";*/
        $temp="";
     //   echo "hello\n";
     
		while ($row=mysqli_fetch_array($result)) {
		    
		    
		        		    echo $row['sym_id'].":".$row['latitude'].":".$row['longitude'].":".$row['altitude'].":".$row['side'].":".$row['city'].":\n";
		        
		    
		     $data_array[] = $row;
	/*		    
			echo "<tr><td>".$row[0]."</td>";
			echo "<td>".$row[1]."</td>";
			echo "<td>".$row[2]."</td>";
			echo "<td>".$row[3]."</td>";
			echo "<td>".$row[4]."</td>";
			echo "<td>".$row[5]."</td>";
		echo"<td><a href=index.php?sym_id=".$row[0].">Delete</a>&nbsp&nbsp&nbsp&nbsp";
			echo"<a href=symbol.php?sym_id=".$row[0].">Update</a></td></tr>";
	
		*/
		}	
		
		//echo	json_encode($data_array);
	//	echo"<a href=symbol.php>Add</a>"	;
	}
//	echo"</table></center>";

	if(isset($_REQUEST['sym_id']))
	{
		$quer="delete from log where sym_id=".$_REQUEST['sym_id'];
		mysql_query($quer);
	}
?>
