'use strict';

angular.module('[model:name:lower:plural]').directive('[model:name:lower]List', ['[model:name:plural]', 'Notify',
    function([model:name:plural], Notify) {

    return {
        restrict: 'E',
        transclude: true,
        templateUrl: 'modules/[model:name:lower:plural]/views/[model:name:lower]-list-template.html',
        link: function(scope, element, attrs){

            //when a new [model:name:lower] is added, update the [model:name:lower] list

            Notify.getMsg('New[model:name]', function(event, data) {

                scope.[model:name:lower:plural]Ctrl.[model:name:lower:plural] = [model:name:plural].query();

            });

            Notify.getMsg('Updated[model:name]', function(event, data) {

                scope.[model:name:lower:plural]Ctrl.[model:name:lower:plural] = [model:name:plural].query();

            });
        }
    };

}]);

