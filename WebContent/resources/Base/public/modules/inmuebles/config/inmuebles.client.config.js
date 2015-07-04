'use strict';

// Configuring the Articles module
angular.module('inmuebles').run(['Menus',
	function(Menus) {
		// Set top bar menu items
		Menus.addMenuItem('topbar', 'Inmuebles', 'inmuebles', 'dropdown', '/inmuebles(/create)?');
		Menus.addSubMenuItem('topbar', 'inmuebles', 'List Inmuebles', 'inmuebles');
		Menus.addSubMenuItem('topbar', 'inmuebles', 'New Inmueble', 'inmuebles/create');
	}
]);