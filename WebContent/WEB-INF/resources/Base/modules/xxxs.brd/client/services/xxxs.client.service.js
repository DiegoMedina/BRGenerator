'use strict';

//[model:name:plural] service used for communicating with the [model:name:lower:plural] REST endpoints
angular.module('[model:name:lower:plural]').factory('[model:name:plural]', ['$resource',
	function($resource) {
		return $resource('api/[model:name:lower:plural]/:[model:name:lower]Id', {
			[model:name:lower]Id: '@_id'
		}, {
			update: {
				method: 'PUT'
			}
		});
	}
]);
