'use strict';

// Configuring the [model:name:plural] module
angular.module('[model:name:lower:plural]').run(['Menus',
	function(Menus) {
		// Add the [model:name:lower:plural] dropdown item
		Menus.addMenuItem('topbar', {
			title: '[model:name:plural]',
			state: '[model:name:lower:plural]',
			type: 'dropdown'
		});

		// Add the dropdown list item
		Menus.addSubMenuItem('topbar', '[model:name:lower:plural]', {
			title: 'List [model:name:plural]',
			state: '[model:name:lower:plural].list'
		});

		// Add the dropdown create item
		Menus.addSubMenuItem('topbar', '[model:name:lower:plural]', {
			title: 'Create [model:name:plural]',
			state: '[model:name:lower:plural].create'
		});
	}
]);
