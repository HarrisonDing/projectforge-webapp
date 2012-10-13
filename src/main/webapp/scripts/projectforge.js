function showConfirmDialog(text) {
	return window.confirm(text);
}

function toggle(component) {
	$(component).toggle('fast');
}

function rowClick(row) {
	if (suppressRowClick != 'true') {
		link = $(row).find("a:first");
		if ($(link).attr('onclick')) {
			suppressNextRowClick();
			$(link).click();
		} else {
			window.location.href = $(link).attr("href");
		}
	}
	suppressRowClick = 'false';
}

function rowCheckboxClick(row, event) {
	if(!event) event = window.event; // For ie.
	var t = event.target || event.srcElement;
	if(t.type != "checkbox") { /* disables tableRowClickFunction if you are over the checkbox */
		cb = $(row).find("input[type='checkbox']");
		cb.attr('checked', !cb.is(':checked'));
	}
}

function suppressNextRowClick() {
	suppressRowClick = 'true';
}
function preventBubble(e) {
	if (!e)
		var e = window.event;
	if (e.stopPropagation) { // if stopPropagation method supported
		e.stopPropagation();
		e.preventDefault();
	} else {
		e.cancelBubble = true;
		e.returnValue = false;
	}
	return false;
}
function initTooltips() {
	// Enable all title attributes as tooltip:
	$(':*[title]').tooltip({
		track : true,
		delay : 0,
		fade : 250,
		showURL : false,
		showBody : " - "
	});
}
function showBookmark() {
	$("#bookmark").toggle("normal");
}

var suppressRowClick = 'false';

// Begin: Functionality for DropDownMenu
var timeout = 500;
var closetimer = 0;
var ddmenuitem = 0;

// open hidden layer
function mopen(id) {
	// cancel close timer
	mcancelclosetime();

	// close old layer
	if (ddmenuitem)
		ddmenuitem.style.visibility = 'hidden';

	// get new layer and show it
	ddmenuitem = document.getElementById(id);
	ddmenuitem.style.visibility = 'visible';

}
// close showed layer
function mclose() {
	if (ddmenuitem)
		ddmenuitem.style.visibility = 'hidden';
}

// go close timer
function mclosetime() {
	closetimer = window.setTimeout(mclose, timeout);
}

// cancel close timer
function mcancelclosetime() {
	if (closetimer) {
		window.clearTimeout(closetimer);
		closetimer = null;
	}
}

// close layer when click-out
document.onclick = mclose;
// End: Functionality for DropDownMenu

function openDialog(element) {
	$('#' + element).dialog({
		'resizable': false,
		'draggable': false
		}).dialog('open');
}

function bindDialogCallback(element, script) {
	$('#' + element).bind('dialogclose', function(event, ui) {
		script();
	});
}

$(function() {
	$(".dialog_content").live("dialogopen", function(event, ui) {
		disableScroll();
	});
	$(".dialog_content").live("dialogclose", function(event, ui) {
		enableScroll();
	});
	
	initColorPicker();
})
function initColorPicker() {
	$(".pf_colorPreview").live("click", function(e) {
		e.preventDefault();
		e.stopPropagation();
		$(this).siblings(".pf_colorContainer").slideDown(300);
		var ref = $(this);
		$("body").click(function() {
			$(ref).siblings(".pf_colorContainer").slideUp(300, function() {
				$("body").unbind("click");
			});
		});
	});
	$(".pf_colorContainer").click(function(e) {
		e.preventDefault();
		e.stopPropagation();
	});
	$(".pf_colorContainer span").click(function(e) {
		var color = $(this).attr("style").substring(18,27);
		var input = $(this).parent().siblings(".pf_colorForm").find("input");
		$(input).val(color);
		$(input).change();
		$(this).parent().slideUp(300, function() {
			$("body").unbind("click");
		});
	});
}

function disableScroll() {
    var before = $(document).width();
    $("html").css("overflow", "hidden");
    var after = $(document).width();
    $("body").css("padding-right", after-before);
}

function enableScroll() {
    $("html").css("overflow", "auto");
    $("body").css("padding-right", 0);
}
