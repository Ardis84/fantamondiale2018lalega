<?php

//include 'formazioni.php';

$content = file_get_contents("php://input");
$update = json_decode($content, true);

if(!$update)
{
  exit;
}

$message = isset($update['message']) ? $update['message'] : "";
$messageId = isset($message['message_id']) ? $message['message_id'] : "";
$chatId = isset($message['chat']['id']) ? $message['chat']['id'] : "";
$firstname = isset($message['chat']['first_name']) ? $message['chat']['first_name'] : "";
$lastname = isset($message['chat']['last_name']) ? $message['chat']['last_name'] : "";
$username = isset($message['chat']['username']) ? $message['chat']['username'] : "";
$date = isset($message['date']) ? $message['date'] : "";
$text = isset($message['text']) ? $message['text'] : "";

$text = trim($text);
$text = strtolower($text);


$myfile = fopen("newfile.txt", "w") or die("Unable to open file!");
$txt = $chatId."\n";
fwrite($myfile, $txt);
fclose($myfile);

header("Content-Type: application/json");
$response = '';
/*if(strpos($text, "/start") === 0 || $text=="ciao")
{
	$response = "Ciao $firstname, bentornato/a!";
}elseif($text=="/formazioni"){
	$response = "certo, di che squadra?";
}elseif($text=="/formazione" ||  strpos($text, 'dammi formazione') !== false){
	$response = getFormazioneByTeam('amodeo');
}elseif($text=="mondiale" || $text=="mondiali" || $text=="Mondiali" || $text=="Mondiale" ){
	$response = "http://www.sportmediaset.mediaset.it/mondiali-russia-2018/fifa/";
}elseif($text=='/piu@FantaMondiale2018LaLegaBot'){
	$response = "$firstname, sei un idiota!!";
}

/*if($chatId == '122135009'){
	$response = getFormazioni();
}*/

$response =$chatId;

/*
$parameters = array('chat_id' => $chatId, "text" => $text);
$parameters["method"] = "sendMessage";
echo json_encode($parameters);*/

$parameters = array('chat_id' => $chatId, "text" => $response);
$parameters["method"] = "sendMessage";
echo json_encode($parameters);
