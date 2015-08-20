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
			[properties]
			[property:name][property:name:value]: this.[property:name:value][cuote:0][/property:name]
			[/properties]
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

