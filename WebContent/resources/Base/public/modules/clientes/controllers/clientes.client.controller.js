'use strict';

var clientesApp = angular.module('clientes');

// Clientes controller
clientesApp.controller('ClientesController', ['$scope', '$stateParams', 'Authentication', 'Clientes', '$modal','$log',
	function($scope, $stateParams, Authentication, Clientes, $modal, $log) {

		this.authentication = Authentication;
		this.clientes = Clientes.query();

		this.modalUpdate = function (size, selectedCustomer) {

			var modalInstance = $modal.open({
				animation: $scope.animationsEnabled,
				templateUrl: 'modules/clientes/views/edit-cliente.client.view.html',
				controller: function ($scope, $modalInstance, cliente) {
					$scope.cliente = cliente;

					$scope.ok = function () {

						if(updateClienteForm.valid)
						{
							$modalInstance.close($scope.cliente);
						}
					};

					$scope.cancel = function () {
						$modalInstance.dismiss('cancel');
					};
				},
				size: size,
				resolve: {
					cliente: function () {
						return selectedCustomer;
					}
				}
			});

			modalInstance.result.then(function (selectedItem) {
				$scope.selected = selectedItem;
			}, function () {
				$log.info('Modal dismissed at: ' + new Date());
			});
		};

		$scope.toggleAnimation = function () {
			$scope.animationsEnabled = !$scope.animationsEnabled;
		};
	}
]);

// Clientes controller
clientesApp.controller('ClientesCreateController', ['$scope', 'Clientes',
	function($scope, Clientes) {


	}
]);

// Clientes controller
clientesApp.controller('ClientesEditController', ['$scope', 'Clientes',
	function($scope, Clientes) {

		// Update existing Cliente
		this.update = function(updatedCliente) {
			var cliente = updatedCliente;

			cliente.$update(function() {
				$location.path('clientes/' + cliente._id);
			}, function(errorResponse) {
				$scope.error = errorResponse.data.message;
			});
		};

	}
]);


		//$scope.authentication = Authentication;
        //
		//// Create new Cliente
		//$scope.create = function() {
		//	// Create new Cliente object
		//	var cliente = new Clientes ({
		//		nombre: this.nombre,
		//		apellido: this.apellido,
		//		ciudad: this.ciudad,
		//		pais: this.pais,
		//		email: this.email,
		//		telefono: this.telefono,
		//		referido: this.referido
        //
		//	});
        //
		//	// Redirect after save
		//	cliente.$save(function(response) {
		//		$location.path('clientes/' + response._id);
        //
		//		// Clear form fields
		//		$scope.nombre = '';
		//		$scope.apellido = '';
		//		$scope.ciudad = '';
		//		$scope.pais = '';
		//		$scope.email = '';
		//		$scope.telefono = '';
		//		$scope.referido = '';
		//	}, function(errorResponse) {
		//		$scope.error = errorResponse.data.message;
		//	});
		//};
        //
		//// Remove existing Cliente
		//$scope.remove = function(cliente) {
		//	if ( cliente ) {
		//		cliente.$remove();
        //
		//		for (var i in $scope.clientes) {
		//			if ($scope.clientes [i] === cliente) {
		//				$scope.clientes.splice(i, 1);
		//			}
		//		}
		//	} else {
		//		$scope.cliente.$remove(function() {
		//			$location.path('clientes');
		//		});
		//	}
		//};
        //
		//// Update existing Cliente
		//$scope.update = function() {
		//	var cliente = $scope.cliente;
        //
		//	cliente.$update(function() {
		//		$location.path('clientes/' + cliente._id);
		//	}, function(errorResponse) {
		//		$scope.error = errorResponse.data.message;
		//	});
		//};
        //
		//// Find a list of Clientes
		//$scope.find = function() {
		//	$scope.clientes = Clientes.query();
		//};
        //
		//// Find existing Cliente
		//$scope.findOne = function() {
		//	$scope.cliente = Clientes.get({
		//		clienteId: $stateParams.clienteId
		//	});
		//};

