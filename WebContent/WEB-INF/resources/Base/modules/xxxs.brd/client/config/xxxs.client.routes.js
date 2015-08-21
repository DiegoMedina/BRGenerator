'use strict';

// Setting up route
angular.module('[model:name:lower:plural]').config(['$stateProvider',
	function($stateProvider) {
		// [model:name:plural] state routing
		$stateProvider.
		state('[model:name:lower:plural]', {
			abstract: true,
			url: '/[model:name:lower:plural]',
			template: '<ui-view/>'
		}).
		state('[model:name:lower:plural].list', {
			url: '',
			templateUrl: 'modules/[model:name:lower:plural]/views/list-[model:name:lower:plural].client.view.html'
		});
	}
]);
