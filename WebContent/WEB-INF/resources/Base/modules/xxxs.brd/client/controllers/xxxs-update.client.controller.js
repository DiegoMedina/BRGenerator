'use strict';

angular.module('[model:name:lower:plural]').controller('[model:name:plural]UpdateController', ['$scope', '[model:name:plural]', 'Notify',
    function($scope, [model:name:plural], Notify ) {
		// [model:name:plural] update controller logic

        $scope.channelOptions = [
            {id: '1', opt:'Facebook'},
            {id: '2', opt:'Twitter'},
            {id: '3', opt:'Email'}
        ];


        // Update existing [model:name]
        this.update = function(updated[model:name]) {
            var [model:name:lower] = updated[model:name] ;

            [model:name:lower].$update(function(response) {

                Notify.sendMsg('Updated[model:name]', {'id': response._id});

            }, function(errorResponse) {
                $scope.error = errorResponse.data.message;
            });
        };
	}
]);
