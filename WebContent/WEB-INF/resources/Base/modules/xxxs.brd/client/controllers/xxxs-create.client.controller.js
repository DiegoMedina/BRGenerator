'use strict';

angular.module('[model:name:lower:plural]').controller('[model:name:plural]CreateController', ['$scope', '[model:name:plural]', 'Notify',
    function($scope, [model:name:plural], Notify ) {
		// [model:name:plural] create controller logic

        $scope.channelOptions = [
            {id: '1', opt:'Facebook'},
            {id: '2', opt:'Twitter'},
            {id: '3', opt:'Email'}
        ];

        // Create new [model:name]
        this.create = function() {
            // Create new [model:name] object
            var [model:name:lower] = new [model:name:plural] ({
                firstName: this.firstName,
                surname: this.surname,
                suburb: this.suburb,
                country: this.country,
                industry: this.industry,
                email: this.email,
                phone: this.phone,
                referred: this.referred,
                channel: this.channel
            });

            // Redirect after save
            [model:name:lower].$save(function(response) {

                Notify.sendMsg('New[model:name]', {'id': response._id});

            }, function(errorResponse) {
                $scope.error = errorResponse.data.message;
            });
        };
	}
]);

