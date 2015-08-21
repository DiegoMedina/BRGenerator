'use strict';

(function() {
	// [model:name:plural] Controller Spec
	describe('[model:name:plural]Controller', function() {
		// Initialize global variables
		var [model:name:plural]Controller,
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

			// Initialize the [model:name:plural] controller.
			[model:name:plural]Controller = $controller('[model:name:plural]Controller', {
				$scope: scope
			});
		}));

		it('$scope.find() should create an array with at least one [model:name:lower] object fetched from XHR', inject(function([model:name:plural]) {
			// Create sample [model:name:lower] using the [model:name:plural] service
			var sample[model:name] = new [model:name:plural]({
				title: 'An [model:name] about MEAN',
				content: 'MEAN rocks!'
			});

			// Create a sample [model:name:lower:plural] array that includes the new [model:name:lower]
			var sample[model:name:plural] = [sample[model:name]];

			// Set GET response
			$httpBackend.expectGET('api/[model:name:lower:plural]').respond(sample[model:name:plural]);

			// Run controller functionality
			scope.find();
			$httpBackend.flush();

			// Test scope value
			expect(scope.[model:name:lower:plural]).toEqualData(sample[model:name:plural]);
		}));

		it('$scope.findOne() should create an array with one [model:name:lower] object fetched from XHR using a [model:name:lower]Id URL parameter', inject(function([model:name:plural]) {
			// Define a sample [model:name:lower] object
			var sample[model:name] = new [model:name:plural]({
				title: 'An [model:name] about MEAN',
				content: 'MEAN rocks!'
			});

			// Set the URL parameter
			$stateParams.[model:name:lower]Id = '525a8422f6d0f87f0e407a33';

			// Set GET response
			$httpBackend.expectGET(/api\/[model:name:lower:plural]\/([0-9a-fA-F]{24})$/).respond(sample[model:name]);

			// Run controller functionality
			scope.findOne();
			$httpBackend.flush();

			// Test scope value
			expect(scope.[model:name:lower]).toEqualData(sample[model:name]);
		}));

		it('$scope.create() with valid form data should send a POST request with the form input values and then locate to new object URL', inject(function([model:name:plural]) {
			// Create a sample [model:name:lower] object
			var sample[model:name]PostData = new [model:name:plural]({
				title: 'An [model:name] about MEAN',
				content: 'MEAN rocks!'
			});

			// Create a sample [model:name:lower] response
			var sample[model:name]Response = new [model:name:plural]({
				_id: '525cf20451979dea2c000001',
				title: 'An [model:name] about MEAN',
				content: 'MEAN rocks!'
			});

			// Fixture mock form input values
			scope.title = 'An [model:name] about MEAN';
			scope.content = 'MEAN rocks!';

			// Set POST response
			$httpBackend.expectPOST('api/[model:name:lower:plural]', sample[model:name]PostData).respond(sample[model:name]Response);

			// Run controller functionality
			scope.create();
			$httpBackend.flush();

			// Test form inputs are reset
			expect(scope.title).toEqual('');
			expect(scope.content).toEqual('');

			// Test URL redirection after the [model:name:lower] was created
			expect($location.path()).toBe('/[model:name:lower:plural]/' + sample[model:name]Response._id);
		}));

		it('$scope.update() should update a valid [model:name:lower]', inject(function([model:name:plural]) {
			// Define a sample [model:name:lower] put data
			var sample[model:name]PutData = new [model:name:plural]({
				_id: '525cf20451979dea2c000001',
				title: 'An [model:name] about MEAN',
				content: 'MEAN Rocks!'
			});

			// Mock [model:name:lower] in scope
			scope.[model:name:lower] = sample[model:name]PutData;

			// Set PUT response
			$httpBackend.expectPUT(/api\/[model:name:lower:plural]\/([0-9a-fA-F]{24})$/).respond();

			// Run controller functionality
			scope.update();
			$httpBackend.flush();

			// Test URL location to new object
			expect($location.path()).toBe('/[model:name:lower:plural]/' + sample[model:name]PutData._id);
		}));

		it('$scope.remove() should send a DELETE request with a valid [model:name:lower]Id and remove the [model:name:lower] from the scope', inject(function([model:name:plural]) {
			// Create new [model:name:lower] object
			var sample[model:name] = new [model:name:plural]({
				_id: '525a8422f6d0f87f0e407a33'
			});

			// Create new [model:name:lower:plural] array and include the [model:name:lower]
			scope.[model:name:lower:plural] = [sample[model:name]];

			// Set expected DELETE response
			$httpBackend.expectDELETE(/api\/[model:name:lower:plural]\/([0-9a-fA-F]{24})$/).respond(204);

			// Run controller functionality
			scope.remove(sample[model:name]);
			$httpBackend.flush();

			// Test array after successful delete
			expect(scope.[model:name:lower:plural].length).toBe(0);
		}));
	});
}());
