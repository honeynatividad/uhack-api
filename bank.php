<?php

$method = $_SERVER['REQUEST_METHOD'];
$request = explode('/', trim($_SERVER['PATH_INFO'],'/'));
$input = json_decode(file_get_contents('php://input'),true);
 
// connect to the mysql database
$link = mysqli_connect('localhost', 'philipp4_uhack', 'deatheater2493', 'philipp4_uhack');
//$link = mysqli_connect('localhost', 'root', '', 'UHack');
mysqli_set_charset($link,'utf8');
$table = preg_replace('/[^a-z0-9_]+/i','',array_shift($request));
$key = array_shift($request)+0;

//$columns = preg_replace('/[^a-z0-9_]+/i','',array_keys($input));
$set = '';
for ($i=0;$i<count($input);$i++) {
  $set.=($i>0?',':'').'`'.$input[$i].'`=';
  $set.=($values[$i]===null?'NULL':'"'.$values[$i].'"');
}
//$query = "SELECT wp_posts.guid FROM wp_posts INNER JOIN wp_postmeta ON wp_postmeta.post_id = wp_posts.ID WHERE wp_posts.post_id=".$table." AND wp_postmeta.meta_key='_thumbnail_id'";

//$query = "SELECT wp_postmeta.meta_value FROM wp_postmeta WHERE wp_postmeta.post_id=".$table ." AND wp_postmeta.meta_key='_thumbnail_id'";
//$res = mysqli_query($link,$query);
//while($row = mysqli_fetch_array($res)){
//	$post_image = $row['meta_value'];
//}

//$query2 = "SELECT wp_posts.guid FROM wp_posts WHERE wp_posts.ID=".$post_image."";
// create SQL based on HTTP method
switch ($method) {
  case 'GET':
	$sql = "";
	break;
    //$sql = "select * from `$table`".($key?" WHERE id=$key":''); break;
  case 'PUT':
    $sql = ""; 
	break;
  case 'POST':
	$account_no = $_GET['account_no'];
	$pin_code = $_GET['pin_code'];
	
	break;
  case 'DELETE':
    $sql = ""; break;
}
 
// excecute SQL statement

 
// print results, insert id or affected row count
if ($method == 'GET') {
	$arr = array();
	$allBank = mysqli_query($link,"SELECT * FROM bank WHERE status = '1'");
	if (!$key) echo '[';
		$i=0;
		while($listProducts = mysqli_fetch_array($allBank)){
			$account_no = $listProducts['account_no'];
			$pin_code = $listProducts['pin_code'];
			
			$arr['account_no'] = $account_no;
			$arr['pin_code'] = $pin_code;
			
			echo ($i>0?',':'').json_encode($arr,TRUE);
			$i++;
		}
		if (!$key) echo ']';
	
	
	
  /*if (!$key) echo '[';
  for ($i=0;$i<mysqli_num_rows($result);$i++) {
    echo ($i>0?',':'').json_encode(mysqli_fetch_object($result));
  }
  if (!$key) echo ']';*/
} elseif ($method == 'POST') {
	$insertBank = "INSERT INTO bank ('account_no','pin_code','status') VALUES ('$account_no','$pin_code','1')";	
	echo mysqli_insert_id($insertBank);
} else {
  echo mysqli_affected_rows($link);
}
 
// close mysql connection
mysqli_close($link);