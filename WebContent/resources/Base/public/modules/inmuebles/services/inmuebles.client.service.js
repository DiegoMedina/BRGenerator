'use strict';

//Inmuebles service used to communicate Inmuebles REST endpoints
angular.module('inmuebles').factory('Inmuebles', ['$resource',
	function($resource) {
		return $resource('inmuebles/:inmuebleId', { inmuebleId: '@_id'
		}, {
			update: {
				method: 'PUT'
			}
		});
	}
]);