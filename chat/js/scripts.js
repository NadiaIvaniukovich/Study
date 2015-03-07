function run(){
	var appContainer = document.getElementsByClassName('chat')[0];
	
	appContainer.addEventListener('click', delegateEvent);
}

function delegateEvent(evtObj){
	if(evtObj.type === 'click' && evtObj.target.classList.contains('change-name')){
		changeName(evtObj);
	} else if(evtObj.type === 'click' && evtObj.target.classList.contains('send-msg')){
		sendMessage(evtObj);
	} else if(evtObj.type === 'click' && evtObj.target.classList.contains('delete-btn')){
		var labelEl = evtObj.target.parentElement;
		deleteMessage(labelEl);
	} else if(evtObj.type === 'click' && evtObj.target.classList.contains('edit-btn')){
		var labelEl = evtObj.target.parentElement;
		editMessage(labelEl);
	}else if(evtObj.type === 'click' && evtObj.target.classList.contains('change-msg')){
		changeText();
	}
}

var nickname = "Гость";

function changeName(evtObj){
	var nameText = document.getElementById('name-input');
	var name = document.getElementsByClassName('nickname')[0];
	if(nameText.value != ''){
		nickname = nameText.value;
		name.innerText = nickname;
	}
	nameText.value = '';
}

var msgText;

function sendMessage(evtObj){
	msgText = document.getElementsByClassName('msg-input')[0];
	addMessage(msgText.value);
	msgText.value = '';	
}

function addMessage(value) {
	if(!value){
		return;
	}

	var item = createItem(value);
	var items = document.getElementById('msgHis');

	items.appendChild(item);
}

function createItem(text){
	var liItem = document.createElement('li');
	var deleteButton = document.createElement('input');
	var editButton = document.createElement('input');
	
	deleteButton.setAttribute('type', 'button');
	deleteButton.setAttribute('value', 'Удалить');
	deleteButton.classList.add('button15', 'delete-btn');
	
	editButton.setAttribute('type', 'button');
	editButton.setAttribute('value', 'Редактировать');
	editButton.classList.add('button15', 'edit-btn')

	liItem.appendChild(editButton);
	liItem.appendChild(deleteButton);
	liItem.appendChild(document.createTextNode(nickname+": "))
	liItem.appendChild(document.createTextNode(text));
	liItem.appendChild(document.createElement('br'));	

	return liItem;
}

function deleteMessage(labelEl){
	var items = document.getElementById('msgHis');
	items.removeChild(labelEl);
}

var textNode;

function editMessage(labelEl){
	textNode = labelEl.childNodes[3];
	msgText = document.getElementsByClassName('msg-input')[0];
	msgText.value = textNode.textContent;
	var sendBtn = document.getElementsByClassName('send-msg')[0];
	sendBtn.classList.remove('send-msg');
	sendBtn.classList.add('change-msg');
}

function changeText(){
	textNode.textContent = msgText.value;
	var sendBtn = document.getElementsByClassName('change-msg')[0];
	sendBtn.classList.remove('change-msg');
	sendBtn.classList.add('send-msg');
	msgText.value = '';
}