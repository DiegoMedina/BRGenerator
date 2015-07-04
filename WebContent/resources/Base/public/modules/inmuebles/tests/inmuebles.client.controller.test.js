'use strict';

(function() {
	// Inmuebles Controller Spec
	describe('Inmuebles Controller Tests', function() {
		// Initialize global variables
		var InmueblesController,
		scope,
		$httpBackend,
		$stateParams,
		$location;

		// The $resource service augments the response object with methods for updating and deleting the resource.
		// If we were to use the standard toEqual matcher, our tests would fail because the test values would not match
		// the responses exactly. To solve the problem, we define a new toEqualData Jasmine matcher.
		// When the toEqualData matcher compares two objects, it takes only object properties into
		// account and ignores methods.
		beforeEach(function() {
			jasmine.addMatchers({
				toEqualData: function(util, customEqualityTesters) {
					return {
						compare: function(actual, expected) {
							return {
								pass: angular.equals(actual, expected)
							};
						}
					};
				}
			});
		});

		// Then we can start by loading the main application module
		beforeEach(module(ApplicationConfiguration.applicationModuleName));

		// The injector ignores leading and trailing underscores here (i.e. _$httpBackend_).
		// This allows us to inject a service but then attach it to a variable
		// with the same name as the service.
		beforeEach(inject(function($controller, $rootScope, _$location_, _$stateParams_, _$httpBackend_) {
			// Set a new global scope
			scope = $rootScope.$new();

			// Point global variables to injected services
			$stateParams = _$stateParams_;
			$httpBackend = _$httpBackend_;
			$location = _$location_;

			// Initialize the Inmuebles controller.
			InmueblesController = $controller('InmueblesController', {
				$scope: scope
			});
		}));

		it('$scope.find() should create an array with at least one Inmueble object fetched from XHR', inject(function(Inmuebles) {
			// Create sample Inmueble using the Inmuebles service
			var sampleInmueble = new Inmuebles({
				name: 'New Inmueble'
			});

			// Create a sample Inmuebles array that includes the new Inmueble
			var sampleInmuebles = [sampleInmueble];

			// Set GET response
			$httpBackend.expectGET('inmuebles').respond(sampleInmuebles);

			// Run controller functionality
			scope.find();
			$httpBackend.flush();

			// Test scope value
			expect(scope.inmuebles).toEqualData(sampleInmuebles);
		}));

		it('$scope.findOne() should create an array with one Inmueble object fetched from XHR using a inmuebleId URL parameter', inject(function(Inmuebles) {
			// Define a sample Inmueble object
			var sampleInmueble = new Inmuebles({
				name: 'New Inmueble'
			});

			// Set the URL parameter
			$stateParams.inmuebleId = '525a8422f6d0f87f0e407a33';

			// Set GET response
			$httpBackend.expectGET(/inmuebles\/([0-9a-fA-F]{24})$/).respond(sampleInmueble);

			// Run controller functionality
			scope.findOne();
			$httpBackend.flush();

			// Test scope value
			expect(scope.inmueble).toEqualData(sampleInmueble);
		}));

		it('$scope.create() with valid form data should send a POST request with the form input values and then locate to new object URL', inject(function(Inmuebles) {
			// Create a sample Inmueble object
			var sampleInmueblePostData = new Inmuebles({
				name: 'New Inmueble'
			});

			// Create a sample Inmueble response
			var sampleInmuebleResponse = new Inmuebles({
				_id: '525cf20451979dea2c000001',
				name: 'New Inmueble'
			});

			// Fixture mock form input values
			scope.name = 'New Inmueble';

			// Set POST response
			$httpBackend.expectPOST('inmuebles', sampleInmueblePostData).respond(sampleInmuebleResponse);

			// Run controller functionality
			scope.create();
			$httpBackend.flush();

			// Test form inputs are reset
			expect(scope.name).toEqual('');

			// Test URL redirection after the Inmueble was created
			expect($location.path()).toBe('/inmuebles/' + sampleInmuebleResponse._id);
		}));

		it('$scope.update() should update a valid Inmueble', inject(function(Inmuebles) {
			// Define a sample Inmueble put data
			var sampleInmueblePutData = new Inmuebles({
				_id: '525cf20451979dea2c000001',
				name: 'New Inmueble'
			});

			// Mock Inmueble in scope
			scope.inmueble = sampleInmueblePutData;

			// Set PUT response
			$httpBackend.expectPUT(/inmuebles\/([0-9a-fA-F]{24})$/).respond();

			// Run controller functionality
			scope.update();
			$httpBackend.flush();

			// Test URL location to new object
			expect($location.path()).toBe('/inmuebles/' + sampleInmueblePutData._id);
		}));

		it('$scope.remove() should send a DELETE request with a valid inmuebleId and remove the Inmueble from the scope', inject(function(Inmuebles) {
			// Create new Inmueble object
			var sampleInmueble = new Inmuebles({
				_id: '525a8422f6d0f87f0e407a33'
			});

			// Create new Inmuebles array and include the Inmueble
			scope.inmuebles = [sampleInmueble];

			// Set expected DELETE response
			$httpBackend.expectDELETE(/inmuebles\/([0-9a-fA-F]{24})$/).respond(204);

			// Run controller functionality
			scope.remove(sampleInmueble);
			$httpBackend.flush();

			// Test array after successful delete
			expect(scope.inmuebles.length).toBe(0);
		}));
	});
}());