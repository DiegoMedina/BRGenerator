'use strict';

// Inmuebles controller
angular.module('inmuebles').controller('InmueblesController', ['$scope', '$stateParams', '$location', 'Authentication', 'Inmuebles',
	function($scope, $stateParams, $location, Authentication, Inmuebles) {
		$scope.authentication = Authentication;

		// Create new Inmueble
		$scope.create = function() {
			// Create new Inmueble object
			var inmueble = new Inmuebles ({
				name: this.name,
				calle: this.calle,
				numero: this.numero
			});

			// Redirect after save
			inmueble.$save(function(response) {
				$location.path('inmuebles/' + response._id);

				// Clear form fields
				$scope.name = '';
			}, function(errorResponse) {
				$scope.error = errorResponse.data.message;
			});
		};

		// Remove existing Inmueble
		$scope.remove = function(inmueble) {
			if ( inmueble ) { 
				inmueble.$remove();

				for (var i in $scope.inmuebles) {
					if ($scope.inmuebles [i] === inmueble) {
						$scope.inmuebles.splice(i, 1);
					}
				}
			} else {
				$scope.inmueble.$remove(function() {
					$location.path('inmuebles');
				});
			}
		};

		// Update existing Inmueble
		$scope.update = function() {
			var inmueble = $scope.inmueble;

			inmueble.$update(function() {
				$location.path('inmuebles/' + inmueble._id);
			}, function(errorResponse) {
				$scope.error = errorResponse.data.message;
			});
		};

		// Find a list of Inmuebles
		$scope.find = function() {
			$scope.inmuebles = Inmuebles.query();
		};

		// Find existing Inmueble
		$scope.findOne = function() {
			$scope.inmueble = Inmuebles.get({ 
				inmuebleId: $stateParams.inmuebleId
			});
		};
	}
]);