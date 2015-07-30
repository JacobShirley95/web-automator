function getVisibleElements(jquerySelector) {
	var results = [];
	
	var pageOffX = window.pageXOffset;
	var pageOffY = window.pageYOffset;
	
	$(jquerySelector).each(function(index) {
		var $this = $(this);
		var offset = $this.offset();
		var width = getElementWidth($this);
		var height = getElementHeight($this);
		
		var x = offset.left + (width/2);
		var y = offset.top + (height/2);
		
		if (document.elementFromPoint(x-pageOffX, y-pageOffY) == this) {
			results.push($this);
		} else {
			var elems = document.elementsFromPoint(x-pageOffX, y-pageOffY);
			
			for (var i = 0; i < elems.length; i++) {
				if (elems[i] == this) {
					results.push($this);
					break;
				}
			}
		}
	});
	return results;
}

function getRandomElement(elems) {
	var random = Math.round(Math.random()*(elems.length-1));
	return elems[random];
}

function getRandomTextField() {
	var elems = getVisibleElements("input[type='text']");
	return getRandomElement(elems);
}

function getRandomLink() {
	var links = getVisibleElements("a, button, input[type='button'], input[type='submit']");
	return getRandomElement(links);
}

function getElementWidth(elem) {
	var width = elem.outerWidth();
	elem.children().each(function (i) {
		width = Math.max(width, getElementWidth($(this)));
	});
	return width;
}

function getElementHeight(elem) {
	var height = elem.outerHeight();
	elem.children().each(function (i) {
		height = Math.max(height, getElementHeight($(this)));
	});
	return height;
}