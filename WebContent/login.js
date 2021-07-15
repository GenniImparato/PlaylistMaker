/**
 * Login
 */

(function() 
{ // avoid variables ending up in the global scope
	document.getElementById("login_button").addEventListener('click', (e) => 
	{
    	var form = e.target.closest("form");
    	if (form.checkValidity()) 
		{
      		makeCall("POST", 'login', e.target.closest("form"),
        	function(x) 
			{
          		if (x.readyState == XMLHttpRequest.DONE) 
				{
            		var message = x.responseText;
            		switch (x.status) 
					{
		              case 200:
		            	sessionStorage.setItem('currentUsername', message);
		                window.location.href = "home.html";
		                break;
		              case 400: // bad request
		                document.getElementById("error_message").textContent = message;
		                break;
		              case 401: // unauthorized
		                  document.getElementById("error_message").textContent = message;
		                  break;
		              case 500: // server error
		            	document.getElementById("error_message").textContent = message;
		                break;
		            }
          		}
        	});
    	} 
		else 
		{
    		form.reportValidity();
    	}
  	});
})();