'use strict';


angular.module('core').controller('HomeController', ['$scope', 'Authentication',
	function($scope, Authentication) {
		// This provides Authentication context.
		$scope.authentication = Authentication;

		$scope.alerts = [
			{
				icon:'glyphicon-user',
				colour:'btn-success',
				total:'20,488',
				description:'TOTAL CLIENTES'
			},
			{
				icon:'glyphicon-calendar',
				colour:'btn-primary',
				total:'433',
				description:'TOTAL CLIENTES'
			},
			{
				icon:'glyphicon-edit',
				colour:'btn-info',
				total:'944',
				description:'TOTAL CLIENTES'
			},
			{
				icon:'glyphicon-record',
				colour:'btn-warning',
				total:'123,932',
				description:'TOTAL CLIENTES'
			},
			{
				icon:'glyphicon-eye-open',
				colour:'btn-danger',
				total:'12',
				description:'TOTAL CLIENTES'
			},
			{
				icon:'glyphicon-flag',
				colour:'btn-default',
				total:'564',
				description:'TOTAL CLIENTES'
			}

		];

	}
]);
