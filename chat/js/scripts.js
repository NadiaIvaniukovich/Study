var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var nickname;

var theMsg = function(name, text) {
	return {
		name:nickname,
		description:text,
		id: uniqueId()
	};
};

var msgList = [];

function run(){
	var appContainer = document.getElementsByClassName('chat')[0];
	
	appContainer.addEventListener('click', delegateEvent);
	
	nickname = restoreName()||"Гость";
	setCurrentNickname();
	var allMsgs = restoreHistory();
	createAllMsgs(allMsgs);
	
}

function createAllMsgs(allMsgs) {
	for(var i = 0; i < allMsgs.length; i++)
		addMsg(allMsgs[i]);
}

function setCurrentNickname(){
	var name = document.getElementsByClassName('nickname')[0];
	if(nickname == ''){
		return;
	}
	name.innerText = nickname;
}

function addMsg(msg) {
	var item = createItem(msg);
	var items = document.getElementById('msgHis');

	msgList.push(msg);
	items.appendChild(item);
}

function createItem(msg){
	var temp = document.createElement('div');
	var htmlAsText = '<li data-task-id="идентификатор">'+
	'<input type="button" value="Редактировать" class="button15 edit-btn"><input type="button" value="Удалить" class="button15 delete-btn"><span>имя</span><span>: </span>текст сообщения<br></li>';

	temp.innerHTML = htmlAsText;
	updateItem(temp.firstChild, msg);

	return temp.firstChild;
}

function updateItem(liItem, msg){
	
	liItem.setAttribute('data-task-id', msg.id);
	liItem.childNodes[2].textContent = msg.name;
	liItem.childNodes[4].textContent = msg.description;
}

function delegateEvent(evtObj){
	if(evtObj.type === 'click' && evtObj.target.classList.contains('change-name')){
		onChangeNameButtonClick();
	} else if(evtObj.type === 'click' && evtObj.target.classList.contains('send-msg')){
		onSendButtonClick();
	} else if(evtObj.type === 'click' && evtObj.target.classList.contains('delete-btn')){
		var labelEl = evtObj.target.parentElement;
		if(labelEl.childNodes[2].textContent == nickname){
			deleteMessage(labelEl);
		}
	} else if(evtObj.type === 'click' && evtObj.target.classList.contains('edit-btn')){
		var labelEl = evtObj.target.parentElement;
		if(labelEl.childNodes[2].textContent == nickname){
			changeTextColor(labelEl);
			onEditButtonClick(labelEl);
		}
	}else if(evtObj.type === 'click' && evtObj.target.classList.contains('change-msg')){
		onChangeButtonClick();
	}
}

function onChangeNameButtonClick(){
	var nameText = document.getElementById('name-input');
	
	if(nameText.value == ''){
		return;
	}
	nickname = nameText.value;
	setCurrentNickname();
	storeName(nickname);
	nameText.value = '';
}
	
function onSendButtonClick(){
	msgText = document.getElementsByClassName('msg-input')[0];
	var newMsg = theMsg(nickname, msgText.value);

	if(msgText.value == '')
		return;

	addMsg(newMsg);
	msgText.value = '';
	storeHistory(msgList);
} 

var msgText;
var htmlItem;
var sendBtn;

function onEditButtonClick(liItem) {	
	htmlItem = liItem;	
	var textNode = htmlItem.childNodes[4];
	msgText = document.getElementsByClassName('msg-input')[0];
	msgText.value = textNode.textContent;
	sendBtn = document.getElementsByClassName('send-msg')[0];
	if(sendBtn != null){
		changeButton(true);
	}
}

function changeTextColor(liItem){
	var item = document.getElementsByClassName('edit-text')[0];
	if(item){
		item.classList.remove('edit-text');
	}
	
	liItem.classList.add('edit-text');
}

function onChangeButtonClick(){
	var id = htmlItem.attributes['data-task-id'].value;
	for(var i = 0; i < msgList.length; i++) {
		if(msgList[i].id != id)
			continue;

		changeMsgText(msgList[i]);
		updateItem(htmlItem, msgList[i]);
		storeHistory(msgList);
		htmlItem.classList.remove('edit-text');
		sendBtn = document.getElementsByClassName('change-msg')[0];
		changeButton(false);
		msgText.value = '';

		return;
	}	
}

function changeMsgText(msg){
	msg.description = msgText.value;
}

function changeButton(flag){
	if(flag){
		sendBtn.classList.remove('send-msg');
		sendBtn.classList.add('change-msg');
	}else{
		sendBtn.classList.remove('change-msg');
		sendBtn.classList.add('send-msg');
	}
}

function deleteMessage(liItem){
	var id = liItem.attributes['data-task-id'].value;
	
	var items = document.getElementById('msgHis');
	
	for(var i = 0; i < msgList.length; i++) {
		if(msgList[i].id != id)
			continue;

		msgList.splice(i,1);
		storeHistory(msgList);
		items.removeChild(liItem);
		return;
	}
}

function storeHistory(listToSave) {

	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	localStorage.setItem("Msg history", JSON.stringify(listToSave));
}
function storeName(name) {

	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	localStorage.setItem("Nickname", JSON.stringify(name));
}

function restoreHistory() {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	var item = localStorage.getItem("Msg history");

	return item && JSON.parse(item);
}

function restoreName() {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	var item = localStorage.getItem("Nickname");

	return item && JSON.parse(item);
}