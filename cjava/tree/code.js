console.log("hello body");

$( document ).ready(function() {




console.log( jQuery("#tree").length );

  jQuery("#tree")
	.bind('loaded.jstree', function(e, data)
	{
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

$('div').qtip('a');

$('.tooltip').qtip({ // Grab some elements to apply the tooltip to
    content: true,
     position: {
        my: 'top left',  // Position my top left...
        at: 'center', // at the bottom right of...
        target: 'event' // my target
    },
    style: {
        width: '800px'
    },
    hide: {
            // event: 'click',
             //inactive: 1200,
             fixed: true
         }
    /*position: {
        viewport: $(window)
    }*/
});    	

});