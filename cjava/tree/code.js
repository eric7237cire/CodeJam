console.log("hello body");

function bindQTip() {
	console.log("bindqtip");
	
	$('.tooltip').qtip({ 
		position: {
	    viewport: $(window),
	    
        my: 'top left',  // Position my top left...
        at: 'top left', // at the bottom right of...
        target: $(this)
    
	}
	
    });
	
	$('li.jstree-leaf').each(function() {
		
		$(this).qtip({ 
	
    content: {
      text: $(this).attr('board')
    },
    position: {
	    	    
        my: 'top left',  // Position my top left...
        at: 'top left', // at the bottom right of...
        target: $(this),
    adjust: {
        screen: true
    },
    viewport: $(window)
	}
		
		
		});  
});
	
}

$( document ).ready(function() {




console.log( jQuery("#tree").length );

  jQuery("#tree")
	.bind('loaded.jstree', function(e, data)
	{
		
		bindQTip();
		return;
		 jQuery("#tree").jstree('open_all');
		 
		/**
		* Open nodes on load (until x'th level)
		*/
		var depth = 3;
		data.inst.get_container().find('li').each(function(i) 
		{
			if(data.inst.get_path($(this)).length<=depth)
			{
				data.inst.open_node($(this));
			}
		});
	})
    .jstree({
		"core" : { "animation" : 5 },
		"themes" : {
			"theme" : "classic",
			"dots" : true,
			"icons" : false
		},
		"json_data" : {
			"data" : 
			[
				
				allData
			]
		},
		"plugins" : [ "themes", "json_data" ]
       

    });


	console.log("tooltip elems " + $('.tooltip').length);
console.log(jQuery.fn.jquery);

});