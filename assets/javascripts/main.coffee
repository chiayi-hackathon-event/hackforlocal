$(document).ready ->
	isSearching = false
	$('body').click (e)->
		if ($(e.target).is('.menu, .menu *'))
            return
		$('.user-menu-container > .menu').hide()
		if(isSearching)
			$('.search-menu-box').animate({ "right": "-=225px" }, "slow" )
			$('.menu > .nav-search').animate({ width: "0" }, "slow" )
			$('.user-search-container > .menu').hide()
			isSearching = false
	$('.user-menu').click (e)->
		e.stopPropagation()
		$('.user-menu-container > .menu').toggle()
		return
	$('.user-menu-close-button').click (e)->
		e.stopPropagation()
		$('.user-menu-container > .menu').hide()
		return

	$('.search-menu').click (e)->
		e.stopPropagation()
		if(!isSearching)
			$('.search-menu-box').animate({ "right": "+=220px" }, "slow" )
			$('.user-search-container > .menu').show()
			$('.menu > .nav-search').animate({ width: "220px" }, "slow" )
			isSearching = true

	[].forEach.call $('.filter-menu'), (ele) ->
		$(ele).click (e) ->
			e.stopPropagation()
			$(ele).find('.menu').toggle()
			return
		return

	filterMenuItemTrigger = ->
		[].forEach.call $('.filter-menu'), (ele) ->
			[].forEach.call $(ele).find('.menu-container > div'), (eleDiv) ->
				if $(eleDiv).text() == $(ele).find('.filter-menu-selected').text()
					$(ele).find('.menu-container > div').removeClass('selected-div')
					$(eleDiv).addClass('selected-div')
					return
				return
			return

	filterMenuItemTrigger()

	[].forEach.call $('.menu-container'), (ele) ->
		[].forEach.call $(ele).find('> div'), (eleDiv) ->
			$(eleDiv).click (e) ->
				text = $(eleDiv).text()
				$(eleDiv).parents('.filter-menu').find('.filter-menu-selected').html(text)
				filterMenuItemTrigger()
