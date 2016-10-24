var dialog, allFields, form, tips, addUpdatePath, deletePath, dialogWidth, fieldsStartsWith;

dialogWidth=350;

function createDialog(writeUpdate){
	
	var buttonsOpts = {}
	if(writeUpdate) {
		buttonsOpts["Save"] = saveRecord;
	} else {
		buttonsOpts["Save"] = saveRecordNoWrite;
	}
	buttonsOpts["Cancel"] = function() {
		dialog.dialog( "close" );
	};
	
	dialog = $( "#dialog-form" ).dialog({
    	autoOpen: false,
    	width: dialogWidth,
    	modal: true,
    	buttons : buttonsOpts,
		close: function() {
			allFields.removeClass( "ui-state-error" );
			if (typeof closeDialog == 'function') {
				closeDialog();
			}
		}
	});
}

function createLengthValidationObject(fieldObj, fieldName, minimum, maximum){
	var lengthValidationObject = {
		field: fieldObj,
		name: fieldName,
		min: minimum,
		max: maximum
	};
	return lengthValidationObject;
}

function createNumberValidationObject(fieldObj, fieldName, req){
	var numberValidationObject = {
		field: fieldObj,
		name: fieldName,
		required: req
	};
	return numberValidationObject;
}

function createDateValidationObject(fieldObj, fieldName, req){
	var dateValidationObject = {
		field: fieldObj,
		name: fieldName,
		required: req 
	};
	return dateValidationObject;
}

function createPasswordValidationObject(field1Obj, field2Obj){
	var passwordValidationObject = {
		field1: field1Obj,
		field2: field2Obj,
	};
	return passwordValidationObject;
}

var fileFormData = {};

function saveRecord() {
	var valid = validateForm();
	
	if(valid) {
		var formData = populateFormData();
		
		var editRecord = (formData["id"] > 0);
        $.ajax({
            url: contextPath+addUpdatePath,
            type:'POST',
            data: JSON.stringify(formData),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            success:function(resObj, status, fullResponse){
            	var savedRecord = JSON.parse(fullResponse.responseText);
            	if(editRecord){
            		writeEditRecord(savedRecord);
            	}
            	else{
            		writeCreatedRecord(savedRecord);
            	}
		    	dialog.dialog( "close" );
        		setTimeout(function() {
        			$('.new-row').removeClass( "new-row" );
        		}, 5000 );
            },
            error:function(res){
            	alert("Error! Unable to add record. Tell Jeff it's broken!");
            }
       	});
	}
	return valid;
}

function saveRecordNoWrite() {
	var valid = validateForm();
	
	if(valid) {
		var formData = populateFormData();
		
		var editRecord = (formData["id"] > 0);
        $.ajax({
            url: contextPath+addUpdatePath,
            type:'POST',
            data: formData,
            success:function(resObj, status, fullResponse){
            	$('#calendar').fullCalendar( 'refetchEvents' );
            },
            error:function(res){
            	alert("Error! Unable to add record. Tell Jeff it's broken!");
            }
       	});
	}
	return valid;	
}
function validateForm() {
	var valid=true;
	
	clearFormValidation();
	if (typeof preValidation == 'function') { 
		valid = preValidation() && valid;
	}
	if (typeof lengthValidation !== 'undefined') {
		$.each(lengthValidation, function(){
			valid = checkLength( this.field, this.name, this.min, this.max ) && valid;
		});
	}
	if (typeof numberValidation !== 'undefined') {
		$.each(numberValidation, function(){
			valid = checkNumber( this.field, this.name, this.required ) && valid;
		});
	}
	if (typeof dateValidation !== 'undefined') {
		$.each(dateValidation, function(){
			valid = checkDate( this.field, this.name, this.required ) && valid;
		});
	}
	if (typeof passwordValidation !== 'undefined') {
		$.each(passwordValidation, function(){
			valid = checkPasswordsMatch( this.field1, this.field2 ) && valid;
		});
	}
	if (typeof extraValidation == 'function') { 
		valid = extraValidation() && valid;
	}
	valid = checkSelects() && valid;
	
	return valid;
}

function clearFormValidation(){
	tips.empty();
	allFields.removeClass( "ui-state-error" );
	tips.removeClass( "ui-state-highlight" );
	tips.hide();
}
function populateFormData() {
	var formData = {};
	
	$.each(allFields, function(i, v){
		var shortName = findShortName(v.name);
		if(v.nodeName == "SELECT") {
			setSelectValue(formData, v);
		} else 
		if (v.type == "checkbox") {
			if(v.checked) {
				formData[shortName] = true;
			} else {
				formData[shortName] = false;
			}
		} else {
			var thisValue = v.value;
			var hiddenId = v.dataset.hiddenId;
			var isArray = v.dataset.array;
			if(hiddenId){
				thisValue = $("#"+hiddenId).val();
			}
			
			var currentValue = formData[shortName];
			if(currentValue) {
				if(typeof currentValue == "object") { // if it's already an array object, then just add to it
					currentValue.push(thisValue);
					formData[shortName] = currentValue;
				} else { // otherwise, create an array with the old and new values
					formData[shortName] = [currentValue, thisValue];
				}
			} else {
				if(isArray) {
					formData[shortName] = [thisValue];
				} else {
					formData[shortName] = thisValue;
				}
			}
		}
	});
	if (typeof setExtraValues == 'function') { 
		setExtraValues(formData); 
	}
	return formData;
}
function findShortName(name) {
	var underscoreIndex = name.indexOf('_');
	var shortName;
	if(underscoreIndex >= 0) {
		shortName = name.substring(underscoreIndex+1);
	} else {
		shortName = name;
	}
	return shortName;
}

function updateTips( t ) {
	if(tips.text().length > 0) {
		if(tips.text().charAt(0) != '-') {
			tips.prepend('- ');
		}
		tips.append("<br>- ");
	}
	tips.append(t);
	tips.addClass( "ui-state-highlight" );
	tips.show();
	//setTimeout(function() {
	//	tips.removeClass( "ui-state-highlight", 1500 );
	//}, 500 );
}
function checkLength( o, n, min, max ) {
	if ( o.val().length > max || o.val().length < min ) {
		o.addClass( "ui-state-error" );
		updateTips( "Length of " + n + " must be between " + min + " and " + max + "." );
		return false;
	} else {
		return true;
	}
}
function checkNumber( o, n, required ) {
	var value = o.val();
	if(value.trim() == "" && !required) {
		return true;
	}		
	if ( !$.isNumeric(o.val()) ) {
		o.addClass( "ui-state-error" );
		updateTips( n + " must be a valid number." );
		return false;
	} else {
		return true;
	}
}
function checkPasswordsMatch( p1, p2 ) {
	if( p1.val() == p2.val() ) {
		return true;	
	}
	p1.addClass( "ui-state-error" );
	updateTips( "The passwords do not match" );
	return false;
}
function checkDate( o, n, required ){
	var t = o.val().match(/^(\d{2})\/(\d{2})\/(\d{4})$/);
	if(t == null && !required){
		 return true;
	}
	if(t!==null) {
		var m=+t[1], d=+t[2], y=+t[3];
		var date = new Date(y,m-1,d);
		if(date.getFullYear()===y && date.getMonth()===m-1) {
			return true;
	    }
	}
	o.addClass( "ui-state-error" );
	updateTips( n + " must be a valid date in the mm/dd/yyyy format." );
	return false;
}
function checkSelects() {
	var valid = true;
	$('#addEditForm select').each(function(){
		if( $(this).attr("multiple") == 'undefined' ) {
			if( $(this).has('option').length == 0 ) {
				updateTips( "You must add a '"+this.name+"' before adding this record." );
				valid = false;
			}
		}
	});
	return valid;
}

function getNewCheckboxValue(booleanValue) {
	if(booleanValue) {
		return 'checked';
	}
	return '';
}

var _MS_PER_DAY = 1000 * 60 * 60 * 24;
//a and b are javascript Date objects
function dateDiffInDays(a, b) {
	// Discard the time and time-zone information.
	var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
	var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());
	
	return Math.floor((utc2 - utc1) / _MS_PER_DAY);
}

$(function() {
	tips = $( ".validateTips" );
	
	$('.repairLink').attr('title','Repair');
	$('.editLink').attr('title','Edit');
	$('.deleteLink').attr('title','Delete');
	
	if (typeof buildOptions == 'function') {
		buildOptions();
	}
	function deleteRecord(id){
        $.ajax({
            url: domain+deletePath+'/'+id,
            type:'DELETE',
            success:function(response){
        		if (typeof postDeleteAction == 'function') {
        			postDeleteAction(response);
        		}
            },
            error:function(response){
            	// show the record again, since it couldn't be deleted
            	$('div.records-rows.id'+id).toggle();
            	alert('The record could not be deleted. This is likely because this record is being used by another element (ie. Recipe, Date, etc).')
            }
       	});	
	}
	$( "#create-record" ).button().on( "click", function() {
		clearFormValidation();
		$('#addEditForm').trigger("reset");
		checkSelects();
		$('#addEditForm').show();		
		if (typeof populateCreateForm == 'function') {
			populateCreateForm(this);
		}
		dialog.dialog( "open" );
	});
	$(document).on("click", ".editLink", function(){
		clearFormValidation();
		$('#addEditForm').trigger("reset");
		$('#addEditForm').show();
		populateEditForm(this);
		dialog.dialog( "open" );
		return false;
	});
	$(document).on("click", ".deleteLink", function(){
		var deleteName = findDeleteName(this);
		if(window.confirm("Are you sure you want to delete '"+deleteName+"'?")) {
			deleteRecord($(this).attr('data-id'));
			$(this).closest('div.records-rows').toggle();
		}
	});
	$("#addEditForm").submit(function(e){
		e.preventDefault();
		saveRecord();
	});
	$( "#open-squawks" ).button();
	$( "#completed-squawks" ).button();
	
	$( "#year" ).change(function() {
		var url = $(this).val(); // get selected value
        if (url) { // require a URL
            window.location = url; // redirect
        }
        return false;
	});
});