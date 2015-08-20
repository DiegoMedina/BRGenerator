'use strict';

// [model:name:plural] controller

angular.module('[model:name:lower:plural]').controller('[model:name:plural]Controller', ['$scope', '$stateParams', 'Authentication', '[model:name:plural]', '$modal', '$log',
	function($scope, $stateParams, Authentication, [model:name:plural], $modal, $log) {

		this.authentication = Authentication;

		// Find a list of [model:name:plural]
		this.[model:name:lower:plural] = [model:name:plural].query();


		// Open a modal window to Create a single [model:name:lower] record
		this.modalCreate = function (size) {

			var modalInstance = $modal.open({
				templateUrl: 'modules/[model:name:lower:plural]/views/create-[model:name:lower].modal.view.html',
				controller: function ($scope, $modalInstance) {


					$scope.ok = function (isValid) {
						console.log(isValid);
						$modalInstance.close();
					};

					$scope.cancel = function () {
						$modalInstance.dismiss('cancel');
					};
				},
				size: size
			});

			modalInstance.result.then(function (selectedItem) {
			}, function () {
				$log.info('Modal dismissed at: ' + new Date());
			});
		};



		// Open a modal window to Update a single [model:name:lower] record
		this.modalUpdate = function (size, selected[model:name]) {

			var modalInstance = $modal.open({
				templateUrl: 'modules/[model:name:lower:plural]/views/update-[model:name:lower].modal.view.html',
				controller: function ($scope, $modalInstance, a[model:name]) {

					$scope.the[model:name] = {};

					$scope.the[model:name] = angular.copy(a[model:name]);


					$scope.ok = function () {
						$modalInstance.close();
					};

					$scope.cancel = function () {
						$modalInstance.dismiss('cancel');
					};
				},
				size: size,
				resolve: {
					a[model:name]: function () {
						return selected[model:name];
					}
				}
			});

			modalInstance.result.then(function (selectedItem) {
				$scope.selected = selectedItem;
			}, function () {
				$log.info('Modal dismissed at: ' + new Date());
			});
		};



		// Remove existing [model:name]
		this.remove = function( [model:name:lower] ) {
			if ( [model:name:lower] ) { [model:name:lower].$remove();

				for (var i in this.[model:name:lower:plural] ) {
					if (this.[model:name:lower:plural] [i] === [model:name:lower] ) {
						this.[model:name:lower:plural].splice(i, 1);
					}
				}
			} else {
				this.[model:name:lower].$remove(function() {
				});
			}
		};

	}
]);
